package service

import play.api.libs.iteratee.Enumerator
import model.Notification
import settings.GlobalPusher
import play.api.libs.iteratee.Concurrent
import com.rabbitmq.client.ConnectionFactory
import com.github.sstone.amqp.{ Amqp, RabbitMQConnection, ConnectionOwner, ChannelOwner }
import com.github.sstone.amqp.Amqp.{ QueueParameters, Ack, Delivery, Publish, DeclareQueue }
import akka.actor.Props
import akka.actor.Actor
import play.api.libs.concurrent._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.collection.concurrent.TrieMap
import scala.collection.concurrent.Map
import akka.actor.ActorRef
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsString
import play.api.libs.json.JsObject

object PomfNotificationService {
    
  setUpQueue()
  
  implicit val actorSystem = GlobalPusher.system

  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")

  val connDeclare = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")

  val producer = ConnectionOwner.createActor(connDeclare, Props(new ChannelOwner()))

  val conn = new RabbitMQConnection(host = "localhost", name = "Connection-from-pusher")
  
  val queueName = "pomf-notification"
  
  val concurrentEnum = Concurrent.broadcast[JsObject] 
  
  def getStream(): Enumerator[JsObject] = {
    concurrentEnum.broadcast._1  
  }

  def setUpQueue(): FridgeQueue = {
    createPhysicalQueueIfAbsent(fridgeName)

    val listener = actorSystem.actorOf(Props(new Actor {
      def receive = {
        case Delivery(consumerTag, envelope, properties, body) => {
          sender ! Ack(envelope.getDeliveryTag)
          val json: JsValue = Json.parse(new String(body))
          val data = Json.obj(
    		"fridgeName" -> json.\("fridgeName"),
    		"command"    -> json.\("command"),
    		"payload"    -> json.\("payload"),
    		"timestamp"  -> json.\("timestamp"),
    		"token"      -> json.\("token")
    		)
          concurrentEnum._2.push(data)
        }
      }
    }))
    val queueParams = QueueParameters(queueName, passive = true, durable = false, exclusive = false, autodelete = false)
    val consumer = conn.createConsumer(Amqp.StandardExchanges.amqDirect, queueParams, queueName, listener, None)
  }

  def createPhysicalQueueIfAbsent() = {
    producer ! DeclareQueue(QueueParameters(queueName, passive = false, durable = false, exclusive = false, autodelete = false))
    Amqp.waitForConnection(actorSystem, producer).await()
  }

}