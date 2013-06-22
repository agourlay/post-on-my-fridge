package service

import play.api.libs.iteratee.Enumerator
import model.Notification
import controllers.StreamController
import play.api.libs.iteratee.Concurrent
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import play.api.libs.concurrent._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.collection.concurrent.TrieMap
import scala.collection.concurrent.Map
import akka.actor.ActorRef
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsString
import play.api.libs.json.JsObject

class PusherNotificationService extends Actor with ActorLogging{
      
  implicit val actorSystem = context.system

  def receive = {
    case jsonString : String => {
      val json: JsValue = Json.parse(jsonString)
      val data = Json.obj(
                	"fridgeName" -> json.\("fridgeName"),
                	"command"    -> json.\("command"),
                	"payload"    -> json.\("payload"),
                	"timestamp"  -> json.\("timestamp"),
                	"token"      -> json.\("token")
                	)
      StreamController.concurrentStream._2.push(data)
    }
    case _ => println("Cannot handle message")
  }
}