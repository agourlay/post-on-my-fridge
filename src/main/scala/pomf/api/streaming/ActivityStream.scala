package pomf.api.streaming

import akka.actor._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model._

import scala.language.postfixOps

import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.can.Http
import HttpHeaders._

class ActivityStream(responder: ActorRef, filter: (Long, String) => Boolean) extends StreamingResponse(responder) {

  override def preStart {
    super.preStart
    context.system.eventStream.subscribe(self, classOf[Notification])
  }
  
  override def receive = ({
    case Notification(fridgeIdNotif, command, payload, timestamp, token) => {
      if (filter(fridgeIdNotif,token)){
        val pushedEvent = PushedEvent(fridgeIdNotif, command, payload, timestamp)
        val nextChunk = MessageChunk("data: "+ formatEvent.write(pushedEvent) +"\n\n")
        responder ! nextChunk 
      }
    }
  }: Receive) orElse super.receive  
}

object ActivityStream {
   def props(responder: ActorRef, filter: (Long, String) => Boolean) 
     = Props(classOf[ActivityStream], responder, filter).withDispatcher("requests-dispatcher")
} 