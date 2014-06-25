package pomf.api.request

import akka.actor._
import akka.pattern._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRoomProtocol._
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class RemoveChatParticipant(fridgeId: UUID, token: String, chatRepo: ActorRef, ctx : RequestContext) (implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = super.receive orElse waitingLookup

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: UUID, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.RemoveParticipant(token)
      requestOver(token + " removed from chat " +  fridgeId)
    }
    case None      => requestOver(new ChatRoomNotFoundException(id))
  }
}

object RemoveChatParticipant {
   def props(fridgeId: UUID, token: String, chatRepo: ActorRef, ctx : RequestContext)(implicit breaker: CircuitBreaker) 
     = Props(classOf[RemoveChatParticipant], fridgeId, token, chatRepo, ctx, breaker).withDispatcher("requests-dispatcher")
}