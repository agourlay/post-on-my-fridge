package pomf.service

import akka.actor._

import java.security.SecureRandom
import java.math.BigInteger

import pomf.service.TokenServiceProtocol._


class TokenService extends Actor {
  
  val random = new SecureRandom()
  
  def receive = {
    case RequestToken => sender ! NewToken(new BigInteger(130, random).toString(32))
  }
}

object TokenServiceProtocol{
  case object RequestToken
  case class NewToken(token : String)
}

object TokenService {
   def props() = Props(classOf[TokenService]).withDispatcher("service-dispatcher")
}