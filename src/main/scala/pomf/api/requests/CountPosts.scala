package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import spray.json._
import DefaultJsonProtocol._

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CountPosts(ctx: RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CountPosts

  override def receive = super.receive orElse waitingCount

  def waitingCount: Receive = {
    case Count(nb) ⇒ requestOver(nb.toString)
  }
}

object CountPosts {
  def props(ctx: RequestContext, crudService: ActorRef) = Props(classOf[CountPosts], ctx, crudService)
}