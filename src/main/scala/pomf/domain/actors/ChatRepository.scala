package pomf.domain.actors

import akka.actor.{ ActorRef, Props }

import pomf.domain.actors.ChatRepoProtocol._
import java.util.UUID
import pomf.core.actors.CommonActor
import pomf.domain.actors.ChatRoomProtocol.ChatRoomMessage
import pomf.service.ChatRoomNotFoundException

import scala.util.Failure

class ChatRepository extends CommonActor {

  val chatRooms = scala.collection.mutable.Map.empty[UUID, ActorRef]

  def receive = {
    case GetChatRoom(fridgeId) ⇒ sender ! getChatRoom(fridgeId)
    case ToChatRoom(fridgeId, msg) ⇒
      getChatRoom(fridgeId).roomRef match {
        case Some(ref) ⇒
          ref forward msg
        case None ⇒ sender ! Failure(new ChatRoomNotFoundException(fridgeId))
      }
  }

  def getChatRoom(fridgeId: UUID) = ChatRoomRef(fridgeId, Some(getOrCreateChatRoom(fridgeId)))

  def getOrCreateChatRoom(fridgeId: UUID): ActorRef = {
    if (!chatRooms.contains(fridgeId)) {
      val actorChatRoom = context.actorOf(ChatRoom.props(fridgeId), "chat-room-" + fridgeId)
      chatRooms += (fridgeId -> actorChatRoom)
      actorChatRoom
    } else {
      chatRooms(fridgeId)
    }
  }
}

object ChatRepoProtocol {
  case class GetChatRoom(fridgeId: UUID)
  case class ChatRoomRef(fridgeId: UUID, roomRef: Option[ActorRef])
  case class ToChatRoom(fridgeId: UUID, msg: ChatRoomMessage)
}

object ChatRepository {
  def props() = Props(classOf[ChatRepository])
}