package pomf.domain.actors

import akka.actor.{ ActorRef, Props }

import pomf.domain.actors.ChatRepoProtocol._
import java.util.UUID
import pomf.core.actors.CommonActor
import pomf.domain.actors.ChatRoomProtocol.ChatRoomMessage

class ChatRepository extends CommonActor {

  def chatRooms(id: UUID) = context.child(chatRoomName(id))

  def chatRoomName(id: UUID) = "chat-room-" + id

  def receive = {
    case GetChatRoom(fridgeId)     ⇒ sender ! getChatRoom(fridgeId)
    case ToChatRoom(fridgeId, msg) ⇒ getChatRoom(fridgeId).roomRef forward msg
  }

  def getChatRoom(fridgeId: UUID) = ChatRoomRef(fridgeId, getOrCreateChatRoom(fridgeId))

  def getOrCreateChatRoom(fridgeId: UUID): ActorRef =
    chatRooms(fridgeId).getOrElse {
      context.actorOf(ChatRoom.props(fridgeId), chatRoomName(fridgeId))
    }
}

object ChatRepoProtocol {
  case class GetChatRoom(fridgeId: UUID)
  case class ChatRoomRef(fridgeId: UUID, roomRef: ActorRef)
  case class ToChatRoom(fridgeId: UUID, msg: ChatRoomMessage)
}

object ChatRepository {
  def props() = Props(classOf[ChatRepository])
}