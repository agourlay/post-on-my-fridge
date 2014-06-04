package pomf.api.endpoint

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException

import spray.json._
import DefaultJsonProtocol._

import org.joda.time.DateTime
import org.joda.time.format._

import pomf.domain.model.{Post, Fridge, FridgeRest, ChatMessage, PushedEvent}


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
  import CustomJsonProtocol._
  
  implicit val formatPost = jsonFormat8(Post)
  implicit val formatFridge = jsonFormat4(Fridge)
  implicit val formatFridgeRest = jsonFormat5(FridgeRest)
  implicit val formatChatMessage = jsonFormat3(ChatMessage)
  implicit val formatEvent = jsonFormat4(PushedEvent)
}