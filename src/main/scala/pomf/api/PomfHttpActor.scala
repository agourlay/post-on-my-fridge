package pomf.api

import pomf.util._
import pomf.service.CrudServiceActor
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
import CachingDirectives._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import DefaultJsonProtocol._
import reflect.ClassTag
import JsonSupport._


class PomfHttpActor extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(60.seconds)

  def receive = runRoute(restRoute ~ streamRoute ~ statsRoute ~ staticRoute)
  
  val crud = "/user/crud-service"
  val simpleCache = routeCache(maxCapacity = 1000, timeToIdle = Duration("30 min"))

  def restRoute =
    pathPrefix("api") {
      path("fridges" / Rest) { fridgeName =>
        get {
          complete {
            if (fridgeName.isEmpty)
              (context.actorSelection(crud) ? CrudServiceActor.AllFridge()).mapTo[List[Fridge]]
            else
              (context.actorSelection(crud) ? CrudServiceActor.FullFridge(fridgeName)).mapTo[FridgeRest]
          }
        }
      } ~
      path("fridges") {
        post {
          entity(as[Fridge]) { fridge =>
            complete {
              (context.actorSelection(crud) ? CrudServiceActor.CreateFridge(fridge)).mapTo[Fridge]
            }
          }
        }
      } ~
      path("posts") {
        post {
          parameters("token") { token =>
            entity(as[Post]) { post =>
              complete {
                (context.actorSelection(crud) ? CrudServiceActor.CreatePost(post, token)).mapTo[Post]
              }
            }
          }
        } ~
          put {
            parameters("token") { token =>
              entity(as[Post]) { post =>
                complete {
                  (context.actorSelection(crud) ? CrudServiceActor.UpdatePost(post, token)).mapTo[Post]
                }
              }
            }
          }
        } ~
        path("posts" / LongNumber) { postId =>
          get {
            complete {
              (context.actorSelection(crud) ? CrudServiceActor.GetPost(postId)).mapTo[Option[Post]]
            }
          } ~
            delete {
              parameters("token") { token =>
                complete {
                  (context.actorSelection(crud) ? CrudServiceActor.DeletePost(postId, token)).mapTo[String]
                }
              }
            }
        } ~
        pathPrefix("rss") {
          path("fridge" / Rest) { fridgeName =>
            get {
              complete {
                (context.actorSelection(crud) ? CrudServiceActor.FridgeRss(fridgeName)).mapTo[scala.xml.Elem]
              }
            }
          }
        } ~
        pathPrefix("search") {
          path("fridge") {
            parameters("term") { term =>
              get {
                complete {
                  (context.actorSelection(crud) ? CrudServiceActor.SearchFridge(term)).mapTo[List[String]]
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
                  (context.actorSelection(crud) ? CrudServiceActor.PushChat(fridgeName, message, token)).mapTo[ChatMessage]
                }
              }
            }
          } ~
            get {
              complete {
                (context.actorSelection(crud) ? CrudServiceActor.ChatHistory(fridgeName)).mapTo[List[ChatMessage]]
              }
            }
        } ~
        path("token") {
          get {
            complete(TokenSupport.nextSessionId)
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
            .ask(Http.GetStats)(1.second)
            .mapTo[Stats]
        }
      }

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