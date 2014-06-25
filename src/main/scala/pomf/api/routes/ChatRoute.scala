package pomf.api.route

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.service.ChatRepoProtocol
import pomf.api.request._

class ChatRoute(chatRepo : ActorRef)(implicit context: ActorContext) extends RouteWithBreaker{
  
  val route = 
    pathPrefix("chat" / JavaUUID) { fridgeId =>
      path("messages") {
        post {
          parameters("token") { token =>
            entity(as[ChatMessage]) { message =>
              ctx => context.actorOf(SendChatMessage.props(fridgeId, message, token, chatRepo, ctx))
            }
          } 
        } ~
        get {
          ctx => context.actorOf(ChatHistory.props(fridgeId, chatRepo, ctx)) 
        }
      } ~
      path("participants") {
        post {
          parameters("token") { token =>
            entity(as[String]) { participantName =>
              ctx => context.actorOf(AddChatParticipant.props(fridgeId, token, participantName, chatRepo, ctx))
            }
          } 
        } ~
        put {
          parameters("token") { token =>
            entity(as[String]) { newName =>
              ctx => context.actorOf(RenameChatParticipant.props(fridgeId, token, newName, chatRepo, ctx))
            }
          }
        } ~
        get {
          ctx => context.actorOf(ChatParticipantNumber.props(fridgeId, chatRepo, ctx))
        } ~
        delete {
          parameters("token") { token =>
            ctx => context.actorOf(RemoveChatParticipant.props(fridgeId, token, chatRepo, ctx))
          }
        }
      }
    }            
}