package pomf.domain.dao

import scala.slick.driver.PostgresDriver.simple._
import Database.dynamicSession

import org.joda.time.DateTime

import pomf.domain.model._

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Dao(db: Database){
  
  val log = LoggerFactory.getLogger("dao")

  val posts = TableQuery[Posts]
  val fridges = TableQuery[Fridges]

  val ddls = List(fridges.ddl, posts.ddl)

  val fridgeByName = fridges.findBy(_.name)
  val fridgeById = fridges.findBy(_.id)
  def fridgeByPostId(postId : Long) = fridgeById(postById(postId).first.fridgeId)

  val postById = posts.findBy(_.id)
  val postByFridgeId = posts.findBy(_.fridgeId)

  def addFridge(fridge: Fridge) = db withDynTransaction {
    fridges.insert(fridge)
    fridgeByName(fridge.name).firstOption.get
  }

  def createFridge(name : String) = db withDynTransaction {
    val fridge = Fridge(None , name, new DateTime(), new DateTime())
    addFridge(fridge)
  }  
  
  def getFridgeRest(fridgeId: Long) : FridgeRest = db withDynTransaction {
    fridgeById(fridgeId).firstOption match {
      case Some(f) => completeFridge(f)
      case None => throw new IllegalArgumentException("fridge does not exist")
    }							 
  }

  def getAllFridge(): List[FridgeRest] = db withDynTransaction{
    fridges.list.map(completeFridge(_))
  }  

  def completeFridge(f : Fridge) = FridgeRest(f.name, f.creationDate, f.modificationDate,f.id.get , postByFridgeId(f.id.get).list)
  
  def getPost(id :Long):Option[Post] = db withDynTransaction {
    postById(id).firstOption
  }  
  
  def searchByNameLike(term:String) = db withDynTransaction {
    fridges.filter(_.name like "%"+term+"%").list
  }  
  
  def deletePost(postId :Long) =  db withDynTransaction {
    val deleteQuery = postById(postId)
    deleteQuery.delete
  }  
  
  def addPost(post: Post): Option[Post] = db withDynTransaction {
    fridgeById(post.fridgeId).firstOption match {
      case Some(f) => {
        val id = (posts returning posts.map(_.id)) += post
        updateModificationDate(f.id.get)
        Some(post.copy(id = Some(id)))
      }
      case None => None
    }
  }

  def updatePost(post :Post) : Option[Post] = db withDynTransaction {
    postById(post.id.get).firstOption match {
      case None => None
      case Some(p) => {
        updateModificationDate(post.fridgeId)
        postById(p.id.get).update(post)
        postById(p.id.get).firstOption
      }
    }
  }

  def countPosts() = db withDynTransaction {
    posts.length.run
  }  

  def countFridges() = db withDynTransaction {
    fridges.length.run
  }  

  def updateModificationDate(fridgeId: Long) = {
      fridges.filter(_.id === fridgeId).map(_.modificationDate).update(new DateTime())
  }

  def createDB() = db withDynTransaction {
    try {
      ddls.foreach(_.create)
      log.info("Database schema created")
    } catch {
      case e: Exception => log.info("Could not create db ... assuming it already exist")
    }
  }

  def dropDB() = db withDynTransaction {
    try {
      ddls.foreach(_.drop)
      log.info("Database schema dropped")
    } catch {
      case e: Exception => log.error("Could not drop db : {} ", e)
    }
  }

  def purgeDB() = db withDynTransaction { 
    dropDB
    createDB 
  }
}
