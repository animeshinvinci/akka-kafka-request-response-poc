package io.taps.poc

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.{ExecutionContext, Future}

import io.circe.parser._
import io.circe.generic.auto._

/**
  * Created by animesh on 6/25/17.
  */
class KafkaConsumerActor (implicit as: ActorSystem,
                          mat: Materializer,
                          ec: ExecutionContext) extends Actor with ActorLogging {


  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    val consumerSettings = ConsumerSettings(as, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("kafka:9092")
      .withGroupId("cqrsgroup")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

       val done =
         Consumer.committableSource(consumerSettings, Subscriptions.topics("cqrs_app_topic"))
        .mapAsync(1) { msg =>
          decode[Message[ResponsePayload]](msg.record.value) map { m =>
            log.info(s"Received Message from Kafka origin=${m.origin} : payload=${m.payload}")
            context.actorSelection(m.origin) ! m.payload
          }
          Future.successful(msg)
        }
        .mapAsync(1) { msg =>
          msg.committableOffset.commitScaladsl()
        }
        .runWith(Sink.ignore)
  }

}

object KafkaConsumerActor {
  def props(implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext): Props = {
    Props(new KafkaConsumerActor)
  }
}