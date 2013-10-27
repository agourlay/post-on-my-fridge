package pomf.service

import akka.actor._

import pomf.api.JsonSupport._
import pomf.domain.dao.Dao
import pomf.domain.model._
import pomf.service.CrudServiceProtocol._
import pomf.service.NotificationServiceProtocol._

class CrudService(dao : Dao, notificationService : ActorRef, urlSite : String) extends Actor with ActorLogging { 

  implicit def executionContext = context.dispatcher

  def receive = {
    case FullFridge(fridgeName)              => sender ! getFridgeRest(fridgeName)
    case AllFridge                           => sender ! getAllFridge()
    case CreateFridge(fridge)                => sender ! addFridge(fridge)
    case GetPost(postId)                     => sender ! getPost(postId)
    case DeletePost(postId, token)           => sender ! deletePost(postId, token)
    case CreatePost(post, token)             => sender ! addPost(post, token)
    case UpdatePost(post, token)             => sender ! updatePost(post, token)
    case FridgeRss(fridgeName)               => sender ! getFridgeRss(fridgeName)
    case SearchFridge(term)                  => sender ! searchByNameLike(term)
    case CountFridges                        => sender ! countFridges
    case CountPosts                          => sender ! countPosts
    case DeleteOutdatedPost                  => deleteOutdatedPost
  }

  def getAllFridge(): List[FridgeRest] = dao.getAllFridge

  def addFridge(fridge: Fridge): Fridge = dao.addFridge(fridge)

  def addPost(post: Post, token: String): Post = {
    val persistedPost = dao.addPost(post)
    notificationService ! NotificationServiceProtocol.PostCreated(post.fridgeId, persistedPost, token)
    persistedPost
  }

  def getFridgeRest(fridgeName: String): FridgeRest = {
    log.debug("Requesting fridge {}", fridgeName)
    dao.getFridgeRest(fridgeName)
  }

  def getFridgeRss(fridgeName: String): scala.xml.Elem = {
    val fridge = dao.getFridgeRest(fridgeName)
    val fridgeRss = <rss version="2.0">
                      <channel>
                        <title>{ fridge.name }</title>
                        <link>{ urlSite }#/{ fridge.name }</link>
                        {
                          for (post <- fridge.posts) yield {
                            <item>
                              <title>{ post.fridgeId }</title>
                              <description>{ post.content }</description>
                              <link>{ urlSite }#/{ fridge.name }</link>
                              <guid isPermaLink="false">{ post.id.get }</guid>
                              <author>{ post.author }</author>
                              <pubDate>{ post.date }</pubDate>
                            </item>
                          }
                        }
                      </channel>
                    </rss>
    fridgeRss
  }

  def getPost(id: Long): Option[Post] = dao.getPost(id)

  def searchByNameLike(term: String): List[String] = dao.searchByNameLike(term)

  def deletePost(id: Long, token: String): String = {
    val fridgeId = getPost(id).get.fridgeId
    val deleteAck = "post " + dao.deletePost(id) + " deleted"
    notificationService ! NotificationServiceProtocol.PostDeleted(fridgeId, id, token)
    deleteAck
  }

  def deleteOutdatedPost = {
    log.info("Deleting outdated post")
    dao.deleteOutdatedPost
  }  

  def updatePost(post: Post, token: String): Post = {
    val postUpdated = dao.updatePost(post).orNull
    notificationService ! NotificationServiceProtocol.PostUpdated(post.fridgeId, post, token)
    postUpdated
  }

  def countFridges = "" + dao.countFridges

  def countPosts = "" + dao.countPosts

}

object CrudServiceProtocol {
  case class FullFridge(fridgeName : String)
  case object AllFridge
  case class CreateFridge(fridge : Fridge) 
  case class GetPost(postId : Long)
  case class UpdatePost(post: Post, token: String)
  case class CreatePost(post: Post, token: String)
  case class DeletePost(postId: Long, token: String)
  case class FridgeRss(fridgeName: String)
  case class SearchFridge(term: String)
  case object CountFridges
  case object CountPosts  
  case object DeleteOutdatedPost
}
