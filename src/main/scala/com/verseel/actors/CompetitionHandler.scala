package com.verseel.actors

import akka.actor.{ActorLogging, PoisonPill}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.verseel.competitionScheduler.CompetitionScheduler
import com.verseel.messages.CompetitionHandler.Competitor
import com.verseel.messages.Verseel
import com.verseel.model.Table

class CompetitionHandler(competition: String, competitors: Seq[Competitor], scheduler: CompetitionScheduler)
  extends PersistentActor with ActorLogging {

  import com.verseel.messages.CompetitionHandler._

  override def persistenceId: String = competition

  var table: Table = Table.initialTable(competitors)

  def whenCompetitionCreated(created: CompetitionCreated) = table = created.table

  override def receiveRecover: Receive = {
    case event: CompetitionCreated => {
      log.info(s"$event Recover Start")
      whenCompetitionCreated(event)
    }
    // Restore state
    case SnapshotOffer(_, snapshot: Table) => table = snapshot
    case RecoveryCompleted => log.info("Competition recovery completed")

  }

  override def receiveCommand: Receive = {
    case CreateCompetition() =>
      persist(CompetitionCreated(competition, competitors, table)) { event =>
        whenCompetitionCreated(event)
        sender() ! event
      }

    case CancelCompetition =>
      sender() ! Some(Verseel.Competition(competitors.toVector, competition, table))
      self ! PoisonPill


    case GetCompetition => sender() ! Some(Verseel.Competition(competitors.toVector, competition, table))


  }
}
