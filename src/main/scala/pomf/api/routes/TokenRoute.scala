package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.language.postfixOps

import spray.routing._

import pomf.api.request.GenerateToken

class TokenRoute(tokenService : ActorRef)(implicit context: ActorContext) extends Directives{	

  val route = 
    path("token") {
      get { ctx =>
        context.actorOf(GenerateToken.props(ctx, tokenService))
      }
    }        
}