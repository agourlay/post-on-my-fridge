package pomf.service

import pomf.service.caching.PomfCachingActor
import pomf.service.notification.PomfNotificationActor
import pomf.domain.model.Fridge
import pomf.domain.model.Post
import pomf.domain.config.ProductionDB
import pomf.domain.config.DBConfig
import pomf.domain.model.FridgeRest
import pomf.domain.config.TestDB


trait PomfProdServiceLayer extends PomfActionService with ProductionDB {

}

trait PomfTestServiceLayer extends PomfActionService with TestDB {

}

trait PomfActionService { this: DBConfig =>

	def getAllFridge(): List[Fridge] = m.getAllFridge 
	
	def addFridge(fridge: Fridge): Fridge = m.addFridge(fridge)
	  
	def addPost(post: Post): Post = m.addPost(post)
	  
	def getFridgeRest(fridgeName: String):FridgeRest = m.getFridgeRest(fridgeName)
	  
	def getFridgeRss(fridgeName: String): scala.xml.Elem = m.getFridgeRss(fridgeName)
	  
	def getPost(id :Long):Option[Post] = m.getPost(id)
	  
	def searchByNameLike(term:String):List[String] = m.searchByNameLike(term)
	  
	def deletePost(id :Long) = m.deletePost(id)
	  
	def updatePost(post :Post):Option[Post] = m.updatePost(post)
  
}
