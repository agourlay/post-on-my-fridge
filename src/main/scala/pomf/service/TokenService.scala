package pomf.service

import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging

import java.security.SecureRandom
import java.math.BigInteger

import pomf.service.TokenServiceProtocol._


class TokenService extends Actor with ActorLogging {
  
  val random = new SecureRandom()
  
  def receive = {
    case RequestToken => sender ! NewToken(new BigInteger(130, random).toString(32))
  }
}

object TokenServiceProtocol{
  case object RequestToken
  case class NewToken(token : String)
}