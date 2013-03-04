package pomf.domain.config

import scala.slick.driver.H2Driver
import scala.slick.driver.PostgresDriver
import scala.slick.session.Database
import pomf.domain.dao.Dao
import com.redis.RedisClient

trait DBConfig {
  def dao: Dao
  def cache : RedisClient
}

trait TestDB extends DBConfig {
  val dao = new Dao("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:servicetestdb", driver = "org.h2.Driver"))
  dao.createDB
  
  // Change later
  val cache = new RedisClient("50.30.35.9", 2787)
  cache.auth("24ae0c3b7f83ee6b550cc16b9fbed4a7")
}

trait ProductionDB extends DBConfig {
  val dao = new Dao("PostgreSQL", new DAL(PostgresDriver),
    Database.forURL("jdbc:postgresql:test",
                           driver="org.postgresql.Driver",
                           user="postgres",
                           password="root"))
  dao.createDB
  
  val cache = new RedisClient("50.30.35.9", 2787)
  cache.auth("24ae0c3b7f83ee6b550cc16b9fbed4a7")
}
