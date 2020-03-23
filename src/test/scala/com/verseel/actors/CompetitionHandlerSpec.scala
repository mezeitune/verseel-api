package com.verseel.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.verseel.StopSystemAfterAll
import com.verseel.messages.CompetitionHandler
import com.verseel.messages.CompetitionHandler.{CreateCompetition, Buy, Competitor, Competitors}
import org.scalatest.{Ignore, MustMatchers, WordSpecLike}
@Ignore
class CompetitionHandlerSpec extends TestKit(ActorSystem("testCompetitors"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with StopSystemAfterAll {
/*
  "The CompetitionHandler" must {
    "Sell tickets until they are sold out" in {

      def mkCompetitors = Seq(Competitor("manolo"), Competitor("horacio")).toVector
      val competition = "RHCP"
      val competitorActor = system.actorOf(CompetitionHandler.props(competition))

      competitorActor ! CreateCompetition(mkCompetitors)
      competitorActor ! Buy(1)

      expectMsg(Competitors(competition, Vector(Competitor("manolo"))))

      val nrs = 2 to 10
      nrs.foreach(_ => competitorActor ! Buy(1))

      val tickets = receiveN(9)
      tickets.zip(nrs).foreach { case (Competitors(_, Vector(Competitor(id))), ix) => id must be(ix) }

      competitorActor ! Buy(1)
      expectMsg(Competitors(competition))
    }

    "Sell tickets in batches until they are sold out" in {

      val firstBatchSize = 10

      def mkCompetitors = Seq(Competitor("manolo"), Competitor("horacio")).toVector

      val event = "Madlib"
      val ticketingActor = system.actorOf(CompetitionHandler.props(event))

      ticketingActor ! CreateCompetition(mkCompetitors)
      ticketingActor ! Buy(firstBatchSize)
      val bought = Seq(Competitor("manolo"), Competitor("horacio")).toVector

      expectMsg(Competitors(event, bought))

      val secondBatchSize = 5
      val nrBatches = 18

      val batches = 1 to nrBatches * secondBatchSize
      batches.foreach(_ => ticketingActor ! Buy(secondBatchSize))

      val tickets = receiveN(nrBatches)

      tickets.zip(batches).foreach {
        case (Competitors(_, bought), ix) =>
          bought.size must equal(secondBatchSize)
          val last = ix * secondBatchSize + firstBatchSize
          val first = ix * secondBatchSize + firstBatchSize - (secondBatchSize - 1)
          bought.map(_.name) must equal((first to last).toVector)
      }

      ticketingActor ! Buy(1)
      expectMsg(Competitors(event))

      ticketingActor ! Buy(10)
      expectMsg(Competitors(event))
    }
  }


 */
}

