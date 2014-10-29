package pomf.service

import akka.actor.{ Actor, ActorRef, Props }

import pomf.domain.model._
import pomf.service.NotificationServiceProtocol._
import pomf.api.endpoint.JsonSupport._
import pomf.core.actors.CommonActor

import java.util.UUID

class NotificationService extends CommonActor {

  def receive = {
    case PostCreated(post, token)                              ⇒ toEventStream(Notification.createPost(post, token))
    case PostUpdated(post, token)                              ⇒ toEventStream(Notification.updatePost(post, token))
    case PostDeleted(fridgeId, postId, token)                  ⇒ toEventStream(Notification.deletePost(fridgeId, postId, token))
    case MessageSent(fridgeId, message, token)                 ⇒ toEventStream(Notification.sendMessage(fridgeId, message, token))
    case ParticipantAdded(fridgeId, token, name)               ⇒ toEventStream(Notification.addParticipant(fridgeId, name, token))
    case ParticipantRemoved(fridgeId, token, name)             ⇒ toEventStream(Notification.removeParticipant(fridgeId, name, token))
    case ParticipantRenamed(fridgeId, token, newName, oldName) ⇒ toEventStream(Notification.renameParticipant(fridgeId, newName, oldName, token))
  }

  def toEventStream(n: Notification) = context.system.eventStream.publish(n)
}

object NotificationServiceProtocol {
  case class PostCreated(post: Post, token: String)
  case class PostUpdated(post: Post, token: String)
  case class PostDeleted(fridgeId: UUID, id: UUID, token: String)
  case class MessageSent(fridgeId: UUID, message: ChatMessage, token: String)
  case class ParticipantAdded(fridgeId: UUID, token: String, name: String)
  case class ParticipantRemoved(fridgeId: UUID, token: String, name: String)
  case class ParticipantRenamed(fridgeId: UUID, token: String, newName: String, oldName: String)
}

object NotificationService {
  def props() = Props(classOf[NotificationService])
}