package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.httpx.encoding._
import spray.routing._
import spray.routing.directives.CachingDirectives._
import spray.caching.{LruCache, Cache}

import pomf.service.CrudServiceProtocol

class StatsRoute(crudService : ActorRef)(implicit context: ActorContext) extends Directives {

  implicit val timeout = akka.util.Timeout(5 seconds)
  implicit def executionContext = context.dispatcher

  val countCache: Cache[String] = LruCache(maxCapacity = 2, timeToLive = 1 minute)

  val route = 
    path("stats") {
      complete {
        "blah"
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
}