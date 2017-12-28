package io.ml.poc

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

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


  // http  request-

  var requestPayload = RequestPayload(name = "test-messg88777-xyz99880000")
  implicit val timeout =  Timeout(10000 seconds)

  val resFuture  = (asyncActor ? Command(requestPayload)).mapTo[ResponsePayload]
  val result2 = Await.result(resFuture,timeout.duration)

  // res - send as response
  println(result2)


}
