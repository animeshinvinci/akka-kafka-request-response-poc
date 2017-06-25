package io.taps.poc

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

/**
  * Created by animesh on 6/25/17.
  */
object Main extends App{

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val config = ConfigFactory.load()

  //Now have consumer actor
  val consumer = system.actorOf(KafkaConsumerActor.props)

  //Kafka Producer

  val producer = KafkaProducer()



  // service message passing actor


  // kakf a

}
