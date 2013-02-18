package pomf.domain.dao

import scala.slick.session.Database
import pomf.domain.config.DAL
import pomf.domain.model.Fridge
import pomf.domain.model.FridgeRest
import pomf.domain.model.Post

class Dao(name: String, dal: DAL, db: Database) {
  // We only need the DB/session imports outside the DAL
  import dal._
import dal.profile.simple._

  // Put an implicitSession in scope for database actions
  implicit val implicitSession = db.createSession

  def createDB = dal.create

  def dropDB = dal.drop
  
  def purgeDB = dal.purge

   def getAllFridge(): List[Fridge] = {
    val result = Fridges.findAllFridge
    println("Got fridge: " + result)
    result
  }

  def addFridge(fridge: Fridge): Fridge = {
    val result = Fridges.insert(fridge)
    println("Inserted fridge: " + result)
    result
  }
  
   def addPost(post: Post): Post = {
    val result = Posts.insert(post)
    println("Inserted post: " + result)
    result
  }
  
  def getFridgeRest(fridgeName: String):Option[FridgeRest] = {
    val fridgeOpt:Option[Fridge] = Fridges.findByName(fridgeName)
    fridgeOpt match {
      case Some(fridge) => Some(FridgeRest(fridge.name, fridge.description ,fridge.id, Posts.findPostByFridge(fridgeName)))
      case _ => None
    }							 
  }
  
  def getPost(id :Long):Option[Post] = Posts.getPost(id)
  
  def deletePost(id :Long) = Posts.deletePost(id)
  
  def updatePost(post :Post):Option[Post] = Posts.updatePost(post)
}