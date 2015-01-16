package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import java.util.UUID

import spray.json._

import pomf.service.ChatRoomNotFoundException
import pomf.domain.actors.ChatRoomProtocol
import pomf.domain.actors.ChatRoomProtocol._
import pomf.domain.actors.ChatRepoProtocol
import pomf.domain.actors.ChatRepoProtocol._

class AddChatParticipant(fridgeId: UUID, token: String, participantName: String, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ optRef match {
      case Some(ref) ⇒
        ref ! ChatRoomProtocol.AddParticipant(token, participantName)
        requestOver(participantName + " joined chat " + fridgeId)
      case None ⇒ requestOver(new ChatRoomNotFoundException(id))
    }
  }
}

object AddChatParticipant {
  def props(fridgeId: UUID, token: String, participantName: String, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[AddChatParticipant], fridgeId, token, participantName, chatRepo, ctx)
}