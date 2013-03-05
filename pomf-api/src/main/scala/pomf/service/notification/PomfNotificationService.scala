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
import spray.json.JsObject
import spray.json.DefaultJsonProtocol
import pomf.domain.model.Notification
import scala.io.Codec
import spray.json.JsValue

class PomfNotificationActor extends Actor{
  
  implicit val actorSystem = context.system
  
  object NotifImplicit extends DefaultJsonProtocol{
	  implicit val impNotif = jsonFormat5(Notification)
  }

  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")
      
  val conn = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")
  
  val producer = ConnectionOwner.createActor(conn, Props(new ChannelOwner()))
  
  Amqp.waitForConnection(actorSystem, producer).await()
   
  def receive = {
    case Notification(fridgeName,command,user, message,timestamp) =>  {
      println("received message "+command+" for fridge "+fridgeName)
      val jsonNotif : JsValue = NotifImplicit.impNotif.write(Notification(fridgeName,command,user, message,timestamp))
      val queueParams = QueueParameters("fridge."+fridgeName, passive = false, durable = false, exclusive = false, autodelete = false)
          //idem potent
          producer ! DeclareQueue(queueParams)
          producer ! QueueBind("fridge."+fridgeName, "amq.direct", "fridge."+fridgeName)
          producer ! Publish("amq.direct"
        		  		   , "fridge."+fridgeName
        		  		   , jsonNotif.toString.getBytes(Codec.UTF8.charSet))
    }
    case _ => println("received unknown message")
  }
}

