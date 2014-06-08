package pomf.service

import akka.actor._

import pomf.service.ChatRepoProtocol._

class ChatRepository(notificationService : ActorRef) extends Actor {

  var chatRooms = Map.empty[Long, ActorRef]
  
  def receive = {
    case GetChatRoom(fridgeId) => sender ! getChatRoom(fridgeId)
  }
  
  def getChatRoom(fridgeId: Long) = ChatRoomRef(fridgeId, Some(getOrCreateChatRoom(fridgeId)))

  def getOrCreateChatRoom(fridgeId: Long) : ActorRef = {
    if (!chatRooms.contains(fridgeId)){
      val actorchatRoom = context.actorOf(ChatRoom.props(fridgeId, notificationService), "chat-room-"+fridgeId)
      chatRooms += (fridgeId -> actorchatRoom)
      actorchatRoom
    } else {
      chatRooms(fridgeId)
    }
  }
} 

object ChatRepoProtocol {
  case class GetChatRoom(fridgeId: Long)
  case class ChatRoomRef(fridgeId: Long, roomRef : Option[ActorRef])
}

object ChatRepository {
   def props(notificationService: ActorRef) 
     = Props(classOf[ChatRepository], notificationService).withDispatcher("service-dispatcher")
}