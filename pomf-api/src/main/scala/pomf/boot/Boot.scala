package pomf.boot

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.actor.actorRef2Scala
import pomf.service.ApiHttpActor
import pomf.service.NotificationActor
import pomf.service.CrudServiceActor

object Boot extends App {
 
  implicit val system = ActorSystem("pomf")

  // create and start our services actor
  val service = system.actorOf(Props[ApiHttpActor], "pomf-http-service")

  val notificationService = system.actorOf(Props[NotificationActor], "pomf-notification-service")
   
  val crudService = system.actorOf(Props[CrudServiceActor], "pomf-crud-service")
    
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, "localhost", port = 8080)  
}
