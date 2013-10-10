package pomf.domain.model

import scala.compat.Platform

import spray.json.RootJsonFormat

case class Notification(fridgeName : String, command :String, payload :String, timestamp : Long, token : String ) 

object Notification{
    
  def createPost(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "postCreated", conv.write(post).toString, Platform.currentTime,token)
  }
  
  def updatePost(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "postUpdated", conv.write(post).toString, Platform.currentTime,token)
  }
  
  def deletePost(fridgeName : String, id : Long, token :String) = {
    new Notification(fridgeName, "postDeleted", id.toString, Platform.currentTime,token)
  }
  
  def sendMessage(fridgeName : String, message : ChatMessage, token :String)(implicit conv : RootJsonFormat[ChatMessage]) = {
    new Notification(fridgeName, "messageSent", conv.write(message).toString, Platform.currentTime,token)
  }

  def addParticipant(fridgeName : String, name : String, token :String) = {
    new Notification(fridgeName, "participantAdded", name, Platform.currentTime,token)
  }

  def removeParticipant(fridgeName : String, name : String, token :String) = {
    new Notification(fridgeName, "participantRemoved", name, Platform.currentTime,token)
  }

  def renameParticipant(fridgeName : String, name : String, token :String) = {
    new Notification(fridgeName, "participantRenamed", name, Platform.currentTime,token)
  }
}
