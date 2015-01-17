package pomf.api.route

import akka.actor.{ ActorRef, ActorContext }
import akka.pattern._
import akka.http.model.StatusCodes._
import akka.http.marshalling.Marshaller._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.ToResponseMarshallable._
import akka.http.server._
import Directives._
import akka.stream.FlowMaterializer
import pomf.service.CrudService

import spray.json._
import spray.json.DefaultJsonProtocol._

import java.util.UUID

import pomf.domain.model.{ FridgeLight, Fridge, FridgeFull }
import pomf.api.endpoint.JsonSupport._
import pomf.configuration._

object FridgeRoute {

  def build(crudService: CrudService)(implicit context: ActorContext, fm: FlowMaterializer) = {

    implicit val timeout = akka.util.Timeout(Settings(context.system).Timeout)
    implicit val ec = context.dispatcher

    path("fridges" / JavaUUID) { fridgeId: UUID ⇒
      get {
        onSuccess(crudService.getFridgeFull(fridgeId)) { fridge: FridgeFull ⇒
          complete(ToResponseMarshallable(OK -> fridge))
        }
      }
    } ~
      path("fridges") {
        parameters('pageNumber ? 1, 'pageSize ? 50) { (pageNumber: Int, pageSize: Int) ⇒
          get {
            onSuccess(crudService.getAllFridge(pageNumber, pageSize)) { fridges: List[FridgeLight] ⇒
              complete(ToResponseMarshallable(OK -> fridges))
            }
          }
        }
      } ~
      path("fridges") {
        post {
          entity(as[String]) { fridgeName: String ⇒
            onSuccess(crudService.createFridge(fridgeName)) { fridge: Fridge ⇒
              complete(ToResponseMarshallable(OK -> fridge))
            }
          }
        }
      }
  }
}