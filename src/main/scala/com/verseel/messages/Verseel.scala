package com.verseel.messages

import akka.actor.Props
import akka.util.Timeout
import com.verseel.actors.Verseel
import com.verseel.competitionScheduler.CompetitionScheduler
import com.verseel.messages.CompetitionHandler.Competitor
import com.verseel.model.Table

object Verseel {
  def props(implicit timeout: Timeout) = Props(new Verseel)

  sealed trait CompetitionResponse // message response to create a competition

  case class CreateCompetition(name: String, competitors: Seq[Competitor], scheduler: CompetitionScheduler) // message to create a competition
  case class CompetitionCreated(competition: Competition) extends CompetitionResponse // message to indicate the competition was created

  case class CancelCompetition(name: String) // message to cancel a competition
  case class CompetitionCanceled(competition: Competition) extends CompetitionResponse // message to indicate the competition was canceled

  case class GetCompetition(name: String) // message to get a competition
  case object GetCompetitions // message to request all competitions
  case class GetCompetitors(competition: String, competitors: Int) // message to get competitors for a competition


  case class Competition(competitors: Vector[Competitor] = Vector.empty[Competitor], name: String, table: Table) // message describing the competition
  case class Competitions(competitions: Vector[Competition]) // message describing a list of competitions




  case object CompetitionExists extends CompetitionResponse // message to indicate that the competition already exists

}




