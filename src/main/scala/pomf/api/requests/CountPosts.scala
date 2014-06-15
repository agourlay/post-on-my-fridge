package pomf.api.request

import akka.actor._

import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CountPosts(ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CountPosts

  override def receive = waitingCount orElse handleTimeout

  def waitingCount : Receive = {
    case Count(nb) => requestOver(nb.toString)
  }
}

object CountPosts {
   def props(ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[CountPosts], ctx, crudService).withDispatcher("requests-dispatcher")
}