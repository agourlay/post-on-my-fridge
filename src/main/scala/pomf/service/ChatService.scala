package pomf.service

import akka.actor._
import akka.pattern._

import pomf.service.ChatServiceProtocol._
import pomf.service.NotificationServiceProtocol._
import pomf.domain.model._
import pomf.api.endpoint.JsonSupport._

import scala.concurrent.duration._
import scala.concurrent._


class ChatService(notificationService : ActorRef) extends Actor with ActorLogging {
    
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(10 seconds)

  var chatRooms = Map.empty[Long, ActorRef]
  
  def receive = {
    case SendMessage(fridgeId, message, token)    => sender ! addChatMessage(fridgeId, message, token)
    case ChatHistory(fridgeId)                    => retrieveChatHistory(fridgeId, sender)
    case ParticipantNumber(fridgeId)              => retrieveParticipantNumber(fridgeId, sender)
    case AddParticipant(fridgeId, token, name)    => addParticipant(fridgeId, token, name)
    case RemoveParticipant(fridgeId, token)       => removeParticipant(fridgeId, token) 
    case RenameParticipant(fridgeId, token, name) => renameParticipant(fridgeId, token, name)
  }
  
  def getOrCreateChatRoom(fridgeId: Long) : ActorRef = {
    if (!chatRooms.contains(fridgeId)){
      val actorchatRoom = context.actorOf(Props[ChatRoom], "chat-room-"+fridgeId)
      chatRooms += (fridgeId -> actorchatRoom)
      actorchatRoom
    } else {
      chatRooms(fridgeId)
    }
  }

  def addChatMessage(fridgeId: Long, message: ChatMessage, token: String): ChatMessage = {
    val chatRoom = getOrCreateChatRoom(fridgeId)
    chatRoom ! ChatRoomProtocol.SendMessage(message, token) 
    notificationService ! NotificationServiceProtocol.MessageSent(fridgeId, message, token)
    message
  }
  
  def retrieveChatHistory(fridgeId: Long, sender : ActorRef) = {
    val chatRoom = getOrCreateChatRoom(fridgeId)
    val history = (chatRoom ? ChatRoomProtocol.ChatHistory).mapTo[List[ChatMessage]]
    history pipeTo sender
  }

  def retrieveParticipantNumber(fridgeId: Long, sender : ActorRef) = {
    val chatRoom = getOrCreateChatRoom(fridgeId)
    val participantNumber = (chatRoom ? ChatRoomProtocol.ParticipantNumber).mapTo[String]
    participantNumber pipeTo sender
  }

  def addParticipant(fridgeId: Long, token: String, name: String) = {
    val chatRoom = getOrCreateChatRoom(fridgeId)
    chatRoom ! ChatRoomProtocol.AddParticipant(token, name)
    notificationService ! NotificationServiceProtocol.ParticipantAdded(fridgeId, token, name)
  }

  def removeParticipant(fridgeId: Long, token: String) = {
    val chatRoom = getOrCreateChatRoom(fridgeId)
    val nameQuitter = (chatRoom ? ChatRoomProtocol.RemoveParticipant(token)).mapTo[String]
    nameQuitter.onSuccess { 
      case name : String ⇒ notificationService ! NotificationServiceProtocol.ParticipantRemoved(fridgeId, token, name)
    }
  }

  def renameParticipant(fridgeId: Long, token:String, newName:String) = {
    val chatRoom = getOrCreateChatRoom(fridgeId)
    val oldName = (chatRoom ? ChatRoomProtocol.RenameParticipant(token, newName)).mapTo[String]
    oldName.onSuccess { 
      case exName : String ⇒ notificationService ! NotificationServiceProtocol.ParticipantRenamed(fridgeId, token, newName, exName)
    }
  }
} 

object ChatServiceProtocol {
  case class SendMessage(fridgeId: Long, message: ChatMessage, token: String)
  case class ChatHistory(fridgeId: Long)
  case class ParticipantNumber(fridgeId: Long)
  case class AddParticipant(fridgeId: Long, token: String, name: String)
  case class RemoveParticipant(fridgeId: Long, token: String)
  case class RenameParticipant(fridgeId: Long, token: String, name: String)
}