package pomf

import com.typesafe.config.ConfigFactory

trait Configuration {
  val systemName = "pomf-api"
  val pomfConfig = ConfigFactory.load().getConfig(systemName)
  val dbUser     = pomfConfig.getString("database.user")
  val dbPassword = pomfConfig.getString("database.password")
  val dbSchema   = pomfConfig.getString("database.schema")
  val urlSite    = pomfConfig.getString("url")
  val port       = pomfConfig.getInt("port")
}