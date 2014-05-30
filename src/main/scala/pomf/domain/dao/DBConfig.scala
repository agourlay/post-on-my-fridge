package pomf.domain.dao

import scala.slick.driver.PostgresDriver.simple._

trait DBConfig {
  def dao: Dao
}

class PostGresDB(user: String, password : String, schema : String) extends DBConfig {
  val db = Database.forURL("jdbc:postgresql:" + schema,
                           driver = "org.postgresql.Driver",
                           user = user,
                           password = password)	
  val dao = new Dao(db)
  dao.createDB
}
