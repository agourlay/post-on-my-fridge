package pomf.api.request

import akka.actor._

import scala.util.Failure
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import java.util.UUID

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class DeletePost(postId: UUID, token: String, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.DeletePost(postId, token)

  override def receive = super.receive orElse waitingDelete

  def waitingDelete : Receive = {
    case OperationSuccess(msg) => requestOver(msg)
  }
}

object DeletePost {
   def props(postId: UUID, token: String, ctx : RequestContext, crudService: ActorRef)
     = Props(classOf[DeletePost], postId, token, ctx, crudService).withDispatcher("requests-dispatcher")
}