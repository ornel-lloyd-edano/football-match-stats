package assignment

import assignment.service.MatchResultsXMLService


object Main extends App {

  import MatchResultsXMLService._
  for {
    statType <- args.headOption
    xmlFile <- args.tail.headOption
  } yield {
    loadXML(xmlFile) { implicit rootNode=>
      implicit val players = getPlayers
      getTopPlayersBasedOnStat(5, statType).foreach { playerStat =>
        println(s"${playerStat.rank.toOrdinal}. ${playerStat.firstName} ${playerStat.lastName} - ${playerStat.statValue}")
      }

      getAggTeamStats.foreach { case (team, aggStats)=>
        println(s"${team.side}; ${team.name} - ${aggStats.getStat(statType)}")
      }
    }
  }

  System.exit(0)

}
