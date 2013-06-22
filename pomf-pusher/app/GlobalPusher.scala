import play.api._
import akka.actor._
import akka.actor.ActorSystem
import service.PusherNotificationService

object GlobalPusher extends GlobalSettings {

  implicit val system = ActorSystem("PusherSystem")
    
  val pusherNotificationService = system.actorOf(Props[PusherNotificationService], "pusher-listenner-service")

  override def onStart(app: Application) {
    Logger.info("Pomf-pusher has started")
  }  
  
  override def onStop(app: Application) {
    Logger.info("Pomf-pusher shutdown...")
  }  
  
}