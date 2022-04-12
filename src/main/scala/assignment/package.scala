import assignment.model.Stat

package object assignment {
  implicit class IntExtension(value: Int) {
    def toOrdinal: String = value.toString match {
      case "11" | "12" | "13" => s"${value}th"
      case pattern => pattern.last match {
        case '1' => s"${value}st"
        case '2' => s"${value}nd"
        case '3' => s"${value}rd"
        case _ => s"${value}th"
      }
    }
  }

  implicit class IterableStatsExtension(arg: Iterable[Stat]) {
    def getStat(statType: String): Int =
      arg.find(_.statType == statType).map(_.value).getOrElse(0)
  }
}
