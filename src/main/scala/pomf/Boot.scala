package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import akka.actor.OneForOneStrategy
import spray.can.Http
import akka.actor.actorRef2Scala
import scala.concurrent.duration._
import scala.language.postfixOps
import pomf.service.NotificationActor
import pomf.service.CrudServiceActor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pomf.api.PomfHttpActor

object Boot extends App {
 
  val log: Logger = LoggerFactory.getLogger("pomf.boot");

  implicit val system = ActorSystem("pomf-api")

  val notificationService = system.actorOf(Props[NotificationActor], "notification-service")

  val crudService = system.actorOf(Props[CrudServiceActor], "crud-service")
    
  val httpService = system.actorOf(Props[PomfHttpActor], name = "http-service")
  IO(Http) ! Http.Bind(httpService, "localhost", port = 8080)  
}
