package pomf.service

import akka.actor.Actor
import akka.actor.ActorLogging

import pomf.domain.model._
import pomf.service.NotificationServiceProtocol._
import pomf.api.JsonSupport._


class NotificationService extends Actor with ActorLogging {
    
  def receive = {
    case PostCreated(post, token)                              => toEventStream(Notification.createPost(post, token))
    case PostUpdated(post, token)                              => toEventStream(Notification.updatePost(post, token))
    case PostDeleted(fridgeId, postId, token)                  => toEventStream(Notification.deletePost(fridgeId, postId, token))
    case MessageSent(fridgeId, message, token)                 => toEventStream(Notification.sendMessage(fridgeId, message, token))
    case ParticipantAdded(fridgeId, token, name)               => toEventStream(Notification.addParticipant(fridgeId, name, token))
    case ParticipantRemoved(fridgeId, token, name)             => toEventStream(Notification.removeParticipant(fridgeId, name, token))
    case ParticipantRenamed(fridgeId, token, newName, oldName) => toEventStream(Notification.renameParticipant(fridgeId, newName, oldName, token))
  }

  def toEventStream(n : Notification) = context.system.eventStream.publish(n)
}

object NotificationServiceProtocol {
  case class PostCreated(post : Post, token :String)
  case class PostUpdated(post : Post, token :String)
  case class PostDeleted(fridgeId : Long, id : Long, token :String)
  case class MessageSent(fridgeId: Long, message: ChatMessage, token: String)
  case class ParticipantAdded(fridgeId: Long, token:String, name:String)
  case class ParticipantRemoved(fridgeId: Long, token:String, name: String)
  case class ParticipantRenamed(fridgeId: Long, token:String, newName:String, oldName : String)
}