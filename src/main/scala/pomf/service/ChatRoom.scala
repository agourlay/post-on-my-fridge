package pomf.service

import akka.actor._

import pomf.domain.model._
import pomf.service.ChatRoomProtocol._

import scala.concurrent.duration._
import scala.language.postfixOps

class ChatRoom extends Actor with ActorLogging {

	implicit def executionContext = context.dispatcher

    var messages = Map.empty[Long, ChatMessage]
	
	var participantNumber : Int = 0

	context.system.scheduler.scheduleOnce(24 hour,self, ChatRoomProtocol.PurgeChat)

	def receive = {
      case AddMessage(message, token) => addChatMessage(message, token)
      case ChatHistory                => sender ! retrieveChatHistory
      case PurgeChat                  => messages = Map.empty[Long, ChatMessage]
      case AddParticipant			  => participantNumber + 1
      case RemoveParticipant		  => participantNumber - 1
  	}

	def addChatMessage(message: ChatMessage, token: String) = {
		messages += (System.currentTimeMillis -> message)
  	}
  
  	def retrieveChatHistory : List[ChatMessage] = {
     	messages.values.toList.sortBy(_.timestamp)
    }
}

object ChatRoomProtocol {
  case class AddMessage(message: ChatMessage, token: String)	
  case object PurgeChat
  case object ChatHistory
  case object AddParticipant
  case object RemoveParticipant
}