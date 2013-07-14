package pomf.api

import pomf.util._
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
import DefaultJsonProtocol._
import scala.concurrent.duration._
import scala.language.postfixOps
import JsonSupport._
import spray.can.parsing.Result.Ok
import pomf.domain.model.PushedEvent


class ServerSentEventActor(fridgeTarget:Option[String],userToken:Option[String], ctx: RequestContext) extends Actor with ActorLogging {

  val streamStart = "Starts streaming: ...\n"

  val EventStreamType = register(
	  MediaType.custom(
	    mainType = "text",
	    subType = "event-stream",
	    compressible = false,
	    binary = false
	   ))

  val responseStart = HttpResponse(
 			entity  = HttpEntity(EventStreamType, streamStart),
  			headers = `Cache-Control`(CacheDirectives.`no-cache`) :: Nil
            )

  ctx.responder ! ChunkedResponseStart(responseStart) 
          
  def domainFilter(fridgeName:String, token : String) : Boolean = 
    if (fridgeTarget.isDefined && userToken.isDefined)
      fridgeName == fridgeTarget.get && token != userToken.get 
    else true
  
  def receive = {
    case Notification(fridgeNameNotif,command,payload,timestamp,token) => {
      if (domainFilter(fridgeNameNotif,token)){
        val pushedEvent = PushedEvent(command,payload,timestamp)
        val nextChunk = MessageChunk("data: "+ formatEvent.write(pushedEvent) +"\n\n")
        ctx.responder ! nextChunk 
      } 
    }    
    case ev: Http.ConnectionClosed =>
      log.debug("Stopping response streaming due to {}", ev)
      context.stop(self)
     
    case ReceiveTimeout =>
        ctx.responder ! MessageChunk(":\n") // Comment to keep connection alive  
  }
}