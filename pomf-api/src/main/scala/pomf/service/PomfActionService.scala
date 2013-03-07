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
import pomf.domain.model.Notification
import pomf.domain.model.ChatMessage

trait PomfProdServiceLayer extends PomfActionService with PomfCachingService with ProductionDB  {

}

trait PomfTestServiceLayer extends PomfActionService with PomfCachingService with TestDB {

}

trait PomfActionService{ 
    this: DBConfig =>
      
	def getAllFridge(): List[Fridge] = dao.getAllFridge
	
	def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)
	  
	def addPost(post: Post): Post = {
	  Boot.notificationService ! Notification(post.fridgeId,"refresh","none", "none",Platform.currentTime)
	  dao.addPost(post)
	}
	  
	def getFridgeRest(fridgeName: String):FridgeRest = dao.getFridgeRest(fridgeName)
	  
	def getFridgeRss(fridgeName: String): scala.xml.Elem = dao.getFridgeRss(fridgeName)
	  
	def getPost(id :Long):Option[Post] = dao.getPost(id)
	  
	def searchByNameLike(term:String):List[String] = dao.searchByNameLike(term)
	  
	def deletePost(id :Long) = {
	  Boot.notificationService ! Notification(getPost(id).get.fridgeId,"refresh","none", "none",Platform.currentTime)
	  dao.deletePost(id)
	}
	  
	def updatePost(post :Post):Option[Post] = {
	  Boot.notificationService ! Notification(post.fridgeId,"refresh","none", "none",Platform.currentTime)
	  dao.updatePost(post)
	}
	
	def addChatMessage(message: ChatMessage):ChatMessage = {
	  Boot.notificationService ! Notification(message.fridgeName,"message",message.user, message.message,Platform.currentTime)
	  message
	} 
  
}
