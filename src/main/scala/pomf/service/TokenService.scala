package pomf.service

import akka.actor.Actor
import akka.actor.Props
import spray.json.DefaultJsonProtocol._
import scala.io.Codec
import spray.json.JsValue
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging
import java.security.SecureRandom
import java.math.BigInteger


class TokenServiceActor extends Actor with ActorLogging {
  
  val random :SecureRandom = new SecureRandom()
  
  def receive = {
    case RequestToken => sender ! new BigInteger(130, random).toString(32)
  }
  
  object TokenServiceActor{
      case class RequestToken
  }
}