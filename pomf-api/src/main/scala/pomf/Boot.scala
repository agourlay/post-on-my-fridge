package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
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

  implicit val system = ActorSystem("pomf")

  // create and start our services actor   
  val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: NullPointerException     ⇒ Restart
      case _: Exception                ⇒ Restart
    }
   
  val crudRouter = system.actorOf(Props[CrudServiceActor]
                         .withRouter(SmallestMailboxRouter(nrOfInstances = 1, supervisorStrategy = supervisorStrategy)),
                         name = "crud-router")
  
  val notificationRouter = system.actorOf(Props[NotificationActor]
                           .withRouter(SmallestMailboxRouter(nrOfInstances = 1,  supervisorStrategy = supervisorStrategy)), 
                          name ="notification-router")
    
  // start a new HTTP server on port 8080 with our service actor as the handler
  val service = system.actorOf(Props[ApiHttpActor], name = "http-service")
  IO(Http) ! Http.Bind(service, "localhost", port = 8080)  
}
