package com.verseel.messages

import play.api.libs.json._
import com.verseel.messages.Verseel._
import de.heikoseeberger.akkahttpplayjson._

// message containing the initial number of competitors for the competition
case class CompetitionDescription(competitors: Int) {
  require(competitors > 0)
}

// message containing the required number of competitors
case class CompetitorRequest(competitors: Int) {
  require(competitors > 0)
}

// message containing an error
case class Error(message: String)

// convert our case classes from and to JSON
trait VerseelMarshaller$ extends PlayJsonSupport {

  implicit val competitionDescriptionFormat: OFormat[CompetitionDescription] = Json.format[CompetitionDescription]
  implicit val competitorRequests: OFormat[CompetitorRequest] = Json.format[CompetitorRequest]
  implicit val errorFormat: OFormat[Error] = Json.format[Error]
  implicit val competitionFormat: OFormat[Competition] = Json.format[Competition]
  implicit val competitionsFormat: OFormat[Competitions] = Json.format[Competitions]
  implicit val competitorFormat: OFormat[CompetitorScheduler.Competitor] = Json.format[CompetitorScheduler.Competitor]
  implicit val competitorsFormat: OFormat[CompetitorScheduler.Competitors] = Json.format[CompetitorScheduler.Competitors]
}

object VerseelMarshaller$ extends VerseelMarshaller$
