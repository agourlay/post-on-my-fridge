package pomf.api.request

import akka.actor._
import scala.util.Failure

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.FridgeRest
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class FullFridge(fridgeId : Long, ctx: RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.FullFridge(fridgeId)

  override def receive = waitingFridge orElse handleTimeout

  def waitingFridge : Receive = {
    case f : FridgeRest  => {
      ctx.complete(f)
      requestOver()
    }  
    case Failure(e) =>{
      ctx.complete(e)
      requestOver()
    }  
  }
}

object FullFridge {
   def props(fridgeId: Long, ctx: RequestContext, crudService: ActorRef) 
     = Props(classOf[FullFridge], fridgeId, ctx, crudService)
}