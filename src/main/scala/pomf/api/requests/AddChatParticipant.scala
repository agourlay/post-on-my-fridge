package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }

import java.util.UUID

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRoomProtocol._
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class AddChatParticipant(fridgeId: UUID, token: String, participantName: String, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: UUID, optRef: Option[ActorRef]) = optRef match {
    case Some(ref) ⇒ {
      ref ! ChatRoomProtocol.AddParticipant(token, participantName)
      requestOver(participantName + " joined chat " + fridgeId)
    }
    case None ⇒ requestOver(new ChatRoomNotFoundException(id))
  }
}

object AddChatParticipant {
  def props(fridgeId: UUID, token: String, participantName: String, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[AddChatParticipant], fridgeId, token, participantName, chatRepo, ctx).withDispatcher("requests-dispatcher")
}