package pomf.service

import akka.actor._
import java.util.UUID

import pomf.service.TokenServiceProtocol._


class TokenService extends Actor {
  
  def receive = {
    case RequestToken => sender ! NewToken(UUID.randomUUID())
  }
}

object TokenServiceProtocol{
  case object RequestToken
  case class NewToken(token : UUID)
}

object TokenService {
   def props() = Props(classOf[TokenService]).withDispatcher("service-dispatcher")
}