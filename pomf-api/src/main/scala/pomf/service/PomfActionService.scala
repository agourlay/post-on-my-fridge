package pomf.service

import pomf.service.notification.PomfNotificationActor
import pomf.domain.model.Fridge
import pomf.domain.model.Post
import pomf.domain.config.ProductionDB
import pomf.domain.config.DBConfig
import pomf.domain.model.FridgeRest
import pomf.domain.config.TestDB
import pomf.service.caching.PomfCachingService

trait PomfProdServiceLayer extends PomfActionService with PomfCachingService with ProductionDB  {

}

trait PomfTestServiceLayer extends PomfActionService with PomfCachingService with TestDB {

}

trait PomfActionService extends { 
    this: DBConfig =>

	def getAllFridge(): List[Fridge] = dao.getAllFridge
	
	def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)
	  
	def addPost(post: Post): Post = dao.addPost(post)
	  
	def getFridgeRest(fridgeName: String):FridgeRest = dao.getFridgeRest(fridgeName)
	  
	def getFridgeRss(fridgeName: String): scala.xml.Elem = dao.getFridgeRss(fridgeName)
	  
	def getPost(id :Long):Option[Post] = dao.getPost(id)
	  
	def searchByNameLike(term:String):List[String] = dao.searchByNameLike(term)
	  
	def deletePost(id :Long) = dao.deletePost(id)
	  
	def updatePost(post :Post):Option[Post] = dao.updatePost(post)
  
}
