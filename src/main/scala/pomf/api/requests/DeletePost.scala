package pomf.api.request

import akka.actor._
import akka.pattern._

import scala.util.Failure
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import java.util.UUID

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class DeletePost(postId: UUID, token: String, ctx : RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.DeletePost(postId, token)

  override def receive = waitingDelete orElse handleTimeout

  def waitingDelete : Receive = {
    case OperationSuccess(msg) => requestOver(msg)
  }
}

object DeletePost {
   def props(postId: UUID, token: String, ctx : RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) 
     = Props(classOf[DeletePost], postId, token, ctx, crudService, breaker).withDispatcher("requests-dispatcher")
}