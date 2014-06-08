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

class RenameChatParticipant(fridgeId: Long, token: String, newName: String, chatRepo: ActorRef, ctx : RequestContext) extends RestRequest(ctx) {

  chatRepo ! ChatRepoProtocol.GetChatRoom(fridgeId)

  override def receive = waitingLookup orElse handleTimeout

  def waitingLookup : Receive = {
    case ChatRoomRef(id, optRef) => handleChatRoomRef(id, optRef)
  }

  def handleChatRoomRef(id: Long, optRef : Option[ActorRef]) = optRef match {
    case Some(ref) => {
      ref ! ChatRoomProtocol.RenameParticipant(token, newName)
      ctx.complete(newName + "changed name")
      requestOver()
    }
    case None      => {
      ctx.complete(new ChatRoomNotFoundException(id))
      requestOver()
    }  
  }
}

object RenameChatParticipant {
   def props(fridgeId: Long, token: String, newtName: String, chatRepo: ActorRef, ctx : RequestContext) 
     = Props(classOf[RenameChatParticipant], fridgeId, token, newtName, chatRepo, ctx).withDispatcher("requests-dispatcher")
}