package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.Marshaller._
import akka.http.unmarshalling.Unmarshal
import akka.http.server._
import Directives._

import pomf.api.request._

object FridgeRoute {

  def build(crudService: ActorRef)(implicit context: ActorContext) =
    path("fridges" / JavaUUID) { fridgeId ⇒
      get {
        ctx ⇒ context.actorOf(FullFridge.props(fridgeId, ctx, crudService))
      }
    } ~
      path("fridges") {
        parameters('pageNumber ? 1, 'pageSize ? 50) { (pageNumber, pageSize) ⇒
          get {
            ctx ⇒ context.actorOf(AllFridges.props(pageNumber, pageSize, ctx, crudService))
          }
        }
      } ~
      path("fridges") {
        post {
          entity(as[String]) { fridgeName ⇒
            ctx ⇒ context.actorOf(CreateFridge.props(fridgeName, ctx, crudService))
          }
        }
      }
}