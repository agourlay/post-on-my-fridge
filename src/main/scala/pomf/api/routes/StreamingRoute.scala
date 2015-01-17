package pomf.api.route

import akka.actor.{ ActorRef, ActorContext }
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.Marshaller._
import akka.http.unmarshalling.Unmarshal
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{ ImplicitFlowMaterializer, Source }
import akka.http.server._
import Directives._

import de.heikoseeberger.akkasse.{ Sse, SseMarshalling }

import java.util.UUID

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model._
import pomf.api.streaming.FridgeUpdates

object StreamingRoute extends SseMarshalling {

  implicit def flowEventToSseMessage(event: PushedEvent): Sse.Message = {
    Sse.Message(formatEvent.write(event) + "\n\n")
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
    context.actorOf(FridgeUpdates.props(filter))
  }
}