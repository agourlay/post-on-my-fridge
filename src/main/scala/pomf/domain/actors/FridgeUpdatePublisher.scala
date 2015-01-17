package pomf.domain.actors

import java.util.UUID

import akka.actor.Props
import akka.stream.actor.ActorPublisher
import pomf.core.actors.CommonActor
import pomf.domain.model._

class FridgeUpdatePublisher(filter: (UUID, String) ⇒ Boolean)
    extends ActorPublisher[PushedEvent]
    with CommonActor {

  context.system.eventStream.subscribe(self, classOf[Notification])

  override def receive = receiveNotification

  def receiveNotification: Receive = {
    case Notification(fridgeIdNotif, command, payload, timestamp, token) ⇒
      if (filter(fridgeIdNotif, token)) {
        val pushedEvent = PushedEvent(fridgeIdNotif, command, payload, timestamp)
        onNext(pushedEvent)
      }
  }
}

object FridgeUpdatePublisher {
  def props(filter: (UUID, String) ⇒ Boolean) = Props(classOf[FridgeUpdatePublisher], filter)
}