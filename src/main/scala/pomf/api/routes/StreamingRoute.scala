package pomf.api.route

import akka.actor._

import java.util.UUID
import spray.routing._

import pomf.api.streaming._

class StreamingRoute(implicit context: ActorContext) extends Directives {

  val route =
    pathPrefix("stream") {
      get {
        path("fridge" / JavaUUID) { fridgeId =>
          parameters("token") { token =>
            streamUser(fridgeId, token)
          }
        } ~
          path("firehose") {
            streamFirehose
          }
      }
    }

  def streamFirehose(ctx: RequestContext): Unit = {
    context.actorOf(FridgeUpdates.props(ctx.responder, (_, _) => true))
  }

  def streamUser(fridgeId: UUID, token: String)(ctx: RequestContext): Unit = {
    val filter = (fridgeTarget: UUID, userToken: String) => fridgeId == fridgeTarget && token != userToken
    context.actorOf(FridgeUpdates.props(ctx.responder, filter))
  }
}