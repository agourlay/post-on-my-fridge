package pomf.domain

import java.util.UUID

import akka.actor.ActorSystem
import pomf.api.endpoint.JsonSupport
import pomf.domain.dao.Dao
import pomf.domain.model._

import scala.concurrent.Future

class CrudService(dao: Dao, system: ActorSystem) extends JsonSupport {

  implicit val ec = system.dispatcher

  def getAllFridge(pageNumber: Int, pageSize: Int): Future[Seq[FridgeLight]] =
    dao.getAllFridge(pageNumber, pageSize).flatMap { fl ⇒
      Future.sequence(fl.map(dao.buildLight))
    }

  def createFridge(fridgeName: String): Future[Fridge] =
    for {
      _ ← dao.createFridge(fridgeName)
      newFridge ← dao.getFridgeByName(fridgeName)
    } yield newFridge

  def addPost(post: Post, token: String): Future[Post] =
    dao.addPost(post).map { persistedPost ⇒
      pushToStream(Notification.createPost(persistedPost, token))
      persistedPost
    }

  def getFridgeFull(fridgeId: UUID): Future[FridgeFull] =
    dao.getFridgeById(fridgeId).map(_.getOrElse(throw new FridgeNotFoundException(fridgeId))).flatMap(dao.buildFull)

  def getPost(id: UUID): Future[Post] = dao.getPost(id).map(_.getOrElse(throw new PostNotFoundException(id)))

  def searchByNameLike(term: String): Future[Seq[Fridge]] = dao.searchByNameLike(term)

  def deletePost(id: UUID, token: String): Future[String] =
    dao.getPost(id).flatMap { o ⇒
      o.fold(throw new PostNotFoundException(id)) { post: Post ⇒
        dao.deletePost(id).map { uuid ⇒
          pushToStream(Notification.deletePost(post.fridgeId, id, token))
          s"post $uuid deleted"
        }
      }
    }

  def updatePost(post: Post, token: String): Future[Post] =
    post.id.fold(throw new RuntimeException) { pid: UUID ⇒
      dao.updatePost(post).flatMap { _ ⇒
        dao.getPost(pid).map {
          _.fold(throw new PostNotFoundException(pid)) { postUpdated ⇒
            pushToStream(Notification.updatePost(post, token))
            postUpdated
          }
        }
      }
    }

  def countFridges(): Future[Int] = dao.countFridges()

  def countPosts(): Future[Int] = dao.countPosts()

  private def pushToStream(n: Notification) = system.eventStream.publish(n)

}

class PostNotFoundException(val postId: UUID) extends Exception(s"Post $postId does not exist")

class FridgeNotFoundException(val fridgeId: UUID) extends Exception(s"Fridge $fridgeId does not exist")

class FridgeAlreadyExistsException(val fridgeId: UUID) extends Exception(s"Fridge $fridgeId already exists")