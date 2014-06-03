package pomf.domain.model

import pomf.util.XssFilter
import com.github.tototoshi.slick.PostgresJodaSupport._
import scala.slick.driver.PostgresDriver.simple._
import org.joda.time.DateTime

case class Fridge(id: Option[Long] = None, name: String, creationDate: DateTime, modificationDate: DateTime){
  require(!name.isEmpty, "fridge name must not be empty")
  require(!XssFilter.containsScript(name), "name must not contain script tags")
}

case class FridgeRest(name: String, creationDate: DateTime, modificationDate: DateTime, id : Long, posts: List[Post])

class Fridges(tag: Tag) extends Table[Fridge](tag, "FRIDGES") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.NotNull)
  def creationDate = column[DateTime]("CREATION_DATE", O.NotNull)
  def modificationDate = column[DateTime]("MODIFICATION_DATE", O.NotNull)
  def * = (id.?, name, creationDate, modificationDate) <> (Fridge.tupled, Fridge.unapply)
  def idx = index("IDX_NAME", name, unique = true)
}