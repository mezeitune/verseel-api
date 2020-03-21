package com.verseel.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.verseel.StopSystemAfterAll
import com.verseel.messages.Verseel
import com.verseel.messages.Verseel._
import com.verseel.messages.CompetitorScheduler._
import org.scalatest.{MustMatchers, WordSpecLike}

class VerseelSpec extends TestKit(ActorSystem("testBoxOffice"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {
  "Verseel" must {

    "Create an event and get tickets from the correct Ticket Seller" in {

      val verseel = system.actorOf(Verseel.props)
      val eventName = "RHCP"
      verseel ! CreateCompetition(eventName, 10)
      expectMsg(CompetitionCreated(Competition(eventName, 10)))

      verseel ! GetCompetitions
      expectMsg(Competitions(Vector(Competition(eventName, 10))))

      verseel ! Verseel.GetCompetition(eventName)
      expectMsg(Some(Competition(eventName, 10)))

      verseel ! GetCompetitors(eventName, 1)
      expectMsg(Competitors(eventName, Vector(Competitor(1))))

      verseel ! GetCompetitors("DavidBowie", 1)
      expectMsg(Competitors("DavidBowie"))
    }

    "Create a child actor when an event is created and sends it a Tickets message" in {
      val verseel = system.actorOf(Props(
        new Verseel  {
          override def createCompetitorScheduler(name: String): ActorRef = testActor
        }
      )
      )

      val tickets = 3
      val eventName = "RHCP"
      val expectedTickets = (1 to tickets).map(Competitor).toVector
      verseel ! CreateCompetition(eventName, tickets)
      expectMsg(Add(expectedTickets))
      expectMsg(CompetitionCreated(Competition(eventName, tickets)))
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
      verseel ! CreateCompetition(eventName, tickets)
      expectMsg(CompetitionCreated(Competition(eventName, tickets)))

      verseel ! CancelCompetition(eventName)
      expectMsg(Some(Competition(eventName, tickets)))
    }
  }


}
