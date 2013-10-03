package pomf.service

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging
import pomf.service.ChatServiceProtocol._
import pomf.domain.model._
import pomf.api.JsonSupport._
import spray.caching.{LruCache, Cache}
import scala.concurrent.duration._
import scala.concurrent._


class ChatService(notificationService : ActorRef) extends Actor with ActorLogging {
    
  implicit def executionContext = context.dispatcher
  
  val cache: Cache[List[ChatMessage]] = LruCache(maxCapacity = 500, timeToIdle = 2 hour, timeToLive = 48 hour)
  
  val defaultResponse: Future[List[ChatMessage]] = future { List[ChatMessage]() }
  
  def receive = {
      case PushChat(fridgeName, message, token) => sender ! addChatMessage(fridgeName, message, token)
      case ChatHistory(fridgeName)              => sender ! retrieveChatHistory(fridgeName)
  }
  
  def addChatMessage(fridgeName: String, message: ChatMessage, token: String): ChatMessage = {
    val messagesInCache : Future[List[ChatMessage]] = retrieveChatHistory(fridgeName)
    messagesInCache.onSuccess { 
        case messages : List[ChatMessage] ⇒
             cache.remove(fridgeName)
             cache(fridgeName)((message :: messages).sortBy(_.timestamp))
      }
    notificationService ! Notification.message(fridgeName, message, token)
    message
  }
  
  def retrieveChatHistory(fridgeName: String): Future[List[ChatMessage]] = {
     val maybeMessages : Option[Future[List[ChatMessage]]] = cache.get(fridgeName)
     maybeMessages match{
         case None => defaultResponse
         case Some(futureMessage) => futureMessage
     }
  }
} 

object ChatServiceProtocol {
  case class PushChat(fridgeName: String, message: ChatMessage, token: String)
  case class ChatHistory(fridgeName: String)
}