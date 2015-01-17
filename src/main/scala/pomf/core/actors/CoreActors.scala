package pomf.core

import org.slf4j.LoggerFactory

import pomf.service.CrudService
import pomf.domain.actors.ChatRepository
import pomf.domain.dao.PostgresDB
import pomf.configuration.Settings
import pomf.core.metrics.MetricsReporter
import pomf.core.actors.UnhandledMessageListener
import pomf.api.RestAPI

trait CoreActors {
  this: Core ⇒

  val logger = LoggerFactory.getLogger("core.coreActors")

  val dbUser = Settings(system).Database.DbUser
  val dbPassword = Settings(system).Database.DbPassword
  val dbSchema = Settings(system).Database.DbSchema
  val dbHost = Settings(system).Database.DbHost
  val dbPort = Settings(system).Database.DbPort
  val purge = Settings(system).Database.Purge

  val dbConfig = new PostgresDB(dbUser, dbPassword, dbSchema, dbHost, dbPort, purge)

  val crudService = new CrudService(dbConfig.dao, system)

  val chatRepo = system.actorOf(ChatRepository.props(), "chat-repository")

  val metricsReporter = system.actorOf(MetricsReporter.props, "metrics-reporter")

  val unHandledListener = system.actorOf(UnhandledMessageListener.props, "unhandled-message-listener")

  val restAPI = system.actorOf(RestAPI.props(this), "rest-API")
}