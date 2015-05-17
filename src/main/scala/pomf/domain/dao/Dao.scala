package pomf.domain.dao

import org.slf4j.LoggerFactory
import pomf.domain.{ PostNotFoundException, FridgeNotFoundException, FridgeAlreadyExistsException }

import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global

import java.util.UUID

import com.github.tototoshi.slick.PostgresJodaSupport._
import org.joda.time.DateTime

import pomf.core.metrics.Instrumented
import pomf.domain.model._

class Dao(db: Database) extends Instrumented {

  metrics.gauge("posts")(countPosts())
  metrics.gauge("fridges")(countFridges())

  val log = LoggerFactory.getLogger("domain.dao")

  val posts = TableQuery[Posts]
  val fridges = TableQuery[Fridges]

  def fridgeByName(name: String) = for (f ← fridges.filter(_.name === name)) yield f

  def fridgeById(id: UUID) = for (f ← fridges.filter(_.id === id)) yield f

  def postById(id: UUID) = for (f ← posts.filter(_.id === id)) yield f

  def postByFridgeId(fridgeId: UUID) = for (f ← posts.filter(_.fridgeId === fridgeId)) yield f

  def countPostForFridge(id: Rep[UUID]) = db.run {
    for (l ← posts.filter(_.fridgeId === id).length.result) yield l
  }

  def getFridgeById(id: UUID) = db.run {
    fridgeById(id).result.head
  }

  def getFridgeByName(name: String): Future[Fridge] = db.run {
    fridgeByName(name).result.head
  }

  def createFridge(name: String): Future[Unit] = db run {
    fridgeByName(name).result.headOption
  }.map {
    case Some(f) ⇒ throw new FridgeAlreadyExistsException(f.id.get)
    case None    ⇒ fridges += Fridge(Some(UUID.randomUUID()), name, new DateTime(), new DateTime())
  }

  def getFridgeFull(fridgeId: UUID): Future[Option[Fridge]] = db run {
    fridgeById(fridgeId).result.headOption
  }

  def getAllFridge(pageNumber: Int, pageSize: Int): Future[Seq[Fridge]] =
    db run {
      fridges.sortBy(_.modificationDate).drop((pageNumber - 1) * pageSize).take(pageSize).result //_.modificationDate.desc
    }

  def buildFull(f: Fridge): Future[FridgeFull] = db.run(postByFridgeId(f.id.get).result).map { posts ⇒
    FridgeFull(f.name, f.creationDate, f.modificationDate, f.id.get, posts.size, posts.toList)
  }

  def buildLight(f: Fridge): Future[FridgeLight] = countPostForFridge(f.id.get).map { c ⇒
    FridgeLight(f.name, f.creationDate, f.modificationDate, f.id.get, c)
  }

  def getPost(id: UUID): Future[Option[Post]] = db run {
    postById(id).result.headOption
  }

  def searchByNameLike(term: String) = db run {
    val fq = for (f ← fridges.filter(_.name.toLowerCase like "%" + term.toLowerCase + "%").take(100)) yield f
    fq.result
  }

  def deletePost(postId: UUID): Future[UUID] = db run {
    postById(postId).result.headOption.map { op ⇒
      op.fold(throw new PostNotFoundException(postId)) { p ⇒
        postById(postId).delete
        updateModificationDate(p.fridgeId)
        p.id.get
      }
    }
  }

  def addPost(post: Post): Future[Post] = db run {
    fridgeById(post.fridgeId).result.headOption.map { of ⇒
      of.fold(throw new FridgeNotFoundException(post.fridgeId)) { f ⇒
        val toPersist = post.copy(id = Some(UUID.randomUUID()))
        posts.forceInsert(toPersist)
        updateModificationDate(f.id.get)
        toPersist
      }
    }
  }

  def updatePost(post: Post): Future[Unit] = db run {
    postById(post.id.get).result.headOption.map { op ⇒
      op.fold(throw new PostNotFoundException(post.id.get)) { p ⇒
        updateModificationDate(post.fridgeId)
        postById(p.id.get).update(post)
      }
    }
  }

  def countPosts() = db run {
    posts.length.result
  }

  def countFridges() = db.run {
    fridges.length.result
  }

  def updateModificationDate(fridgeId: UUID) = {
    fridges.filter(_.id === fridgeId).map(_.modificationDate).update(new DateTime())
  }

  def createDB() = db.run {
    posts.schema.create
    fridges.schema.create
  }.onComplete {
    case Success(_) ⇒ log.info("Database schema created")
    case Failure(e) ⇒ log.info("Could not create db ... assuming it already exist")
  }

  def dropDB() = db.run {
    posts.schema.drop
    fridges.schema.drop
  }.onComplete {
    case Success(_) ⇒ log.info("Database schema dropped")
    case Failure(e) ⇒ log.error("Could not drop db : {} ", e)
  }

  def purgeDB() = {
    dropDB()
    createDB()
  }
}