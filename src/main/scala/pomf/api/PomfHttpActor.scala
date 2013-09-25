package pomf.api

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
import spray.can.Http
import spray.can.server.Stats
import spray.caching.{LruCache, Cache}
import scala.concurrent.duration._

import scala.concurrent.duration._
import scala.concurrent.Future

import DefaultJsonProtocol._
import reflect.ClassTag
import JsonSupport._


class PomfHttpActor(crudService: ActorRef, chatService: ActorRef, tokenService : ActorRef) extends HttpServiceActor with ActorLogging{
  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(30.seconds)

  def receive = runRoute(fridgeRoute ~ postRoute ~ streamRoute ~ chatRoute ~ miscRoute ~ statsRoute ~ staticRoute)

  val fridgesCache: Cache[List[FridgeRest]] = LruCache(maxCapacity = 1, timeToLive = 2 minute)

  def fridgeRoute =
    path("fridges" / Rest) { fridgeName =>
      get {
        complete {
          if (fridgeName.isEmpty)
            fridgesCache("fridges"){
              (crudService ? CrudServiceActor.AllFridge).mapTo[List[FridgeRest]]
            }
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
    }
      
  def postRoute = 
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
        }
     
    def miscRoute =
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
      path("token") {
        get {
          complete{
            (tokenService ? TokenServiceActor.RequestToken).mapTo[String]
          }
        }
      } ~ 
      pathPrefix("count") {
        path("fridges") {
            get {
              complete {
                (crudService ? CrudServiceActor.CountFridges).mapTo[String]
              }
            }
        } ~ 
        path("posts") {
            get {
              complete {
                (crudService ? CrudServiceActor.CountPosts).mapTo[String]
              }
            }
        }
      } 
    
    def chatRoute = 
      path("messages" / Rest) { fridgeName =>
        post {
          parameters("token") { token =>
            entity(as[ChatMessage]) { message =>
              complete {
                (chatService ? ChatServiceActor.PushChat(fridgeName, message, token)).mapTo[ChatMessage]
              }
            }
          }
        } ~
          get {
            complete {
              (chatService ? ChatServiceActor.ChatHistory(fridgeName)).mapTo[Future[List[ChatMessage]]]
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

  def streamFirehose(fridgeTarget : Option[String] = None, userToken : Option[String] = None)(ctx: RequestContext): Unit = {
    val fireHoseActor = context.actorOf(Props(new FirehoseStreamActor(fridgeTarget, userToken, ctx)))
    context.system.eventStream.subscribe(fireHoseActor, classOf[Notification])
  }

  def streamStat (ctx: RequestContext): Unit = {
    val statActor = context.actorOf(Props(new StatStreamActor(ctx)))
    context.system.scheduler.schedule(1.seconds,1.seconds){
      val stats = context.actorSelection("/user/IO-HTTP/listener-0").ask(Http.GetStats).mapTo[Stats]
      stats pipeTo statActor
    }
  }  
}