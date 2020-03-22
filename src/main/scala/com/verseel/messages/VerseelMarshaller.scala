package com.verseel.messages

import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.verseel.messages.CompetitorEnrollment.Competitor
import com.verseel.messages.Verseel._
import spray.json.DefaultJsonProtocol

// message containing the initial number of competitors for the competition
case class CompetitionDescription(competitors: Seq[Competitor], scheduler: String) {
  require(competitors.size > 1, "More than one competitor is required")
  require(competitionScheduler.isSuccess, "The entered scheduler does not exist")

  def competitionScheduler = CompetitionScheduler.getScheduler(scheduler)

}

// message containing the required number of competitors
case class CompetitorRequest(competitors: Int) {
  require(competitors > 0)
}

// message containing an error
case class Error(message: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()

  implicit val competitorRequests = jsonFormat1(CompetitorRequest)
  implicit val errorFormat = jsonFormat1(Error)
  implicit val competitorFormat = jsonFormat1(CompetitorEnrollment.Competitor)
  implicit val competitorsFormat = jsonFormat2(CompetitorEnrollment.Competitors)
  implicit val competitionFormat = jsonFormat2(Verseel.Competition)
  implicit val competitionsFormat = jsonFormat1(Verseel.Competitions)
  implicit val competitionDescriptionFormat = jsonFormat2(CompetitionDescription)

}

