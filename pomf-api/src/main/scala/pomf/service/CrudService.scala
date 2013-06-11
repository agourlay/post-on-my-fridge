package pomf.service

import JsonImplicits.impChatMessage
import JsonImplicits.impPost
import akka.actor.Actor
import akka.actor.actorRef2Scala
import pomf.boot.Boot
import pomf.domain.config._
import pomf.domain.model._
import com.redis.serialization.Parse
import pomf.service.CrudServiceActor._

class CrudServiceActor extends Actor with PomfActionService with PomfCachingService with ProductionDB  {
    def receive = {
    	case fridgeName:String => getFridgeRest(fridgeName)
    	case fridge:Fridge => addFridge(fridge)
    	case postId:Long => getPost(postId)
    	case (postId:Long, token:String) => deletePost(postId, token)
    	case CreatePost(post, token) => addPost(post, token)
        case UpdatePost(post, token) => updatePost(post, token)
    }  
}

object CrudServiceActor {
  case class UpdatePost(post: Post, token: String)
  case class CreatePost(post: Post, token: String)
}

trait PomfActionService{ 
    this: DBConfig =>
    import Parse.Implicits._
    import JsonImplicits._
          
	def getAllFridge(): List[Fridge] = dao.getAllFridge
	
	def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)
	  
	def addPost(post: Post, token : String): Post = {
	  val persistedPost = dao.addPost(post)
	  Boot.notificationService ! Notifications.create(post.fridgeId, persistedPost, token)
	  persistedPost
	}
	  
	def getFridgeRest(fridgeName: String):FridgeRest = dao.getFridgeRest(fridgeName)
	  
	def getFridgeRss(fridgeName: String): scala.xml.Elem = dao.getFridgeRss(fridgeName)
	  
	def getPost(id :Long):Option[Post] = dao.getPost(id)
	  
	def searchByNameLike(term:String):List[String] = dao.searchByNameLike(term)
	  
	def deletePost(id :Long, token : String) = {
	  Boot.notificationService ! Notifications.delete(getPost(id).get.fridgeId, id, token)
	  dao.deletePost(id)
	}
	  
	def updatePost(post :Post, token : String):Option[Post] = {
	  Boot.notificationService ! Notifications.update(post.fridgeId, post, token)
	  dao.updatePost(post)
	}
	
	def addChatMessage(fridgeName : String, message: ChatMessage, token : String):ChatMessage = {
	  //cache.lpush(fridgeName+".chat", message)
	  Boot.notificationService ! Notifications.message(fridgeName, message, token)
	  message
	} 
  	
	def retrieveChatHistory(fridgeName: String):List[ChatMessage] = {
	  //cache.get[List[ChatMessage]](fridgeName+".chat")
	  List()
	} 
}
