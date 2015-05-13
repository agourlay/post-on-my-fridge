package pomf.domain.actors

import java.util.UUID

import akka.actor.Props
import scala.concurrent.duration.DurationInt
import de.heikoseeberger.akkasse.EventPublisher
import pomf.core.actors.CommonActor
import pomf.domain.model._

class FridgeUpdatePublisher(filter: (UUID, String) ⇒ Boolean) extends EventPublisher[PushedEvent](500, 1 second)
    with CommonActor {

  context.system.eventStream.subscribe(self, classOf[Notification])

  override protected def receiveEvent = {
    case Notification(fridgeIdNotif, command, payload, timestamp, token) ⇒
      if (filter(fridgeIdNotif, token)) {
        onEvent(PushedEvent(fridgeIdNotif, command, payload, timestamp))
      }
  }
}

object FridgeUpdatePublisher {
  def props(filter: (UUID, String) ⇒ Boolean) = Props(classOf[FridgeUpdatePublisher], filter)
}