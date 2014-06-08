package pomf.core

import akka.actor._

import scala.concurrent.duration._
import scala.language.postfixOps

import pomf.service.CrudService
import pomf.service.CrudServiceProtocol
import pomf.service.NotificationService
import pomf.service.ChatRepository
import pomf.service.TokenService
import pomf.domain.dao._
import pomf.configuration._
import pomf.metrics.MetricsReporter

trait CoreActors {
  this: Core =>

  val dbUser = Settings(system).Database.DbUser
  val dbPassword = Settings(system).Database.DbPassword
  val dbSchema = Settings(system).Database.DbSchema

  val dbConfig = new PostGresDB(dbUser,dbPassword,dbSchema)

  val notificationService = system.actorOf(NotificationService.props, "notification-service")
  
  val crudService = system.actorOf(CrudService.props(dbConfig.dao, notificationService), "crud-service")

  val chatRepo = system.actorOf(ChatRepository.props(notificationService), "chat-repository")
  
  val tokenService = system.actorOf(TokenService.props, "token-service")

  val metricsReporter = system.actorOf(MetricsReporter.props, "metrics-reporter")
}