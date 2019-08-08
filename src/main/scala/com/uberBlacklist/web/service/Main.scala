package com.uberBlacklist.web
package service

import com.uberBlacklist.web.http.UBHttpServiceT

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer

import scala.io.StdIn

object UBApp {
  def main(args: Array[String]): Unit = {
    implicit val system           = ActorSystem()
    implicit val materializer     = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val port: Int = sys.env.getOrElse("PORT", "8081").toInt
    WebServer.startServer("0.0.0.0", port)

    /*
    Local deployment

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8083)
    println("UberBlacklist is up at 8083...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())*/
  }
}
object WebServer extends HttpApp {
  override protected def routes: Route =
    pathEndOrSingleSlash{
      get {
        complete("Uber app is online")
      }
    }
}