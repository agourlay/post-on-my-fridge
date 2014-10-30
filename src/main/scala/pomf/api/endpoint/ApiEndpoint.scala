package pomf.api.endpoint

import akka.actor._

import spray.routing._
import spray.httpx.encoding._

import scala.concurrent.Future

import pomf.api.route._
import pomf.api.exceptions.RestFailureHandler
import pomf.core.CoreActors

class ApiEndpoint(coreActors: CoreActors) extends HttpEndpoint with Actor {
  implicit def actorRefFactory = context
  def receive = runRoute(routes(coreActors))
}

trait HttpEndpoint extends HttpService with RestFailureHandler {

  def routes(c: CoreActors)(implicit context: ActorContext) = {

    val chat = new ChatRoute(c.chatRepo).route
    val files = new FilesRoute().route
    val fridge = new FridgeRoute(c.crudService).route
    val post = new PostRoute(c.crudService).route
    val search = new SearchRoute(c.crudService).route
    val stats = new StatsRoute(c.crudService, c.metricsReporter).route
    val streaming = new StreamingRoute().route
    val token = new TokenRoute(c.tokenService).route

    encodeResponse(Gzip) {
      chat ~ files ~ fridge ~ post ~ search ~ stats ~ streaming ~ token
    }
  }
}

object ApiEndpoint {
  def props(coreActors: CoreActors) = Props(classOf[ApiEndpoint], coreActors)
}