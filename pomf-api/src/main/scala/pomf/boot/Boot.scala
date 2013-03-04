package pomf.boot

import akka.actor.{Props, ActorSystem}
import spray.can.server.HttpServer
import spray.io._
import akka.actor.actorRef2Scala
import pomf.service.rest.PomfHttpActor
import pomf.service.notification.PomfNotificationActor
import pomf.service.caching.PomfCachingActor

object Boot extends App {
 
  implicit val system = ActorSystem("pomf")
  
  // notification actor
  val notificationService = system.actorOf(Props[PomfNotificationActor], "pomf-messaging-service")

  // caching actor
  val cachingService = system.actorOf(Props[PomfCachingActor], "pomf-caching-service")
  
  private val ioBridge = IOExtension(system).ioBridge()
    
  // create and start our service actor
  val httpService = system.actorOf(Props[PomfHttpActor], "pomf-http-service")
  
  // create and start the spray-can HttpServer, telling it that
  // we want requests to be handled by our singleton service actor
  val httpServer = system.actorOf(
    Props(new HttpServer(ioBridge, SingletonHandler(httpService))),
    name = "http-server"
  )

  // a running HttpServer can be bound, unbound and rebound
  // initially to need to tell it where to bind to
  httpServer ! HttpServer.Bind("localhost", 8080)
  
}
