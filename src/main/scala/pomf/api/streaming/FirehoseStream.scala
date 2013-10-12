package pomf.api.streaming

import akka.actor._

import pomf.api.JsonSupport._
import pomf.domain.model._

import scala.language.postfixOps

import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.can.Http
import HttpHeaders._

class FirehoseStream(fridgeTarget:Option[String], userToken:Option[String], ctx: RequestContext) extends StreamingResponse(ctx) {
       
  override def startText = "Starts streaming firehose...\n"

  def domainFilter(fridgeName:String, token : String) : Boolean = 
    if (fridgeTarget.isDefined && userToken.isDefined)
      fridgeName == fridgeTarget.get && token != userToken.get 
    else true
  
  override def receive = {
    case Notification(fridgeNameNotif, command, payload, timestamp, token) => {
      if (domainFilter(fridgeNameNotif,token)){
        val pushedEvent = PushedEvent(fridgeNameNotif, command, payload, timestamp)
        val nextChunk = MessageChunk("data: "+ formatEvent.write(pushedEvent) +"\n\n")
        ctx.responder ! nextChunk 
      }
    }

    case _ => super.receive 

  }  
}