package pomf.api.route

import akka.actor.ActorContext
import akka.http.model.StatusCodes._
import akka.http.marshalling.Marshaller._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.server._
import Directives._
import akka.stream.FlowMaterializer

import pomf.api.endpoint.JsonSupport
import pomf.core.metrics.MetricsReporter
import pomf.configuration._
import pomf.domain.CrudService
import spray.json.JsValue

object StatsRoute extends JsonSupport {

  def build(crudService: CrudService, metricsRepo: MetricsReporter)(implicit context: ActorContext, fm: FlowMaterializer) = {
    implicit val timeout = akka.util.Timeout(Settings(context.system).Timeout)
    implicit val ec = context.dispatcher

    path("stats") {
      get {
        onSuccess(metricsRepo.getAllMetrics) { metrics: Map[String, JsValue] ⇒
          complete(ToResponseMarshallable(OK -> metrics))
        }
      }
    } ~
      pathPrefix("count") {
        path("fridges") {
          get {
            onSuccess(crudService.countFridges()) { nb: Int ⇒
              complete(ToResponseMarshallable(OK -> nb.toString))
            }
          }
        } ~
          path("posts") {
            get {
              onSuccess(crudService.countPosts()) { nb: Int ⇒
                complete(ToResponseMarshallable(OK -> nb.toString))
              }
            }
          }
      }
  }
}