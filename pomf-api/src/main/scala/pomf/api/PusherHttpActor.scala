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
import spray.can.Http
import spray.can.server.Stats
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import DefaultJsonProtocol._
import reflect.ClassTag
import JsonSupport._
import spray.can.parsing.Result.Ok
import pomf.domain.model.PushedEvent


class PusherHttpActor extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(60.seconds)

  def receive = runRoute(pusherRoute)
  
 private val pusherRoute =
    pathPrefix("stream") {
//      path("fridge" / Rest) { fridgeName =>
//         path("token" / Rest) { token =>
//          	sendStreamingResponse
//         }    
//      } ~ 
      path("firehose") {
        get {
          streamActivity()
        }
      }    
    }
    
  
  // Server Sent Event actor
   def streamActivity(fridgeTarget : Option[String] = None 
                     ,userToken : Option[String] = None)(ctx: RequestContext): Unit =
    actorRefFactory.actorOf {
      Props {
        new Actor with ActorLogging {
          lazy val streamStart = " " * 2048 
          val responseStart = HttpResponse(entity = HttpEntity(EventStreamType, streamStart))
          ctx.responder ! ChunkedResponseStart(responseStart)    
          
          def domainFilter(fridgeName:String, token : String) : Boolean = 
            if (fridgeTarget.isDefined && userToken.isDefined)
              fridgeName == fridgeTarget.get && token != userToken.get 
            else true
          
          def receive = {
            case Notification(fridgeNameNotif,command,payload,timestamp,token) =>
                if (domainFilter(fridgeNameNotif,token)){
                  val pushedEvent = PushedEvent(command,payload,timestamp)
                  val nextChunk = MessageChunk("data: "+ formatEvent.write(pushedEvent) +"\n ")
                  ctx.responder ! nextChunk 
                } 
                
            case ev: Http.ConnectionClosed =>
              log.warning("Stopping response streaming due to {}", ev)
             
            case ReceiveTimeout =>
                ctx.responder ! MessageChunk(":\n") // Comment to keep connection alive  
          }

          //subscribe to notification
          context.system.eventStream.subscribe(self, classOf[Notification])
        }
      }
    }
  
  val EventStreamType = register(
	  MediaType.custom(
	    mainType = "text",
	    subType = "event-stream",
	    compressible = true,
	    binary = false
	   ))  
}