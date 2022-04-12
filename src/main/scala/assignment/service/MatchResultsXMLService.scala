package assignment.service

import assignment.model.{PlayerEntry, PlayerMatchEntry, PlayerRef, PlayerStat, Stat, Team}

import scala.util.Try
import scala.xml.{Elem, Source, XML}

object MatchResultsXMLService {

  def loadXML(xmlFile: String)( block: Elem=> Unit ): Unit = Try {
    XML.load(Source.fromFile(xmlFile))
  }.fold(ex => {
    println(s"Fail to load XML file: ${ex.getMessage}")
  },
    implicit rootNode=> {
      block(rootNode)
    }
  )

  def getPlayers(implicit root: Elem): Map[PlayerRef, PlayerEntry] =
    (root \ "SoccerDocument" \ "Team" \ "Player")
      .flatMap { player =>
        player.attribute("uID").flatMap(_.headOption.map(uID => uID.text -> player \ "PersonName"))
          .map { case (playerRef, personName) =>
            playerRef -> PlayerEntry(playerRef, (personName \ "First").text, (personName \ "Last").text)
          }
      }.toMap

  def getPlayerStats(implicit root: Elem): Map[PlayerMatchEntry, Seq[Stat]] = {
    (root \ "SoccerDocument" \ "MatchData" \ "TeamData").flatMap { node =>
      for {
        side <- node.attribute("Side").flatMap(_.headOption.map(_.text))
        team <- node.attribute("TeamRef").flatMap(_.headOption.map(_.text))
      } yield {
        (node \ "PlayerLineUp" \ "MatchPlayer").map { matchPlayer =>
          matchPlayer.attribute("PlayerRef").flatMap(_.headOption.map(_.text)).map { player =>
            val playerStats = (matchPlayer \ "Stat").map { statNode =>
              statNode.attribute("Type").flatMap(_.headOption).map(statType =>
                Stat(statType = statType.text, value = Try(statNode.text.toInt).getOrElse(0)))
            }
            PlayerMatchEntry(side, team, player) -> playerStats.flatten
          }
        }.flatten
      }.toMap
    }.flatten.toMap
  }

  def getTopPlayersBasedOnStat(top: Int, stat: String)(implicit root: Elem, players: Map[PlayerRef, PlayerEntry]): Seq[PlayerStat] = {
    getPlayerStats.toSeq.sortBy { case (playerMatchEntry, stats) =>
      (stats.find(_.statType == stat).map(_.value * -1).getOrElse(0), playerMatchEntry.playerRef)
    }.take(top).flatMap { matchEntry =>
      players.get(matchEntry._1.playerRef)
        .map(_ -> matchEntry._2.find(_.statType == stat).getOrElse(Stat(stat, 0)))
    }.zipWithIndex.map { case ((playerEntry, stat), idx) =>
      PlayerStat(playerEntry.playerRef, playerEntry.firstName, playerEntry.lastName, stat.statType, stat.value, rank = idx + 1)
    }
  }

  def getAggTeamStats(implicit root: Elem): Map[Team, Seq[Stat]] = {
    getPlayerStats.toSeq.groupBy { case (playerMatchEntry, _) =>
      Team(playerMatchEntry.side, playerMatchEntry.team)
    }.view.mapValues(_.flatMap(_._2).groupBy(_.statType)
      .view.mapValues(_.map(_.value).sum).toMap
      .map { case (statType, totalTeamValue) =>
        Stat(statType, totalTeamValue)
      }.toSeq).toMap
  }
}
