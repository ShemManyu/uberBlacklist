package com.uberBlacklist
package rating

object RatingService {

  final case class RatingRequest(
                                  userEmail: String,
                                  driverName: String,
                                  driverDl: String,
                                  driverPhonenumber: String,
                                  driverLocation: String,
                                  quotaRating: String,
                                  conductRating: String,
                                  theftRating: String,
                                  workEthicRating: String,
                                  damageRating: String,
                                  comment: String,
                                  positiveRating: List[String]
                                )
  final case class RatingResponse (
                                  userEmail: String,
                                  driverDl: String,
                                  driverPhonenumber: String,
                                  driverLocation: String,
                                  rating: Double,
                                  )
  final case class PoorDriverRateRequest(
                                          remitsQuotaRating: Double,
                                          goodConductRating: Double,
                                          goodStewardRating: Double,
                                          goodWorkEthicRating: Double
                                        )

  def rater (quota: Double, conduct: Double, theft: Double, ethic: Double, damage: Double, trustRating: Double,
             initiativeCommSkillsRating:Double, custodianRating: Double, responsibleRating: Double, professionalRating: Double) : Double = {
    (quota + conduct + theft + ethic + damage + trustRating + initiativeCommSkillsRating + custodianRating + responsibleRating + professionalRating) / 10
  }
}