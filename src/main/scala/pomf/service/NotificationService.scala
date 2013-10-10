package pomf.service

import akka.actor.Actor
import akka.actor.ActorLogging

import pomf.domain.model._
import pomf.service.NotificationServiceProtocol._
import pomf.api.JsonSupport._


class NotificationService extends Actor with ActorLogging {
    
  def receive = {
  	case PostCreated(fridgeName , post, token)       => pushToEventStream(Notification.createPost(fridgeName, post, token))
    case PostUpdated(fridgeName , post, token)       => pushToEventStream(Notification.updatePost(fridgeName, post, token))
    case PostDeleted(fridgeName , id, token)         => pushToEventStream(Notification.deletePost(fridgeName, id, token))
    case MessageSent(fridgeName, message, token)     => pushToEventStream(Notification.sendMessage(fridgeName, message, token))
    case ParticipantAdded(fridgeName, token, name)   => pushToEventStream(Notification.addParticipant(fridgeName, name, token))
    case ParticipantRemoved(fridgeName, token, name) => pushToEventStream(Notification.removeParticipant(fridgeName, name, token))
    case ParticipantRenamed(fridgeName, token, name) => pushToEventStream(Notification.renameParticipant(fridgeName, name, token))
  }

  def pushToEventStream(n : Notification) = context.system.eventStream.publish(n)
}

object NotificationServiceProtocol {
  case class PostCreated(fridgeName : String, post : Post, token :String)
  case class PostUpdated(fridgeName : String, post : Post, token :String)
  case class PostDeleted(fridgeName : String, id : Long, token :String)
  case class MessageSent(fridgeName: String, message: ChatMessage, token: String)
  case class ParticipantAdded(fridgeName: String, token:String, name:String)
  case class ParticipantRemoved(fridgeName: String, token:String, name: String)
  case class ParticipantRenamed(fridgeName: String, token:String, name:String)
}