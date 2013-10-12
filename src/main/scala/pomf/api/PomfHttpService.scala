package pomf.api

import pomf.service.CrudServiceProtocol
import pomf.service.ChatServiceProtocol
import pomf.service.TokenServiceProtocol
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


class PomfHttpService(crudService: ActorRef, chatService: ActorRef, tokenService : ActorRef) extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(10 seconds)

  def receive = runRoute(fridgeRoute ~ postRoute ~ streamRoute ~ chatRoute ~ miscRoute ~ statsRoute ~ staticRoute)

  val fridgesCache: Cache[List[FridgeRest]] = LruCache(maxCapacity = 1, timeToLive = 1 minute)

  def fridgeRoute =
    path("fridges" / Rest) { fridgeName =>
      get {
        complete {
          if (fridgeName.isEmpty)
            fridgesCache("fridges"){
              (crudService ? CrudServiceProtocol.AllFridge).mapTo[List[FridgeRest]]
            }
          else
            (crudService ? CrudServiceProtocol.FullFridge(fridgeName)).mapTo[FridgeRest]
        }
      }
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
    pathPrefix("rss") {
      path("fridge" / Rest) { fridgeName =>
        get {
          complete {
            (crudService ? CrudServiceProtocol.FridgeRss(fridgeName)).mapTo[scala.xml.Elem]
          }
        }
      }
    } ~
    pathPrefix("search") {
      path("fridge") {
        parameters("term") { term =>
          get {
            complete {
              (crudService ? CrudServiceProtocol.SearchFridge(term)).mapTo[List[String]]
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
    pathPrefix("chat" / Segment) { fridgeName =>
      path("messages") {
        post {
          parameters("token") { token =>
            entity(as[ChatMessage]) { message =>
              complete {
                (chatService ? ChatServiceProtocol.SendMessage(fridgeName, message, token)).mapTo[ChatMessage]
              }
            }
          } 
        } ~
        get {
          complete {
              (chatService ? ChatServiceProtocol.ChatHistory(fridgeName)).mapTo[List[ChatMessage]]
          }  
        }
      } ~
      path("participants") {
        post {
          parameters("token") { token =>
            entity(as[String]) { participantName =>
              complete {
                chatService ! ChatServiceProtocol.AddParticipant(fridgeName, token, participantName)
                participantName + " joined chat " +  fridgeName
              }
            }
          } 
        } ~
        put {
          parameters("token") { token =>
            entity(as[String]) { participantName =>
              complete {
                chatService ! ChatServiceProtocol.RenameParticipant(fridgeName, token, participantName)
                participantName + "changed name" 
              }
            }
          }
        } ~
        get {
          complete {
              (chatService ? ChatServiceProtocol.ParticipantNumber(fridgeName)).mapTo[String]
          }  
        } ~
        delete {
          parameters("token") { token =>
            complete {
              chatService ! ChatServiceProtocol.RemoveParticipant(fridgeName, token)
              token + " removed from chat " +  fridgeName
            }
          }
        }
      }
    } 
      
  def streamRoute = 
    pathPrefix("stream") {
      get {
        path("fridge" / Rest) { fridgeName =>
            parameters("token") { token =>
              streamFirehose(Some(fridgeName),Some(token))
            }    
        } ~  
        path("firehose") {
          streamFirehose() 
        } ~  
        path("stats") {
          streamStat 
        }        
      }
    } 
  
  def statsRoute = 
    path("stats") {
        complete {
          (context.actorSelection("/user/IO-HTTP/listener-0") ? Http.GetStats).mapTo[Stats]
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

  def streamFirehose(fridgeTarget : Option[String] = None, userToken : Option[String] = None)(ctx: RequestContext): Unit = {
    val fireHoseActor = context.actorOf(Props(new FirehoseStream(fridgeTarget, userToken, ctx)))
    context.system.eventStream.subscribe(fireHoseActor, classOf[Notification])
  }

  def streamStat (ctx: RequestContext): Unit = {
    val statActor = context.actorOf(Props(new StatStream(ctx)))
    context.system.scheduler.schedule(1.seconds,1.seconds){
      val stats = (context.actorSelection("/user/IO-HTTP/listener-0") ? Http.GetStats).mapTo[Stats]
      stats pipeTo statActor
    }
  }  
}