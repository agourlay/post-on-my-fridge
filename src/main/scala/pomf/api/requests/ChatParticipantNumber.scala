package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRoomProtocol._
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class ChatParticipantNumber(fridgeId: UUID, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ handleChatRoomRef(id, optRef)
  }

  def waitingCounter: Receive = {
    case ParticipantNumberRoom(nb) ⇒ requestOver(nb.toString)
  }

  def handleChatRoomRef(id: UUID, optRef: Option[ActorRef]) = optRef match {
    case Some(ref) ⇒ {
      ref ! ChatRoomProtocol.ParticipantNumber
      context.become(super.receive orElse waitingCounter)
    }
    case None ⇒ requestOver(new ChatRoomNotFoundException(id))
  }
}

object ChatParticipantNumber {
  def props(fridgeId: UUID, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[ChatParticipantNumber], fridgeId, chatRepo, ctx).withDispatcher("requests-dispatcher")
}