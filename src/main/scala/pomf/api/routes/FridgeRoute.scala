package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.routing._
import spray.http._
import spray.httpx.encoding._
import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.httpx.encoding._
import spray.routing.directives.CachingDirectives._
import spray.caching.{LruCache, Cache}

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.{Fridge, FridgeRest}
import pomf.api.request._
import pomf.service.CrudServiceProtocol

class FridgeRoute(crudService : ActorRef)(implicit context: ActorContext) extends Directives {

  implicit val timeout = akka.util.Timeout(5 seconds)
  implicit def executionContext = context.dispatcher

  val fridgesCache: Cache[List[FridgeRest]] = LruCache(maxCapacity = 1, timeToLive = 10 seconds)

  val route = 
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
          ctx => context.actorOf(CreateFridge.props(fridge, ctx, crudService))
        }
      }
    }   
}