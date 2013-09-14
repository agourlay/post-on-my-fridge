package pomf

import akka.actor._
import akka.routing._
import akka.io.IO
import akka.actor.actorRef2Scala

import com.typesafe.config.ConfigFactory

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import pomf.service.NotificationActor
import pomf.service.CrudServiceActor
import pomf.service.CrudServiceActor._
import pomf.service.ChatServiceActor
import pomf.service.TokenServiceActor
import pomf.api.PomfHttpActor
import pomf.domain.config._

import scala.concurrent.duration._
import scala.language.postfixOps

import spray.can.Http

object Boot extends App {
 
  val log: Logger = LoggerFactory.getLogger("boot")
  log.info(" ...")
  log.info(" +--------------------+")
  log.info(" |  Fridge starting   |")
  log.info(" |--------------------|")
  log.info(" |                    |")
  log.info(" |                    |")
  log.info("xx                    |")
  log.info("x|                    |")
  log.info("xx                    |")
  log.info(" |                    |")
  log.info(" +--------------------+")
  log.info(" |                    |")
  log.info("xx                    |")
  log.info("x|                    |")
  log.info("xx                    |")
  log.info(" |                    |")
  log.info(" |                    |")
  log.info(" +--------------------+")

  implicit val system = ActorSystem("pomf-api")
  implicit def executionContext = system.dispatcher

  val pomfConfig = ConfigFactory.load().getConfig("pomf-api")
  val dbUser     = pomfConfig.getString("database.user")
  val dbPassword = pomfConfig.getString("database.password")
  val dbSchema   = pomfConfig.getString("database.schema")
  val urlSite    = pomfConfig.getString("url")
  val port       = pomfConfig.getInt("port")

  val dbConfig = new PostGresDB(dbUser,dbPassword,dbSchema)

  val notificationService = system.actorOf(Props[NotificationActor], "notification-service")
  
  val crudService = system.actorOf(Props(new CrudServiceActor(dbConfig.dao, notificationService, urlSite))
                          .withRouter(SmallestMailboxRouter(Runtime.getRuntime.availableProcessors)), "crud-service")

  val chatService = system.actorOf(Props(new ChatServiceActor(notificationService)), "chat-service")
  
  val tokenService = system.actorOf(Props[TokenServiceActor], "token-service")
      
  val httpService = system.actorOf(Props(new PomfHttpActor(crudService, chatService, tokenService)), "http-service")
  
  IO(Http) ! Http.Bind(httpService, "localhost", port = port) 
  
  // schedule delete outdated post every 24 hours
  system.scheduler.schedule(24 hour, 24 hour, crudService, DeleteOutdatedPost)
}