package pomf.service

import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging

import java.security.SecureRandom
import java.math.BigInteger

import pomf.service.TokenServiceActor._


class TokenServiceActor extends Actor with ActorLogging {
  
  val random :SecureRandom = new SecureRandom()
  
  def receive = {
    case RequestToken => sender ! new BigInteger(130, random).toString(32)
  }
}

object TokenServiceActor{
  case object RequestToken
}