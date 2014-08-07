package pomf.api.streaming

import akka.actor._

import spray.http._
import HttpHeaders._
import spray.can.Http

import pomf.metrics.Instrumented
import pomf.api.endpoint.ServerSentEvent

abstract class StreamingResponse(responder: ActorRef) extends Actor with ActorLogging with Instrumented {

  val timerCtx = metrics.timer("streaming").timerContext()

  lazy val responseStart = HttpResponse(
    entity  = HttpEntity(ServerSentEvent.EventStreamType, "streaming updates..."),
    headers = `Cache-Control`(CacheDirectives.`no-cache`) :: Nil
  )

  override def preStart() = {
    super.preStart()
    responder ! ChunkedResponseStart(responseStart)
  }

  override def postStop() = {
    responder ! ChunkedMessageEnd
    timerCtx.stop()
  }
  
  def receive = {   
    case ev: Http.ConnectionClosed => {
      log.debug("Stopping response streaming due to {}", ev)
      self ! PoisonPill
    }
    case ReceiveTimeout => responder ! MessageChunk(":\n") // Comment to keep connection alive  
  }
} 