package pomf.domain.dao

import scala.slick.driver.PostgresDriver.simple._

import org.joda.time.DateTime

import pomf.domain.model._

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Dao(db: Database){

  implicit val implicitSession = db.createSession
  
  val logger: Logger = LoggerFactory.getLogger("dao")

  val posts = TableQuery[Posts]
  val fridges = TableQuery[Fridges]

  val ddls = List(posts.ddl, fridges.ddl)

  def addFridge(fridge: Fridge) = {
    fridges.insert(fridge)
    fridgeByName(fridge.name).get
  }  
  
  def getFridgeRest(fridgeName: String) : FridgeRest = {
    val fridgeOpt = fridgeByName(fridgeName)
    fridgeOpt match {
      case Some(f) => completeFridge(f)
      case None => FridgeRest(name = fridgeName, new DateTime(), new DateTime(), posts = List[Post](), id = None)
    }							 
  }

  def fridgeByName(fridgeName: String) : Option[Fridge] = fridges.filter(_.name === fridgeName).firstOption

  def getAllFridge(): List[FridgeRest] = fridges.list.map(completeFridge(_))

  def completeFridge(f : Fridge) = FridgeRest(f.name, f.creationDate, f.modificationDate,f.id, posts.filter(_.fridgeId === f.name).list)
  
  def getPost(id :Long):Option[Post] = posts.filter(_.id === id).firstOption
  
  def searchByNameLike(term:String):List[String] = fridges.filter(_.name like "%"+term+"%").map(_.name).list
  
  def deletePost(postId :Long) = {
    val deleteQuery = posts.filter(_.id === postId)
    deleteQuery.delete
  }  
  
  def addPost(post: Post): Post = {
    fridgeByName(post.fridgeId).getOrElse(addFridge(Fridge(name = post.fridgeId, creationDate = new DateTime(), modificationDate = new DateTime()))) 
    val id = posts.insert(post)
    updateModificationDate(post.fridgeId)
    post.copy(id = Some(id))
  }

  def updatePost(post :Post):Option[Post] = {
    updateModificationDate(post.fridgeId)
    posts.filter(_.id === post.id).update(post)
    posts.filter(_.id === post.id).firstOption
  }

  def deleteOutdatedPost() = {
    val now = new DateTime()
    //val deleteQuery: Query[Posts,Post] = posts.filter( p => p.dueDate.isDefined && p.dueDate.get.isBefore(now))
    //deleteQuery.delete 
  }

  def countPosts() = posts.length

  def countFridges() = fridges.length

  def updateModificationDate(fridgeName: String) = {
    fridges.filter(_.name === fridgeName).map(_.modificationDate).update(new DateTime())
  }

  def createDB() = {
    try {
      ddls.foreach(_.create)
    } catch {
      case e: Exception => logger.info("Could not create database.... assuming it already exists")
    }
  }

  def dropDB() = {
    try {
      ddls.foreach(_.drop)
    } catch {
      case e: Exception => logger.info("Could not drop database")
    }
  }

  def purgeDB() = { dropDB; createDB }
}
