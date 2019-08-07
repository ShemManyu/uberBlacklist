/*
import akka.event.Logging
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import akka.http.scaladsl.Http

import scala.io.StdIn
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import java.sql.{ Connection, DriverManager }


// domain model
final case class Dere(name: String, DriverLicence: String, rating1: Double, rating2: Double, rating3: Double, rating4: Double, rating5: Double)
//final case class Order(items: List[Driver])
final case class DriverList(drivers: Map[String, String])

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat                               = jsonFormat7(Dere)
  //implicit val orderFormat                              = jsonFormat1(Order)
  implicit val driverListFormat                         = jsonFormat1(DriverList)
}

// use it wherever json (un)marshalling is needed
object MyJsonService extends App with Directives with JsonSupport {
  implicit val system = ActorSystem ()
  implicit val materializer = ActorMaterializer ()
  // needed for the future map/ flatmap in the end
  implicit val executionContext = system.dispatcher

  val url1 = "http:localhost:8083/UberBlacklist/drivers/driver/example"
  val url2 = "http:localhost:8083/UberBlacklist/drivers/driverList/example"
  val url3 = "http:localhost:8083/UberBlacklist/ratings/ratingList/example"

  // format: OFF
  val route =
    get {
      path("UberBlacklist" / "drivers" / "driver" / "example") {
        logRequestResult("thing:message", Logging.InfoLevel){
          complete{
            Dere("Dan Mugendi", "ADS0932", 42, 3, 2, 4, 4)
          } // will render as JSON
        }
      }
    } ~
      get {
        path("UberBlacklist" / "drivers" / "driverList" / "example") {
          logRequestResult("thing:message", Logging.InfoLevel){
            complete{
              DriverList(Map(
              "Sam Nyamai"    -> "ADS0932",
              "Davis Osiemo"  -> "ADS0933",
              "John Doe"      -> "ADS0934",
              "Jane Doe"      -> "ADS0935"
            ))
            } // will render as JSON
          }
        }
      } ~
      get {
        path("UberBlacklist" / "ratings" / "ratingList" / "example") {
          logRequestResult("thing:message", Logging.InfoLevel){
            complete{DriverList(Map(
              //Retrieve data from DB and send to user
              "ADS0932" -> "4",
              "ADS0933" -> "3.5",
              "ADS0934" -> "3",
              "ADS0935" -> "2"
              ))
            } // will render as JSON
          }
        }
      } ~
      path("rate") {
        logRequestResult("request:message", Logging.InfoLevel) {
          post {
            entity(as [Dere]) { order  => //will unmarshall JSON to Order
              val pName = order.name
              val pDl  = order.DriverLicence
              val pRating1 = order.rating1
              val pRating2 = order.rating2
              val pRating3 = order.rating3
              val pRating4 = order.rating4
              val pRating5 = order.rating5
              val ratingDigest = (pRating1 + pRating2 + pRating3 + pRating4 + pRating5) / 5
              complete {
                //Send data to db
                val driver      = "com.mysql.jdbc.Driver"
                val dburl       = "jdbc:mysql://localhost:3306/Uber?useSSL=false"
                val dbUsername  = "root"
                val dbPassword  = "root"
                var connection: Connection = null

                val ratingDigest1 = s"$ratingDigest".toString
                val pName1 = s"$pName".toString
                val pDl1 = s"$pDl".toString
                try {
                  Class.forName(driver)
                  connection  = DriverManager.getConnection(dburl, dbUsername, dbPassword)
                  val statement = connection.createStatement()
                  val query = statement.executeUpdate(s"INSERT INTO TableDrivers (rating, DriverName, DL)  VALUES ('$ratingDigest1', '$pName1', '$pDl1')")
                } catch {
                  case e: Exception => e.printStackTrace()
                }
                connection.close()
                //Complete request with this message
                s"You have given a rating of $ratingDigest to $pName whose DL is $pDl"
              }
            }
          }
        }
      }
  // format: ON
  val bindingFuture = Http().bindAndHandle (route, "localhost", 8083)
  //val bindingFuture = Http().bindAndHandleSync (route, "localhost", 8083)
  println(s"Server online at http:localhost:8083/\n Press RETURN to stop...")
  StdIn.readLine() // Let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) //trigger unbinding from the port
    .onComplete(_ => system.terminate()) //and shutdown when done
}


/*
package com.uberBlacklist.web.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import com.uberBlacklist.web.service.MyJsonProtocol.jsonFormat3

import spray.json._
import spray.json.DefaultJsonProtocol

import scala.io.StdIn

case class Person (
                    name: String,
                    fname: String,
                    age: Long
                  )

object testRoute extends App with DefaultJsonProtocol {

  implicit val system = ActorSystem ()
  implicit val materializer = ActorMaterializer ()
  // needed for the future map/ flatmap in the end
  implicit val executionContext = system.dispatcher

  implicit val personFormat = jsonFormat3(Person)

  //val person = Person("a", "b", 10).toString

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path ("/"), _, _, _) =>
      HttpResponse (entity = HttpEntity (
        ContentTypes.`text/html(UTF-8)`,
        "<html> <body> HTML content types! </body></html>"
      ))
    case HttpRequest (GET, Uri.Path ("/ping"),_ , _, _) =>

      HttpResponse (entity = HttpEntity(
        ContentTypes.`application/json`,
        Person("Pete", "Don", 7).toString
      ))

    case HttpRequest (GET, Uri.Path ("/crash"), _, _, _) =>
      sys.error("BOOM!")

    case HttpRequest (GET, Uri.Path("/fetch"), _,_, _) =>
      HttpResponse (entity = HttpEntity (
        ContentTypes.`application/json`,
        List ("phoneNumber" -> "072324423", "phoneNumber"->"074384744").toString
      ))

    case r: HttpRequest =>
      r.discardEntityBytes()
      HttpResponse(404, entity = "Unknown resource")
  }
  val bindingFuture = Http().bindAndHandleSync (requestHandler, "localhost", 8083)
  println(s"Server online at http:localhost:8083/\n Press RETURN to stop...")
  StdIn.readLine() // Let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) //trigger unbinding from the port
    .onComplete(_ => system.terminate()) //and shutdown when done

}

*/
*/
