package pomf.service

import pomf.service.notification.PomfNotificationActor
import pomf.domain.model.Fridge
import pomf.domain.model.Post
import pomf.domain.config.ProductionDB
import pomf.domain.config.DBConfig
import pomf.domain.model.FridgeRest
import pomf.domain.config.TestDB
import pomf.service.caching.PomfCachingService
import akka.actor.Actor
import akka.actor.Props
import scala.compat.Platform
import pomf.boot.Boot
import pomf.domain.model.ChatMessage
import com.redis.serialization.Parse
import pomf.service.rest.JsonImplicits
import pomf.domain.model.NotificationObj

trait PomfProdServiceLayer extends PomfActionService with PomfCachingService with ProductionDB  {

}

trait PomfTestServiceLayer extends PomfActionService with PomfCachingService with TestDB {

}

trait PomfActionService{ 
    this: DBConfig =>
    import Parse.Implicits._
    import JsonImplicits._
          
	def getAllFridge(): List[Fridge] = dao.getAllFridge
	
	def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)
	  
	def addPost(post: Post): Post = {
	  val persistedPost = dao.addPost(post)
	  Boot.notificationService ! NotificationObj.create(post.fridgeId, persistedPost)
	  persistedPost
	}
	  
	def getFridgeRest(fridgeName: String):FridgeRest = dao.getFridgeRest(fridgeName)
	  
	def getFridgeRss(fridgeName: String): scala.xml.Elem = dao.getFridgeRss(fridgeName)
	  
	def getPost(id :Long):Option[Post] = dao.getPost(id)
	  
	def searchByNameLike(term:String):List[String] = dao.searchByNameLike(term)
	  
	def deletePost(id :Long) = {
	  Boot.notificationService ! NotificationObj.delete(getPost(id).get.fridgeId, id)
	  dao.deletePost(id)
	}
	  
	def updatePost(post :Post):Option[Post] = {
	  Boot.notificationService ! NotificationObj.update(post.fridgeId, post)
	  dao.updatePost(post)
	}
	
	def addChatMessage(fridgeName : String, message: ChatMessage):ChatMessage = {
	  //cache.lpush(fridgeName+".chat", message)
	  Boot.notificationService ! NotificationObj.message(fridgeName, message)
	  message
	} 
  	
	def retrieveChatHistory(fridgeName: String):List[ChatMessage] = {
	  //cache.get[List[ChatMessage]](fridgeName+".chat")
	  List()
	} 
}
