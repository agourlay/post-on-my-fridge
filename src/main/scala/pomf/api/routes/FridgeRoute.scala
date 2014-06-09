package pomf.api.route

import akka.actor._

import spray.routing._
import spray.httpx.encoding._
import spray.httpx.SprayJsonSupport._

import pomf.api.endpoint.JsonSupport._
import pomf.api.request._

class FridgeRoute(crudService : ActorRef)(implicit context: ActorContext) extends Directives {

  val route = 
    path("fridges" / JavaUUID) { fridgeId =>
      get {
        ctx => context.actorOf(FullFridge.props(fridgeId, ctx, crudService))
      }
    } ~
    path("fridges") {
      get {
        ctx => context.actorOf(AllFridges.props(ctx, crudService))
      }
    } ~
    path("fridges") {
      post {
        entity(as[String]) { fridgeName =>
          ctx => context.actorOf(CreateFridge.props(fridgeName, ctx, crudService))
        }
      }
    }   
}