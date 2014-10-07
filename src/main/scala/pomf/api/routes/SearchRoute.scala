package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }

import spray.routing._

import pomf.api.request.SearchFridge

class SearchRoute(crudService: ActorRef)(implicit context: ActorContext) extends Directives {

  val route =
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