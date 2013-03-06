package service

import play.api.libs.iteratee.Enumerator
import model.Notification
import settings.GlobalPusher
import play.api.libs.iteratee.Concurrent
import com.rabbitmq.client.ConnectionFactory
import com.github.sstone.amqp.{Amqp, RabbitMQConnection,ConnectionOwner,ChannelOwner}
import com.github.sstone.amqp.Amqp.{QueueParameters, Ack, Delivery, Publish,DeclareQueue}
import akka.actor.Props
import akka.actor.Actor
import play.api.libs.concurrent._
import play.api.libs.json.Json
import play.api.libs.json.JsValue

object NotificationEnumerator{
    
  def messageStream() : Enumerator[Notification] = {
    PomfNotificationActor.init
    PomfNotificationActor.fridgeEnumerator
  }
}

object PomfNotificationActor{
 
  implicit val actorSystem = GlobalPusher.system
  
  val (fridgeEnumerator, fridgeChannel) = Concurrent.broadcast[Notification]

  def createQueueIfAbsent() = {
    println("Building queue if absent")
    val connFactory = new ConnectionFactory()
        connFactory.setHost("localhost")
    val connDeclare = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")
    val producer = ConnectionOwner.createActor(connDeclare, Props(new ChannelOwner()))
    producer ! DeclareQueue(QueueParameters("fridge.demo", passive = false, durable = false, exclusive = false, autodelete = false))
    Amqp.waitForConnection(actorSystem,producer).await()
  }
  
  def init = {
      println("building consumer") 
	  // create an AMQP connection
	  val conn = new RabbitMQConnection(host = "localhost", name = "Connection-from-pusher")
	  // create a consumer that will route incoming AMQP messages to our listener
	  val queueParams = QueueParameters("fridge.demo", passive = true, durable = false, exclusive = false, autodelete = false)
	  createQueueIfAbsent()
	  val consumer = conn.createConsumer(Amqp.StandardExchanges.amqDirect, queueParams, "fridge.demo", listener, None)
	  // wait till everyone is actually connected to the broker
      Amqp.waitForConnection(actorSystem, consumer).await()
  }
  
  // create an actor that will receive AMQP deliveries
  val listener = actorSystem.actorOf(Props(new Actor {
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
        fridgeChannel.push(Notification(fridgeName.get,command.get,user.get,message.get,timestamp.get))
      }
    }
  }))
}
