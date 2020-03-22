package com.verseel.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.verseel.messages.Verseel._
import com.verseel.messages._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import StatusCodes._
import com.verseel.messages.CompetitorEnrollment.Competitor


class RestApi(system: ActorSystem, timeout: Timeout) extends RestRoutes {
  implicit val requestTimeout: Timeout = timeout
  implicit def executionContext: ExecutionContextExecutor = system.dispatcher

  def createVerseel(): ActorRef = system.actorOf(Verseel.props)
}

trait RestRoutes extends VerseelApi with JsonSupport{
  val service = "verseel-api"
  val version = "v1"

//  endpoint for creating a competition with competitors
  protected val createCompetitionRoute: Route = {
    pathPrefix(service / version / "competitions" / Segment ) { competitionName ⇒
      post {
//    POST verseel-api/v1/competitions/competition_name
        pathEndOrSingleSlash {
          entity(as[CompetitionDescription]) { competitionDescription =>
            onSuccess(createCompetition(competitionName, competitionDescription.competitors)) {
              case Verseel.CompetitionCreated(competition) => complete(Created, competition)
              case Verseel.CompetitionExists =>
                val err = Error(s"$competitionName competition already exists!")
                complete(err)
            }
          }
        }
      }
    }
  }

  protected val getAllCompetitionsRoute: Route = {
    pathPrefix(service / version / "competitions") {
      get {
        // GET verseel-api/v1/competitions
        pathEndOrSingleSlash {
          onSuccess(getCompetitions()) { competitions ⇒
            complete(OK, competitions)
          }
        }
      }
    }
  }

  protected val getCompetitionRoute: Route = {
    pathPrefix(service / version / "competitions" / Segment) { competition ⇒
      get {
        // GET verseel-api/v1/competitions/:competition
        pathEndOrSingleSlash {
          onSuccess(getCompetition(competition)) {
            _.fold(complete(NotFound))(e ⇒ complete(OK, e))
          }
        }
      }
    }
  }

  protected val deleteCompetitionRoute: Route = {
    pathPrefix(service / version / "competitions" / Segment) { competition ⇒
      delete {
        // DELETE verseel-api/v1/competitions/:competition
        pathEndOrSingleSlash {
          onSuccess(cancelCompetition(competition)) {
            _.fold(complete(NotFound))(e => complete(OK, e))
          }
        }
      }
    }
  }

  protected val purchaseCompetitionTicketRoute: Route = {
    pathPrefix(service / version / "competitions" / Segment / "tickets") { competition ⇒
      post {
        // POST verseel-api/v1/competitions/:competition/tickets
        pathEndOrSingleSlash {
          entity(as[CompetitorRequest]) { request ⇒
            onSuccess(requestCompetitors(competition, request.competitors)) { tickets ⇒
              if (tickets.entries.isEmpty) complete(NotFound)
              else complete(Created, tickets)
            }
          }
        }
      }
    }
  }


  val routes: Route = createCompetitionRoute ~ getAllCompetitionsRoute ~ getCompetitionRoute ~ deleteCompetitionRoute ~ purchaseCompetitionTicketRoute
}

trait VerseelApi {

  def createVerseel(): ActorRef

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  lazy val verseel: ActorRef = createVerseel()

  def createCompetition(competition: String, competitors: Seq[Competitor]): Future[CompetitionResponse] = {
    verseel.ask(CreateCompetition(competition, competitors))
      .mapTo[CompetitionResponse]
  }

  def getCompetitions(): Future[Competitions] = verseel.ask(GetCompetitions).mapTo[Competitions]

  def getCompetition(competition: String): Future[Option[Competition]] = verseel.ask(GetCompetition(competition)).mapTo[Option[Competition]]

  def cancelCompetition(competition: String): Future[Option[Competition]] = verseel.ask(CancelCompetition(competition)).mapTo[Option[Competition]]

  def requestCompetitors(competition: String, competitors: Int): Future[CompetitorEnrollment.Competitors] = {
    verseel.ask(GetCompetitors(competition, competitors)).mapTo[CompetitorEnrollment.Competitors]
  }
}
