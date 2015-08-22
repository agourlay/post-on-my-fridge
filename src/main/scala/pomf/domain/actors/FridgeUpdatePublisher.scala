package pomf.domain.actors

import java.util.UUID

import akka.actor.Props
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{ Cancel, Request }
import pomf.core.actors.CommonActor
import pomf.domain.model._

class FridgeUpdatePublisher(filter: (UUID, String) ⇒ Boolean)
    extends ActorPublisher[PushedEvent]
    with CommonActor {

  context.system.eventStream.subscribe(self, classOf[Notification])

  val events = scala.collection.mutable.Queue.empty[PushedEvent]

  override def receive = {
    case Notification(fridgeIdNotif, command, payload, timestamp, token) ⇒
      if (filter(fridgeIdNotif, token)) {
        if (events.size > 500) events.dequeue()
        events.enqueue(PushedEvent(fridgeIdNotif, command, payload, timestamp))
        pushToSub()
      }

    case Request(_) ⇒
      pushToSub()

    case Cancel ⇒
      context.stop(self)
  }

  def pushToSub() =
    while (totalDemand > 0 && events.nonEmpty) {
      onNext(events.dequeue())
    }
}

object FridgeUpdatePublisher {
  def props(filter: (UUID, String) ⇒ Boolean) = Props(classOf[FridgeUpdatePublisher], filter)
}