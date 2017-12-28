package io.ml.poc

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import io.circe.generic.auto._
import io.circe.syntax._
/**
  * Created by animesh on 6/26/17.
  */
class RequestResponseActor (producer: KafkaProducer, origin: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case s: RequestPayload =>
      context.become(responseHandler)
      val msg = Message(self.path.toStringWithAddress(self.path.address), ResponsePayload(s.name)).asJson
      producer.sendToKafka(msg.noSpaces)
      log.info(s"Send payload kafka")

  }

  def responseHandler: Receive = {
    case resp: ResponsePayload =>
      log.info(s"Got Response from Kafka  received: $resp, sending to ${origin}")
      origin ! resp
      context.stop(self)
  }

}

object RequestResponseActor {
    def props(producer: KafkaProducer, origin: ActorRef): Props = {
      Props(new RequestResponseActor(producer, origin))
    }
  }
