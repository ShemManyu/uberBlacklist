package com.uberBlacklist.web
package http.marshalling

import com.uberBlacklist.rating.RatingService.{RatingRequest, RatingRequestV2, RatingResponse, RatingResponseV2}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.uberBlacklist.UBDriver.AllDrivers.{AllDriverNames, AllDriversRequest, AllDriversResponse}
import com.uberBlacklist.UBDriver.UBDriverDetails.{DriverDetailsByLocationRequest, DriverDetailsByLocationResponse, DriverDetailsRequest, DriverDetailsResponse}
import com.uberBlacklist.comments.commentsService.{CommentsRequest, CommentsResponse}
import spray.json._

import scala.collection.mutable.{ ListBuffer }




trait UBHttpJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {
  //Custom marshallers

  // Mutable list
  implicit def listBufferFormat[T: JsonFormat] = new RootJsonFormat[ListBuffer[T]] {
    def write(listBuffer: ListBuffer[T]) = JsArray(listBuffer.map(_.toJson).toVector)
    def read(value: JsValue): ListBuffer[T] = value match {
      case JsArray(elements) => elements.map(_.convertTo[T])(collection.breakOut)
      case x => deserializationError("Expected ListBuffer as JsArray, but got " + x)
    }
  }

  // Immutable Map
  implicit def mpFormat[K: JsonFormat, V: JsonFormat] = new RootJsonFormat[Map[K, V]] {
    def write(m: Map[K, V]) = JsObject {
      m.map {
        field =>
          field._1.toJson match {
            case JsString(x) => x -> field._2.toJson
            case x => throw new SerializationException("Map key must be formatted as JsString, not '" + x + "'")
          }
      }
    }

    def read(value: JsValue) = value match {
      case x: JsObject => x.fields.map{ field =>
        (JsString(field._1).convertTo[K], field._2.convertTo[V])
      } (collection.breakOut)
      case x => deserializationError("Expected Map as JsObject, but got " + x)
    }
  }

  // Array
  /*implicit def arrayFormat[T :JsonFormat :ClassManifest] = new RootJsonFormat[Array[T]] {
    def write(array: Array[T]) = JsArray(array.map(_.toJson).toVector)
    def read(value: JsValue) = value match {
      case JsArray(elements) => elements.map(_.convertTo[T]).toArray[T]
      case x => deserializationError("Expected Array as JsArray, but got " + x)
    }
  }*/

  implicit val allDeres         = jsonFormat1(AllDriverNames.apply)
  implicit val commentsResponse = jsonFormat1(CommentsResponse.apply)

  implicit val ratingRequest          = jsonFormat17(RatingRequest)
  implicit val ratingResponse         = jsonFormat6(RatingResponse)
  implicit val driverDetailsRequest   = jsonFormat2(DriverDetailsRequest)
  implicit val driverDetailsResponse  = jsonFormat6(DriverDetailsResponse)
  implicit val ratingRequestV2        = jsonFormat7(RatingRequestV2)
  implicit val ratingResponseV2       = jsonFormat5(RatingResponseV2)
  implicit val driverDetailsByLocationRequest = jsonFormat2(DriverDetailsByLocationRequest)
  implicit val driverDetailsByLocationResponse = jsonFormat1(DriverDetailsByLocationResponse)
  implicit val commentsRequest        = jsonFormat1(CommentsRequest)
  implicit val allDriversRequest      = jsonFormat1(AllDriversRequest)
}