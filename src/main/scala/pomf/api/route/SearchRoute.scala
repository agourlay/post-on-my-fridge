package pomf.api.route

import akka.actor.ActorContext
import akka.http.model.StatusCodes._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.server._
import Directives._
import akka.stream.FlowMaterializer

import pomf.core.configuration._
import pomf.api.endpoint.JsonSupport
import pomf.domain.CrudService
import pomf.domain.model.Fridge

object SearchRoute extends JsonSupport {

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