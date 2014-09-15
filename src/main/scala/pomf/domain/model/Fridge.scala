package pomf.domain.model

import pomf.util.XssFilter
import com.github.tototoshi.slick.PostgresJodaSupport._
import scala.slick.driver.PostgresDriver.simple._
import org.joda.time.DateTime
import java.util.UUID

case class Fridge(id: Option[UUID] = None, name: String, creationDate: DateTime, modificationDate: DateTime) {
  require(!name.isEmpty, "fridge name must not be empty")
  require(!XssFilter.containsScript(name), "name must not contain script tags")
}

case class FridgeLight(name: String, creationDate: DateTime, modificationDate: DateTime, id: UUID, postNumber: Int, posts: List[Post] = List())

case class FridgeFull(name: String, creationDate: DateTime, modificationDate: DateTime, id: UUID, postNumber: Int, posts: List[Post])

class Fridges(tag: Tag) extends Table[Fridge](tag, "FRIDGES") {
  def id = column[UUID]("ID", O.PrimaryKey, O.DBType("UUID"))
  def name = column[String]("NAME", O.NotNull)
  def creationDate = column[DateTime]("CREATION_DATE", O.NotNull)
  def modificationDate = column[DateTime]("MODIFICATION_DATE", O.NotNull)
  def * = (id.?, name, creationDate, modificationDate) <> (Fridge.tupled, Fridge.unapply)
  def idx = index("IDX_NAME", name, unique = true)
}