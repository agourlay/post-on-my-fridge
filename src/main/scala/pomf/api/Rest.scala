package pomf.api

import akka.io.IO
import akka.actor._
import spray.can.Http

import pomf.configuration._
import pomf.api.endpoint.HttpEndpointActor
import pomf.core.{CoreActors, Core}

trait Rest {
  this: CoreActors with Core =>

  val rootService = system.actorOf(HttpEndpointActor.props(this), "http-service")

}

trait Web {
  this: Rest with CoreActors with Core =>

  val httpPort = Settings(system).Http.Port

  IO(Http)(system) ! Http.Bind(rootService, "localhost", port = httpPort)
}