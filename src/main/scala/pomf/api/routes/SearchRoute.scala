package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Fridge
import pomf.service.CrudServiceProtocol

class SearchRoute(crudService : ActorRef)(implicit context: ActorContext) extends Directives {

  implicit val timeout = akka.util.Timeout(5 seconds)
  implicit def executionContext = context.dispatcher
  
  val route = 
    pathPrefix("search") {
      path("fridge") {
        parameters("term") { term =>
          get {
            complete {
              (crudService ? CrudServiceProtocol.SearchFridge(term)).mapTo[List[Fridge]]
            }
          }
        }
      }
    }       
}