package pomf.service

import akka.actor.Actor
import akka.actor.ActorLogging

import pomf.domain.model._
import pomf.service.NotificationServiceProtocol._
import pomf.api.JsonSupport._


class NotificationService extends Actor with ActorLogging {
    
  def receive = {
    case PostCreated(fridgeName , post, token)                   => toEventStream(Notification.createPost(fridgeName, post, token))
    case PostUpdated(fridgeName , post, token)                   => toEventStream(Notification.updatePost(fridgeName, post, token))
    case PostDeleted(fridgeName , id, token)                     => toEventStream(Notification.deletePost(fridgeName, id, token))
    case MessageSent(fridgeName, message, token)                 => toEventStream(Notification.sendMessage(fridgeName, message, token))
    case ParticipantAdded(fridgeName, token, name)               => toEventStream(Notification.addParticipant(fridgeName, name, token))
    case ParticipantRemoved(fridgeName, token, name)             => toEventStream(Notification.removeParticipant(fridgeName, name, token))
    case ParticipantRenamed(fridgeName, token, newName, oldName) => toEventStream(Notification.renameParticipant(fridgeName, newName, oldName, token))
  }

  def toEventStream(n : Notification) = context.system.eventStream.publish(n)
}

object NotificationServiceProtocol {
  case class PostCreated(fridgeName : String, post : Post, token :String)
  case class PostUpdated(fridgeName : String, post : Post, token :String)
  case class PostDeleted(fridgeName : String, id : Long, token :String)
  case class MessageSent(fridgeName: String, message: ChatMessage, token: String)
  case class ParticipantAdded(fridgeName: String, token:String, name:String)
  case class ParticipantRemoved(fridgeName: String, token:String, name: String)
  case class ParticipantRenamed(fridgeName: String, token:String, newName:String, oldName : String)
}