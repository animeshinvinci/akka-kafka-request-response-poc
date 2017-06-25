name := "akka-kafka-request-response-poc"

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.5.3"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka"         %% "akka-stream-kafka"      % "0.16"
)