package pomf.service

import akka.actor._
import akka.pattern._

import pomf.service.ChatServiceProtocol._
import pomf.service.NotificationServiceProtocol._
import pomf.domain.model._
import pomf.api.JsonSupport._

import scala.concurrent.duration._
import scala.concurrent._


class ChatService(notificationService : ActorRef) extends Actor with ActorLogging {
    
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(10 seconds)

  var chatRooms = Map.empty[String, ActorPath]
  
  def receive = {
    case SendMessage(fridgeName, message, token)    => sender ! addChatMessage(fridgeName, message, token)
    case ChatHistory(fridgeName)                    => retrieveChatHistory(fridgeName, sender)
    case ParticipantNumber(fridgeName)              => retrieveParticipantNumber(fridgeName, sender)
    case AddParticipant(fridgeName, token, name)    => addParticipant(fridgeName, token, name)
    case RemoveParticipant(fridgeName, token)       => removeParticipant(fridgeName, token) 
    case RenameParticipant(fridgeName, token, name) => renameParticipant(fridgeName, token, name)
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
    context.actorSelection(chatRoomPath) ! ChatRoomProtocol.SendMessage(message, token) 
    notificationService ! NotificationServiceProtocol.MessageSent(fridgeName, message, token)
    message
  }
  
  def retrieveChatHistory(fridgeName: String, sender : ActorRef) = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    val history = (context.actorSelection(chatRoomPath) ? ChatRoomProtocol.ChatHistory).mapTo[List[ChatMessage]]
    history pipeTo sender
  }

  def retrieveParticipantNumber(fridgeName: String, sender : ActorRef) = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    val participantNumber = (context.actorSelection(chatRoomPath) ? ChatRoomProtocol.ParticipantNumber).mapTo[String]
    participantNumber pipeTo sender
  }

  def addParticipant(fridgeName: String, token:String, name:String) = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    context.actorSelection(chatRoomPath) ! ChatRoomProtocol.AddParticipant(token, name)
    notificationService ! NotificationServiceProtocol.ParticipantAdded(fridgeName, token, name)
  }

  def removeParticipant(fridgeName: String, token:String) = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    val nameQuitter = (context.actorSelection(chatRoomPath) ? ChatRoomProtocol.RemoveParticipant(token)).mapTo[String]
    nameQuitter.onSuccess { 
      case name : String ⇒ notificationService ! NotificationServiceProtocol.ParticipantRemoved(fridgeName, token, name)
    }
  }

  def renameParticipant(fridgeName: String, token:String, newName:String) = {
    val chatRoomPath = getOrCreateChatRoom(fridgeName)
    val oldName = (context.actorSelection(chatRoomPath) ? ChatRoomProtocol.RenameParticipant(token, newName)).mapTo[String]
    oldName.onSuccess { 
      case exName : String ⇒ notificationService ! NotificationServiceProtocol.ParticipantRenamed(fridgeName, token, newName, exName)
    }
  }
} 

object ChatServiceProtocol {
  case class SendMessage(fridgeName: String, message: ChatMessage, token: String)
  case class ChatHistory(fridgeName: String)
  case class ParticipantNumber(fridgeName: String)
  case class AddParticipant(fridgeName: String, token:String, name:String)
  case class RemoveParticipant(fridgeName: String, token:String)
  case class RenameParticipant(fridgeName: String, token:String, name:String)
}