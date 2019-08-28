package com.uberBlacklist.UBDriver


import scala.collection.mutable.{ ListBuffer, Map }

object AllDrivers {
  final case class AllDriversRequest(
                                    allDrivers:String
                                  )
  final case class AllDriversResponse(
                                     allDrivers: Map[String, String]
                                   )
  final case class AllDriverNames (
                        names: ListBuffer[String]
                      )
}
