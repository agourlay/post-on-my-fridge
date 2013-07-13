package pomf.api

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import spray.json._
import pomf.domain.model._
import pomf.domain.model.ChatMessage
import pomf.domain.model.Notification
import pomf.domain.model.Notification
import pomf.domain.model.ChatMessage

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
}