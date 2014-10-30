package pomf.api.streaming

import akka.actor.{ Actor, ActorRef, Props }

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model._

import java.util.UUID

import spray.http._
import spray.http.MediaTypes._
import spray.can.Http
import HttpHeaders._

class FridgeUpdates(responder: ActorRef, filter: (UUID, String) ⇒ Boolean) extends StreamingResponse(responder) {

  context.system.eventStream.subscribe(self, classOf[Notification])

  override def receive = receiveNotification orElse super.receive

  def receiveNotification: Receive = {
    case Notification(fridgeIdNotif, command, payload, timestamp, token) ⇒
      if (filter(fridgeIdNotif, token)) {
        val pushedEvent = PushedEvent(fridgeIdNotif, command, payload, timestamp)
        responder ! MessageChunk("data: " + formatEvent.write(pushedEvent) + "\n\n")
      }
  }
}

object FridgeUpdates {
  def props(responder: ActorRef, filter: (UUID, String) ⇒ Boolean) = Props(classOf[FridgeUpdates], responder, filter)
}