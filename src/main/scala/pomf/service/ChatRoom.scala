package pomf.service

import akka.actor._

import pomf.domain.model._
import pomf.service.ChatRoomProtocol._

import scala.concurrent.duration._
import scala.language.postfixOps

class ChatRoom extends Actor with ActorLogging {

  implicit def executionContext = context.dispatcher

  var messages = Map.empty[Long, ChatMessage]
  var participantByToken = Map.empty[String, String]

  context.system.scheduler.scheduleOnce(2 hour,self, ChatRoomProtocol.PurgeChat)

  def receive = {
    case SendMessage(message, token)       => addChatMessage(message, token)
    case ChatHistory                       => sender ! retrieveChatHistory
    case PurgeChat                         => messages = Map.empty[Long, ChatMessage]
    case AddParticipant(token, name)       => participantByToken += (token -> name)
    case RemoveParticipant(token)          => sender ! removeParticipant(token)
    case ParticipantNumber                 => sender ! participantByToken.size.toString
    case RenameParticipant(token, newName) => sender ! renameParticipant(token, newName)
  }

  def addChatMessage(message: ChatMessage, token: String) = {
    messages += (System.currentTimeMillis -> message)
  }
  
  def retrieveChatHistory : List[ChatMessage] = {
    messages.values.toList.sortBy(_.timestamp)
  }

  def removeParticipant(token: String) : String = {
    val name = participantByToken(token) 
    participantByToken -= token
    name
  }

  def renameParticipant(token: String, newName : String) : String = {
    val oldName = participantByToken(token) 
    participantByToken += (token -> newName)
    oldName
  }
}

object ChatRoomProtocol {
  case class SendMessage(message: ChatMessage, token: String)
  case class AddParticipant(token:String, name:String)
  case class RemoveParticipant(token:String)
  case class RenameParticipant(token:String, name:String) 
  case object PurgeChat
  case object ChatHistory
  case object ParticipantNumber
}