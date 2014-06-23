package pomf.domain.dao

import org.slf4j.LoggerFactory

import scala.slick.driver.PostgresDriver.simple._
import org.apache.commons.dbcp.BasicDataSource

trait DBConfig {
  def dao: Dao
}

class PostGresDB(user: String, password : String, schema : String, host : String, port : String) extends DBConfig {
  val log = LoggerFactory.getLogger("domain.dbConfig")

  log.info(s"Connecting to db $host:$port on schema $schema with user $user")
  
  val url = "jdbc:postgresql://"+ host + ":" + port + "/" + schema

  val databasePool = {
    val ds = new BasicDataSource
    ds.setDriverClassName("org.postgresql.Driver")
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
