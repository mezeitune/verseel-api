package com.verseel.messages

import akka.actor.Props
import com.verseel.actors.CompetitionHandler
import com.verseel.competitionScheduler.CompetitionScheduler
import com.verseel.model.Table

object CompetitionHandler {

  def props(competition: String, competitors: Seq[Competitor], scheduler: CompetitionScheduler) =
    Props(new CompetitionHandler(competition, competitors, scheduler))

  case class CreateCompetition() // message to add competitors to the CompetitionHandler
  case class CompetitionCreated(competition: String, competitors: Seq[Competitor], table: Table)

  case object CancelCompetition // a message to cancel the competition
  case class CompetitionCanceled(competition: String, competitors: Seq[Competitor], table: Table)

  case class Buy(competitors: Int) // message to buy competitors from the CompetitionHandler
  case class Competitor(name: String) // A competitor
  case class Competitors(competition: String,
                         entries: Vector[Competitor] = Vector.empty[Competitor]) // a list of competitors for an competition
  case object GetCompetition // a message containing the remaining competitors for an competition

}
