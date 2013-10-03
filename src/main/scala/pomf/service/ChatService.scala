package pomf.service

import akka.actor._
import akka.pattern._
import pomf.service.ChatServiceProtocol._
import pomf.domain.model._
import pomf.api.JsonSupport._
import scala.concurrent.duration._
import scala.concurrent._


class ChatService(notificationService : ActorRef) extends Actor with ActorLogging {
    
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(10 seconds)

  var chatRooms = Map.empty[String, ActorPath]
  
  def receive = {
      case PushChat(fridgeName, message, token) => sender ! addChatMessage(fridgeName, message, token)
      case ChatHistory(fridgeName)              => retrieveChatHistory(fridgeName, sender)
  }
  
  def getOrCreateChatRoom(fridgeName: String) : ActorPath = {
    if (!chatRooms.contains(fridgeName)){
        val actorchatRoom = context.actorOf(Props[ChatRoom], "chat-room-"+fridgeName)
        chatRooms += (fridgeName -> actorchatRoom.path)
        actorchatRoom.path
      } else {
        chatRooms(fridgeName)
      }
  }

  def addChatMessage(fridgeName: String, message: ChatMessage, token: String): ChatMessage = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    context.actorSelection(chatRoomPath) ! ChatRoomProtocol.AddMessage(message, token) 
    notificationService ! Notification.message(fridgeName, message, token)
    message
  }
  
  def retrieveChatHistory(fridgeName: String, sender : ActorRef) = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    val history = (context.actorSelection(chatRoomPath) ? ChatRoomProtocol.ChatHistory).mapTo[List[ChatMessage]]
    history pipeTo sender
  }
} 

object ChatServiceProtocol {
  case class PushChat(fridgeName: String, message: ChatMessage, token: String)
  case class ChatHistory(fridgeName: String)
  case object AddParticipant
  case object RemoveParticipant
}