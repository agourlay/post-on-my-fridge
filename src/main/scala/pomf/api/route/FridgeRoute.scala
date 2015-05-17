package pomf.api.route

import akka.actor.ActorContext
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.marshalling.Marshaller._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server._
import Directives._
import akka.stream.ActorFlowMaterializer

import java.util.UUID

import pomf.domain.CrudService
import pomf.domain.model.{ FridgeLight, Fridge, FridgeFull }
import pomf.api.endpoint.JsonSupport
import pomf.core.configuration._

object FridgeRoute extends JsonSupport {

  def build(crudService: CrudService)(implicit context: ActorContext, fm: ActorFlowMaterializer) = {

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
            onSuccess(crudService.getAllFridge(pageNumber, pageSize)) { fridges: Seq[FridgeLight] ⇒
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