package pomf.api.request

import akka.actor._
import scala.util.Failure

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class AllFridges(ctx: RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.AllFridge

  override def receive = waitingFridges orElse handleTimeout

  def waitingFridges : Receive = {
    case LightFridges(f)  => {
      ctx.complete(f)
      requestOver()
    }  
    case Failure(e) =>{
      ctx.complete(e)
      requestOver()
    }  
  }
}

object AllFridges {
   def props(ctx: RequestContext, crudService: ActorRef) 
     = Props(classOf[AllFridges], ctx, crudService).withDispatcher("requests-dispatcher")
}