package com.uberBlacklist.web
package http.marshalling

import com.uberBlacklist.rating.RatingService.{ RatingRequest, RatingResponse }
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.uberBlacklist.UBDriver.UBDriverDetails.{ DriverDetailsRequest, DriverDetailsResponse }
import spray.json._

trait UBHttpJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val ratingRequest          = jsonFormat12(RatingRequest)
  implicit val ratingResponse         = jsonFormat5(RatingResponse)
  implicit val driverDetailsRequest   = jsonFormat2(DriverDetailsRequest)
  implicit val driverDetailsResponse  = jsonFormat5(DriverDetailsResponse)
}