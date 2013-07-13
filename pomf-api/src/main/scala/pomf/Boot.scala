package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import akka.actor.OneForOneStrategy
import spray.can.Http
import akka.actor.actorRef2Scala
import scala.concurrent.duration._
import scala.language.postfixOps
import pomf.api.RestHttpActor
import pomf.service.NotificationActor
import pomf.service.CrudServiceActor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pomf.api.RestHttpActor
import pomf.api.PusherHttpActor

object Boot extends App {
 
  val log: Logger = LoggerFactory.getLogger("pomf.boot");

  implicit val system = ActorSystem("pomf-api")

  val notificationService = system.actorOf(Props[NotificationActor], "notification-service")

  val crudService = system.actorOf(Props[CrudServiceActor], "crud-service")
    
  // starts apiService on port 8080 
  val restService = system.actorOf(Props[RestHttpActor], name = "rest-service")
  IO(Http) ! Http.Bind(restService, "localhost", port = 8080)  
  
    // starts pusherService on port 8080 
  val pusherService = system.actorOf(Props[PusherHttpActor], name = "pusher-service")
  IO(Http) ! Http.Bind(pusherService, "localhost", port = 9000)  
}
