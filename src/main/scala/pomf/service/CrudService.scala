package pomf.service

import akka.actor._

import scala.util._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.dao.Dao
import pomf.metrics.Instrumented
import pomf.domain.model._
import pomf.service.CrudServiceProtocol._
import pomf.service.NotificationServiceProtocol._
import java.util.UUID

class CrudService(dao: Dao, notificationService: ActorRef) extends Actor with Instrumented {

  def receive = {
    case FullFridge(fridgeId)            => sender ! getFridgeFull(fridgeId)
    case AllFridge(pageNumber, pageSize) => sender ! getAllFridge(pageNumber, pageSize)
    case CreateFridge(fridge)            => sender ! createFridge(fridge)
    case GetPost(postId)                 => sender ! getPost(postId)
    case DeletePost(postId, token)       => sender ! deletePost(postId, token)
    case CreatePost(post, token)         => sender ! addPost(post, token)
    case UpdatePost(post, token)         => sender ! updatePost(post, token)
    case SearchFridge(term)              => sender ! searchByNameLike(term)
    case CountFridges                    => sender ! countFridges
    case CountPosts                      => sender ! countPosts
  }

  def getAllFridge(pageNumber: Int, pageSize: Int) = LightFridges(dao.getAllFridge(pageNumber, pageSize))

  def createFridge(fridgeName: String) = {
    dao.createFridge(fridgeName) match {
      case Success(id) => dao.getFridgeById(id)
      case Failure(ex) => Failure(ex)
    }
  }

  def addPost(post: Post, token: String) = {
    dao.addPost(post) match {
      case None => Failure(new FridgeNotFoundException(post.fridgeId))
      case Some(persistedPost) => {
        notificationService ! NotificationServiceProtocol.PostCreated(persistedPost, token)
        persistedPost
      }
    }
  }

  def getFridgeFull(fridgeId: UUID) = {
    dao.getFridgeFull(fridgeId) match {
      case None             => Failure(new FridgeNotFoundException(fridgeId))
      case Some(fullFridge) => fullFridge
    }
  }

  def getPost(id: UUID) = dao.getPost(id) match {
    case None       => Failure(new PostNotFoundException(id))
    case Some(post) => post
  }

  def searchByNameLike(term: String) = SearchResult(term, dao.searchByNameLike(term))

  def deletePost(id: UUID, token: String) = {
    dao.getPost(id) match {
      case None => Failure(new PostNotFoundException(id))
      case Some(post) => {
        val deleteAck = "post " + dao.deletePost(id) + " deleted"
        notificationService ! NotificationServiceProtocol.PostDeleted(post.fridgeId, id, token)
        OperationSuccess(deleteAck)
      }
    }
  }

  def updatePost(post: Post, token: String) = {
    dao.updatePost(post) match {
      case None => Failure(new PostNotFoundException(post.id.get))
      case Some(postUpdated) => {
        notificationService ! NotificationServiceProtocol.PostUpdated(post, token)
        postUpdated
      }
    }
  }

  def countFridges = Count(dao.countFridges)

  def countPosts = Count(dao.countPosts)

}

object CrudServiceProtocol {
  case class FullFridge(fridgeId: UUID)
  case class AllFridge(pageNumber: Int, pageSize: Int)
  case class CreateFridge(fridgeName: String)
  case class GetPost(postId: UUID)
  case class UpdatePost(post: Post, token: String)
  case class CreatePost(post: Post, token: String)
  case class DeletePost(postId: UUID, token: String)
  case class SearchFridge(term: String)
  case class OperationSuccess(result: String)
  case object CountFridges
  case object CountPosts
  case class Count(nb: Int)
  case class LightFridges(fridges: List[FridgeLight])
  case class SearchResult(term: String, result: List[Fridge])
}

object CrudService {
  def props(dao: Dao, notificationService: ActorRef) = Props(classOf[CrudService], dao, notificationService).withDispatcher("service-dispatcher")
}
