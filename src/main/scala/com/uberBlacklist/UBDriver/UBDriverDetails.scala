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
                                        driverLocation: List[String],
                                        comments: List[String]
                                        )
}
