package pomf.api.request

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class GetPost(postId: UUID, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.GetPost(postId)

  override def receive = waitingDelete orElse handleTimeout

  def waitingDelete : Receive = {
    case p : Post  => {
      ctx.complete(p)
      requestOver()
    }  
  }
}

object GetPost {
   def props(postId: UUID, ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[GetPost], postId, ctx, crudService).withDispatcher("requests-dispatcher")
}