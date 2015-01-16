package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }
import akka.http.server._
import Directives._

import pomf.api.request.SearchFridge

object SearchRoute {

  def build(crudService: ActorRef)(implicit context: ActorContext) =
    pathPrefix("search") {
      path("fridge") {
        parameters("term") { term ⇒
          get { ctx ⇒
            context.actorOf(SearchFridge.props(term, ctx, crudService))
          }
        }
      }
    }
}