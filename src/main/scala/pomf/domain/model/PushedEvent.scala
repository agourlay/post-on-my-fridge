package pomf.domain.model
import org.joda.time.DateTime
import spray.json._

case class PushedEvent(fridgeId : Long, command :String, payload :JsValue, timestamp : DateTime) 