package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }

import scala.concurrent.duration._
import scala.language.postfixOps

import spray.routing._

import pomf.api.request.GenerateToken

class TokenRoute(tokenService: ActorRef)(implicit context: ActorContext) extends Directives {

  val route =
    path("token") {
      get { ctx ⇒
        context.actorOf(GenerateToken.props(ctx, tokenService))
      }
    }
}