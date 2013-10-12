package pomf.domain.model
import org.joda.time.DateTime
import spray.json._

case class PushedEvent(fridgeName : String, command :String, payload :JsValue, timestamp : DateTime) 