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
import scala.util.Marshal
import scala.compat.Platform
import akka.actor.ActorContext
import akka.actor.ActorSystem

class PomfNotificationActor extends Actor{

  implicit val actorSystem = context.system

  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")
      
  val conn = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")
  
  val producer = ConnectionOwner.createActor(conn, Props(new ChannelOwner()))
  
  Amqp.waitForConnection(actorSystem, producer).await()
   
  def receive = {
    case PomfNotification(fridgeName,command,user, message,timestamp) =>  {
      println("received message "+command+" for fridge "+fridgeName)
      val queueParams = QueueParameters("fridge."+fridgeName, passive = false, durable = false, exclusive = false, autodelete = true)
          //idem potent
          producer ! DeclareQueue(queueParams)
          producer ! QueueBind("fridge."+fridgeName, "amq.direct", "fridge."+fridgeName)
          producer ! Publish("amq.direct", "fridge."+fridgeName,Marshal.dump(PomfNotification(fridgeName,command,user, message,timestamp)) )
    }
    case _ => println("received unknown message")
  }

}

case class PomfNotification(fridgeName : String, command :String, user : String, message : String, timestamp : Long ) extends Serializable {

}

// For later use with reverse rest on client ;)
object NotificationCmd extends Enumeration {
	type NotificationCmd = Value
	val Create = Value("Create")
	val Delete = Value("Delete") 
	val Update = Value("Update")
	val Message = Value("message") 
}
