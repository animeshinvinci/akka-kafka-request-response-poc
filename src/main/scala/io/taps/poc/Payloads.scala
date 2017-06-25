package io.taps.poc

/**
  * Created by animesh on 6/25/17.
  */

trait Payload

case class RequestPayload(name: String) extends Payload

case class ResponsePayload(name: String) extends Payload
