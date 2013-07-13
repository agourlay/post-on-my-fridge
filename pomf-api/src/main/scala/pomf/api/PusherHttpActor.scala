package pomf.api

import pomf.util._
import pomf.service.CrudServiceActor
import pomf.domain.model._
import akka.pattern._
import akka.actor._
import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import HttpHeaders._
import spray.can.Http
import spray.can.server.Stats
import Directives._
import spray.http.CacheDirectives._
import spray.util._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import DefaultJsonProtocol._
import scala.concurrent.duration._
import scala.language.postfixOps
import JsonSupport._


class PusherHttpActor extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher

  def receive = runRoute(pusherRoute)
  
 private val pusherRoute =
  get {
    pathPrefix("stream") {
//      path("fridge" / Rest) { fridgeName =>
//         path("token" / Rest) { token =>
//          	streamActivity(fridgeName,token)
//         }    
//      } ~
      path("stats") {
        complete {
          actorRefFactory.actorSelection("/user/IO-HTTP/listener-1")
            .ask(Http.GetStats)(1.second)
            .mapTo[Stats]
        }
      } ~  
      path("firehose") {
        streamActivity() 
      }    
    }
  }
    
  def streamActivity(fridgeTarget : Option[String] = None 
                     ,userToken : Option[String] = None)(ctx: RequestContext): Unit = {
    val connectionHandler = context.actorOf(Props(new ServerSentEventActor(fridgeTarget,userToken,ctx)))
    //subscribe to notification event
    context.system.eventStream.subscribe(connectionHandler, classOf[Notification])
  }  
}