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

object PomfNotificationService {
  lazy val notifier = new PomfNotificationActor
  def getStream(fridgeName: String) = notifier.getStream(fridgeName)
}

class PomfNotificationActor{
   
  print("Initialisation PomfNotificationService")
  
  implicit val actorSystem = GlobalPusher.system

  val connFactory = new ConnectionFactory()
  connFactory.setHost("localhost")

  val connDeclare = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")

  val producer = ConnectionOwner.createActor(connDeclare, Props(new ChannelOwner()))

  val conn = new RabbitMQConnection(host = "localhost", name = "Connection-from-pusher")

  val queues: Map[String, FridgeQueue] = TrieMap[String, FridgeQueue]()
  
  def getStream(fridgeName: String): Enumerator[JsValue] = {
    println("Get Stream for "+fridgeName)
    queues.getOrElse(fridgeName, setUpQueue(fridgeName)).broadcast._1  
  }

  def setUpQueue(fridgeName: String): FridgeQueue = {
    createPhysicalQueueIfAbsent(fridgeName)

    val (fridgeEnumerator, fridgeChannel) = Concurrent.broadcast[JsValue]

    // create an actor that will receive AMQP deliveries
    val listener = actorSystem.actorOf(Props(new Actor {
      def receive = {
        case Delivery(consumerTag, envelope, properties, body) => {
          sender ! Ack(envelope.getDeliveryTag)
          val json: JsValue = Json.parse(new String(body))
          //println("Pushing "+json.toString)
          fridgeChannel.push(json)
        }
      }
    }))
    val queueParams = QueueParameters("fridge." + fridgeName, passive = true, durable = false, exclusive = false, autodelete = false)
    val consumer = conn.createConsumer(Amqp.StandardExchanges.amqDirect, queueParams, "fridge." + fridgeName, listener, None)
    val fridgeQueue = FridgeQueue(listener, (fridgeEnumerator, fridgeChannel))
    queues.putIfAbsent(fridgeName, fridgeQueue)
    fridgeQueue
  }

  def createPhysicalQueueIfAbsent(fridgeName: String) = {
    producer ! DeclareQueue(QueueParameters("fridge." + fridgeName, passive = false, durable = false, exclusive = false, autodelete = false))
    Amqp.waitForConnection(actorSystem, producer).await()
  }

}

case class FridgeQueue(val listener: ActorRef, val broadcast: (Enumerator[JsValue], Channel[JsValue]))
