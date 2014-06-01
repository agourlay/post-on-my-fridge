package pomf.api.request

import akka.actor._

import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.service.TokenServiceProtocol._
import pomf.service.TokenServiceProtocol

class GenerateToken(ctx : RequestContext, tokenService: ActorRef) extends RestRequest(ctx) {

  tokenService !  TokenServiceProtocol.RequestToken

  override def receive = waitingToken orElse handleTimeout

  def waitingToken : Receive = {
    case NewToken(token) => {
      ctx.complete(token)
      requestOver()
    }  
  }
}

object GenerateToken {
   def props(ctx : RequestContext, tokenService: ActorRef) 
     = Props(classOf[GenerateToken], ctx, tokenService)
}