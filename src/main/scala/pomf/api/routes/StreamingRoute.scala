package pomf.api.route

import akka.actor._

import spray.httpx.encoding._
import spray.routing._

import pomf.api.streaming._

class StreamingRoute(implicit context: ActorContext) extends Directives {

  val route = 
    pathPrefix("stream") {
       get {
	      path("fridge" / LongNumber) { fridgeId =>
	        parameters("token") { token =>
	            streamUser(fridgeId, token)
	        }    
        } ~  
        path("firehose") {
          	streamFirehose
        }      
      }
    }


  def streamFirehose(ctx: RequestContext): Unit = {
    context.actorOf(Props(new ActivityStream(ctx.responder)((_,_) => true)))
  }

  def streamUser(fridgeId : Long, token : String)(ctx: RequestContext): Unit = {
    val filter = (fridgeTarget:Long, userToken : String) => fridgeId == fridgeTarget && token != userToken
    context.actorOf(Props(new ActivityStream(ctx.responder)(filter)))
  }         
}