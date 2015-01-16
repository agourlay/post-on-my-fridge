package pomf.api.streaming

import akka.actor.{ Actor, ActorRef, Props }
import akka.http._
import akka.http.model._
import akka.stream.actor.{ ActorPublisher, ActorPublisherMessage }

import pomf.core.actors.CommonActor
import pomf.api.endpoint.JsonSupport._
import pomf.domain.model._

import java.util.UUID

class FridgeUpdates(filter: (UUID, String) ⇒ Boolean)
    extends ActorPublisher[PushedEvent]
    with CommonActor {

  context.system.eventStream.subscribe(self, classOf[Notification])

  override def receive = receiveNotification orElse super.receive

  def receiveNotification: Receive = {
    case Notification(fridgeIdNotif, command, payload, timestamp, token) ⇒
      if (filter(fridgeIdNotif, token)) {
        val pushedEvent = PushedEvent(fridgeIdNotif, command, payload, timestamp)
        onNext(pushedEvent)
      }
  }
}

object FridgeUpdates {
  def props(filter: (UUID, String) ⇒ Boolean) = Props(classOf[FridgeUpdates], filter)
}