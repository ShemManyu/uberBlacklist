package com.uberBlacklist.UBDriver

import org.json.JSONObject

object AllDrivers {
  final case class AllDriversRequest(
                                    allDrivers:String
                                  )
  final case class AllDriversResponse(
                                     allDrivers: List[String]
                                   )
}
