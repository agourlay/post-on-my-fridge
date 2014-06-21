package pomf.api.request

import akka.actor._
import akka.pattern._

import spray.routing._
import spray.json._
import spray.httpx.SprayJsonSupport._

import DefaultJsonProtocol._

import pomf.api.endpoint.CustomJsonProtocol._

import pomf.service.TokenServiceProtocol._
import pomf.service.TokenServiceProtocol

class GenerateToken(ctx : RequestContext, tokenService: ActorRef) (implicit breaker: CircuitBreaker) extends RestRequest(ctx) {

  tokenService !  TokenServiceProtocol.RequestToken

  override def receive = waitingToken orElse handleTimeout

  def waitingToken : Receive = {
    case NewToken(token) => requestOver(token)
  }
}

object GenerateToken {
   def props(ctx : RequestContext, tokenService: ActorRef)(implicit breaker: CircuitBreaker) 
     = Props(classOf[GenerateToken], ctx, tokenService, breaker).withDispatcher("requests-dispatcher")
}