package model

import play.api.libs.iteratee.Enumerator
import java.io.File
import java.io.InputStream
import java.io.ByteArrayInputStream
import akka.actor.Actor
import akka.actor.Props
import scala.util.Marshal
import com.github.sstone.amqp.{Amqp, RabbitMQConnection}
import com.github.sstone.amqp.Amqp.{QueueParameters, Ack, Delivery, Publish}
import com.github.sstone.amqp.Amqp.DeclareQueue
import settings.GlobalPusher
import com.github.sstone.amqp.ConnectionOwner
import com.github.sstone.amqp.ChannelOwner
import com.rabbitmq.client.ConnectionFactory


object NotificationEnumerator{
    
  def messageStream() : Enumerator[Notification] = {
    PomfNotificationActor.init
    Enumerator(Seq(Notification("demo","refresh","none","none",1l), Notification("demo","refresh","none","none",1l)):_*)
  }
}

object PomfNotificationActor{
 
  implicit val actorSystem = GlobalPusher.system
  
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
        val message = new String(body)
        println("got a message: " + message)
      }
    }
  }))
}

case class Notification(fridgeName : String, command :String, user : String, message : String, timestamp : Long ) extends Serializable {

}