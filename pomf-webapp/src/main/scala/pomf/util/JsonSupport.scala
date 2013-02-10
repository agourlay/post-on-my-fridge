package pomf.util

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import spray.json._

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
    if (date.length != 22){
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