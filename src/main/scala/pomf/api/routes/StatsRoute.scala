package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }
import akka.http.marshallers.sprayjson.SprayJsonSupport
import akka.http.marshalling.Marshaller._
import akka.http.unmarshalling.Unmarshal
import akka.http.server._
import Directives._

import pomf.api.request.{ AllMetrics, CountFridges, CountPosts }

object StatsRoute {

  def build(crudService: ActorRef, metricsRepo: ActorRef)(implicit context: ActorContext) =
    path("stats") {
      get { ctx ⇒
        context.actorOf(AllMetrics.props(ctx, metricsRepo))
      }
    } ~
      pathPrefix("count") {
        path("fridges") {
          get { ctx ⇒
            context.actorOf(CountFridges.props(ctx, crudService))
          }
        } ~
          path("posts") {
            get { ctx ⇒
              context.actorOf(CountPosts.props(ctx, crudService))
            }
          }
      }
}