package pomf.api.endpoint

import java.util.Date
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.UUID

import spray.json._
import DefaultJsonProtocol._

import nl.grons.metrics.scala._

import org.joda.time.DateTime
import org.joda.time.format._

import pomf.domain.model.{ Post, Fridge, FridgeLight, FridgeFull, ChatMessage, PushedEvent }

object CustomJsonProtocol {
  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {

    private val parserISO: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();

    override def write(obj: DateTime) = JsString(parserISO.print(obj))

    override def read(json: JsValue): DateTime = json match {
      case JsString(s) ⇒ parserISO.parseDateTime(s)
      case _           ⇒ throw new DeserializationException("Expected ISO Date format")
    }
  }

  implicit object UUIDFormat extends RootJsonFormat[UUID] {

    def write(obj: UUID): JsValue = JsString(obj.toString())

    def read(json: JsValue): UUID = json match {
      case JsString(x) ⇒ UUID.fromString(x)
      case _           ⇒ deserializationError("Expected UUID as JsString")
    }
  }
}

object JsonSupport {
  import CustomJsonProtocol._

  implicit val formatPost = jsonFormat8(Post)
  implicit val formatFridge = jsonFormat4(Fridge)
  implicit val formatFridgeLight = jsonFormat6(FridgeLight)
  implicit val formatFridgeFull = jsonFormat6(FridgeFull)
  implicit val formatChatMessage = jsonFormat3(ChatMessage)
  implicit val formatEvent = jsonFormat4(PushedEvent)

  implicit val formatCounter = new RootJsonFormat[Counter] {
    def write(obj: Counter) = JsObject(
      "count" -> JsNumber(obj.count)
    )
    // we don't need to deserialize the TopicPath
    def read(json: JsValue): Counter = ???
  }

  implicit val formatGauge = new RootJsonFormat[Gauge[Int]] {
    def write(obj: Gauge[Int]) = JsObject(
      "count" -> JsNumber(obj.value)
    )
    // we don't need to deserialize the TopicPath
    def read(json: JsValue): Gauge[Int] = ???
  }

  implicit val formatMeter = new RootJsonFormat[Meter] {
    def write(obj: Meter) = JsObject(
      "count" -> JsNumber(obj.count),
      "fifteenMinuteRate" -> JsNumber(obj.fifteenMinuteRate),
      "fiveMinuteRate" -> JsNumber(obj.fiveMinuteRate),
      "meanRate" -> JsNumber(obj.meanRate),
      "oneMinuteRate" -> JsNumber(obj.oneMinuteRate)
    )
    // we don't need to deserialize the TopicPath
    def read(json: JsValue): Meter = ???
  }

  implicit val formatTimer = new RootJsonFormat[Timer] {
    def write(obj: Timer) = JsObject(
      "count" -> JsNumber(obj.count),
      "max" -> JsNumber(obj.max / 1000000),
      "min" -> JsNumber(obj.min / 1000000),
      "mean" -> JsNumber(obj.mean / 1000000),
      "stdDev" -> JsNumber(obj.stdDev / 1000000),
      "fifteenMinuteRate" -> JsNumber(obj.fifteenMinuteRate),
      "fiveMinuteRate" -> JsNumber(obj.fiveMinuteRate),
      "meanRate" -> JsNumber(obj.meanRate),
      "oneMinuteRate" -> JsNumber(obj.oneMinuteRate),
      "50p" -> JsNumber(obj.snapshot.getMedian() / 1000000),
      "75p" -> JsNumber(obj.snapshot.get75thPercentile() / 1000000),
      "95p" -> JsNumber(obj.snapshot.get95thPercentile() / 1000000),
      "98p" -> JsNumber(obj.snapshot.get98thPercentile() / 1000000),
      "99p" -> JsNumber(obj.snapshot.get99thPercentile() / 1000000),
      "999p" -> JsNumber(obj.snapshot.get999thPercentile() / 1000000)
    )
    // we don't need to deserialize the TopicPath
    def read(json: JsValue): Timer = ???
  }
}