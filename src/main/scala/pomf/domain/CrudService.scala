package pomf.domain

import java.util.UUID

import akka.actor.ActorSystem
import pomf.api.endpoint.JsonSupport
import pomf.domain.dao.Dao
import pomf.domain.model._

import scala.concurrent.Future
import scala.util._

class CrudService(dao: Dao, system: ActorSystem) extends JsonSupport {

  implicit val ec = system.dispatcher

  def getAllFridge(pageNumber: Int, pageSize: Int): Future[List[FridgeLight]] = Future {
    dao.getAllFridge(pageNumber, pageSize)
  }

  def createFridge(fridgeName: String): Future[Fridge] = Future {
    dao.createFridge(fridgeName) match {
      case Success(id) ⇒ dao.getFridgeById(id)
      case Failure(ex) ⇒ throw ex
    }
  }

  def addPost(post: Post, token: String): Future[Post] = Future {
    dao.addPost(post) match {
      case None ⇒ throw new FridgeNotFoundException(post.fridgeId)
      case Some(persistedPost) ⇒
        pushToStream(Notification.createPost(persistedPost, token))
        persistedPost
    }
  }

  def getFridgeFull(fridgeId: UUID): Future[FridgeFull] = Future {
    dao.getFridgeFull(fridgeId).getOrElse(throw new FridgeNotFoundException(fridgeId))
  }

  def getPost(id: UUID): Future[Post] = Future {
    dao.getPost(id).getOrElse(throw new PostNotFoundException(id))
  }

  def searchByNameLike(term: String): Future[List[Fridge]] = Future {
    dao.searchByNameLike(term)
  }

  def deletePost(id: UUID, token: String): Future[String] = Future {
    dao.getPost(id).fold(throw new PostNotFoundException(id)) { post: Post ⇒
      val deleteAck = "post " + dao.deletePost(id) + " deleted"
      pushToStream(Notification.deletePost(post.fridgeId, id, token))
      deleteAck
    }
  }

  def updatePost(post: Post, token: String): Future[Post] = Future {
    dao.updatePost(post).fold(throw new PostNotFoundException(post.id.get)) { postUpdated ⇒
      pushToStream(Notification.updatePost(post, token))
      postUpdated
    }
  }

  def countFridges(): Future[Int] = Future {
    dao.countFridges()
  }

  def countPosts(): Future[Int] = Future {
    dao.countPosts()
  }

  private def pushToStream(n: Notification) = system.eventStream.publish(n)

}

class PostNotFoundException(val postId: UUID) extends Exception(s"Post $postId does not exist")

class FridgeNotFoundException(val fridgeId: UUID) extends Exception(s"Fridge $fridgeId does not exist")

class FridgeAlreadyExistsException(val fridgeId: UUID) extends Exception(s"Fridge $fridgeId already exists")