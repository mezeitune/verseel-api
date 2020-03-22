package com.verseel.actors

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.verseel.messages.Verseel._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Verseel(implicit timeout: Timeout) extends Actor {
  import com.verseel.messages.CompetitorEnrollment

//  CompetitorEnrollment child
  def createCompetitorEnrollment(name: String): ActorRef = {
    context.actorOf(CompetitorEnrollment.props(name), name)
  }

  def receive: PartialFunction[Any, Unit] = {
    case CreateCompetition(name, competitors) ⇒
      def create(): Unit = {
//        creates the competitor seller
        val competitionCompetitors = createCompetitorEnrollment(name)
//        sends the competitors to the CompetitorEnrollment
        competitionCompetitors ! CompetitorEnrollment.Add(competitors.toVector)
//        creates an competition and responds with CompetitionCreated
        sender() ! CompetitionCreated(Competition(competitors.toVector, name))
      }
//      If competition exists it responds with CompetitionExists
      context.child(name).fold(create())(_ ⇒ sender() ! CompetitionExists)


    case GetCompetitors(competition, competitors) ⇒
//      sends an empty Competitors message if the competitor seller couldn't be found
      def notFound(): Unit = sender() ! CompetitorEnrollment.Competitors(competition)
//      buys from the found CompetitorEnrollment
      def buy(child: ActorRef): Unit = {
        child.forward(CompetitorEnrollment.Buy(competitors))
      }
//      executes notFound or buys with the found CompetitorEnrollment
      context.child(competition).fold(notFound())(buy)


    case GetCompetition(competition) =>
      def notFound() = sender() ! None
      def getCompetition(child: ActorRef) = child forward CompetitorEnrollment.GetCompetition
      context.child(competition).fold(notFound())(getCompetition)


    case GetCompetitions ⇒
      def getCompetitions = {
        context.children.map { child ⇒
//          asks all CompetitorEnrollment about the competitions they are selling for
          self.ask(GetCompetition(child.path.name)).mapTo[Option[Competition]]
        }
      }
      def convertToCompetitions(f: Future[Iterable[Option[Competition]]]): Future[Competitions] = {
        f.map(_.flatten).map(l ⇒ Competitions(l.toVector))
      }
      pipe(convertToCompetitions(Future.sequence(getCompetitions))) to sender()


    case CancelCompetition(competition) ⇒
      def notFound(): Unit = sender() ! None
//      ActorRef carries the message that should be sent to an Actor
//      Here we'll forward the message to the CompetitorEnrollment actor that an competition was canceled
      def cancelCompetition(child: ActorRef): Unit = child forward CompetitorEnrollment.Cancel
      context.child(competition).fold(notFound())(cancelCompetition)
  }

}
