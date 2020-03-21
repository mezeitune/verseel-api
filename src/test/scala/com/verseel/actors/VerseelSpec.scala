package com.verseel.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.verseel.StopSystemAfterAll
import com.verseel.messages.Verseel
import com.verseel.messages.Verseel._
import com.verseel.messages.TicketSeller._
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
      verseel ! CreateEvent(eventName, 10)
      expectMsg(EventCreated(Event(eventName, 10)))

      verseel ! GetEvents
      expectMsg(Events(Vector(Event(eventName, 10))))

      verseel ! Verseel.GetEvent(eventName)
      expectMsg(Some(Event(eventName, 10)))

      verseel ! GetTickets(eventName, 1)
      expectMsg(Tickets(eventName, Vector(Ticket(1))))

      verseel ! GetTickets("DavidBowie", 1)
      expectMsg(Tickets("DavidBowie"))
    }

    "Create a child actor when an event is created and sends it a Tickets message" in {
      val verseel = system.actorOf(Props(
        new Verseel  {
          override def createTicketSeller(name: String): ActorRef = testActor
        }
      )
      )

      val tickets = 3
      val eventName = "RHCP"
      val expectedTickets = (1 to tickets).map(Ticket).toVector
      verseel ! CreateEvent(eventName, tickets)
      expectMsg(Add(expectedTickets))
      expectMsg(EventCreated(Event(eventName, tickets)))
    }

    "Get and cancel an event that is not created yet" in {
      val verseel = system.actorOf(Verseel.props)
      val noneExitEventName = "noExitEvent"
      verseel ! Verseel.GetEvent(noneExitEventName)
      expectMsg(None)

      verseel ! CancelEvent(noneExitEventName)
      expectMsg(None)
    }

    "Cancel a ticket which event is not created " in {
      val verseel = system.actorOf(Verseel.props)
      val noneExitEventName = "noExitEvent"

      verseel ! CancelEvent(noneExitEventName)
      expectMsg(None)
    }

    "Cancel a ticket which event is created" in {
      val verseel = system.actorOf(Verseel.props)
      val eventName = "RHCP"
      val tickets = 10
      verseel ! CreateEvent(eventName, tickets)
      expectMsg(EventCreated(Event(eventName, tickets)))

      verseel ! CancelEvent(eventName)
      expectMsg(Some(Event(eventName, tickets)))
    }
  }


}
