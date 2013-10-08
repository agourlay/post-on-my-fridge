package pomf.service

import akka.actor.Actor
import akka.actor.ActorLogging

import pomf.domain.model._
import pomf.service.NotificationProtocol._


class NotificationService extends Actor with ActorLogging {
    
  def receive = {
    case n : Notification => context.system.eventStream.publish(n)
  }
}

object NotificationProtocol {
  case class SendMessage(fridgeName: String, message: ChatMessage, token: String)
  case class AddParticipant(fridgeName: String, token:String, name:String)
  case class RemoveParticipant(fridgeName: String, token:String)
  case class RenameParticipant(fridgeName: String, token:String, name:String)
}