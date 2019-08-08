package com.uberBlacklist.web
package http

import akka.event.Logging
import akka.http.scaladsl.server.{Directives, HttpApp, Route}
import com.uberBlacklist.UBDriver.UBDriverDetails.{DriverDetailsRequest, DriverDetailsResponse}
import com.uberBlacklist.rating.RatingService.{RatingRequest, RatingResponse, rater}
import com.uberBlacklist.web.http.marshalling.UBHttpJsonSupportT
import com.uberBlacklist.core.util.DBConnectionHandler._

/*
  1.Local signature
  val route = ???

  2.Deployment signature
  object importer extends HttpApp {
  override protected def routes: Route = ???
}
*/

object UBHttpServiceT extends HttpApp with UBHttpJsonSupportT with Directives {
  override protected def routes: Route =
    pathEndOrSingleSlash {
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

            val mquotaRating       = request.quotaRating
            val mconductRating     = request.conductRating
            val mtheftRating       = request.theftRating
            val mworkEthicRating   = request.workEthicRating
            val mdamageRating      = request.damageRating


            val mcomment           = request.comment

            def ratingHandler() : Double = {
              if (mquotaRating == "quotaRating" && mconductRating == "conductRating" && mtheftRating == "theftRating" && mworkEthicRating == "workEthicRating" && mdamageRating == "damageRating") {
                val setQuotaRating = -5.25
                val setConductRating = -2.25
                val setTheftRating = -3.0
                val setEthicRating = -2.25
                val setDamageRating = -2.25

                val trustRating = 5.25
                val initiativeCommSkillsRating = 2.25
                val custodianRating = 3.0
                val responsibleRating = 2.25
                val professionalRating = 2.25

                val rating = rater(setQuotaRating, setConductRating, setTheftRating, setEthicRating, setDamageRating, trustRating, initiativeCommSkillsRating, custodianRating, responsibleRating, professionalRating)
                return rating

              } else if (mquotaRating == "" && mconductRating == "" && mtheftRating == "" && mworkEthicRating == "" && mdamageRating == "") {
                val setQuotaRating = 0.0
                val setConductRating = 0.0
                val setTheftRating = 0.0
                val setEthicRating = 0.0
                val setDamageRating = 0.0
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
                    s"'$ratingDigest', '$mdriverName', '$mdriverDl', '$muserEmail', '$mdriverPhonenumber', '$mquotaRating', '$mconductRating', '$mtheftRating', '$mworkEthicRating', '$mdamageRating', '$mdriverLocation', '$mcomment' " +
                    s")")
              } catch {
                case e: Exception => e.printStackTrace()
              }
              RatingResponse(
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
          complete{
            //Select values  from db
            //Pass them as cc arguments to be sent as response
              val selectStatement = connection.createStatement()
              val query           = selectStatement.executeQuery(
                "SELECT `driverName`, `DL`, `driverPhonenumber`, `driverLocation`, `comment` FROM TableDrivers"
              )
              while (query.next) {
                val mdriverName       = query.getString("driverName")
                val driverDl          = query.getString("DL")
                val driverPhonenumber = query.getString("driverPhonenumber")
                val driverLocation    = List(query.getString("driverLocation"))
                val comment           = List(query.getString("comment"))
              }
            query.toString
          }
        }
      }
    }
  }
}
