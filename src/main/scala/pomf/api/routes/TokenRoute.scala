package pomf.api.route

import akka.actor._

import spray.routing._

import pomf.api.request.GenerateToken
import pomf.service.TokenServiceProtocol

class TokenRoute(tokenService : ActorRef)(implicit context: ActorContext) extends Directives {	

  val route = 
    path("token") {
      get { ctx =>
        context.actorOf(GenerateToken.props(ctx, tokenService))
      }
    }        
}