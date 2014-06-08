package pomf.api.request

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class SendChatMessage(fridgeId: Long, message: ChatMessage, token: String, chatRepo: ActorRef, ctx : RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = waitingLookup orElse handleTimeout

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: Long, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.SendMessage(message, token) 
      ctx.complete(message)
      requestOver()
    }
    case None      => {
      ctx.complete(new ChatRoomNotFoundException(id))
      requestOver()
    }  
  }
}

object SendChatMessage {
   def props(fridgeId: Long, message: ChatMessage, token: String, chatRepo: ActorRef, ctx : RequestContext) 
     = Props(classOf[SendChatMessage], fridgeId, message, token, chatRepo, ctx).withDispatcher("requests-dispatcher")
}