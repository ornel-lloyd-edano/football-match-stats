package assignment.service

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MatchResultsXMLServiceSpec extends AnyWordSpec with Matchers  {

  "MatchResultsXMLService" should {
    val file = "src/test/resources/1-F9-2192085691-srml-8-2017-f919230-matchresults.xml"
    MatchResultsXMLService.loadXML(file) { implicit rootNode=>
      "extract root node from the XML file" in {
        rootNode.label mustBe "SoccerFeed"
      }

      "extract some players from the XML file" in {
        val someExpectedPlayers = Seq(
          ("Morgan", "Schneiderlin"),
          ("Federico","Fernandez"),
          ("Michael","Keane"),
          ("Oumar","Niasse"),
          ("Rhu-Endly","Martina"),
          ("Tom","Davies"),
          ("Theo","Walcott"),
          ("Luciano","Narsingh")
        )

        MatchResultsXMLService.getPlayers.map(p=>
          (p._2.firstName, p._2.lastName)).toSeq must contain allElementsOf someExpectedPlayers
      }

      "extract some stats by player ref from the XML file" in {
        val somePlayerStats = Seq(
          "p20467" -> ("duel_lost",3),
          "p93464" -> ("leftside_pass",10),
          "p45124" -> ("rightside_pass",5),
          "p66838" -> ("accurate_pass",8),
          "p49539" -> ("won_tackle",3)
        )

        MatchResultsXMLService.getPlayerStats.toSeq.flatMap(p=>
          p._2.map(stat=> p._1.playerRef -> (stat.statType, stat.value))) must contain allElementsOf somePlayerStats
      }

      "extract top 3 players based on any given stat" in {
        val expectedTopThreeAccuratePassers = Seq(
          ("Morgan", "Schneiderlin", 34),
          ("Andy","King",33),
          ("Kyle","Naughton",28)
        )

        implicit val players = MatchResultsXMLService.getPlayers
        MatchResultsXMLService.getTopPlayersBasedOnStat(3, "accurate_pass").map(p=>
          (p.firstName, p.lastName, p.statValue)) must contain allElementsOf expectedTopThreeAccuratePassers
      }

      "sum all points of a team based on any given stat" in {
        val expectedScoreAway = 179
        val expectedScoreHome = 192

        MatchResultsXMLService.getAggTeamStats.find(_._1.side == "Away").flatMap(entry=>
          entry._2.find(_.statType == "accurate_pass").map(_.value)).get mustBe expectedScoreAway

        MatchResultsXMLService.getAggTeamStats.find(_._1.side == "Home").flatMap(entry=>
          entry._2.find(_.statType == "accurate_pass").map(_.value)).get mustBe expectedScoreHome

      }

    }
  }
}
