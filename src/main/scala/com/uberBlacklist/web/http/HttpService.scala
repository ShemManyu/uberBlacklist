package com.uberBlacklist.web
package http

import akka.event.Logging
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, HttpApp, Route}
import com.uberBlacklist.UBDriver.UBDriverDetails.{DriverDetailsByLocationRequest, DriverDetailsByLocationResponse, DriverDetailsRequest, DriverDetailsResponse}
import com.uberBlacklist.rating.RatingService._
import com.uberBlacklist.web.http.marshalling.UBHttpJsonSupportT
import com.uberBlacklist.core.util.DBConnectionHandler._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.google.gson.JsonObject
import com.uberBlacklist.UBDriver.AllDrivers.{AllDriversRequest, AllDriversResponse}
import com.uberBlacklist.comments.commentsService.{CommentsRequest, CommentsResponse}
import spray.json._

import scala.util.parsing.json.JSONObject


/*
  1.Local signature
  val route = ???

  2.Deployment signature
  object importer extends HttpApp {
  override protected def routes: Route = ???
}
*/

object UBHttpServiceT extends UBHttpJsonSupportT with Directives with SprayJsonSupport with DefaultJsonProtocol{
  val route =
    path(""){
      get {
        complete("Uber blacklist is deployed")
      }
    }~
    path("UBapi" / "service" / "driver" / "rate") {
      logRequestResult("driver:rate", Logging.InfoLevel) {
        post{
          entity(as [RatingRequest]) { request =>
            val muserEmail         = request.userEmail
            val mdriverName        = request.driverName
            val mdriverDl          = request.driverDl
            val mdriverPhonenumber = request.driverPhonenumber
            val mdriverLocation    = request.driverLocation
            val mcomment           = request.comment
            val mquotaRating       = request.quotaRating
            val mconductRating     = request.conductRating
            val mtheftRating       = request.theftRating
            val mworkEthicRating   = request.workEthicRating
            val mdamageRating      = request.damageRating
            val mtrustRating       = request.remitsQuotaRating
            val mgoodConductRating = request.goodConductRating
            val mstewardRating     = request.goodStewardRating
            val mgoodworkEthicRating  = request.goodWorkEthicRating
            val mgoodCustomerCare  = request.goodCustomerCare

            val detailsArray = new Array[Double](20)
            if (mquotaRating == "quotaRating" && mconductRating == "conductRating" && mtheftRating == "theftRating" && mworkEthicRating == "workEthicRating" && mdamageRating == "damageRating" && mtrustRating == "remitsQuotaRating" && mgoodConductRating == "mgoodConductRating" && mstewardRating == "mstewardRating" && mgoodworkEthicRating == "mgoodworkEthicRating" && mgoodCustomerCare =="mgoodCustomerCare" ){
              val setQuotaRating = -6.25
              val setConductRating = -2.25
              val setTheftRating = -3.0
              val setEthicRating = -2.25
              val setDamageRating = -2.25

              val trustRating = 7.25
              val initiativeCommSkillsRating = 2.25
              val custodianRating = 2.4
              val responsibleRating = 3.25
              val professionalRating = 3.25

              detailsArray(0) = setQuotaRating
              detailsArray(1) = setConductRating
              detailsArray(2) = setTheftRating
              detailsArray(3) = setEthicRating
              detailsArray(4) = setDamageRating
              detailsArray(5) = trustRating
              detailsArray(6) = initiativeCommSkillsRating
              detailsArray(7) = custodianRating
              detailsArray(8) = responsibleRating
              detailsArray(9) = professionalRating
            }
            //val ratingDigest      = rater (mquotaRating, mconductRating, mtheftRating, mworkEthicRating, mdamageRating)
            //val ratingDigest = ratingHandler()

            //Db values from request
            val qtRating = -6.25
            val cdRating = -2.25
            val tfRating = -3
            val etRating = -2.25
            val dmgRating = 6.25
            val tstRating = 6.25
            val inRating = 2.4
            val cuRating = 3.2
            val rblRating = 3.34
            val profRating = 2.46

            val ratingDigest = rater(qtRating, cdRating, tfRating, etRating, dmgRating, tstRating, inRating, cuRating, rblRating, profRating)
            complete{
              try {
                val insertStatement = connection.createStatement()
                val query           = insertStatement.executeUpdate(
                  s"INSERT INTO TableDrivers (rating, driverName, DL, userEmail, driverPhonenumber, quotaRating, conductRating, theftRating, workEthicRating, damageRating, trustRating, initiativeRating, custodianRating, responsibleRating, professionalRating, driverLocation, comment" +
                    s") VALUES (" +
                    s"'$ratingDigest', '$mdriverName', '$mdriverDl', '$muserEmail', '$mdriverPhonenumber', '$qtRating', '$cdRating', '$tfRating', '$etRating', '$dmgRating'," +
                    s" $tstRating, $inRating, $cuRating, $rblRating, $profRating,'$mdriverLocation', '$mcomment' " +
                    s")")
              } catch {
                case e: Exception => e.printStackTrace()
              }
              RatingResponse(
                muserEmail, mdriverDl, mdriverPhonenumber, mdriverLocation, ratingDigest
              )
            }
          }
        }
      }
    } ~
      path("UBapi" / "service" / "driver" / "rate" / "v2") {
        logRequestResult("driver:rate", Logging.InfoLevel) {
          post{
            entity(as [RatingRequestV2]) { request =>
              val muserEmail         = request.userEmail
              val mdriverName        = request.driverName
              val mdriverDl          = request.driverDl
              val mdriverPhonenumber = request.driverPhonenumber
              val mdriverLocation    = request.driverLocation

              val mquotaRating       = request.quotaRating
              val mconductRating     = request.conductRating

              def ratingHandler() : Double = {
                if (mquotaRating == "quotaRating" || mconductRating == "conductRating") {
                  val setQuotaRating = -5.25
                  val setConductRating = -2.25
                  val setTheftRating = -3.0
                  val setEthicRating = -2.25
                  val setDamageRating = -2.25

                  val trustRating = 8.25
                  val initiativeCommSkillsRating = 5.25
                  val custodianRating = 5.0
                  val responsibleRating = 5.25
                  val professionalRating = 5.25

                  val rating = rater(setQuotaRating, setConductRating, setTheftRating, setEthicRating, setDamageRating, trustRating, initiativeCommSkillsRating, custodianRating, responsibleRating, professionalRating)
                  return rating

                } else if (mquotaRating == "" || mconductRating == "" ) {
                  val setQuotaRating = 0.0
                  val setConductRating = 0.0
                  val setTheftRating = 0.0
                  val setEthicRating = 0.0
                  val setDamageRating = 0.0
                } else {
                  sys.exit()
                }
                ratingHandler()
              }

              //val ratingDigest      = rater (mquotaRating, mconductRating, mtheftRating, mworkEthicRating, mdamageRating)
              val ratingDigest = ratingHandler()

              complete{
                try {
                  val insertStatement = connection.createStatement()
                  val query           = insertStatement.executeUpdate(
                    s"INSERT INTO TableDrivers (rating, driverName, DL, userEmail, driverPhonenumber, quotaRating, conductRating, theftRating, workEthicRating, damageRating, driverLocation, comment" +
                      s") VALUES (" +
                      s"'$ratingDigest', '$mdriverName', '$mdriverDl', '$muserEmail', '$mdriverPhonenumber', '$mquotaRating', '$mconductRating', '', '', '', '$mdriverLocation', '' " +
                      s")")
                } catch {
                  case e: Exception => e.printStackTrace()
                }
                RatingResponseV2(
                  muserEmail, mdriverDl, mdriverPhonenumber, mdriverLocation, ratingDigest
                ).toString
              }
            }
          }
        }
      } ~
  path("UBapi" / "service" / "driver" / "details") {
    logRequestResult("driver:details", Logging.InfoLevel) {
      post {
        entity(as [DriverDetailsRequest]) { request =>
          val muserEmail  = request.userEmail
          val mdriverDl   = request.driverDl
          //Select values  from db
          //Pass them as cc arguments to be sent as response
          val selectStatement = connection.createStatement()
          val query           = selectStatement.executeQuery(
            s"SELECT `driverName`, `DL`, `driverPhonenumber`, `driverLocation`, `comment` , `rating` FROM TableDrivers WHERE `DL` = '$mdriverDl'"
          )
            val detailsArray = new Array[String](20)
            while (query.next) {
              val mdriverName       = query.getString("driverName")
              val driverDl          = query.getString("DL")
              val driverPhonenumber = query.getString("driverPhonenumber")
              val driverLocation    = query.getString("driverLocation")
              val comment           = query.getString("comment")
              val rating            = query.getString("rating")
              //DriverDetailsResponse(mdriverName, driverDl, driverPhonenumber, driverLocation, comment)
              detailsArray(0) = mdriverName
              detailsArray(1) = driverDl
              detailsArray(2) = driverPhonenumber
              detailsArray(3) = driverLocation
              detailsArray(4) = comment
              detailsArray(5) = rating
            }
          complete {
            val a1 = detailsArray(0)
            val a2 = detailsArray(1)
            val a3 = detailsArray(2)
            val a4 = detailsArray(3)
            val a5 = detailsArray(4)
            val a6 = detailsArray(5)
            /*
            HttpEntity(ContentTypes.`application/json`, s"$a1")
            //StatusCodes.OK + " ("+ s"$a1 , $a2 , $a3 , $a4 , $a5" + " )"*/
            DriverDetailsResponse(a1, a2, a3, a4, a5, a6)
          }
        }
      }
    }
  }~
      path("UBapi" / "service" / "driver" / "location") {
        logRequestResult("driver:details", Logging.InfoLevel) {
          post {
            entity(as [DriverDetailsByLocationRequest]) { request =>
              val muserEmail        = request.userEmail
              val mdriverLocation   = request.driverLocation
              //Select values  from db
              //Pass them as cc arguments to be sent as response
              val selectStatement = connection.createStatement()
              val query           = selectStatement.executeQuery(
                s"SELECT `driverName` FROM TableDrivers WHERE driverLocation='$mdriverLocation';"
              )
              val detailsArray = new Array[String](20)
              while (query.next) {
                val driverName    = query.getString("driverName")

                //DriverDetailsResponse(mdriverName, driverDl, driverPhonenumber, driverLocation, comment)

                detailsArray (0) = driverName
              }
              complete {
                val a1 = detailsArray(0)
                //val a2 = detailsArray(1)
                /*val a3 = detailsArray(2)
                val a4 = detailsArray(3)
                val a5 = detailsArray(4)*/
                DriverDetailsByLocationResponse(List(a1))
              }
            }
          }
        }
      } ~
      path("UBapi" / "service" / "driver" / "comments") {
        logRequestResult("driver:details", Logging.InfoLevel) {
          post {
            entity(as [CommentsRequest]) { request =>
              val mDriverdl = request.driverDl
              //Select values  from db
              //Pass them as cc arguments to be sent as response
              val selectStatement = connection.createStatement()
              val query           = selectStatement.executeQuery(
                s"SELECT `comment`, `reporter` FROM TableDrivers WHERE DL='$mDriverdl';"
              )
              val detailsArray = new Array[String](20)
              while (query.next) {
                val comment    = query.getString("comment")
                val reporter   = query.getString("reporter")
                //DriverDetailsResponse(mdriverName, driverDl, driverPhonenumber, driverLocation, comment)

                detailsArray(0) = comment
                detailsArray(1) = reporter
              }
              complete {
                val a1 = detailsArray(0)
                val a2 = detailsArray(1)
                /*val a3 = detailsArray(2)
                val a4 = detailsArray(3)
                val a5 = detailsArray(4)*/
                //CommentsResponse(List(a1, a2))
                "OK"
              }
            }
          }
        }
      } ~
      path("UBapi" / "service" / "driver" / "drivers") {
        logRequestResult("driver:details", Logging.InfoLevel) {
          post {
            entity(as [AllDriversRequest]) { request =>
              val mallDrivers = request.allDrivers
              //Select values  from db
              //Pass them as cc arguments to be sent as response
              val selectStatement = connection.createStatement()
              val query           = selectStatement.executeQuery(
                s"SELECT * FROM TableDrivers ;"
              )
              val detailsArray = new Array[String](20)
              while (query.next) {
                val name    = query.getString("driverName")
                val reporter = query.getString("reporter")
                val comment = query.getString("comment")
                val rating = query.getString("rating")
                val userEmail = query.getString("userEmail")
                val driverPhoneNumber = query.getString("driverPhoneNumber")
                //DriverDetailsResponse(mdriverName, driverDl, driverPhonenumber, driverLocation, comment)

                detailsArray(0) = name
                detailsArray(1) = reporter
                detailsArray(2) = comment
                detailsArray(3) = rating
                detailsArray (4) = userEmail
                detailsArray (5) = driverPhoneNumber
              }
              complete {
                val a1 = detailsArray(0)
                /*val a2 = detailsArray(1)
                val a3 = detailsArray(2)
                val a4 = detailsArray(3)
                val a5 = detailsArray(4)
                val a6 = detailsArray(5)*/

                AllDriversResponse(List(a1))
              }
            }
          }
        }
      }
  /*path("UBapi" / "service" / "driver" / "location") {
    logRequestResult("driver:location", Logging.InfoLevel) {
      post {
        entity(as [])
      }
    }
  }*/
}
