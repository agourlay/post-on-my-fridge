package pomf.api

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import spray.json._
import pomf.domain.model._

trait DateMarshalling {

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

object JsonImplicits extends DefaultJsonProtocol with DateMarshalling {
  implicit val impPost = jsonFormat9(Post)
  implicit val impFridge = jsonFormat3(Fridge)
  implicit val impFridgeRest = jsonFormat4(FridgeRest)
  implicit val impChatMessage = jsonFormat3(ChatMessage)
  implicit val impNotif = jsonFormat5(Notification)
}