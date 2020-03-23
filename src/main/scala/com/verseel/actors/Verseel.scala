package com.verseel.actors

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.verseel.competitionScheduler.CompetitionScheduler
import com.verseel.messages.CompetitionHandler.Competitor
import com.verseel.messages.Verseel._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Verseel(implicit timeout: Timeout) extends Actor {

  import com.verseel.messages.CompetitionHandler

  //  CompetitionHandler child
  def createCompetitionHandler(competition: String, competitors: Seq[Competitor], scheduler: CompetitionScheduler): ActorRef = {
    context.actorOf(CompetitionHandler.props(competition, competitors, scheduler), competition)
  }

  def receive: PartialFunction[Any, Unit] = {
    case CreateCompetition(name, competitors, scheduler) =>
      def create(): Unit = {
        //        creates the CompetitionHandler
        val competitionCompetitors = createCompetitionHandler(name, competitors, scheduler)
        //        sends the competitors to the CompetitionHandler
        pipe(competitionCompetitors.ask(CompetitionHandler.CreateCompetition())
          .mapTo[CompetitionHandler.CompetitionCreated]
          .map(c => CompetitionCreated(Competition(competitors.toVector, name, c.table)))) to sender()
      }
      //      If competition exists it responds with CompetitionExists
      context.child(name).fold(create())(_ ⇒ sender() ! CompetitionExists)

    case CancelCompetition(competition) =>
      def notFound(): Unit = sender() ! None

      //      ActorRef carries the message that should be sent to an Actor
      //      Here we'll forward the message to the CompetitionHandler actor that an competition was canceled
      def cancelCompetition(child: ActorRef): Unit = child forward CompetitionHandler.CancelCompetition

      context.child(competition).fold(notFound())(cancelCompetition)

    case GetCompetitors(competition, competitors) =>
      //      sends an empty Competitors message if the competitor seller couldn't be found
      def notFound(): Unit = sender() ! CompetitionHandler.Competitors(competition)

      //      buys from the found CompetitionHandler
      def buy(child: ActorRef): Unit = {
        child.forward(CompetitionHandler.Buy(competitors))
      }
      //      executes notFound or buys with the found CompetitionHandler
      context.child(competition).fold(notFound())(buy)


    case GetCompetition(competition) =>
      def notFound() = sender() ! None

      def getCompetition(child: ActorRef) = child forward CompetitionHandler.GetCompetition

      context.child(competition).fold(notFound())(getCompetition)


    case GetCompetitions ⇒
      def getCompetitions = {
        context.children.map { child ⇒
          //          asks all CompetitionHandler about the competitions they are selling for
          self.ask(GetCompetition(child.path.name)).mapTo[Option[Competition]]
        }
      }

      def convertToCompetitions(f: Future[Iterable[Option[Competition]]]): Future[Competitions] = {
        f.map(_.flatten).map(l ⇒ Competitions(l.toVector))
      }

      pipe(convertToCompetitions(Future.sequence(getCompetitions))) to sender()

  }

}
