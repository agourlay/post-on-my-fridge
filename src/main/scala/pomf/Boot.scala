package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import akka.actor.OneForOneStrategy
import spray.can.Http
import akka.actor.actorRef2Scala
import scala.concurrent.duration._
import scala.language.postfixOps
import java.util.concurrent.TimeUnit
import pomf.service.NotificationActor
import pomf.service.CrudServiceActor
import pomf.service.CrudServiceActor._
import pomf.service.ChatServiceActor
import pomf.service.TokenServiceActor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pomf.api.PomfHttpActor

object Boot extends App {
 
  val log: Logger = LoggerFactory.getLogger("pomf.boot");

  implicit val system = ActorSystem("pomf-api")
  
  implicit def executionContext = system.dispatcher

  val notificationService = system.actorOf(Props[NotificationActor], "notification-service")

  val crudService = system.actorOf(Props[CrudServiceActor], "crud-service")
  
  val chatService = system.actorOf(Props[ChatServiceActor], "chat-service")
  
  val tokenService = system.actorOf(Props[TokenServiceActor], "token-service")
      
  val httpService = system.actorOf(Props[PomfHttpActor], "http-service")
  
  IO(Http) ! Http.Bind(httpService, "localhost", port = 8080) 
  
  // schedule delete outdated post every 24 hours
  system.scheduler.schedule(Duration(24, HOURS), Duration(24, HOURS), crudService, DeleteOutdatedPost)
}
