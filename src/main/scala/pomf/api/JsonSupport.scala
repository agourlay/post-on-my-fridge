package pomf.api

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import spray.json._
import spray.can.server.Stats
import pomf.domain.model._
import org.joda.time.DateTime
import org.joda.time.format._


object CustomJsonProtocol {
  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {

    private val parserISO : DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();

    override def write(obj: DateTime) = JsString(parserISO.print(obj))

    override def read(json: JsValue) : DateTime = json match {
      case JsString(s) => parserISO.parseDateTime(s)
      case _ => throw new DeserializationException("Expected ISO Date format")
    }
  }
}

object JsonSupport{
  import spray.json._
  import DefaultJsonProtocol._
  import CustomJsonProtocol._
  
  implicit val formatPost = jsonFormat9(Post)
  implicit val formatFridge = jsonFormat5(Fridge)
  implicit val formatFridgeRest = jsonFormat6(FridgeRest)
  implicit val formatChatMessage = jsonFormat3(ChatMessage)
  implicit val formatEvent = jsonFormat4(PushedEvent)
  implicit val formatHttpServerStats = new RootJsonFormat[Stats] {
    def write(obj: Stats): JsValue = JsObject(
      "uptimeInMilli" -> JsNumber(obj.uptime.toMillis),
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