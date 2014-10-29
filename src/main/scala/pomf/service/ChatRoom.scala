package pomf.service

import akka.actor.{ Actor, ActorRef, Props }

import scala.concurrent.duration._
import scala.language.postfixOps

import java.util.UUID

import pomf.domain.model._
import pomf.service.ChatRoomProtocol._
import pomf.util.CustomOrdering._
import pomf.core.actors.CommonActor

class ChatRoom(fridgeId: UUID, notificationService: ActorRef) extends CommonActor {

  implicit def executionContext = context.dispatcher

  val messages = scala.collection.mutable.Map.empty[Long, ChatMessage]
  val participantByToken = scala.collection.mutable.Map.empty[String, String]

  context.system.scheduler.schedule(1 hour, 1 hour, self, ChatRoomProtocol.PurgeChat)

  def receive = {
    case SendMessage(message, token)       ⇒ addChatMessage(message, token)
    case ChatHistory                       ⇒ sender ! retrieveChatHistory
    case PurgeChat                         ⇒ purgeState()
    case AddParticipant(token, name)       ⇒ addParticipant(token, name)
    case RemoveParticipant(token)          ⇒ removeParticipant(token)
    case ParticipantNumber                 ⇒ sender ! ParticipantNumberRoom(participantByToken.size)
    case RenameParticipant(token, newName) ⇒ renameParticipant(token, newName)
  }

  def addParticipant(token: String, name: String) {
    notificationService ! NotificationServiceProtocol.ParticipantAdded(fridgeId, token, name)
    participantByToken += (token -> name)
  }

  def addChatMessage(message: ChatMessage, token: String) = {
    messages += (System.currentTimeMillis -> message)
    notificationService ! NotificationServiceProtocol.MessageSent(fridgeId, message, token)
  }

  def retrieveChatHistory = ChatHistoryContent(messages.values.toVector.sortBy(_.timestamp))

  def removeParticipant(token: String) {
    val nameQuitter = participantByToken.get(token).getOrElse("Anonymous")
    participantByToken -= token
    notificationService ! NotificationServiceProtocol.ParticipantRemoved(fridgeId, token, nameQuitter)
  }

  def renameParticipant(token: String, newName: String) {
    val formerName = participantByToken.get(token).getOrElse("Anonymous")
    participantByToken += (token -> newName)
    notificationService ! NotificationServiceProtocol.ParticipantRenamed(fridgeId, token, newName, formerName)
  }

  def purgeState() = {
    messages.clear()
    participantByToken.clear()
  }
}

object ChatRoomProtocol {
  case class SendMessage(message: ChatMessage, token: String)
  case class AddParticipant(token: String, name: String)
  case class RemoveParticipant(token: String)
  case class RenameParticipant(token: String, name: String)
  case object PurgeChat
  case object ChatHistory
  case class ChatHistoryContent(messages: Vector[ChatMessage])
  case object ParticipantNumber
  case class ParticipantNumberRoom(counter: Int)
}

object ChatRoom {
  def props(fridgeId: UUID, notificationService: ActorRef) = Props(classOf[ChatRoom], fridgeId, notificationService)
}