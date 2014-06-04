package pomf.core

import akka.actor._

import scala.concurrent.duration._
import scala.language.postfixOps

import pomf.service.CrudService
import pomf.service.CrudServiceProtocol
import pomf.service.NotificationService
import pomf.service.ChatService
import pomf.service.TokenService
import pomf.domain.dao._
import pomf.configuration._
import pomf.metrics.MetricsReporter

trait CoreActors {
  this: Core =>

  implicit def executionContext = system.dispatcher

  val dbUser = Settings(system).Database.DbUser
  val dbPassword = Settings(system).Database.DbPassword
  val dbSchema = Settings(system).Database.DbSchema

  val dbConfig = new PostGresDB(dbUser,dbPassword,dbSchema)

  val notificationService = system.actorOf(Props[NotificationService], "notification-service")
  
  val crudService = system.actorOf(Props(classOf[CrudService], dbConfig.dao, notificationService), "crud-service")

  val chatService = system.actorOf(Props(classOf[ChatService],notificationService), "chat-service")
  
  val tokenService = system.actorOf(Props[TokenService], "token-service")

  val metricsReporter = system.actorOf(MetricsReporter.props, "metrics-reporter")
}