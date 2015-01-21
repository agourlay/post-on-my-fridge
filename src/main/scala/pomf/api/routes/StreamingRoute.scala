package pomf.api.route

import akka.actor.{ ActorRef, ActorContext }
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.Source
import akka.http.server._
import Directives._

import de.heikoseeberger.akkasse.{ ServerSentEvent, EventStreamMarshalling }

import java.util.UUID

import pomf.api.endpoint.JsonSupport
import pomf.domain.actors.FridgeUpdatePublisher
import pomf.domain.model._

object StreamingRoute extends EventStreamMarshalling with JsonSupport {

  implicit def flowEventToSseMessage(event: PushedEvent): ServerSentEvent = {
    ServerSentEvent(formatEvent.write(event) + "\n\n")
  }

  def build(implicit context: ActorContext) = {
    implicit val ec = context.dispatcher

    pathPrefix("stream") {
      get {
        path("fridge" / JavaUUID) { fridgeId ⇒
          parameters("token") { token ⇒
            complete {
              Source(ActorPublisher[PushedEvent](streamUser(fridgeId, token, context)))
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