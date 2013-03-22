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
import spray.json.DefaultJsonProtocol._
import spray.json.DefaultJsonProtocol
import scala.io.Codec
import spray.json.JsValue
import pomf.domain.model.Post
import pomf.util.DateMarshalling
import pomf.domain.model.NotificationObj
import spray.json.RootJsonFormat
import pomf.service.rest.JsonImplicits
import pomf.domain.model.Notification

class PomfNotificationActor extends Actor{
  import JsonImplicits._
  
  implicit val actorSystem = context.system
  
  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")
      
  val conn = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)), name = "conn")
  
  val producer = ConnectionOwner.createActor(conn, Props(new ChannelOwner()))
  
  
  override def preStart() = {
	  Amqp.waitForConnection(actorSystem, producer).await()
  }  
  
  def receive = {
    case Notification(fridgeName,command,payload,timestamp,token) =>  {
      val jsonNotif : JsValue = impNotif.write(Notification(fridgeName,command,payload,timestamp,token))
      val queueParams = QueueParameters("fridge."+fridgeName, passive = false, durable = false, exclusive = false, autodelete = false)
      producer ! DeclareQueue(queueParams)  //idem potent
      producer ! QueueBind("fridge."+fridgeName, "amq.direct", "fridge."+fridgeName)
      producer ! Publish("amq.direct", "fridge." + fridgeName, jsonNotif.toString.getBytes(Codec.UTF8.charSet))
    }
    case _ => 
  }
}