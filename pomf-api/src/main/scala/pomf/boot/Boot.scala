package pomf.boot

import akka.actor.{Props, ActorSystem}
import spray.can.server.HttpServer
import spray.io._
import akka.actor.actorRef2Scala
import com.rabbitmq.client.ConnectionFactory
import com.github.sstone.amqp.Amqp.ExchangeParameters
import com.github.sstone.amqp.ConnectionOwner
import pomf.service.rest.PomfServiceActor


object Boot extends App {
 
  val system = ActorSystem("pomf")
  
  private val ioBridge = IOExtension(system).ioBridge()
  // create and start our service actor
  val service = system.actorOf(Props[PomfServiceActor], "pomf-service")

  // create and start the spray-can HttpServer, telling it that
  // we want requests to be handled by our singleton service actor
  val httpServer = system.actorOf(
    Props(new HttpServer(ioBridge, SingletonHandler(service))),
    name = "http-server"
  )

  // a running HttpServer can be bound, unbound and rebound
  // initially to need to tell it where to bind to
  httpServer ! HttpServer.Bind("localhost", 8080)
  
  // prepare the AMQP connection factory
  val connectionFactory = new ConnectionFactory()
  connectionFactory.setHost("localhost")
  
  // connect to the AMQP exchange
  val amqpExchange = ExchangeParameters(name = "amq.direct", exchangeType = "", passive = true)

  // create a "connection owner" actor, which will try and 
  // reconnect automatically if the connection ins lost
  val connection = system.actorOf(
                     Props(new ConnectionOwner(connectionFactory)))
}
