package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._

import spray.json._

import java.util.UUID

import pomf.service.ChatRoomNotFoundException
import pomf.domain.actors.ChatRoomProtocol
import pomf.domain.actors.ChatRoomProtocol._
import pomf.domain.actors.ChatRepoProtocol
import pomf.domain.actors.ChatRepoProtocol._

class RenameChatParticipant(fridgeId: UUID, token: String, newName: String, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ optRef match {
      case Some(ref) ⇒
        ref ! ChatRoomProtocol.RenameParticipant(token, newName)
        requestOver(newName + "changed name")
      case None ⇒ requestOver(new ChatRoomNotFoundException(id))
    }
  }
}

object RenameChatParticipant {
  def props(fridgeId: UUID, token: String, newtName: String, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[RenameChatParticipant], fridgeId, token, newtName, chatRepo, ctx)
}