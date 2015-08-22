package pomf.api.route

import akka.actor.{ ActorRef, ActorContext }
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.Source
import akka.http.scaladsl.server._
import Directives._
import de.heikoseeberger.akkasse.{ WithHeartbeats, EventStreamMarshalling }
import scala.concurrent.duration._
import java.util.UUID

import pomf.api.endpoint.JsonSupport
import pomf.domain.actors.FridgeUpdatePublisher
import pomf.domain.model._

object StreamingRoute extends EventStreamMarshalling with JsonSupport {

  def build(implicit context: ActorContext) = {
    implicit val ec = context.dispatcher

    pathPrefix("stream") {
      get {
        path("fridge" / JavaUUID) { fridgeId ⇒
          parameters("token") { token ⇒
            complete {
              Source(ActorPublisher[PushedEvent](streamUser(fridgeId, token, context)))
                .map(PushedEvent.toServerSentEvent)
                .via(WithHeartbeats(1.second))
            }
          }
        }
      }
    }
  }

  def streamUser(fridgeId: UUID, token: String, context: ActorContext): ActorRef = {
    val filter = (fridgeTarget: UUID, userToken: String) ⇒ fridgeId == fridgeTarget && token != userToken
    context.actorOf(FridgeUpdatePublisher.props(filter))
  }
}