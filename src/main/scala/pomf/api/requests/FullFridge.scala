package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import spray.json._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.FridgeFull
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class FullFridge(fridgeId: UUID, ctx: RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.FullFridge(fridgeId)

  override def receive = super.receive orElse waitingFridge

  def waitingFridge: Receive = {
    case f: FridgeFull ⇒ requestOver(f)
  }
}

object FullFridge {
  def props(fridgeId: UUID, ctx: RequestContext, crudService: ActorRef) = Props(classOf[FullFridge], fridgeId, ctx, crudService)
}