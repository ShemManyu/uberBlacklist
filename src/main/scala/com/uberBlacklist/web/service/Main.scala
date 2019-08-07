package com.uberBlacklist.web
package service

import com.uberBlacklist.web.http.UBHttpServiceT

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.io.StdIn

object UBApp extends App with UBHttpServiceT {
  implicit val system           = ActorSystem()
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8083)
  println("UberBlacklist is up at 8083...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
