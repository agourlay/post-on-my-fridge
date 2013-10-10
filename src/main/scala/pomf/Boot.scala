package pomf

import akka.actor._
import akka.routing._
import akka.io.IO

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import pomf.service.CrudService
import pomf.service.CrudServiceProtocol
import pomf.service.NotificationService
import pomf.service.ChatService
import pomf.service.TokenService
import pomf.api.PomfHttpService
import pomf.domain.config._

import scala.concurrent.duration._
import scala.language.postfixOps

import spray.can.Http

object Boot extends App with Configuration{
 
  val log: Logger = LoggerFactory.getLogger("boot")
  log.info("                       ")
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
  log.info("                       ")

  implicit val system = ActorSystem(systemName)
  implicit def executionContext = system.dispatcher

  val dbConfig = new PostGresDB(dbUser,dbPassword,dbSchema)

  val notificationService = system.actorOf(Props[NotificationService], "notification-service")
  
  val crudService = system.actorOf(Props(classOf[CrudService], dbConfig.dao, notificationService, urlSite)
                          .withRouter(SmallestMailboxRouter(Runtime.getRuntime.availableProcessors)), "crud-service")

  val chatService = system.actorOf(Props(classOf[ChatService],notificationService), "chat-service")
  
  val tokenService = system.actorOf(Props[TokenService], "token-service")
      
  val httpService = system.actorOf(Props(classOf[PomfHttpService], crudService, chatService, tokenService), "http-service")
  
  IO(Http) ! Http.Bind(httpService, "localhost", port = port) 
  
  // schedule delete outdated post every 24 hours
  system.scheduler.schedule(24 hour, 24 hour, crudService, CrudServiceProtocol.DeleteOutdatedPost)
}