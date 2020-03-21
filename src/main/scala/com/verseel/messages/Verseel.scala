package com.verseel.messages

import akka.actor.Props
import akka.util.Timeout
import com.verseel.actors.Verseel

object Verseel {
  def props(implicit timeout: Timeout) = Props(new Verseel)

  case class CreateCompetition(name: String, competitors: Int) // message to create a competition
  case class GetCompetition(name: String) // message to get a competition
  case object GetCompetitions // message to request all competitions
  case class GetCompetitors(competition: String, competitors: Int) // message to get competitors for a competition
  case class CancelCompetition(name: String) // message to cancel a competition

  case class Competition(name: String, competitors: Int) // message describing the competition
  case class Competitions(competitions: Vector[Competition]) // message describing a list of competitions

  sealed trait CompetitionResponse // message response to create a competition
  case class CompetitionCreated(competition: Competition) extends CompetitionResponse // message to indicate the competition was created
  case object CompetitionExists extends CompetitionResponse // message to indicate that the competition already exists
}




