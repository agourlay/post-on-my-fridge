package pomf.api.request

import akka.actor._
import akka.pattern._

import java.util.UUID

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class AllFridges(pageNumber : Int, pageSize : Int, ctx: RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.AllFridge(pageNumber, pageSize)

  override def receive = super.receive orElse waitingFridges

  def waitingFridges : Receive = {
    case LightFridges(f)  => requestOver(f)
  }
}

object AllFridges {
   def props(pageNumber : Int, pageSize : Int, ctx: RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) 
     = Props(classOf[AllFridges], pageNumber, pageSize, ctx, crudService, breaker).withDispatcher("requests-dispatcher")
}