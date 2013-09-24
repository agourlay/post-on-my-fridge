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


class StatStreamActor(ctx: RequestContext) extends StreamingResponseActor(ctx) {
  
  override def receive = {

    case stat : Stats => {
        val nextChunk = MessageChunk("data: "+ formatHttpServerStats.write(stat) +"\n\n")
        ctx.responder ! nextChunk 
    }
    case _ => super.receive   
    
  }
}