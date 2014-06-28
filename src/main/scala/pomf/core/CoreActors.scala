package pomf.core

import akka.actor._
import akka.routing.RoundRobinPool

import org.slf4j.LoggerFactory

import pomf.service.CrudService
import pomf.service.CrudServiceProtocol
import pomf.service.NotificationService
import pomf.service.ChatRepository
import pomf.service.TokenService
import pomf.domain.dao.PostGresDB
import pomf.configuration.Settings
import pomf.metrics.MetricsReporter

trait CoreActors {
  this: Core =>

  val logger = LoggerFactory.getLogger("core.coreActors")

  val dbUser = Settings(system).Database.DbUser
  val dbPassword = Settings(system).Database.DbPassword
  val dbSchema = Settings(system).Database.DbSchema
  val dbHost = Settings(system).Database.DbHost
  val dbPort = Settings(system).Database.DbPort

  val dbConfig = new PostGresDB(dbUser, dbPassword, dbSchema, dbHost, dbPort)

  val parallelism = Settings(system).Parallelism
  val coreNum = Runtime.getRuntime().availableProcessors()
  val routerSize = coreNum * parallelism

  logger.info(s"Service router size -> coreNumber ($coreNum) * parallelism ($parallelism) = $routerSize")

  val notificationService = system.actorOf(RoundRobinPool(routerSize).props(NotificationService.props)
                                           , "notification-service")
  
  val crudService = system.actorOf(RoundRobinPool(routerSize).props(CrudService.props(dbConfig.dao, notificationService))
                                   , "crud-service")

  val tokenService = system.actorOf(RoundRobinPool(routerSize).props(TokenService.props), "token-service")

  val chatRepo = system.actorOf(ChatRepository.props(notificationService), "chat-repository")

  val metricsReporter = system.actorOf(MetricsReporter.props, "metrics-reporter")
}