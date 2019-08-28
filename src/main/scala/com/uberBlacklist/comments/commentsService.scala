package com.uberBlacklist.comments


object commentsService {
      final case class CommentsRequest(
                                        /*driverDl: String,*/
                                        driverName: String
                                      )
      final case class CommentsResponse(
                                   comments: Map[String, String]
                                   )
}
