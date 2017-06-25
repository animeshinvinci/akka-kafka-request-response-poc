package io.taps.poc

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.{ExecutionContext, Future}

case class Sent(details: String)
case class Command(requestPayload: RequestPayload)

/**
  * Created by animesh on 6/25/17.
  */
class KafkaProducer (implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext) {

  val producerSettings = ProducerSettings(as, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("kafka:9092")

  def sendToKafka(data: String) : Future[Sent] = {
    val done = Source.single(data)
      .map { elem =>
        new ProducerRecord[Array[Byte], String]("cqrs_app_topic", elem)
      }
      .runWith(Producer.plainSink(producerSettings))

    done.map { d =>
      as.log.info(s"sending to kafka  ${Sent(data)}, $d")
      Sent(data)
    }
  }

}

object KafkaProducer {
  def apply()(implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext): KafkaProducer = new KafkaProducer()
}