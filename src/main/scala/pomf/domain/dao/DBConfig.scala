package pomf.domain.dao

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.slick.driver.PostgresDriver.simple._
import org.apache.commons.dbcp.BasicDataSource

trait DBConfig {
  def dao: Dao
}

class PostGresDB(user: String, password : String, schema : String) extends DBConfig {
  val log = LoggerFactory.getLogger("db")
  log.info(s"Using database schema $schema with user $user")

  val url = "jdbc:postgresql://localhost:5432/" + schema
  val driver = "org.postgresql.Driver"

  val databasePool = {
    val ds = new BasicDataSource
    ds.setDriverClassName(driver)
    ds.setUrl(url)
    ds.setMaxIdle(10);
    ds.setInitialSize(10);
    ds.setMaxActive(100)
    ds.setUsername(user);
    ds.setPassword(password );
    Database.forDataSource(ds)
  }

  val dao = new Dao(databasePool)
  dao.createDB
}
