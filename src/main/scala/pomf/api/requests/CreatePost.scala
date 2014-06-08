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

class CreatePost(post: Post, token : String, ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CreatePost(post, token)

  override def receive = waitingCreate orElse handleTimeout

  def waitingCreate : Receive = {
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

object CreatePost {
   def props(post: Post, token: String, ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[CreatePost], post, token, ctx, crudService).withDispatcher("requests-dispatcher")
}