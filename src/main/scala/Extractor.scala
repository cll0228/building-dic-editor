import java.util.regex.Pattern

trait Resolver {

  def resolve(input: String): Option[AddressModel]

}

trait AddressModel {
  def output: Option[String]
}

class Address1 extends AddressModel {
  // XXX路XXX弄XXX号XXX室
  var road: String = _
  var lane: String = _
  var building: String = _
  var room: String = _

  override def output: Option[String] = {
    if (road == null || lane == null || building == null || room == null)
      None
    else
      Some(road + "路" + lane + "弄" + building + "号" + room + "室")
  }
}

class Address2 extends AddressModel {
  // XXX(小区名)XXX号XXX室
  var residence: String = _
  var building: String = _
  var room: String = _

  override def output: Option[String] = {
    if (residence == null || building == null || room == null)
      None
    else
      Some(residence + building + "号" + room + "室")

  }
}

class Address3 extends AddressModel {
  // XXX(村)XXX(组、队)XXX号
  var village: String = _
  var group: String = _
  var no: String = _

  override def output: Option[String] = {
    if (village == null || group == null || no == null)
      None
    else
      Some(village + "村" + group + "组（队）" + no + "号")
  }
}

class Extractor(val pre: Array[Resolver]) {

  def parse(input: String): Option[AddressModel] = {

    if (pre != null) {
      for (e <- pre) {
        val result = e.resolve(input)
        if (result.isDefined) {
          return result
        }
      }
    }

    var result: Option[AddressModel] = None
    null
  }
}

final class PreResolver extends Resolver {
  private[this] val regex1 = """^([\u4E00-\u9FA5]+)路(\d+)弄(\d+)\-(\d+)$""".r
  private[this] val regex2 = """^(.+)村([\d一二三四五六七八九十]+)[组队](\d+)号$""".r
  private[this] val regex3 = """^([\u4E00-\u9FA5]+)([\d一二三四五六七八九十]+)[组队](\d+)号$""".r

  override def resolve(input: String): Option[AddressModel] = {
    input match {
      case regex1(a, b, c, d) =>
        Some(Address1(a, b, c, d))
      case regex2(a, b, c) =>
        Some(Address3(a, b, c))
      case regex3(a, b, c) =>
        Some(Address3(a, b, c))
      case _ =>
        None
    }
  }
}


object Address3 {
  def apply(village: String, group: String, no: String): Address3 = {
    val r = new Address3
    r.village = village
    r.group = group
    r.no = no
    r
  }
}

object Address2 {
  def apply(residence: String, building: String, room: String): AddressModel = {
    val r = new Address2
    r.residence = residence
    r.building = building
    r.room = room
    r
  }
}

object Address1 {
  def apply(road: String, lane: String, building: String, room: String): Address1 = {
    val r = new Address1
    r.road = road
    r.lane = lane
    r.building = building
    r.room = room
    r
  }
}

object Extractor {

  def apply(pre: Array[Resolver]): Extractor = new Extractor(pre)


}

object App1 extends App {
  val p11: Pattern = Pattern.compile("")


}