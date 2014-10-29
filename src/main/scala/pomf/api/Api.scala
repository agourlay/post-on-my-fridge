package pomf.api

import akka.io.IO
import spray.can.Http

import pomf.configuration.Settings
import pomf.api.endpoint.ApiEndpoint
import pomf.core.{ CoreActors, Core }

trait Api {
  this: CoreActors with Core ⇒

  val rootService = system.actorOf(ApiEndpoint.props(this), "http-service")

  IO(Http)(system) ! Http.Bind(rootService, "localhost", port = Settings(system).Http.Port)
}