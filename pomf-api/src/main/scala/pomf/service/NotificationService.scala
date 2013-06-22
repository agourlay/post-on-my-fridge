package pomf.service

import akka.actor.Actor
import akka.actor.Props
import spray.json.DefaultJsonProtocol._
import scala.io.Codec
import spray.json.JsValue
import pomf.domain.model.Notification
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging

class NotificationActor extends Actor with ActorLogging {
  import pomf.api.JsonSupport._
  
  implicit val actorSystem = context.system

  val remote = "akka://pomf-pusher@127.0.0.1:2553/user/pusher-listener"
  
  def receive = {
    case Notification(fridgeName,command,payload,timestamp,token) =>  {    
      val jsonNotif : JsValue = formatNotif.write(Notification(fridgeName,command,payload,timestamp,token))
      log.info("Sending notification {}", jsonNotif)
      context.actorFor(remote) ! jsonNotif.toString
    }
  }
}