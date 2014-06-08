package pomf.api.request

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.service.ChatRoomNotFoundException
import pomf.service.ChatRoomProtocol
import pomf.service.ChatRoomProtocol._
import pomf.service.ChatRepoProtocol
import pomf.service.ChatRepoProtocol._

class AddChatParticipant(fridgeId: Long, token: String, participantName: String, chatRepo: ActorRef, ctx : RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = waitingLookup orElse handleTimeout

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: Long, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.AddParticipant(token, participantName)
      ctx.complete(participantName + " joined chat " +  fridgeId)
      requestOver()
    }
    case None      => {
      ctx.complete(new ChatRoomNotFoundException(id))
      requestOver()
    }  
  }
}

object AddChatParticipant {
   def props(fridgeId: Long, token: String, participantName: String, chatRepo: ActorRef, ctx : RequestContext) 
     = Props(classOf[AddChatParticipant], fridgeId, token, participantName, chatRepo, ctx).withDispatcher("requests-dispatcher")
}