package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.Marshaller._
import akka.http.unmarshalling.Unmarshal
import akka.http.server.Directives._

import spray.json._
import spray.json.DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.api.request._

object ChatRoute {

  def build(chatRepo: ActorRef)(implicit context: ActorContext) =
    pathPrefix("chat" / JavaUUID) { fridgeId ⇒
      path("messages") {
        post {
          parameters("token") { token ⇒
            entity(as[ChatMessage]) { message ⇒
              ctx ⇒ context.actorOf(SendChatMessage.props(fridgeId, message, token, chatRepo, ctx))
            }
          }
        } ~
          get {
            ctx ⇒ context.actorOf(ChatHistory.props(fridgeId, chatRepo, ctx))
          }
      } ~
        path("participants") {
          post {
            parameters("token") { token ⇒
              entity(as[String]) { participantName ⇒
                ctx ⇒ context.actorOf(AddChatParticipant.props(fridgeId, token, participantName, chatRepo, ctx))
              }
            }
          } ~
            put {
              parameters("token") { token ⇒
                entity(as[String]) { newName ⇒
                  ctx ⇒ context.actorOf(RenameChatParticipant.props(fridgeId, token, newName, chatRepo, ctx))
                }
              }
            } ~
            get {
              ctx ⇒ context.actorOf(ChatParticipantNumber.props(fridgeId, chatRepo, ctx))
            } ~
            delete {
              parameters("token") { token ⇒
                ctx ⇒ context.actorOf(RemoveChatParticipant.props(fridgeId, token, chatRepo, ctx))
              }
            }
        }
    }
}