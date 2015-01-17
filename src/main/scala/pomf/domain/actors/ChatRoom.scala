package pomf.domain.actors

import akka.actor.Props

import scala.concurrent.duration._
import scala.language.postfixOps

import java.util.UUID

import pomf.domain.model._
import pomf.api.endpoint.JsonSupport
import pomf.domain.actors.ChatRoomProtocol._
import pomf.util.CustomOrdering._
import pomf.core.actors.CommonActor

class ChatRoom(fridgeId: UUID) extends CommonActor with JsonSupport {

  implicit val executionContext = context.dispatcher

  val messages = scala.collection.mutable.Map.empty[Long, ChatMessage]
  val participantByToken = scala.collection.mutable.Map.empty[String, String]

  context.system.scheduler.schedule(1 hour, 1 hour, self, ChatRoomProtocol.PurgeChat)

  def receive = {
    case SendMessage(message, token)       ⇒ sender ! addChatMessage(message, token)
    case ChatHistory                       ⇒ sender ! retrieveChatHistory
    case PurgeChat                         ⇒ purgeState()
    case AddParticipant(token, name)       ⇒ sender ! addParticipant(token, name)
    case RemoveParticipant(token)          ⇒ sender ! removeParticipant(token)
    case ParticipantNumber                 ⇒ sender ! ParticipantNumberRoom(participantByToken.size)
    case RenameParticipant(token, newName) ⇒ sender ! renameParticipant(token, newName)
  }

  def addParticipant(token: String, name: String): String = {
    toEventStream(Notification.addParticipant(fridgeId, name, token))
    participantByToken += (token -> name)
    s"$name joined chat $fridgeId"
  }

  def addChatMessage(message: ChatMessage, token: String) = {
    messages += (System.currentTimeMillis -> message)
    toEventStream(Notification.sendMessage(fridgeId, message, token))
    message
  }

  def retrieveChatHistory = ChatHistoryContent(messages.values.toVector.sortBy(_.timestamp))

  def removeParticipant(token: String) = {
    val nameQuitter = participantByToken.getOrElse(token, "Anonymous")
    participantByToken -= token
    toEventStream(Notification.removeParticipant(fridgeId, nameQuitter, token))
    s"$token removed from chat $fridgeId"
  }

  def renameParticipant(token: String, newName: String) = {
    val formerName = participantByToken.getOrElse(token, "Anonymous")
    participantByToken += (token -> newName)
    toEventStream(Notification.renameParticipant(fridgeId, token, newName, formerName))
    s"$newName changed name"
  }

  def purgeState() = {
    messages.clear()
    participantByToken.clear()
  }

  def toEventStream(n: Notification) = context.system.eventStream.publish(n)
}

object ChatRoomProtocol {
  trait ChatRoomMessage
  case class SendMessage(message: ChatMessage, token: String) extends ChatRoomMessage
  case class AddParticipant(token: String, name: String) extends ChatRoomMessage
  case class RemoveParticipant(token: String) extends ChatRoomMessage
  case class RenameParticipant(token: String, name: String) extends ChatRoomMessage
  case object PurgeChat extends ChatRoomMessage
  case object ChatHistory extends ChatRoomMessage
  case class ChatHistoryContent(messages: Vector[ChatMessage]) extends ChatRoomMessage
  case object ParticipantNumber extends ChatRoomMessage
  case class ParticipantNumberRoom(counter: Int) extends ChatRoomMessage
}

object ChatRoom {
  def props(fridgeId: UUID) = Props(classOf[ChatRoom], fridgeId)
}