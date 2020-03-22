package com.verseel.messages

import akka.actor.Props
import akka.util.Timeout
import com.verseel.actors.Verseel
import com.verseel.messages.CompetitorEnrollment.Competitor

import scala.util.{Failure, Success, Try}

object Verseel {
  def props(implicit timeout: Timeout) = Props(new Verseel)

  case class CreateCompetition(name: String, competitors: Seq[Competitor]) // message to create a competition
  case class GetCompetition(name: String) // message to get a competition
  case object GetCompetitions // message to request all competitions
  case class GetCompetitors(competition: String, competitors: Int) // message to get competitors for a competition
  case class CancelCompetition(name: String) // message to cancel a competition

  case class Competition(competitors: Vector[Competitor] = Vector.empty[Competitor], name: String) // message describing the competition
  case class Competitions(competitions: Vector[Competition]) // message describing a list of competitions

  sealed trait CompetitionResponse // message response to create a competition
  case class CompetitionCreated(competition: Competition) extends CompetitionResponse // message to indicate the competition was created
  case object CompetitionExists extends CompetitionResponse // message to indicate that the competition already exists


  //despues sacar de aca
  sealed trait CompetitionScheduler
  object BetterBetter extends CompetitionScheduler
  object Custom extends CompetitionScheduler
  object BetterWorst extends CompetitionScheduler

  object  CompetitionScheduler{
  def getScheduler(name: String): Try[CompetitionScheduler] = name.toLowerCase match {
      case "betterbetter" => Success(BetterBetter)
      case "custom" => Success(BetterBetter)
      case "betterworst" => Success(BetterWorst)
      case default => Failure(throw new RuntimeException("Not Scheduler"))
    }
  }
}




