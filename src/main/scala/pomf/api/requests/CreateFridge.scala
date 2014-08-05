package pomf.api.request

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Fridge
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CreateFridge(fridgeName: String, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CreateFridge(fridgeName)

  override def receive = super.receive orElse waitingCreate

  def waitingCreate : Receive = {
    case f : Fridge  => requestOver(f)
  }
}

object CreateFridge {
   def props(fridgeName: String, ctx : RequestContext, crudService: ActorRef)
     = Props(classOf[CreateFridge], fridgeName, ctx, crudService).withDispatcher("requests-dispatcher")
}