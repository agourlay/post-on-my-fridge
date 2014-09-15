package pomf.api.request

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRoomProtocol._
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class RenameChatParticipant(fridgeId: UUID, token: String, newName: String, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: UUID, optRef: Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.RenameParticipant(token, newName)
      requestOver(newName + "changed name")
    }
    case None => requestOver(new ChatRoomNotFoundException(id))
  }
}

object RenameChatParticipant {
  def props(fridgeId: UUID, token: String, newtName: String, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[RenameChatParticipant], fridgeId, token, newtName, chatRepo, ctx).withDispatcher("requests-dispatcher")
}