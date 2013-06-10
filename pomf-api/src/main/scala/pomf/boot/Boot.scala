package pomf.boot

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.actor.actorRef2Scala
import pomf.service.rest.PomfHttpActor
import pomf.service.notification.PomfNotificationActor

object Boot extends App {
 
  implicit val system = ActorSystem("pomf")

  // create and start our service actor
  val service = system.actorOf(Props[PomfHttpActor], "pomf-http-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, "localhost", port = 8080)
    
  val notificationService = system.actorOf(Props[PomfNotificationActor], "pomf-messaging-service")  
}
