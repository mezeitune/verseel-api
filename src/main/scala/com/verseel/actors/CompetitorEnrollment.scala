package com.verseel.actors

import akka.actor.{Actor, PoisonPill}
import com.verseel.messages.Verseel

class CompetitorEnrollment(competition: String) extends Actor {
  import com.verseel.messages.CompetitorEnrollment._
//  list of competitors
  var competitors = Vector.empty[Competitor]

  def receive: PartialFunction[Any, Unit] = {
// Adds the new competitors to the existing list of competitors when Competitors message is received
    case Add(newCompetitors) ⇒ competitors = competitors ++ newCompetitors

    case Buy(numberOfCompetitors) ⇒
      // Takes a number of competitors off the list
      val entries = competitors.take(numberOfCompetitors)

    if (entries.size >= numberOfCompetitors) {
// if there are enough competitors available, responds with a Competitors message containing the competitors
      sender() ! Competitors(competition, entries)
      competitors = competitors.drop(numberOfCompetitors)
//   otherwise respond with an empty Competitors message
    } else sender() ! Competitors(competition)
// returns an competition containing the number of competitors left when GetCompetition is received
    case GetCompetition ⇒ sender() ! Some(Verseel.Competition(competitors, competition))

    case Cancel ⇒ sender() ! Some(Verseel.Competition(competitors, competition))
      self ! PoisonPill
  }
}
