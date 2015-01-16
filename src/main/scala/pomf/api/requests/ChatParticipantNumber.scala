package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import spray.json._

import java.util.UUID

import pomf.service.ChatRoomNotFoundException
import pomf.domain.actors.ChatRoomProtocol
import pomf.domain.actors.ChatRoomProtocol._
import pomf.domain.actors.ChatRepoProtocol
import pomf.domain.actors.ChatRepoProtocol._

class ChatParticipantNumber(fridgeId: UUID, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ optRef match {
      case Some(ref) ⇒
        ref ! ChatRoomProtocol.ParticipantNumber
        context.become(super.receive orElse waitingForCounter)
      case None ⇒ requestOver(new ChatRoomNotFoundException(id))
    }
  }

  def waitingForCounter: Receive = {
    case ParticipantNumberRoom(nb) ⇒ requestOver(nb.toString)
  }
}

object ChatParticipantNumber {
  def props(fridgeId: UUID, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[ChatParticipantNumber], fridgeId, chatRepo, ctx)
}