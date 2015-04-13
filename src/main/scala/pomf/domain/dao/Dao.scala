package pomf.domain.dao

import org.slf4j.LoggerFactory
import pomf.domain.FridgeAlreadyExistsException

import scala.slick.driver.PostgresDriver.simple._
import scala.util._

import Database.dynamicSession

import java.util.UUID

import org.joda.time.DateTime

import pomf.core.metrics.Instrumented
import pomf.domain.model._

class Dao(db: Database) extends Instrumented {

  metrics.gauge("posts")(countPosts())
  metrics.gauge("fridges")(countFridges())

  val log = LoggerFactory.getLogger("domain.dao")

  val posts = TableQuery[Posts]
  val fridges = TableQuery[Fridges]

  val fridgeByName = fridges.findBy(_.name)

  val fridgeById = fridges.findBy(_.id)

  val postById = posts.findBy(_.id)

  val postByFridgeId = posts.findBy(_.fridgeId)

  def countPostForFridge(id: Column[UUID]) = {
    val query = for {
      p ← posts
      if p.fridgeId === id
    } yield 1
    query.length.run
  }

  def getFridgeById(id: UUID) = db withDynSession {
    fridgeById(id).firstOption.get
  }

  def createFridge(name: String): Try[UUID] = db withDynTransaction {
    fridgeByName(name).firstOption.fold {
      val fridge = Fridge(Some(UUID.randomUUID()), name, new DateTime(), new DateTime())
      Try((fridges returning fridges.map(_.id)) += fridge)
    } { f ⇒ Failure(new FridgeAlreadyExistsException(f.id.get)) }
  }

  def getFridgeFull(fridgeId: UUID): Option[FridgeFull] = db withDynSession {
    fridgeById(fridgeId).firstOption.map(f ⇒ buildFull(f))
  }

  def getAllFridge(pageNumber: Int, pageSize: Int): List[FridgeLight] = db withDynSession {
    fridges.sortBy(_.modificationDate.desc)
      .drop((pageNumber - 1) * pageSize).take(pageSize)
      .list
      .map(buildLight)
  }

  def buildFull(f: Fridge) = {
    val posts = postByFridgeId(f.id.get).list
    FridgeFull(f.name, f.creationDate, f.modificationDate, f.id.get, posts.size, posts)
  }

  def buildLight(f: Fridge) = {
    FridgeLight(f.name, f.creationDate, f.modificationDate, f.id.get, countPostForFridge(f.id.get))
  }

  def getPost(id: UUID): Option[Post] = db withDynSession {
    postById(id).firstOption
  }

  def searchByNameLike(term: String) = db withDynSession {
    fridges.filter(_.name.toLowerCase like "%" + term.toLowerCase + "%")
      .take(100)
      .list
  }

  def deletePost(postId: UUID) = db withDynTransaction {
    val postQuery = postById(postId)
    postQuery.firstOption.map { p ⇒
      postQuery.delete
      updateModificationDate(p.fridgeId)
    }
  }

  def addPost(post: Post): Option[Post] = db withDynTransaction {
    fridgeById(post.fridgeId).firstOption.map { f ⇒
      val toPersist = post.copy(id = Some(UUID.randomUUID()))
      posts.insert(toPersist)
      updateModificationDate(f.id.get)
      toPersist
    }
  }

  def updatePost(post: Post): Option[Post] = db withDynTransaction {
    postById(post.id.get).firstOption.flatMap { p ⇒
      updateModificationDate(post.fridgeId)
      postById(p.id.get).update(post)
      postById(p.id.get).firstOption
    }
  }

  def countPosts() = db withDynSession {
    posts.length.run
  }

  def countFridges() = db withDynSession {
    fridges.length.run
  }

  def updateModificationDate(fridgeId: UUID) = {
    fridges.filter(_.id === fridgeId).map(_.modificationDate).update(new DateTime())
  }

  def createDB() = db withDynTransaction {
    try {
      List(fridges.ddl, posts.ddl).foreach(_.create)
      log.info("Database schema created")
    } catch {
      case e: Exception ⇒ log.info("Could not create db ... assuming it already exist")
    }
  }

  def dropDB() = db withDynTransaction {
    try {
      List(posts.ddl, fridges.ddl).foreach(_.drop)
      log.info("Database schema dropped")
    } catch {
      case e: Exception ⇒ log.error("Could not drop db : {} ", e)
    }
  }

  def purgeDB() = db withDynTransaction {
    dropDB()
    createDB()
  }
}