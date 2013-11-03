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

class FirehoseStream(responder: ActorRef)(filter: (String, String) => Boolean) extends StreamingResponse(responder) {
       
  override def startText = "Streaming firehose...\n"

  override def preStart {
    context.system.eventStream.subscribe(self, classOf[Notification])
  }
  
  override def receive = {
    case Notification(fridgeNameNotif, command, payload, timestamp, token) => {
      if (filter(fridgeNameNotif,token)){
        val pushedEvent = PushedEvent(fridgeNameNotif, command, payload, timestamp)
        val nextChunk = MessageChunk("data: "+ formatEvent.write(pushedEvent) +"\n\n")
        responder ! nextChunk 
      }
    }

    case _ => super.receive 

  }  
}