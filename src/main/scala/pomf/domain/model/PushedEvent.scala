package pomf.domain.model
import org.joda.time.DateTime

case class PushedEvent(fridgeName : String, command :String, payload :String, timestamp : DateTime) 