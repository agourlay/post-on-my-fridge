package pomf.domain

trait DBConfig {
  def m: Dao
}

import scala.slick.driver.H2Driver
import scala.slick.driver.PostgresDriver
import scala.slick.session.{ Database, Session }

trait TestDB extends DBConfig {
  val m = new Dao("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:servicetestdb", driver = "org.h2.Driver"))
  m.createDB
}

trait ProductionDB extends DBConfig {
  val m = new Dao("PostgreSQL", new DAL(PostgresDriver),
    Database.forURL("jdbc:postgresql:test",
                           driver="org.postgresql.Driver",
                           user="postgres",
                           password="root"))
  m.createDB
}
