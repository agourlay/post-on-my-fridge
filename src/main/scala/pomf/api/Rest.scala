package pomf.api

import akka.io.IO
import spray.can.Http

import pomf.configuration._
import pomf.api.endpoint.ApiEndpoint
import pomf.core.{ CoreActors, Core }

trait Rest {
  this: CoreActors with Core ⇒

  val rootService = system.actorOf(ApiEndpoint.props(this), "http-service")
}

trait Web {
  this: Rest with Core ⇒

  val httpPort = Settings(system).Http.Port

  IO(Http)(system) ! Http.Bind(rootService, "localhost", port = httpPort)
}