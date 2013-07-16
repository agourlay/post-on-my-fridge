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
import spray.caching.{LruCache, Cache}
import spray.util._
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._


class ChatServiceActor extends Actor with ActorLogging {
      
  val notification = "/user/notification-service"
  
  val cache: Cache[List[ChatMessage]] = LruCache(maxCapacity = 500, timeToIdle = Duration(2, HOURS), timeToLive = Duration(48, HOURS))
  
  
  def receive = {
      case PushChat(fridgeName, message, token) => sender ! addChatMessage(fridgeName, message, token)
      case ChatHistory(fridgeName)              => sender ! retrieveChatHistory(fridgeName)
  }
  
  def addChatMessage(fridgeName: String, message: ChatMessage, token: String): ChatMessage = {
    cache(fridgeName, cache.get(fridgeName).getOrElse(List()) :: message)
    context.actorSelection(notification) ! Notification.message(fridgeName, message, token)
    message
  }

  def retrieveChatHistory(fridgeName: String): List[ChatMessage] = {
   cache.get(fridgeName).getOrElse(List()).sortBy(_.timestamp)
  }
} 

object ChatServiceActor {
  case class PushChat(fridgeName: String, message: ChatMessage, token: String)
  case class ChatHistory(fridgeName: String)
}