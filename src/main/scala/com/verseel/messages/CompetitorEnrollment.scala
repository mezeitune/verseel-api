package com.verseel.messages

import akka.actor.Props
import com.verseel.actors.CompetitorEnrollment

object CompetitorEnrollment {

  def props(competition: String) = Props(new CompetitorEnrollment(competition))

  case class Add(competitors: Vector[Competitor]) // message to add competitors to the CompetitorEnrollment
  case class Buy(competitors: Int) // message to buy competitors from the CompetitorEnrollment
  case class Competitor(name: String) // A competitor
  case class Competitors(competition: String,
                         entries: Vector[Competitor] = Vector.empty[Competitor]) // a list of competitors for an competition
  case object GetCompetition // a message containing the remaining competitors for an competition
  case object Cancel // a message to cancel the competition
}
