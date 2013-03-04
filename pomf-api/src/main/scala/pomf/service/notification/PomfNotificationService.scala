package pomf.service.notification

import akka.actor.Actor
import akka.actor.ActorRef
import com.github.sstone.amqp.RabbitMQConnection
import com.github.sstone.amqp.Amqp
import com.rabbitmq.client.ConnectionFactory
import akka.actor.Props
import com.github.sstone.amqp.Amqp._
import com.github.sstone.amqp.ConnectionOwner
import com.github.sstone.amqp.ChannelOwner

class PomfNotificationActor extends Actor with PomfNotificationService {

  implicit val actorSystem = context.system

  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")
  val conn = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")
  val queueParams = QueueParameters("fridge.demo", passive = false, durable = false, exclusive = false, autodelete = true)
  
  val producer = ConnectionOwner.createActor(conn, Props(new ChannelOwner()))
      producer ! DeclareQueue(queueParams)
      producer ! QueueBind("fridge_queue", "amq.direct", "fridge_key")
  
  Amqp.waitForConnection(actorSystem, producer).await()

  def receive = {
    case "test" =>
      println("received test")
      producer ! Publish("amq.direct", "fridge_key", "test publish!".getBytes)
    case _ => println("received unknown message")
  }

}

trait PomfNotificationService {

}