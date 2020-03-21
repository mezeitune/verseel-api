package com.verseel.messages

import akka.actor.Props
import com.verseel.actors.CompetitorScheduler

object CompetitorScheduler {

  def props(competition: String) = Props(new CompetitorScheduler(competition))

  case class Add(competitors: Vector[Competitor]) // message to add competitors to the CompetitorScheduler
  case class Buy(competitors: Int) // message to buy competitors from the CompetitorScheduler
  case class Competitor(id: Int) // A competitor
  case class Competitors(competition: String,
                         entries: Vector[Competitor] = Vector.empty[Competitor]) // a list of competitors for an competition
  case object GetCompetition // a message containing the remaining competitors for an competition
  case object Cancel // a message to cancel the competition
}
