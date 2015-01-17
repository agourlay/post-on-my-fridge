package pomf.api.route

import akka.actor.ActorContext
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.model.StatusCodes._
import akka.http.marshalling.Marshaller._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.marshalling.ToResponseMarshallable._
import akka.http.server._
import Directives._
import akka.stream.FlowMaterializer

import spray.json._
import spray.json.DefaultJsonProtocol

import pomf.configuration._

import pomf.domain.model.Fridge
import pomf.service.CrudService

object SearchRoute {

  def build(crudService: CrudService)(implicit context: ActorContext, fm: FlowMaterializer) = {
    implicit val timeout = akka.util.Timeout(Settings(context.system).Timeout)
    implicit val ec = context.dispatcher

    pathPrefix("search") {
      path("fridge") {
        get {
          parameters("term") { term: String ⇒
            onSuccess(crudService.searchByNameLike(term)) { fridges: List[Fridge] ⇒
              complete(ToResponseMarshallable(OK -> fridges))
            }
          }
        }
      }
    }
  }
}