package pomf.domain.model

import org.joda.time.DateTime
import spray.json.JsValue
import java.util.UUID

case class PushedEvent(fridgeId: UUID, command: String, payload: JsValue, timestamp: DateTime)