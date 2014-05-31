package pomf.api

import pomf.service.CrudServiceProtocol
import pomf.service.ChatServiceProtocol
import pomf.service.TokenServiceProtocol
import pomf.api.exceptions.RestFailureHandling
import pomf.domain.model._
import pomf.api.streaming._

import akka.pattern._
import akka.actor._

import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.httpx.encoding._
import spray.routing._
import spray.routing.directives.CachingDirectives._
import spray.can.Http
import spray.can.server.Stats
import spray.caching.{LruCache, Cache}
import scala.concurrent.duration._

import scala.concurrent.duration._
import scala.concurrent.Future

import DefaultJsonProtocol._
import reflect.ClassTag
import JsonSupport._


class PomfHttpService(crudService: ActorRef, chatService: ActorRef, tokenService: ActorRef) extends HttpServiceActor with RestFailureHandling with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(5 seconds)

  def receive = runRoute(fridgeRoute ~ postRoute ~ streamRoute ~ chatRoute ~ miscRoute ~ statsRoute ~ staticRoute)

  val fridgesCache: Cache[List[FridgeRest]] = LruCache(maxCapacity = 1, timeToLive = 10 seconds)

  def fridgeRoute =
    path("fridges" / LongNumber) { fridgeId =>
      get {
        complete {
          (crudService ? CrudServiceProtocol.FullFridge(fridgeId)).mapTo[FridgeRest]
        }
      }
    } ~
    path("fridges") {
      get {
        complete {
          fridgesCache("fridges"){
           (crudService ? CrudServiceProtocol.AllFridge).mapTo[List[FridgeRest]]
          }
      } }
    } ~
    path("fridges") {
      post {
        entity(as[Fridge]) { fridge =>
          complete {
            (crudService ? CrudServiceProtocol.CreateFridge(fridge)).mapTo[Fridge]
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
              (crudService ? CrudServiceProtocol.CreatePost(post, token)).mapTo[Post]
            }
          }
        }
      } ~
        put {
          parameters("token") { token =>
            entity(as[Post]) { post =>
              complete {
                (crudService ? CrudServiceProtocol.UpdatePost(post, token)).mapTo[Post]
              }
            }
          }
        }
      } ~
      path("posts" / LongNumber) { postId =>
        get {
          complete {
            (crudService ? CrudServiceProtocol.GetPost(postId)).mapTo[Option[Post]]
          }
        } ~
          delete {
            parameters("token") { token =>
              complete {
                (crudService ? CrudServiceProtocol.DeletePost(postId, token)).mapTo[String]
              }
            }
          }
        }
    
  val countCache: Cache[String] = LruCache(maxCapacity = 2, timeToLive = 1 minute)

  def miscRoute =
    pathPrefix("search") {
      path("fridge") {
        parameters("term") { term =>
          get {
            complete {
              (crudService ? CrudServiceProtocol.SearchFridge(term)).mapTo[List[Fridge]]
            }
          }
        }
      }
    } ~ 
    path("token") {
      get {
        complete{
          (tokenService ? TokenServiceProtocol.RequestToken).mapTo[String]
        }
      }
    } ~ 
    pathPrefix("count") {
      path("fridges") {
          get {
            complete {
              countCache("fridges"){
                (crudService ? CrudServiceProtocol.CountFridges).mapTo[String]
              }     
            }
          }
      } ~ 
      path("posts") {
          get {
            complete {
              countCache("posts"){
                (crudService ? CrudServiceProtocol.CountPosts).mapTo[String]
              }   
            }
          }
      }
    } 
    
  def chatRoute = 
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
      
  def streamRoute = 
    pathPrefix("stream") {
      get {
        path("fridge" / LongNumber) { fridgeId =>
          parameters("token") { token =>
            streamUser(fridgeId, token)
          }    
        } ~  
        path("firehose") {
          streamFirehose
        }      
      }
    } 
  
  def statsRoute = 
    path("stats") {
      complete {
        "blah"
      }
    }
  
  def staticRoute = 
    pathSingleSlash{
      encodeResponse(Gzip){
        getFromResource("frontend/web/index.html")   
      }
    } ~
    encodeResponse(Gzip){
      getFromResourceDirectory("frontend/web")
    }    

  def streamFirehose(ctx: RequestContext): Unit = {
    context.actorOf(Props(new FirehoseStream(ctx.responder)((_,_) => true)))
  }

  def streamUser(fridgeId : Long, token : String)(ctx: RequestContext): Unit = {
    val filter = (fridgeTarget:Long, userToken : String) => fridgeId == fridgeTarget && token != userToken
    context.actorOf(Props(new FirehoseStream(ctx.responder)(filter)))
  }
}