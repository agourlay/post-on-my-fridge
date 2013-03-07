package settings

import play.api._
import akka.actor.ActorSystem
import service.PomfNotificationService

object GlobalPusher extends GlobalSettings {

  implicit val system = ActorSystem("PusherSystem")
    
  override def onStart(app: Application) {
    Logger.info("Pomf-pusher has started")
  }  
  
  override def onStop(app: Application) {
    Logger.info("Pomf-pusher shutdown...")
  }  
  
}