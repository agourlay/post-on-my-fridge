package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.ChatMessage
import pomf.service.ChatServiceProtocol

class ChatRoute(chatService : ActorRef)(implicit context: ActorContext) extends Directives {

  implicit val timeout = akka.util.Timeout(5 seconds)
  implicit def executionContext = context.dispatcher
  
  val route = 
    pathPrefix("chat" / LongNumber) { fridgeId =>
      path("messages") {
        post {
          parameters("token") { token =>
            entity(as[ChatMessage]) { message =>
              complete {
                (chatService ? ChatServiceProtocol.SendMessage(fridgeId, message, token)).mapTo[ChatMessage]
              }
            }
          } 
        } ~
        get {
          complete {
              (chatService ? ChatServiceProtocol.ChatHistory(fridgeId)).mapTo[List[ChatMessage]]
          }  
        }
      } ~
      path("participants") {
        post {
          parameters("token") { token =>
            entity(as[String]) { participantName =>
              complete {
                chatService ! ChatServiceProtocol.AddParticipant(fridgeId, token, participantName)
                participantName + " joined chat " +  fridgeId
              }
            }
          } 
        } ~
        put {
          parameters("token") { token =>
            entity(as[String]) { participantName =>
              complete {
                chatService ! ChatServiceProtocol.RenameParticipant(fridgeId, token, participantName)
                participantName + "changed name" 
              }
            }
          }
        } ~
        get {
          complete {
              (chatService ? ChatServiceProtocol.ParticipantNumber(fridgeId)).mapTo[String]
          }  
        } ~
        delete {
          parameters("token") { token =>
            complete {
              chatService ! ChatServiceProtocol.RemoveParticipant(fridgeId, token)
              token + " removed from chat " +  fridgeId
            }
          }
        }
      }
    }        
}