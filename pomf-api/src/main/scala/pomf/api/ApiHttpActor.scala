package pomf.api

import pomf.util._
import pomf.service.CrudServiceActor
import pomf.domain.model._

import akka.pattern._
import akka.actor._

import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.can.Http
import spray.can.server.Stats

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import DefaultJsonProtocol._
import reflect.ClassTag
import JsonSupport._


class ApiHttpActor extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(60.seconds)

  def receive = runRoute(pomfRoute)
  
  private var crudService : ActorRef = _

  override def preStart() {
    crudService = context.actorFor("/user/crud-service")
  }

  private val pomfRoute =
    pathPrefix("api") {
      path("fridges" / Rest) { fridgeName =>
        get {
          complete {
            if (fridgeName.isEmpty)
              (crudService ? CrudServiceActor.AllFridge()).mapTo[List[Fridge]]
            else
              (crudService ? CrudServiceActor.FullFridge(fridgeName)).mapTo[FridgeRest]
          }
        }
      } ~
      path("fridges") {
        post {
          entity(as[Fridge]) { fridge =>
            complete {
              (crudService ? CrudServiceActor.CreateFridge(fridge)).mapTo[Fridge]
            }
          }
        }
      } ~
      path("posts") {
        post {
          parameters("token") { token =>
            entity(as[Post]) { post =>
              complete {
                (crudService ? CrudServiceActor.CreatePost(post, token)).mapTo[Post]
              }
            }
          }
        } ~
          put {
            parameters("token") { token =>
              entity(as[Post]) { post =>
                complete {
                  (crudService ? CrudServiceActor.UpdatePost(post, token)).mapTo[Post]
                }
              }
            }
          }
        } ~
        path("posts" / LongNumber) { postId =>
          get {
            complete {
              (crudService ? CrudServiceActor.GetPost(postId)).mapTo[Option[Post]]
            }
          } ~
            delete {
              parameters("token") { token =>
                complete {
                  (crudService ? CrudServiceActor.DeletePost(postId, token)).mapTo[String]
                }
              }
            }
        } ~
        pathPrefix("rss") {
          path("fridge" / Rest) { fridgeName =>
            get {
              complete {
                (crudService ? CrudServiceActor.FridgeRss(fridgeName)).mapTo[scala.xml.Elem]
              }
            }
          }
        } ~
        pathPrefix("search") {
          path("fridge") {
            parameters("term") { term =>
              get {
                complete {
                  (crudService ? CrudServiceActor.SearchFridge(term)).mapTo[List[String]]
                }
              }
            }
          }
        } ~
        path("messages" / Rest) { fridgeName =>
          post {
            parameters("token") { token =>
              entity(as[ChatMessage]) { message =>
                complete {
                  (crudService ? CrudServiceActor.PushChat(fridgeName, message, token)).mapTo[ChatMessage]
                }
              }
            }
          } ~
            get {
              complete {
                (crudService ? CrudServiceActor.ChatHistory(fridgeName)).mapTo[List[ChatMessage]]
              }
            }
        } ~
        path("token") {
          get {
            complete(TokenSupport.nextSessionId)
          }
        } ~ 
        path("stats") {
          complete {
              context.actorFor("/user/IO-HTTP/listener-0") ? Http.GetStats map {
                case stats: Stats ⇒
                  s"""
                  | Uptime                : ${Duration(stats.uptime.toHours, TimeUnit.HOURS)}
                  | Total requests        : ${stats.totalRequests}
                  | Open requests         : ${stats.openRequests}
                  | Max open requests     : ${stats.maxOpenRequests}
                  | Total connections     : ${stats.totalConnections}
                  | Open connections      : ${stats.openConnections}
                  | Max open connections  : ${stats.maxOpenConnections}
                  | Requests timed out    : ${stats.requestTimeouts}
                  """.trim.stripMargin
              }
           }
        }
    }
  }