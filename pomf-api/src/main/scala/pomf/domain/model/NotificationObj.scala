package pomf.domain.model

import scala.compat.Platform
import spray.json.RootJsonFormat

case class Notification(fridgeName : String, command :String, payload :String, timestamp : Long ) 

object NotificationObj{
    
  def create(fridgeName : String, post : Post)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "create", conv.write(post).toString, Platform.currentTime)
  }
  
  def update(fridgeName : String, post : Post)(implicit conv : RootJsonFormat[Post]) = {
    new Notification(fridgeName, "update", conv.write(post).toString, Platform.currentTime)
  }
  
  def delete(fridgeName : String, id : Long) = {
    new Notification(fridgeName, "delete", id.toString, Platform.currentTime)
  }
  
  def message(fridgeName : String, message : ChatMessage)(implicit conv : RootJsonFormat[ChatMessage]) = {
    new Notification(fridgeName, "message", conv.write(message).toString, Platform.currentTime)
  }

}
