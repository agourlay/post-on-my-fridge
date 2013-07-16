package pomf.service

import akka.actor.Actor
import akka.actor.Props
import spray.json.DefaultJsonProtocol._
import scala.io.Codec
import spray.json.JsValue
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging
import pomf.domain.model.ChatMessage
import pomf.service.ChatServiceActor._
import pomf.domain.model._
import pomf.api.JsonSupport._


class ChatServiceActor extends Actor with ActorLogging {
      
  val notification = "/user/notification-service"
  
  def receive = {
      case PushChat(fridgeName, message, token) => sender ! addChatMessage(fridgeName, message, token)
      case ChatHistory(fridgeName)              => sender ! retrieveChatHistory(fridgeName)
  }
  
  def addChatMessage(fridgeName: String, message: ChatMessage, token: String): ChatMessage = {
    //send to fridge chat history
    context.actorSelection(notification) ! Notification.message(fridgeName, message, token)
    message
  }

  def retrieveChatHistory(fridgeName: String): List[ChatMessage] = {
    //get fridge chat history
    List()
  }
} 

object ChatServiceActor {
  case class PushChat(fridgeName: String, message: ChatMessage, token: String)
  case class ChatHistory(fridgeName: String)
}