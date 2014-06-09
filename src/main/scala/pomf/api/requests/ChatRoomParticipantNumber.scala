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

class ChatRoomParticipantNumber(fridgeId: UUID, chatRepo: ActorRef, ctx : RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = waitingLookup orElse handleTimeout

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def waitingCounter : Receive = {
    case ParticipantNumberRoom(nb) => {
      ctx.complete(nb.toString)
      requestOver()
    }
  }

  def handleChatRoomRef(id: UUID, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.ParticipantNumber
      context.become(waitingCounter orElse handleTimeout)
    }
    case None      => {
      ctx.complete(new ChatRoomNotFoundException(id))
      requestOver()
    }  
  }
}

object ChatRoomParticipantNumber {
   def props(fridgeId: UUID, chatRepo: ActorRef, ctx : RequestContext) 
     = Props(classOf[ChatRoomParticipantNumber], fridgeId, chatRepo, ctx).withDispatcher("requests-dispatcher")
}