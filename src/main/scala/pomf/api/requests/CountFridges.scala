package pomf.api.request

import akka.actor._

import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.service.CrudServiceProtocol._
import pomf.service.CrudServiceProtocol

class CountFridges(ctx : RequestContext, crudService: ActorRef) extends RestRequest(ctx) {

  crudService ! CrudServiceProtocol.CountFridges

  override def receive = waitingCount orElse handleTimeout

  def waitingCount : Receive = {
    case Count(nb) => requestOver(nb.toString)
  }
}

object CountFridges {
   def props(ctx : RequestContext, crudService: ActorRef) 
     = Props(classOf[CountFridges], ctx, crudService).withDispatcher("requests-dispatcher")
}