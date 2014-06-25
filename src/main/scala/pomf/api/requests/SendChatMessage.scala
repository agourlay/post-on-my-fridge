package pomf.api.request

import akka.actor._
import akka.pattern._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class SendChatMessage(fridgeId: UUID, message: ChatMessage, token: String, chatRepo: ActorRef, ctx : RequestContext)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: UUID, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.SendMessage(message, token) 
      requestOver(message)
    }
    case None => requestOver(new ChatRoomNotFoundException(id))
  }
}

object SendChatMessage {
   def props(fridgeId: UUID, message: ChatMessage, token: String, chatRepo: ActorRef, ctx : RequestContext)(implicit breaker: CircuitBreaker) 
     = Props(classOf[SendChatMessage], fridgeId, message, token, chatRepo, ctx, breaker).withDispatcher("requests-dispatcher")
}