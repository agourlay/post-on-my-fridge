package pomf.domain.model

import de.heikoseeberger.akkasse.ServerSentEvent
import org.joda.time.DateTime
import pomf.api.route.StreamingRoute._
import spray.json.JsValue
import java.util.UUID

case class PushedEvent(fridgeId: UUID, command: String, payload: JsValue, timestamp: DateTime)

object PushedEvent {
  implicit def toServerSentEvent(event: PushedEvent): ServerSentEvent = {
    ServerSentEvent(formatEvent.write(event) + "\n\n")
  }
}