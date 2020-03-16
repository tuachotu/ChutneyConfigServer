package common

import info.vikrant.chutneyconfigserver.proto.configserver.ConfigType
import play.api.libs.json._
object ConfigJsonGenerator {

  def stringToConfigType(s: String): Option[ConfigType] = s match {
    case "BOOLEAN" => Some(ConfigType.BOOLEAN)
    case "INTEGER" => Some(ConfigType.INTEGER)
    case "STRING" => Some(ConfigType.STRING)
    case "FLOAT" => Some(ConfigType.FLOAT)
    case "LIST_INTEGER" => Some(ConfigType.LIST_INTEGER)
    case "LIST_BOOLEAN" => Some(ConfigType.LIST_BOOLEAN)
    case "LIST_FLOAT" => Some(ConfigType.LIST_FLOAT)
    case "LIST_STRING" => Some(ConfigType.LIST_STRING)
    case _ => None
  }

  def stringToBool(s:String):Boolean = s match {
    case "true" => true
    case "false" => false
    case _ => throw new RuntimeException("Bad Data")
  }

  def stringToInt(s:String):Int = s.toInt

  def stringToFloat(s:String):Float = s.toFloat

  def stringToIntegerList(s:String):Seq[Int] = s.split(",").map(_.toInt)

  def stringToFloatList(s:String):Seq[Float] = s.split(",").map(_.toFloat)

  def stringToBooleanList(s:String):Seq[Boolean] = s.split(",").map(stringToBool)


  def getValueJson(configType: ConfigType, value: String): String =
    (configType match {
      case ConfigType.BOOLEAN =>
        Json.obj("configType" -> configType.toString(), "value" -> stringToBool(value))
      case ConfigType.INTEGER =>
        Json.obj("configType" -> configType.toString(), "value" -> stringToInt(value))
      case ConfigType.FLOAT =>
        Json.obj("configType" -> configType.toString(), "value" -> stringToFloat(value))
      case ConfigType.STRING =>
        Json.obj("configType" -> configType.toString(), "value" -> value)
      case ConfigType.LIST_BOOLEAN =>
        Json.obj("configType" -> configType.toString(), "value" -> stringToBooleanList(value))
      case ConfigType.LIST_INTEGER =>
        Json.obj("configType" -> configType.toString(), "value" -> stringToIntegerList(value))
      case ConfigType.LIST_FLOAT =>
        Json.obj("configType" -> configType.toString(), "value" -> stringToFloatList(value))
      case ConfigType.LIST_STRING =>
        Json.obj("configType" -> configType.toString(), "value" -> value.split(","))
    }).toString


def getJsonForConfigType(key: String, configType: ConfigType, data: String): String = {
  val jsData = Json.parse(data)

  configType match {
    case ConfigType.BOOLEAN => Json.obj(key -> (jsData \ "value").as[Boolean]).toString()
    case ConfigType.INTEGER => Json.obj(key -> (jsData \ "value").as[Int]).toString()
    case ConfigType.FLOAT => Json.obj(key -> (jsData \ "value").as[Boolean]).toString()
    case ConfigType.STRING => Json.obj(key -> (jsData \ "value").as[String]).toString()
    case ConfigType.LIST_BOOLEAN => Json.obj(key -> (jsData \ "value").as[Seq[Boolean]]).toString()
    case ConfigType.LIST_INTEGER => Json.obj(key -> (jsData \ "value").as[Seq[Int]]).toString()
    case ConfigType.LIST_FLOAT => Json.obj(key -> (jsData \ "value").as[Seq[Float]]).toString()
    case ConfigType.LIST_STRING => Json.obj(key -> (jsData \ "value").as[Seq[String]]).toString()
  }
}

  def getConfigString(key: String, value: String, configType: ConfigType): Option[String] = {
    (configType match {
      case ConfigType.BOOLEAN => Some(Json.obj(key -> stringToBool(value)))
      case ConfigType.INTEGER => Some(Json.obj(key -> stringToInt(value)))
      case ConfigType.FLOAT => Some(Json.obj(key -> stringToFloat(value)))
      case ConfigType.STRING => Some(Json.obj(key -> value))
      case ConfigType.LIST_BOOLEAN => Some(Json.obj(key -> stringToBooleanList(value)))
      case ConfigType.LIST_INTEGER => Some(Json.obj(key -> stringToIntegerList(value)))
      case ConfigType.LIST_FLOAT => Some(Json.obj(key -> stringToFloatList(value)))
      case ConfigType.LIST_STRING => Some(Json.obj(key -> value.split(",")))
      case _ => None
    }) map (_.toString)
  }

}
