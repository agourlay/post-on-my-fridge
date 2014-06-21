package pomf.api.request

import akka.actor._
import akka.pattern._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class UpdatePost(post: Post, token : String, ctx : RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.UpdatePost(post, token)

  override def receive = waitingUpdate orElse handleTimeout

  def waitingUpdate : Receive = {
    case p : Post => requestOver(p)
  }
}

object UpdatePost {
   def props(post: Post, token: String, ctx : RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) 
     = Props(classOf[UpdatePost], post, token, ctx, crudService, breaker).withDispatcher("requests-dispatcher")
}