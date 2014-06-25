package pomf.api.request

import akka.actor._
import akka.pattern._

import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CountPosts(ctx : RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CountPosts

  override def receive = super.receive orElse waitingCount
  
  def waitingCount : Receive = {
    case Count(nb) => requestOver(nb.toString)
  }
}

object CountPosts {
   def props(ctx : RequestContext, crudService: ActorRef)(implicit breaker: CircuitBreaker)
     = Props(classOf[CountPosts], ctx, crudService, breaker).withDispatcher("requests-dispatcher")
}