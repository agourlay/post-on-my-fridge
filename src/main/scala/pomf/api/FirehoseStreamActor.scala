package pomf.api

import akka.actor._

import JsonSupport._
import pomf.domain.model._

import scala.language.postfixOps

import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.can.Http
import HttpHeaders._

class FirehoseStreamActor(fridgeTarget:Option[String],userToken:Option[String], ctx: RequestContext) extends Actor with ActorLogging {

  val streamStart = "Starts streaming activity...\n"

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