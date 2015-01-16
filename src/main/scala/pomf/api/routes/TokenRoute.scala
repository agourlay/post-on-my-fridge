package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }
import akka.http.marshallers.sprayjson.SprayJsonSupport
import akka.http.unmarshalling.Unmarshal
import akka.http.server._
import Directives._

import scala.concurrent.duration._
import scala.language.postfixOps

import pomf.api.request.GenerateToken

object TokenRoute {

  def build(tokenService: ActorRef)(implicit context: ActorContext) =
    path("token") {
      get { ctx ⇒
        context.actorOf(GenerateToken.props(ctx, tokenService))
      }
    }
}