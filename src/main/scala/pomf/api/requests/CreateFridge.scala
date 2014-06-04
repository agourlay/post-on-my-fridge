package pomf.api.request

import akka.actor._
import scala.util.Failure

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Fridge
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CreateFridge(fridge: Fridge, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CreateFridge(fridge)

  override def receive = waitingCreate orElse handleTimeout

  def waitingCreate : Receive = {
    case f : Fridge  => {
      ctx.complete(f)
      requestOver()
    }  
    case Failure(e) =>{
      ctx.complete(e)
      requestOver()
    }  
  }
}

object CreateFridge {
   def props(fridge: Fridge, ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[CreateFridge], fridge, ctx, crudService)
}