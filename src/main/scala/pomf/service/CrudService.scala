package pomf.service

import akka.actor.ActorSystem
import pomf.api.endpoint.JsonSupport

import scala.concurrent.Future
import scala.util._

import pomf.domain.dao.Dao
import pomf.domain.model._

import java.util.UUID

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
        toEventStream(Notification.createPost(persistedPost, token))
        persistedPost
    }
  }

  def getFridgeFull(fridgeId: UUID): Future[FridgeFull] = Future {
    dao.getFridgeFull(fridgeId) match {
      case None             ⇒ throw new FridgeNotFoundException(fridgeId)
      case Some(fullFridge) ⇒ fullFridge
    }
  }

  def getPost(id: UUID): Future[Post] = Future {
    dao.getPost(id) match {
      case None       ⇒ throw new PostNotFoundException(id)
      case Some(post) ⇒ post
    }
  }

  def searchByNameLike(term: String): Future[List[Fridge]] = Future {
    dao.searchByNameLike(term)
  }

  def deletePost(id: UUID, token: String): Future[String] = Future {
    dao.getPost(id) match {
      case None ⇒ throw new PostNotFoundException(id)
      case Some(post) ⇒
        val deleteAck = "post " + dao.deletePost(id) + " deleted"
        toEventStream(Notification.deletePost(post.fridgeId, id, token))
        deleteAck
    }
  }

  def updatePost(post: Post, token: String): Future[Post] = Future {
    dao.updatePost(post) match {
      case None ⇒ throw new PostNotFoundException(post.id.get)
      case Some(postUpdated) ⇒
        toEventStream(Notification.updatePost(post, token))
        postUpdated
    }
  }

  def countFridges(): Future[Int] = Future {
    dao.countFridges()
  }

  def countPosts(): Future[Int] = Future {
    dao.countPosts()
  }

  private def toEventStream(n: Notification) = system.eventStream.publish(n)

}
