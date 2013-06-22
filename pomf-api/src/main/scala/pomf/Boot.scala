package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import akka.actor.OneForOneStrategy
import spray.can.Http
import akka.actor.actorRef2Scala
import scala.concurrent.duration._
import scala.language.postfixOps
import pomf.api.ApiHttpActor
import pomf.service.NotificationActor
import pomf.service.CrudServiceActor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Boot extends App {
 
  val log: Logger = LoggerFactory.getLogger("pomf.boot");

  implicit val system = ActorSystem("pomf-api")

  val notificationService = system.actorOf(Props[NotificationActor], "notification-service")

  val crudService = system.actorOf(Props[CrudServiceActor], "crud-service")
    
  // start a new HTTP server on port 8080 with our service actor as the handler
  val service = system.actorOf(Props[ApiHttpActor], name = "http-service")
  IO(Http) ! Http.Bind(service, "localhost", port = 8080)  
}
