package pomf.api.request

import akka.actor._
import scala.util.Failure

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class UpdatePost(post: Post, token : String, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.UpdatePost(post, token)

  override def receive = waitingUpdate orElse handleTimeout

  def waitingUpdate : Receive = {
    case p : Post  => {
      ctx.complete(p)
      requestOver()
    }  
    case Failure(e) =>{
      ctx.complete(e)
      requestOver()
    }  
  }
}

object UpdatePost {
   def props(post: Post, token: String, ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[UpdatePost], post, token, ctx, crudService).withDispatcher("requests-dispatcher")
}