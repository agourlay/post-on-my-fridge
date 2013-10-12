package pomf.domain.model

import scala.compat.Platform
import org.joda.time.DateTime
import spray.json._
import pomf.api.JsonSupport._

case class Notification(fridgeName : String, command :String, payload : JsValue, date : DateTime, token : String ) 

object Notification{
    
  def createPost(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "postCreated", conv.write(post), new DateTime(), token)
  }
  
  def updatePost(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "postUpdated", conv.write(post), new DateTime(), token)
  }
  
  def deletePost(fridgeName : String, id : Long, token :String) = {
    new Notification(fridgeName, "postDeleted", JsNumber(id), new DateTime(), token)
  }
  
  def sendMessage(fridgeName : String, message : ChatMessage, token :String)(implicit conv : RootJsonFormat[ChatMessage]) = {
    new Notification(fridgeName, "messageSent", conv.write(message), new DateTime(), token)
  }

  def addParticipant(fridgeName : String, name : String, token :String) = {
    new Notification(fridgeName, "participantAdded", JsString(name + " joined the chat"), new DateTime(), token)
  }

  def removeParticipant(fridgeName : String, name : String, token :String) = {
    new Notification(fridgeName, "participantRemoved", JsString(name + " left the chat"), new DateTime(), token)
  }

  def renameParticipant(fridgeName : String, newName : String, oldName : String, token :String) = {
    new Notification(fridgeName, "participantRenamed", JsString(oldName + " changed name to " + newName), new DateTime(), token)
  }
}
