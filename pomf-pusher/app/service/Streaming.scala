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
  
  def getStream(fridgeName: String): Enumerator[Notification] = {
    println("Get Stream for "+fridgeName)
    queues.getOrElse(fridgeName, setUpQueue(fridgeName)).broadcast._1  
  }

  def setUpQueue(fridgeName: String): FridgeQueue = {
    println("Setup for "+fridgeName)
    createPhysicalQueueIfAbsent(fridgeName)

    val (fridgeEnumerator, fridgeChannel) = Concurrent.broadcast[Notification]

    // create an actor that will receive AMQP deliveries
    val listener = actorSystem.actorOf(Props(new Actor {
      println("Create Listener for "+fridgeName)
      def receive = {
        case Delivery(consumerTag, envelope, properties, body) => {
          sender ! Ack(envelope.getDeliveryTag)
          val json: JsValue = Json.parse(new String(body))
          println("json: " + json)
          val fridgeName = (json \ "fridgeName").asOpt[String]
          val command = (json \ "command").asOpt[String]
          val user = (json \ "user").asOpt[String]
          val message = (json \ "message").asOpt[String]
          val timestamp = (json \ "timestamp").asOpt[Long]
          fridgeChannel.push(Notification(fridgeName.get, command.get, user.get, message.get, timestamp.get))
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
    println("Building queue if absent")
    producer ! DeclareQueue(QueueParameters("fridge." + fridgeName, passive = false, durable = false, exclusive = false, autodelete = false))
    Amqp.waitForConnection(actorSystem, producer).await()
  }

}

case class FridgeQueue(val listener: ActorRef, val broadcast: (Enumerator[Notification], Channel[Notification]))
