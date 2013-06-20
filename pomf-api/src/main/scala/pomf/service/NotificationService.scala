package pomf.service

import akka.actor.Actor
import com.github.sstone.amqp.Amqp
import com.rabbitmq.client.ConnectionFactory
import akka.actor.Props
import com.github.sstone.amqp.Amqp._
import com.github.sstone.amqp.ConnectionOwner
import com.github.sstone.amqp.ChannelOwner
import spray.json.DefaultJsonProtocol._
import scala.io.Codec
import spray.json.JsValue
import pomf.domain.model.Notification
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging

class NotificationActor extends Actor with ActorLogging {
  import pomf.api.JsonSupport._
  
  implicit val actorSystem = context.system
  
  val connFactory = new ConnectionFactory()
      connFactory.setHost("localhost")
      
  val conn = actorSystem.actorOf(Props(new ConnectionOwner(connFactory)))
  
  val producer = ConnectionOwner.createActor(conn, Props(new ChannelOwner()))
  
  
  override def preStart() = {
	  Amqp.waitForConnection(actorSystem, producer).await()
  }  
  
  def receive = {
    case Notification(fridgeName,command,payload,timestamp,token) =>  {    
      val jsonNotif : JsValue = formatNotif.write(Notification(fridgeName,command,payload,timestamp,token))
      log.debug("Sending notification {}", jsonNotif)
      val queueParams = QueueParameters("fridge."+fridgeName, passive = false, durable = false, exclusive = false, autodelete = false)
      producer ! DeclareQueue(queueParams)  //idem potent
      producer ! QueueBind("fridge."+fridgeName, "amq.direct", "fridge."+fridgeName)
      producer ! Publish("amq.direct", "fridge." + fridgeName, jsonNotif.toString.getBytes(Codec.UTF8.charSet))
    }
    case _ => 
  }
}