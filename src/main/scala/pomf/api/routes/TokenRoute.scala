package pomf.api.route

import java.util.UUID

import akka.actor.ActorContext
import akka.http.marshalling.ToResponseMarshallable._
import akka.http.server._
import Directives._

import pomf.configuration._

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