package pomf.api

import akka.actor._
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import HttpHeaders._
import spray.can.Http
import spray.can.server.Stats
import scala.language.postfixOps
import JsonSupport._


class StatStreamActor(ctx: RequestContext) extends Actor with ActorLogging {

  val streamStart = "Starts streaming stats...\n"

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
  
  def receive = {
    case stat : Stats => {
        val nextChunk = MessageChunk("data: "+ formatHttpServerStats.write(stat) +"\n\n")
        ctx.responder ! nextChunk 
    }   
     
    case ev: Http.ConnectionClosed =>
      log.debug("Stopping response streaming due to {}", ev)
      context.stop(self)
     
    case ReceiveTimeout =>
        ctx.responder ! MessageChunk(":\n") // Comment to keep connection alive  
  }
}