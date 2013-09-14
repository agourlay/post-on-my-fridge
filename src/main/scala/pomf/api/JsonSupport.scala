package pomf.api

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import spray.json._
import spray.can.server.Stats
import pomf.domain.model._

object CustomJsonProtocol {
  implicit object DateJsonFormat extends RootJsonFormat[Date] with IsoDateChecker {
    override def write(obj: Date) = JsString(dateToIsoString(obj))
    override def read(value: JsValue) = value match {
      case JsString(x) => parseIsoDateString(x)  match {
        							  case Some(date) => date
								      case None => throw new DeserializationException("Expected ISO Date format, got %s" format(x))
								    }
      case _ => deserializationError("Not a String? ")
    }
  }
}

trait IsoDateChecker {
  private val localIsoDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  def dateToIsoString(date: Date) = localIsoDateFormatter.format(date)

  def parseIsoDateString(date: String): Option[Date] =
    if (date.length != 19){
      println(date + " " + date.length)
      None
    }
    else try {
     Some(localIsoDateFormatter.parse(date)) 
    }catch {
      case p: ParseException =>
        println(date + " " + p)
        None
    }
}

object JsonSupport{
  import spray.json._
  import DefaultJsonProtocol._
  import CustomJsonProtocol._
  
  implicit val formatPost = jsonFormat9(Post)
  implicit val formatFridge = jsonFormat3(Fridge)
  implicit val formatFridgeRest = jsonFormat4(FridgeRest)
  implicit val formatChatMessage = jsonFormat3(ChatMessage)
  implicit val formatEvent = jsonFormat3(PushedEvent)
  implicit val formatHttpServerStats = new RootJsonFormat[Stats] {
    def write(obj: Stats): JsValue = JsObject(
      "uptime" -> JsObject("length" -> JsNumber(obj.uptime.length), "unit" -> JsString(obj.uptime.unit.name)),
      "totalRequests" -> JsNumber(obj.totalRequests),
      "openRequests" -> JsNumber(obj.openRequests),
      "maxOpenRequests" -> JsNumber(obj.maxOpenRequests),
      "totalConnections" -> JsNumber(obj.totalConnections),
      "openConnections" -> JsNumber(obj.openConnections),
      "maxOpenConnections" -> JsNumber(obj.maxOpenConnections),
      "requestTimeouts" -> JsNumber(obj.requestTimeouts)
    )

    def read(json: JsValue): Stats = {
      val fields = json.asJsObject.fields
      val uptimeFields = fields.get("uptime").get.asJsObject.fields
      Stats(
        FiniteDuration(
          uptimeFields.get("length").get.asInstanceOf[JsNumber].value.toLong,
          uptimeFields.get("unit").get.asInstanceOf[JsString].value
        ),
        fields.get("totalRequests").get.asInstanceOf[JsNumber].value.toLong,
        fields.get("openRequests").get.asInstanceOf[JsNumber].value.toLong,
        fields.get("maxOpenRequests").get.asInstanceOf[JsNumber].value.toLong,
        fields.get("totalConnections").get.asInstanceOf[JsNumber].value.toLong,
        fields.get("openConnections").get.asInstanceOf[JsNumber].value.toLong,
        fields.get("maxOpenConnections").get.asInstanceOf[JsNumber].value.toLong,
        fields.get("requestTimeouts").get.asInstanceOf[JsNumber].value.toLong
      )
    }
  }
}