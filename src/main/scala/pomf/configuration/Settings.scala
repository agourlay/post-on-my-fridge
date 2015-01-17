package pomf.configuration

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration
import com.typesafe.config.Config
import akka.actor._

class Settings(config: Config, extendedSystem: ExtendedActorSystem) extends Extension {

  object Http {
    val Port = config.getInt("pomf.port")
  }

  val Timeout = FiniteDuration(config.getDuration("pomf.timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)

  object Database {
    val DbUser = config.getString("pomf.database.user")
    val DbPassword = config.getString("pomf.database.password")
    val DbSchema = config.getString("pomf.database.schema")
    val DbHost = config.getString("pomf.database.host")
    val DbPort = config.getInt("pomf.database.port")
    val Purge = config.getBoolean("pomf.database.purge")
  }

  object Graphite {
    val Enable = config.getBoolean("pomf.graphite.enable")
    val Host = config.getString("pomf.graphite.host")
    val Port = config.getInt("pomf.graphite.port")
    val Prefix = config.getString("pomf.graphite.prefix")
  }
}

object Settings extends ExtensionId[Settings] with ExtensionIdProvider {
  override def lookup = Settings
  override def createExtension(system: ExtendedActorSystem) = new Settings(system.settings.config, system)
}