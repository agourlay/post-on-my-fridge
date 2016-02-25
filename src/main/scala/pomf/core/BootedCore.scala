package pomf.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import pomf.api.endpoint.RestAPI
import pomf.core.configuration.Settings
import pomf.core.actors.UnhandledMessageListener
import pomf.core.metrics.MetricsReporter
import pomf.domain.CrudService
import pomf.domain.actors.ChatRepository
import pomf.domain.dao.PostgresDB

trait Core {
  implicit lazy val system = ActorSystem("pomf")
  implicit lazy val materializer = ActorMaterializer()
  sys.addShutdownHook(system.terminate())
}

trait CoreComponents {
  this: Core ⇒

  val dbUser = Settings(system).Database.DbUser
  val dbPassword = Settings(system).Database.DbPassword
  val dbSchema = Settings(system).Database.DbSchema
  val dbHost = Settings(system).Database.DbHost
  val dbPort = Settings(system).Database.DbPort
  val purge = Settings(system).Database.Purge

  val dbConfig = new PostgresDB(dbUser, dbPassword, dbSchema, dbHost, dbPort, purge)

  val crudService = new CrudService(dbConfig.dao, system)

  val metricsReporter = new MetricsReporter(system)

  val chatRepo = system.actorOf(ChatRepository.props(), "chat-repository")

  val unHandledListener = system.actorOf(UnhandledMessageListener.props, "unhandled-message-listener")

  val restAPI = system.actorOf(RestAPI.props(this), "rest-API")
}