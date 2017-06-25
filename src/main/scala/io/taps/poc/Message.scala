package io.taps.poc

/**
  * Created by animesh on 6/25/17.
  */
case class Message[A <: Payload](origin: String, payload: A)

