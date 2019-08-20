package com.uberBlacklist.web
package http.marshalling

import com.uberBlacklist.rating.RatingService.{RatingRequest, RatingRequestV2, RatingResponse, RatingResponseV2}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.uberBlacklist.UBDriver.AllDrivers.{ AllDriversRequest, AllDriversResponse }
import com.uberBlacklist.UBDriver.UBDriverDetails.{DriverDetailsByLocationRequest, DriverDetailsByLocationResponse, DriverDetailsRequest, DriverDetailsResponse}
import com.uberBlacklist.comments.commentsService.{CommentsRequest, CommentsResponse}
import spray.json._

trait UBHttpJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val ratingRequest          = jsonFormat16(RatingRequest)
  implicit val ratingResponse         = jsonFormat5(RatingResponse)
  implicit val driverDetailsRequest   = jsonFormat2(DriverDetailsRequest)
  implicit val driverDetailsResponse  = jsonFormat6(DriverDetailsResponse)
  implicit val ratingRequestV2        = jsonFormat7(RatingRequestV2)
  implicit val ratingResponseV2       = jsonFormat5(RatingResponseV2)
  implicit val driverDetailsByLocationRequest = jsonFormat2(DriverDetailsByLocationRequest)
  implicit val driverDetailsByLocationResponse = jsonFormat1(DriverDetailsByLocationResponse)
  implicit val commentsRequest        = jsonFormat1(CommentsRequest)
  implicit val commentsResponse       = jsonFormat1(CommentsResponse)
  implicit val allDriversReqiest      = jsonFormat1(AllDriversRequest)
  implicit val allDriversResponse     = jsonFormat1(AllDriversResponse)
}