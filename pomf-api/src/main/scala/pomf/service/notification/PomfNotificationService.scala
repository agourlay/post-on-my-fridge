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

class PomfNotificationActor extends Actor{

  implicit val actorSystem = context.system

  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")
      
  val conn = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")
  
  val producer = ConnectionOwner.createActor(conn, Props(new ChannelOwner()))
  
  Amqp.waitForConnection(actorSystem, producer).await()

  def receive = {
    case Notification(fridgeName,command,payload) =>  {
      val queueParams = QueueParameters("fridge."+fridgeName, passive = false, durable = false, exclusive = false, autodelete = true)
          //idem potent
          producer ! DeclareQueue(queueParams)
          producer ! QueueBind("fridge."+fridgeName, "amq.direct", "fridge."+fridgeName)
          producer ! Publish("amq.direct", "fridge."+fridgeName, "test publish!".getBytes)
    }
    case _ => println("received unknown message")
  }

}

case class Notification(fridgeName : String, command :String, payload : Any ) {

}

object NotificationCmd extends Enumeration {
	type NotificationCmd = Value
	val Create = Value("Create")
	val Delete = Value("Delete") 
	val Update = Value("Update")
	val Message = Value("message") 
}
