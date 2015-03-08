package pomf.api.route

import akka.actor.{ ActorRef, ActorContext }
import akka.pattern._
import akka.http.marshalling.Marshaller._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.stream.ActorFlowMaterializer
import pomf.domain.actors.{ ChatRoomProtocol, ChatRepoProtocol }

import pomf.core.configuration._
import pomf.domain.model.ChatMessage
import pomf.domain.actors.ChatRoomProtocol._
import pomf.api.endpoint.JsonSupport

object ChatRoute extends JsonSupport {

  def build(chatRepo: ActorRef)(implicit context: ActorContext, fm: ActorFlowMaterializer) = {
    implicit val timeout = akka.util.Timeout(Settings(context.system).Timeout)
    implicit val ec = context.dispatcher

    pathPrefix("chat" / JavaUUID) { fridgeId ⇒
      path("messages") {
        post {
          parameters("token") { token ⇒
            entity(as[ChatMessage]) { message ⇒
              complete {
                (chatRepo ? ChatRepoProtocol.ToChatRoom(fridgeId, ChatRoomProtocol.SendMessage(message, token))).mapTo[ChatMessage].map {
                  case cm: ChatMessage ⇒ ToResponseMarshallable(OK -> cm)
                }
              }
            }
          }
        } ~
          get {
            complete {
              (chatRepo ? ChatRepoProtocol.ToChatRoom(fridgeId, ChatRoomProtocol.ChatHistory)).mapTo[ChatHistoryContent].map {
                case ChatHistoryContent(messages) ⇒ ToResponseMarshallable(OK -> messages)
              }
            }
          }
      } ~
        path("participants") {
          post {
            parameters("token") { token ⇒
              entity(as[String]) { participantName ⇒
                complete {
                  (chatRepo ? ChatRepoProtocol.ToChatRoom(fridgeId, ChatRoomProtocol.AddParticipant(token, participantName))).mapTo[String]
                }
              }
            }
          } ~
            put {
              parameters("token") { token ⇒
                entity(as[String]) { newName ⇒
                  complete {
                    (chatRepo ? ChatRepoProtocol.ToChatRoom(fridgeId, ChatRoomProtocol.RenameParticipant(token, newName))).mapTo[String]
                  }
                }
              }
            } ~
            get {
              complete {
                (chatRepo ? ChatRepoProtocol.ToChatRoom(fridgeId, ChatRoomProtocol.ParticipantNumber)).mapTo[ParticipantNumberRoom].map {
                  case ParticipantNumberRoom(nb) ⇒ ToResponseMarshallable(OK -> nb.toString)
                }
              }
            } ~
            delete {
              parameters("token") { token ⇒
                complete {
                  (chatRepo ? ChatRepoProtocol.ToChatRoom(fridgeId, ChatRoomProtocol.RemoveParticipant(token))).mapTo[String]
                }
              }
            }
        }
    }
  }
}