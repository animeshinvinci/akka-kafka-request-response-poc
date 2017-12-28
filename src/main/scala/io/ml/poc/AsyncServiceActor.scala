package io.ml.poc

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by Animesh on 6/26/17.
  */
class AsyncServiceActor(producer: KafkaProducer)(implicit system: ActorSystem) extends Actor {

  override def receive: Receive = {
    case Command(requestPayload) => system.actorOf(RequestResponseActor.props(producer, sender)) ! requestPayload
  }

}

object AsyncServiceActor {
  def props(producer: KafkaProducer)(implicit system: ActorSystem): Props = {
    Props(new AsyncServiceActor(producer))
  }
}