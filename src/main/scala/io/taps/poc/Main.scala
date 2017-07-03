package io.taps.poc

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.Await

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

  val asyncActor = system.actorOf(AsyncServiceActor.props(producer))
  implicit val timeout =  Timeout(1 seconds)


  // http  request-

  var requestPayload = RequestPayload(name = "test-messg-xyz99880000")

  val resFuture :Future[ResponsePayload] = (asyncActor ? Command(requestPayload)).mapTo[ResponsePayload]
   // res - send as response
  println(resFuture)


}
