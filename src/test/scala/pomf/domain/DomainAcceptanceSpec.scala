package pomf.domain

import org.specs2._
import pomf.domain.dao.Dao
import pomf.domain.config.DAL
import pomf.domain.model.Fridge
import org.joda.time.DateTime

class DomainAcceptanceSpec extends Specification {
  def is =
    "Pomf Model should:" ^
      p ^
      "Return an empty list if asked for all clients"
      "Add a fridge" ! addFridge ^
      end

import scala.slick.driver.H2Driver
  import scala.slick.session.{ Database, Session }
  val dao = new Dao("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:testdb", driver = "org.h2.Driver"))
  dao.createDB

  val date = new DateTime()

  def addFridge = dao.addFridge(Fridge("Demo", date, date)) === (Fridge(id = Some(1), name = "Demo", creationDate = date, modificationDate = date))
}
