package pomf.api.route

import akka.actor.{ Props, ActorRef, ActorContext }
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.Source
import akka.http.scaladsl.server._
import Directives._
import de.heikoseeberger.akkasse.EventStreamMarshalling
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
              Source.actorPublisher[PushedEvent](streamUser(fridgeId, token, context))
                .map(PushedEvent.toServerSentEvent)
            }
          }
        }
      }
    }
  }

  def streamUser(fridgeId: UUID, token: String, context: ActorContext): Props = {
    val filter = (fridgeTarget: UUID, userToken: String) ⇒ fridgeId == fridgeTarget && token != userToken
    FridgeUpdatePublisher.props(filter)
  }
}