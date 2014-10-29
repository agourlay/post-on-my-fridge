package pomf.service

import akka.actor.{ Actor, ActorRef, Props }

import pomf.service.ChatRepoProtocol._
import java.util.UUID
import pomf.core.actors.CommonActor

class ChatRepository(notificationService: ActorRef) extends CommonActor {

  val chatRooms = scala.collection.mutable.Map.empty[UUID, ActorRef]

  def receive = {
    case GetChatRoom(fridgeId) ⇒ sender ! getChatRoom(fridgeId)
  }

  def getChatRoom(fridgeId: UUID) = ChatRoomRef(fridgeId, Some(getOrCreateChatRoom(fridgeId)))

  def getOrCreateChatRoom(fridgeId: UUID): ActorRef = {
    if (!chatRooms.contains(fridgeId)) {
      val actorchatRoom = context.actorOf(ChatRoom.props(fridgeId, notificationService), "chat-room-" + fridgeId)
      chatRooms += (fridgeId -> actorchatRoom)
      actorchatRoom
    } else {
      chatRooms(fridgeId)
    }
  }
}

object ChatRepoProtocol {
  case class GetChatRoom(fridgeId: UUID)
  case class ChatRoomRef(fridgeId: UUID, roomRef: Option[ActorRef])
}

object ChatRepository {
  def props(notificationService: ActorRef) = Props(classOf[ChatRepository], notificationService)
}