package com.verseel.model

import com.verseel.messages.CompetitionHandler.Competitor

case class Table(positions: Seq[Position])

case class Position(competitor: Competitor, points: Int,
                    playedGames: Int, winnedGames: Int,
                    lostGames: Int, remainingGames: Int,
                    tiedGames: Int, goalsScored: Int,
                    goalsAgainst: Int)

object Table {

  def initialTable(competitors: Seq[Competitor]): Table =
    Table(positions = competitors.map(competitor => Position.initialPosition(competitor, competitors.size - 1)))

}

object Position {

  def initialPosition(initialCompetitor: Competitor, initialRemainingGames: Int): Position =
    Position(
      competitor = initialCompetitor,
      points = 0,
      playedGames = 0,
      winnedGames = 0,
      lostGames = 0,
      remainingGames = initialRemainingGames,
      tiedGames = 0,
      goalsScored = 0,
      goalsAgainst = 0
    )
}


