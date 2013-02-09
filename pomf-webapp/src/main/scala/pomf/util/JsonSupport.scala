package pomf.util

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import spray.json._

trait DateMarshalling {

  implicit object DateJsonFormat extends JsonFormat[Date] with IsoDateChecker {
    override def write(obj: Date) = JsString(dateToIsoString(obj))
    override def read(value: JsValue) = parseIsoDateString(value.toString) match {
      case None => throw new DeserializationException("Expected ISO Date format, got %s" format(value.toString))
      case Some(date) => date
    }
  }
}

trait IsoDateChecker {
  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  }

  def dateToIsoString(date: Date) = localIsoDateFormatter.get().format(date)

  def parseIsoDateString(date: String): Option[Date] =
    if (date.length != 24){
      println(date.length)
      None
    } 
    else try {
     Some(localIsoDateFormatter.get().parse(date)) 
    }catch {
      case p: ParseException =>
        println(date + " " + p)
        None
    }
}