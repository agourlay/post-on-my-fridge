package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRoomProtocol._
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class ChatHistory(fridgeId: UUID, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ optRef match {
      case Some(ref) ⇒
        ref ! ChatRoomProtocol.ChatHistory
        context.become(super.receive orElse waitingHistory)
      case None ⇒ requestOver(new ChatRoomNotFoundException(id))
    }
  }

  def waitingHistory: Receive = {
    case ChatHistoryContent(messages) ⇒ requestOver(messages)
  }

}

object ChatHistory {
  def props(fridgeId: UUID, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[ChatHistory], fridgeId, chatRepo, ctx)
}