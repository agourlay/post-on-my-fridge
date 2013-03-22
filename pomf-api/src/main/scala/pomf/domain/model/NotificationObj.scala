package pomf.domain.model

import scala.compat.Platform
import spray.json.RootJsonFormat

case class Notification(fridgeName : String, command :String, payload :String, timestamp : Long, token : String ) 

object NotificationObj{
    
  def create(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "create", conv.write(post).toString, Platform.currentTime,token)
  }
  
  def update(fridgeName : String, post : Post, token :String)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "update", conv.write(post).toString, Platform.currentTime,token)
  }
  
  def delete(fridgeName : String, id : Long, token :String) = {
    new Notification(fridgeName, "delete", id.toString, Platform.currentTime,token)
  }
  
  def message(fridgeName : String, message : ChatMessage, token :String)(implicit conv : RootJsonFormat[ChatMessage]) = {
    new Notification(fridgeName, "message", conv.write(message).toString, Platform.currentTime,token)
  }

}
