package pomf.api.request

import akka.actor._

import scala.util.Failure
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class DeletePost(postId: Long, token: String, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.DeletePost(postId, token)

  override def receive = waitingDelete orElse handleTimeout

  def waitingDelete : Receive = {
    case OperationSuccess(msg)  => {
      ctx.complete(msg)
      requestOver()
    }  
    case Failure(e) =>{
      ctx.complete(e)
      requestOver()
    }  
  }
}

object DeletePost {
   def props(postId: Long, token: String, ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[DeletePost], postId, token, ctx, crudService).withDispatcher("requests-dispatcher")
}