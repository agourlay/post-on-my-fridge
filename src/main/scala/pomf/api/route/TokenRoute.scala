package pomf.api.route

import java.util.UUID

import akka.actor.ActorContext
import akka.http.scaladsl.marshalling.ToResponseMarshallable._
import akka.http.scaladsl.server._
import Directives._

import pomf.core.configuration._

object TokenRoute {

  def build()(implicit context: ActorContext) = {
    implicit val timeout = akka.util.Timeout(Settings(context.system).Timeout)
    implicit val ec = context.dispatcher

    path("token") {
      get {
        complete {
          UUID.randomUUID().toString
        }
      }
    }
  }
}