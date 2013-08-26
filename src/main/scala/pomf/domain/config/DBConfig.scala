package pomf.domain.config

import scala.slick.driver.H2Driver
import scala.slick.driver.PostgresDriver
import scala.slick.session.Database
import pomf.domain.dao.Dao

trait DBConfig {
  def dao: Dao
}

class TestDB extends DBConfig {
  val dao = new Dao("H2", new DAL(H2Driver), Database.forURL("jdbc:h2:mem:servicetestdb", driver = "org.h2.Driver"))
  dao.createDB
}

class PostGresDB(user: String, password : String) extends DBConfig {
  val dao = new Dao("PostgreSQL", new DAL(PostgresDriver),
    Database.forURL("jdbc:postgresql:pomf",
                           driver="org.postgresql.Driver",
                           user="pomf_api",
                           password="root"))
  dao.createDB
}
