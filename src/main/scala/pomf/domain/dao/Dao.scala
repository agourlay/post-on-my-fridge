package pomf.domain.dao

import scala.slick.session.Database
import org.joda.time.DateTime
import pomf.domain.config.DAL
import pomf.domain.model.Fridge
import pomf.domain.model.FridgeRest
import pomf.domain.model.Post
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Dao(name: String, dal: DAL, db: Database) {
  // We only need the DB/session imports outside the DAL
  import dal._

  // Put an implicitSession in scope for database actions
  implicit val implicitSession = db.createSession
  
  val logger: Logger = LoggerFactory.getLogger("dao")

  def createDB = dal.create

  def dropDB = dal.drop
  
  def purgeDB = dal.purge

  def addFridge(fridge: Fridge): Fridge = Fridges.insert(fridge)
  
  def getFridgeRest(fridgeName: String):FridgeRest = {
    val fridgeOpt:Option[Fridge] = Fridges.findByName(fridgeName)
    fridgeOpt match {
      case Some(fridge) => completeFridge(fridge)
      case _ => FridgeRest(name = fridgeName, description = "", new DateTime(), new DateTime(), posts = List[Post](), id = None)
    }							 
  }

  def getAllFridge(): List[FridgeRest] = Fridges.findAllFridge.map(completeFridge(_))

  def completeFridge(f : Fridge):FridgeRest = FridgeRest(f.name, f.description, f.creationDate, f.modificationDate,f.id, Posts.findPostByFridge(f.name))
  
  def getPost(id :Long):Option[Post] = Posts.getPost(id)
  
  def searchByNameLike(term:String):List[String] = Fridges.searchByNameLike(term)
  
  def deletePost(id :Long):Long = {
    Posts.deletePost(id)
  }  
  
  def addPost(post: Post): Post = {
    Fridges.findByName(post.fridgeId).getOrElse(addFridge(Fridge(name = post.fridgeId, creationDate = new DateTime(), modificationDate = new DateTime()))) 
    val persistedPost = Posts.insert(post)
    Fridges.updateModificationDate(post.fridgeId)
    persistedPost
  }

  def updatePost(post :Post):Option[Post] = {
    Fridges.updateModificationDate(post.fridgeId)
    Posts.updatePost(post)
  }

  def deleteOutdatedPost() = Posts.deleteOutdatedPost

  def countPosts() = Posts.count

  def countFridges() = Fridges.count
}
