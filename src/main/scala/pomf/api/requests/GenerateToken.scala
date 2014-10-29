package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }

import spray.routing._
import spray.json._
import spray.httpx.SprayJsonSupport._

import DefaultJsonProtocol._

import pomf.api.endpoint.CustomJsonProtocol._

import pomf.service.TokenServiceProtocol._
import pomf.service.TokenServiceProtocol

class GenerateToken(ctx: RequestContext, tokenService: ActorRef) extends RestRequest(ctx) {

  tokenService ! TokenServiceProtocol.RequestToken

  override def receive = super.receive orElse waitingToken

  def waitingToken: Receive = {
    case NewToken(token) ⇒ requestOver(token)
  }
}

object GenerateToken {
  def props(ctx: RequestContext, tokenService: ActorRef) = Props(classOf[GenerateToken], ctx, tokenService)
}