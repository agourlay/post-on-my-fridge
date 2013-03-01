package pomf.domain.dao

import scala.slick.session.Database
import pomf.domain.config.DAL
import pomf.domain.model.Fridge
import pomf.domain.model.FridgeRest
import pomf.domain.model.Post

class Dao(name: String, dal: DAL, db: Database) {
  // We only need the DB/session imports outside the DAL
  import dal._
import dal.profile.simple._

  // Put an implicitSession in scope for database actions
  implicit val implicitSession = db.createSession

  def createDB = dal.create

  def dropDB = dal.drop
  
  def purgeDB = dal.purge

   def getAllFridge(): List[Fridge] = {
    val result = Fridges.findAllFridge
    println("Got fridge: " + result)
    result
  }

  def addFridge(fridge: Fridge): Fridge = {
    val result = Fridges.insert(fridge)
    println("Inserted fridge: " + result)
    result
  }
  
   def addPost(post: Post): Post = {
    Fridges.findByName(post.fridgeId).getOrElse(addFridge(Fridge(name = post.fridgeId))) 
    val result = Posts.insert(post)
    println("Inserted post: " + result)
    result
  }
  
  def getFridgeRest(fridgeName: String):FridgeRest = {
    val fridgeOpt:Option[Fridge] = Fridges.findByName(fridgeName)
    fridgeOpt match {
      case Some(fridge) => FridgeRest(fridge.name, fridge.description ,fridge.id, Posts.findPostByFridge(fridgeName))
      case _ => FridgeRest(name = fridgeName, description = "" ,posts = List[Post](), id = None)
    }							 
  }
  
  def getFridgeRss(fridgeName: String): scala.xml.Elem = {
    val fridge = getFridgeRest(fridgeName)
    val myRss = <rss version="2.0">
  <channel>
  <title>{fridge.name}</title>
  <description>{fridge.description}</description>
  <link>http://www.example.com/rss</link>
  <lastBuildDate>Mon, 05 Oct 2012 11:12:55</lastBuildDate>
  <pubDate>Tue, 06 Oct 2012 09:00:00 +0100</pubDate>
  {
   for (post <- fridge.posts) yield
   {
     <item>
     <title>{post.fridgeId}</title>
     <description>{post.content}</description>
     <link>http://www.example.com/item</link>
     <guid isPermaLink="false">123</guid>
     <author>{post.author}</author>
     <pubDate>{post.date}</pubDate>
     </item>
	}
  }
  </channel>
  </rss>
   myRss					 
  }
  
  def getPost(id :Long):Option[Post] = Posts.getPost(id)
  
  def searchByNameLike(term:String):List[String] = Fridges.searchByNameLike(term)
  
  def deletePost(id :Long) = Posts.deletePost(id)
  
  def updatePost(post :Post):Option[Post] = Posts.updatePost(post)
}