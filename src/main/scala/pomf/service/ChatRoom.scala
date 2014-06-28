package pomf.service

import akka.actor._

import pomf.domain.model._
import pomf.service.ChatRoomProtocol._
import pomf.util.CustomOrdering._

import scala.concurrent.duration._
import scala.language.postfixOps
import java.util.UUID

class ChatRoom(fridgeId : UUID, notificationService: ActorRef) extends Actor {

  implicit def executionContext = context.dispatcher

  var messages = Map.empty[Long, ChatMessage]
  var participantByToken = Map.empty[String, String]

  context.system.scheduler.scheduleOnce(2 hour,self, ChatRoomProtocol.PurgeChat)

  def receive = {
    case SendMessage(message, token)       => addChatMessage(message, token)
    case ChatHistory                       => sender ! retrieveChatHistory
    case PurgeChat                         => purgeState
    case AddParticipant(token, name)       => addParticipant(token, name)
    case RemoveParticipant(token)          => sender ! removeParticipant(token)
    case ParticipantNumber                 => sender ! ParticipantNumberRoom(participantByToken.size)
    case RenameParticipant(token, newName) => sender ! renameParticipant(token, newName)
  }

  def addParticipant(token:String, name:String) {
    notificationService ! NotificationServiceProtocol.ParticipantAdded(fridgeId, token, name)
    participantByToken += (token -> name)
  }
      
  def addChatMessage(message: ChatMessage, token: String) = {
    messages += (System.currentTimeMillis -> message)
    notificationService ! NotificationServiceProtocol.MessageSent(fridgeId, message, token)
  }
  
  def retrieveChatHistory = ChatHistoryContent(messages.values.toList.sortBy(_.timestamp))

  def removeParticipant(token: String) : String = {
    val name = participantByToken.get(token)
    participantByToken -= token
    val nameQuitter = name.getOrElse("Anonymous")
    notificationService ! NotificationServiceProtocol.ParticipantRemoved(fridgeId, token, nameQuitter)
    nameQuitter
  }

  def renameParticipant(token: String, newName : String) : String = {
    val oldName = participantByToken.get(token) 
    participantByToken += (token -> newName)
    val formerName = oldName.getOrElse("Anonymous")
    notificationService ! NotificationServiceProtocol.ParticipantRenamed(fridgeId, token, newName, formerName)
    formerName
  }

  def purgeState() = {
    messages = Map.empty[Long, ChatMessage]
    participantByToken = Map.empty[String, String]
  }
}

object ChatRoomProtocol {
  case class SendMessage(message: ChatMessage, token: String)
  case class AddParticipant(token:String, name:String)
  case class RemoveParticipant(token:String)
  case class RenameParticipant(token:String, name:String) 
  case object PurgeChat
  case object ChatHistory
  case class ChatHistoryContent(messages : List[ChatMessage])
  case object ParticipantNumber
  case class ParticipantNumberRoom(counter : Int)
}

object ChatRoom {
  def props(fridgeId : UUID, notificationService: ActorRef) 
     = Props(classOf[ChatRoom], fridgeId, notificationService).withDispatcher("chat-room-dispatcher")
}