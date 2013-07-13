package pomf.service

import akka.actor.Actor
import akka.actor.Props
import spray.json.DefaultJsonProtocol._
import scala.io.Codec
import spray.json.JsValue
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging
import pomf.domain.model.Notification


class NotificationActor extends Actor with ActorLogging {
    
  def receive = {
    case n : Notification => context.system.eventStream.publish(n)
  }
}