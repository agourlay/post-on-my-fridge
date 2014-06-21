package pomf.api.request

import akka.actor._
import akka.pattern._

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

class ChatHistory(fridgeId: UUID, chatRepo: ActorRef, ctx : RequestContext)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = waitingLookup orElse handleTimeout

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def waitingHistory : Receive = {
    case ChatHistoryContent(messages) => requestOver(messages)
  }

  def handleChatRoomRef(id: UUID, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.ChatHistory 
      context.become(waitingHistory orElse handleTimeout)
    }
    case None => requestOver(new ChatRoomNotFoundException(id))
  }
}

object ChatHistory {
   def props(fridgeId: UUID, chatRepo: ActorRef, ctx : RequestContext)(implicit breaker: CircuitBreaker) 
     = Props(classOf[ChatHistory], fridgeId, chatRepo, ctx, breaker).withDispatcher("requests-dispatcher")
}