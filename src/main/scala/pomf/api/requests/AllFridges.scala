package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import java.util.UUID

import spray.json._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class AllFridges(pageNumber: Int, pageSize: Int, ctx: RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.AllFridge(pageNumber, pageSize)

  override def receive = super.receive orElse waitingFridges

  def waitingFridges: Receive = {
    case LightFridges(f) ⇒ requestOver(f)
  }
}

object AllFridges {
  def props(pageNumber: Int, pageSize: Int, ctx: RequestContext, crudService: ActorRef) = Props(classOf[AllFridges], pageNumber, pageSize, ctx, crudService)
}