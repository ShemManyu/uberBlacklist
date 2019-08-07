package com.uberBlacklist.feedback

object OwnerFeedback {
  final case class DriverDetails(
                                reporter: String,
                                reportee: String,
                                comment: String
                                )
}
