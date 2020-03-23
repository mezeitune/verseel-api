package com.verseel.messages

import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.verseel.competitionScheduler.CompetitionScheduler
import com.verseel.messages.CompetitionHandler.Competitor
import com.verseel.model.{Position, Table}
import spray.json.DefaultJsonProtocol

// message containing the initial number of competitors for the competition
case class CompetitionDescription(competitors: Seq[Competitor], private val scheduler: String) {
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
  implicit val competitorFormat = jsonFormat1(CompetitionHandler.Competitor)
  implicit val positionFormat = jsonFormat9(Position.apply)
  implicit val tableFormat = jsonFormat1(Table.apply)
  implicit val competitionFormat = jsonFormat3(Verseel.Competition)
  implicit val competitorsFormat = jsonFormat2(CompetitionHandler.Competitors)
  implicit val competitionDescriptionFormat = jsonFormat2(CompetitionDescription)
  implicit val competitionsFormat = jsonFormat1(Verseel.Competitions)


}

