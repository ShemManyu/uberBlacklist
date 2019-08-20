package com.uberBlacklist.comments

object commentsService {
  final case class CommentsRequest(
                                  driverDl:String
                                  )
  final case class CommentsResponse(
                                   comments: List[String]
                                   )
}
