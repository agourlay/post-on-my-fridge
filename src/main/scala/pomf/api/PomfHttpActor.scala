package pomf.api

import pomf.util._
import pomf.service.CrudServiceActor
import pomf.service.ChatServiceActor
import pomf.service.TokenServiceActor
import pomf.domain.model._

import akka.pattern._
import akka.actor._

import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.httpx.encoding._
import spray.routing._
import spray.routing.directives.CachingDirectives._
import spray.http._
import spray.http.MediaTypes._
import spray.can.Http
import spray.can.server.Stats

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

import DefaultJsonProtocol._
import reflect.ClassTag
import JsonSupport._


class PomfHttpActor extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(30.seconds)

  def receive = runRoute(fridgeRoute ~ postRoute ~ streamRoute ~ chatRoute ~ miscRoute ~ statsRoute ~ staticRoute)
  
  val crudActor = "/user/crud-service"
  val chatActor = "/user/chat-service"
  val tokenActor = "/user/token-service"

  def fridgeRoute =
      path("fridges" / Rest) { fridgeName =>
        get {
          complete {
            if (fridgeName.isEmpty)
              (context.actorSelection(crudActor) ? CrudServiceActor.AllFridge()).mapTo[List[Fridge]]
            else
              (context.actorSelection(crudActor) ? CrudServiceActor.FullFridge(fridgeName)).mapTo[FridgeRest]
          }
        }
      } ~
      path("fridges") {
        post {
          entity(as[Fridge]) { fridge =>
            complete {
              (context.actorSelection(crudActor) ? CrudServiceActor.CreateFridge(fridge)).mapTo[Fridge]
            }
          }
        }
      }
      
  def postRoute = 
      path("posts") {
        post {
          parameters("token") { token =>
            entity(as[Post]) { post =>
              complete {
                (context.actorSelection(crudActor) ? CrudServiceActor.CreatePost(post, token)).mapTo[Post]
              }
            }
          }
        } ~
          put {
            parameters("token") { token =>
              entity(as[Post]) { post =>
                complete {
                  (context.actorSelection(crudActor) ? CrudServiceActor.UpdatePost(post, token)).mapTo[Post]
                }
              }
            }
          }
        } ~
        path("posts" / LongNumber) { postId =>
          get {
            complete {
              (context.actorSelection(crudActor) ? CrudServiceActor.GetPost(postId)).mapTo[Option[Post]]
            }
          } ~
            delete {
              parameters("token") { token =>
                complete {
                  (context.actorSelection(crudActor) ? CrudServiceActor.DeletePost(postId, token)).mapTo[String]
                }
              }
            }
          }
     
    def miscRoute =
        pathPrefix("rss") {
          path("fridge" / Rest) { fridgeName =>
            get {
              complete {
                (context.actorSelection(crudActor) ? CrudServiceActor.FridgeRss(fridgeName)).mapTo[scala.xml.Elem]
              }
            }
          }
        } ~
        pathPrefix("search") {
          path("fridge") {
            parameters("term") { term =>
              get {
                complete {
                  (context.actorSelection(crudActor) ? CrudServiceActor.SearchFridge(term)).mapTo[List[String]]
                }
              }
            }
          }
        } ~ 
        path("token") {
          get {
            complete{
              (context.actorSelection(tokenActor) ? TokenServiceActor.RequestToken).mapTo[String]
            }
          }
        }
    
    def chatRoute = 
        path("messages" / Rest) { fridgeName =>
          post {
            parameters("token") { token =>
              entity(as[ChatMessage]) { message =>
                complete {
                  (context.actorSelection(chatActor) ? ChatServiceActor.PushChat(fridgeName, message, token)).mapTo[ChatMessage]
                }
              }
            }
          } ~
            get {
              complete {
                (context.actorSelection(chatActor) ? ChatServiceActor.ChatHistory(fridgeName)).mapTo[Future[List[ChatMessage]]]
              }
            }
        }
      
  def streamRoute = 
    pathPrefix("stream") {
      get {
        path("fridge" / Rest) { fridgeName =>
            parameters("token") { token =>
              streamActivity(Some(fridgeName),Some(token))
            }    
        } ~  
        path("firehose") {
          streamActivity() 
        }    
      }
    } 
  
  def statsRoute = 
    path("stats") {
        complete {
          context.actorSelection("/user/IO-HTTP/listener-0")
            .ask(Http.GetStats)
            .mapTo[Stats]
        }
      }

  val simpleCache = routeCache(maxCapacity = 500)
  
  def staticRoute = 
    path(""){
        cache(simpleCache) {
            encodeResponse(Gzip){
                getFromResource("web/index.html")   
            }
        }
    } ~
    cache(simpleCache) {
        encodeResponse(Gzip){
            getFromResourceDirectory("web")
        }
    }    

  def streamActivity(fridgeTarget : Option[String] = None, userToken : Option[String] = None)(ctx: RequestContext): Unit = {
    val connectionHandler = context.actorOf(Props(new ServerSentEventActor(fridgeTarget, userToken, ctx)))
    //subscribe to notification event
    context.system.eventStream.subscribe(connectionHandler, classOf[Notification])
  }  
}