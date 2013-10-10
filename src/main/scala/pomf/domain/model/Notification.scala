package pomf.domain.model

import scala.compat.Platform

import spray.json.RootJsonFormat

case class Notification(fridgeName : String, command :String, payload :String, timestamp : Long, token : String ) 

object Notification{
    
  def createPost(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "createPost", conv.write(post).toString, Platform.currentTime,token)
  }
  
  def updatePost(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "updatePost", conv.write(post).toString, Platform.currentTime,token)
  }
  
  def deletePost(fridgeName : String, id : Long, token :String) = {
    new Notification(fridgeName, "deletePost", id.toString, Platform.currentTime,token)
  }
  
  def sendMessage(fridgeName : String, message : ChatMessage, token :String)(implicit conv : RootJsonFormat[ChatMessage]) = {
    new Notification(fridgeName, "sendMessage", conv.write(message).toString, Platform.currentTime,token)
  }

}
