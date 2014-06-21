package pomf.api.request

import akka.actor._
import akka.pattern._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.FridgeFull
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class FullFridge(fridgeId : UUID, ctx: RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.FullFridge(fridgeId)

  override def receive = waitingFridge orElse handleTimeout

  def waitingFridge : Receive = {
    case f : FridgeFull  => requestOver(f)
  }
}

object FullFridge {
   def props(fridgeId: UUID, ctx: RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) 
     = Props(classOf[FullFridge], fridgeId, ctx, crudService, breaker).withDispatcher("requests-dispatcher")
}