package pomf.domain.model

import scala.compat.Platform
import org.joda.time.DateTime
import spray.json._
import pomf.api.JsonSupport._

case class Notification(fridgeId : Long, command :String, payload : JsValue, date : DateTime, token : String ) 

object Notification{
    
  def createPost(post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(post.fridgeId, "postCreated", conv.write(post), new DateTime(), token)
  }
  
  def updatePost(post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(post.fridgeId, "postUpdated", conv.write(post), new DateTime(), token)
  }
  
  def deletePost(fridgeId : Long, id : Long, token :String) = {
    new Notification(fridgeId, "postDeleted", JsNumber(id), new DateTime(), token)
  }
  
  def sendMessage(fridgeId : Long, message : ChatMessage, token :String)(implicit conv : RootJsonFormat[ChatMessage]) = {
    new Notification(fridgeId, "messageSent", conv.write(message), new DateTime(), token)
  }

  def addParticipant(fridgeId : Long, name : String, token :String) = {
    new Notification(fridgeId, "participantAdded", JsString(name + " joined the chat"), new DateTime(), token)
  }

  def removeParticipant(fridgeId : Long, name : String, token :String) = {
    new Notification(fridgeId, "participantRemoved", JsString(name + " left the chat"), new DateTime(), token)
  }

  def renameParticipant(fridgeId : Long, newName : String, oldName : String, token :String) = {
    new Notification(fridgeId, "participantRenamed", JsString(oldName + " changed name to " + newName), new DateTime(), token)
  }
}
