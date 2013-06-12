package pomf.service

import pomf.api.JsonImplicits._
import akka.actor.Actor
import akka.actor.actorRef2Scala
import pomf.Boot
import pomf.domain.config._
import pomf.domain.model._
import com.redis.serialization.Parse
import pomf.service.CrudServiceActor._

class CrudServiceActor extends Actor with PomfCachingService with ProductionDB { 
  this: DBConfig =>
  import Parse.Implicits._

  def actorRefFactory = context
  
  val notificationService = actorRefFactory.actorFor("notification-router")

  def receive = {
    case fridgeName: String => getFridgeRest(fridgeName)
    case fridge: Fridge => addFridge(fridge)
    case postId: Long => getPost(postId)
    case DeletePost(postId, token) => deletePost(postId, token)
    case CreatePost(post, token) => addPost(post, token)
    case UpdatePost(post, token) => updatePost(post, token)
    case FridgeRss(fridgeName) => getFridgeRss(fridgeName)
    case SearchFridge(term) => searchByNameLike(term)
    case PushChat(fridgeName, message, token) => addChatMessage(fridgeName, message, token)
    case ChatHistory(fridgeName) => retrieveChatHistory(fridgeName)
  }

  def getAllFridge(): List[Fridge] = dao.getAllFridge

  def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)

  def addPost(post: Post, token: String): Post = {
    val persistedPost = dao.addPost(post)
    notificationService ! Notifications.create(post.fridgeId, persistedPost, token)
    persistedPost
  }

  def getFridgeRest(fridgeName: String): FridgeRest = dao.getFridgeRest(fridgeName)

  def getFridgeRss(fridgeName: String): scala.xml.Elem = dao.getFridgeRss(fridgeName)

  def getPost(id: Long): Option[Post] = dao.getPost(id)

  def searchByNameLike(term: String): List[String] = dao.searchByNameLike(term)

  def deletePost(id: Long, token: String): Long = {
    notificationService ! Notifications.delete(getPost(id).get.fridgeId, id, token)
    dao.deletePost(id)
  }

  def updatePost(post: Post, token: String): Option[Post] = {
    notificationService ! Notifications.update(post.fridgeId, post, token)
    dao.updatePost(post)
  }

  def addChatMessage(fridgeName: String, message: ChatMessage, token: String): ChatMessage = {
    //cache.lpush(fridgeName+".chat", message)
    notificationService ! Notifications.message(fridgeName, message, token)
    message
  }

  def retrieveChatHistory(fridgeName: String): List[ChatMessage] = {
    //cache.get[List[ChatMessage]](fridgeName+".chat")
    List()
  }
}

object CrudServiceActor {
  case class UpdatePost(post: Post, token: String)
  case class CreatePost(post: Post, token: String)
  case class DeletePost(postId: Long, token: String)
  case class FridgeRss(fridgeName: String)
  case class SearchFridge(term: String)
  case class PushChat(fridgeName: String, message: ChatMessage, token: String)
  case class ChatHistory(fridgeName: String)
}