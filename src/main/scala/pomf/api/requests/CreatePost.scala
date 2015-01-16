package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import spray.json._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CreatePost(post: Post, token: String, ctx: RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CreatePost(post, token)

  override def receive = super.receive orElse waitingCreate

  def waitingCreate: Receive = {
    case p: Post ⇒ requestOver(p)
  }
}

object CreatePost {
  def props(post: Post, token: String, ctx: RequestContext, crudService: ActorRef) = Props(classOf[CreatePost], post, token, ctx, crudService)
}