package pomf.api.endpoint

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import pomf.api.route._
import pomf.core.configuration.Settings
import pomf.core.CoreComponents
import pomf.core.actors.CommonActor

class RestAPI(coreComponents: CoreComponents, system: ActorSystem, fm: ActorMaterializer)
    extends CommonActor
    with RestFailureHandler {

  implicit val executionContext = system.dispatcher
  implicit val ifm = fm

  override def receive: Receive = Actor.emptyBehavior

  import coreComponents._

  val chat = ChatRoute.build(chatRepo)
  val files = FilesRoute.build
  val fridge = FridgeRoute.build(crudService)
  val post = PostRoute.build(crudService)
  val search = SearchRoute.build(crudService)
  val stats = StatsRoute.build(crudService, metricsReporter)
  val streaming = StreamingRoute.build
  val token = TokenRoute.build()

  val routes = encodeResponse {
    chat ~ files ~ fridge ~ post ~ search ~ stats ~ streaming ~ token
  }

  Http(system).bindAndHandle(routes, "localhost", port = Settings(system).Http.Port)

}

object RestAPI {
  def props(coreActors: CoreComponents)(implicit system: ActorSystem, fm: ActorMaterializer) =
    Props(classOf[RestAPI], coreActors, system, fm)
}