package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling._

import spray.json._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.service.ChatRoomNotFoundException
import pomf.domain.actors.ChatRoomProtocol
import pomf.domain.actors.ChatRepoProtocol
import pomf.domain.actors.ChatRepoProtocol._

class SendChatMessage(fridgeId: UUID, message: ChatMessage, token: String, chatRepo: ActorRef, ctx: RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup: Receive = {
    case ChatRoomRef(id, optRef) ⇒ optRef match {
      case Some(ref) ⇒
        ref ! ChatRoomProtocol.SendMessage(message, token)
        requestOver(message)
      case None ⇒ requestOver(new ChatRoomNotFoundException(id))
    }
  }
}

object SendChatMessage {
  def props(fridgeId: UUID, message: ChatMessage, token: String, chatRepo: ActorRef, ctx: RequestContext) = Props(classOf[SendChatMessage], fridgeId, message, token, chatRepo, ctx)
}