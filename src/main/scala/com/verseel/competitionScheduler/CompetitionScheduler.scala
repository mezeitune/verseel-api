package com.verseel.competitionScheduler
import scala.util.{Failure, Success, Try}

sealed trait CompetitionScheduler
object BetterBetter extends CompetitionScheduler
object Custom extends CompetitionScheduler
object BetterWorst extends CompetitionScheduler

object  CompetitionScheduler{
  def getScheduler(name: String): Try[CompetitionScheduler] = name.toLowerCase match {
    case "betterbetter" => Success(BetterBetter)
    case "custom" => Success(BetterBetter)
    case "betterworst" => Success(BetterWorst)
    case default => Failure(throw new RuntimeException("The entered scheduler does not exist"))
  }
}
