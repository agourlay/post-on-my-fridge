package pomf.service

import akka.actor._

import scala.util.Failure

import pomf.api.endpoint.JsonSupport._
import pomf.domain.dao.Dao
import pomf.domain.model._
import pomf.service.CrudServiceProtocol._
import pomf.service.NotificationServiceProtocol._

class CrudService(dao : Dao, notificationService : ActorRef) extends Actor with ActorLogging { 

  implicit def executionContext = context.dispatcher

  def receive = {
    case FullFridge(fridgeId)      => sender ! getFridgeRest(fridgeId)
    case AllFridge                 => sender ! getAllFridge()
    case CreateFridge(fridge)      => sender ! addFridge(fridge)
    case GetPost(postId)           => sender ! getPost(postId)
    case DeletePost(postId, token) => sender ! deletePost(postId, token)
    case CreatePost(post, token)   => sender ! addPost(post, token)
    case UpdatePost(post, token)   => sender ! updatePost(post, token)
    case SearchFridge(term)        => sender ! searchByNameLike(term)
    case CountFridges              => sender ! countFridges
    case CountPosts                => sender ! countPosts
    case DeleteOutdatedPost        => deleteOutdatedPost
  }

  def getAllFridge(): List[FridgeRest] = dao.getAllFridge

  def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)

  def addPost(post: Post, token: String): Post = {
    val persistedPost = dao.addPost(post)
    notificationService ! NotificationServiceProtocol.PostCreated(persistedPost, token)
    persistedPost
  }

  def getFridgeRest(fridgeName: Long): FridgeRest = {
    log.debug("Requesting fridge {}", fridgeName)
    dao.getFridgeRest(fridgeName)
  }

  def getPost(id: Long) = dao.getPost(id) match {
      case None => Failure(new PostNotFoundException(id))
      case Some(post) => post
    }

  def searchByNameLike(term: String) = SearchResult(term, dao.searchByNameLike(term))

  def deletePost(id: Long, token: String) = {
    dao.getPost(id) match {
      case None => Failure(new PostNotFoundException(id))
      case Some(post) => {
        val deleteAck = "post " + dao.deletePost(id) + " deleted"
        notificationService ! NotificationServiceProtocol.PostDeleted(post.fridgeId, id, token)
        OperationSuccess(deleteAck)
      }
    }
  }

  def deleteOutdatedPost = {
    log.info("Deleting outdated post")
    dao.deleteOutdatedPost
  }  

  def updatePost(post: Post, token: String): Post = {
    val postUpdated = dao.updatePost(post)
    notificationService ! NotificationServiceProtocol.PostUpdated(post, token)
    postUpdated
  }

  def countFridges = Count(dao.countFridges)

  def countPosts = Count(dao.countPosts)

}

object CrudServiceProtocol {
  case class FullFridge(fridgeId : Long)
  case object AllFridge
  case class CreateFridge(fridge : Fridge) 
  case class GetPost(postId : Long)
  case class UpdatePost(post: Post, token: String)
  case class CreatePost(post: Post, token: String)
  case class DeletePost(postId: Long, token: String)
  case class SearchFridge(term: String)
  case class OperationSuccess(result: String)
  case object CountFridges
  case object CountPosts 
  case class Count(nb : Int)
  case object DeleteOutdatedPost
  case class SearchResult(term : String, result : List[Fridge])
}
