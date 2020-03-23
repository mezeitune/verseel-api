package com.verseel.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.verseel.StopSystemAfterAll
import com.verseel.messages.Verseel
import com.verseel.messages.Verseel._
import com.verseel.messages.CompetitionHandler._
import org.scalatest.{Ignore, MustMatchers, WordSpecLike}
@Ignore
class VerseelSpec extends TestKit(ActorSystem("testBoxOffice"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {

  /*
  "Verseel" must {

    "Create a competition and get competitors from the correct CompetitionHandler" in {

      val verseel = system.actorOf(Verseel.props)
      val eventName = "RHCP"
      verseel ! CreateCompetition(eventName, Seq(Competitor("manolo")), Custom)
      expectMsg(CompetitionCreated(Competition(Seq(Competitor("manolo")).toVector, eventName)))
    }

    "Create a child actor when an event is created and sends it a Tickets message" in {
      val verseel = system.actorOf(Props(
        new Verseel  {
          override def createCompetitionHandler(name: String): ActorRef = testActor
        }
      )
      )

      val tickets = 3
      val eventName = "RHCP"
      val expectedTickets = Seq(Competitor("manolo"), Competitor("horacio")).toVector
      verseel ! CreateCompetition(eventName, Seq(Competitor("manolo"), Competitor("horacio")), Custom)
      expectMsg(CreateCompetition(expectedTickets))
      expectMsg(CompetitionCreated(Competition(Seq(Competitor("manolo"), Competitor("horacio")).toVector, eventName)))
    }

    "Get and cancel an event that is not created yet" in {
      val verseel = system.actorOf(Verseel.props)
      val noneExitEventName = "noExitEvent"
      verseel ! Verseel.GetCompetition(noneExitEventName)
      expectMsg(None)

      verseel ! CancelCompetition(noneExitEventName)
      expectMsg(None)
    }

    "Cancel a ticket which event is not created " in {
      val verseel = system.actorOf(Verseel.props)
      val noneExitEventName = "noExitEvent"

      verseel ! CancelCompetition(noneExitEventName)
      expectMsg(None)
    }

    "Cancel a ticket which event is created" in {
      val verseel = system.actorOf(Verseel.props)
      val eventName = "RHCP"
      val tickets = 10
      verseel ! CreateCompetition(eventName, Seq(Competitor("manolo"), Competitor("horacio")), Custom)
      expectMsg(CompetitionCreated(Competition(Seq(Competitor("manolo"), Competitor("horacio")).toVector, eventName)))

      verseel ! CancelCompetition(eventName)
      expectMsg(Some(Competition(Seq(Competitor("manolo"), Competitor("horacio")).toVector, eventName)))
    }
  }

*/
}
