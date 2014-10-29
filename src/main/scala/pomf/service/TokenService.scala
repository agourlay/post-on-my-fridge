package pomf.service

import akka.actor.{ Actor, Props }
import java.util.UUID

import pomf.service.TokenServiceProtocol._
import pomf.core.actors.CommonActor

class TokenService extends CommonActor {
  def receive = {
    case RequestToken ⇒ sender ! NewToken(UUID.randomUUID())
  }
}

object TokenServiceProtocol {
  case object RequestToken
  case class NewToken(token: UUID)
}

object TokenService {
  def props() = Props(classOf[TokenService])
}