package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.service.ChatRoomNotFoundException
import pomf.domain.actors.ChatRoomProtocol
import pomf.domain.actors.ChatRoomProtocol._
import pomf.domain.actors.ChatRepoProtocol
import pomf.domain.actors.ChatRepoProtocol._

class RemoveChatParticipant(fridgeId: UUID, token: String, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ optRef match {
      case Some(ref) ⇒
        ref ! ChatRoomProtocol.RemoveParticipant(token)
        requestOver(token + " removed from chat " + fridgeId)
      case None ⇒ requestOver(new ChatRoomNotFoundException(id))
    }
  }
}

object RemoveChatParticipant {
  def props(fridgeId: UUID, token: String, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[RemoveChatParticipant], fridgeId, token, chatRepo, ctx)
}