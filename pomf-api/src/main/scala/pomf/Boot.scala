package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import spray.can.Http
import akka.actor.actorRef2Scala
import pomf.api.ApiHttpActor
import pomf.service.NotificationActor
import pomf.service.CrudServiceActor

object Boot extends App {
 
  implicit val system = ActorSystem("pomf")

  // create and start our services actor
  val service = system.actorOf(Props[ApiHttpActor], "http-service")

  val crudRouter = system.actorOf(Props[CrudServiceActor].withRouter(SmallestMailboxRouter(Runtime.getRuntime.availableProcessors)), "crud-router")
  
  val notificationRouter = system.actorOf(Props[NotificationActor].withRouter(SmallestMailboxRouter(Runtime.getRuntime.availableProcessors)), "notification-router")
    
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, "localhost", port = 8080)  
}
