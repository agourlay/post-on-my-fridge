import play.api._
import akka.actor._
import akka.actor.ActorSystem
import service.PusherNotificationService

object GlobalPusher extends GlobalSettings {

  implicit val system = ActorSystem("pomf-pusher")
    
  val pusherNotificationService = system.actorOf(Props[PusherNotificationService], "pusher-listener")

  override def onStart(app: Application) {
    Logger.info("pomf-pusher has started")
  }  
  
  override def onStop(app: Application) {
    Logger.info("pomf-pusher shutdown...")
  }  
  
}