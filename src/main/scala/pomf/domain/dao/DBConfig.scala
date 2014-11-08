package pomf.domain.dao

import org.slf4j.LoggerFactory

import scala.slick.driver.PostgresDriver.simple._
import com.zaxxer.hikari.HikariDataSource

import pomf.core.metrics.Instrumented

trait DBConfig {
  def dao: Dao
}

class PostgresDB(user: String, password: String, schema: String, host: String, port: Int, purge: Boolean) extends DBConfig with Instrumented {
  val log = LoggerFactory.getLogger("domain.dbConfig")

  val url = s"jdbc:postgresql://$host:$port/$schema"
  log.info(s"Connecting to db $url with user $user")

  val databasePool = {
    val ds = new HikariDataSource()
    ds.setMaximumPoolSize(50)
    ds.setDriverClassName("org.postgresql.ds.PGSimpleDataSource")
    ds.setJdbcUrl(url)
    ds.setMetricRegistry(metricRegistry)
    ds.setPoolName("pomf")
    ds.addDataSourceProperty("user", user)
    ds.addDataSourceProperty("password", password)
    Database.forDataSource(ds)
  }

  val dao = new Dao(databasePool)
  if (purge) dao.purgeDB else dao.createDB
}
