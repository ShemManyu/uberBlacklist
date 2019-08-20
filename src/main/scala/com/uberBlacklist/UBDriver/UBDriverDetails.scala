package com.uberBlacklist
package UBDriver

object UBDriverDetails {
  final case class DriverDetailsRequest(
                                userEmail: String,
                                driverDl: String
                                )
  final case class DriverDetailsResponse(
                                        driverName: String,
                                        driverDl: String,
                                        driverPhonenumber: String,
                                        driverLocation: String,
                                        comments: String,
                                        rating: String
                                        )
  final case class DriverDetailsByLocationRequest(
                                          userEmail: String,
                                          driverLocation: String
                                          )
  final case class DriverDetailsByLocationResponse(
                                                  drivers: List[String]
                                                  )
}
