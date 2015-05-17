package pomf.domain.model

import com.github.tototoshi.slick.PostgresJodaSupport._
import org.joda.time.DateTime
import pomf.util.XssFilter
import slick.driver.PostgresDriver.api._
import java.util.UUID

case class Post(id: Option[UUID],
                author: String,
                content: String,
                color: String,
                date: DateTime,
                positionX: Double,
                positionY: Double,
                fridgeId: UUID) {
  require(!author.isEmpty, "author must not be empty")
  require(!XssFilter.containsScript(author), "author must not contain script tags")
  require(!XssFilter.containsScript(content), "content must not contain script tags")
}

class Posts(tag: Tag) extends Table[Post](tag, "POSTS") {
  def id = column[UUID]("ID", O.PrimaryKey, O.SqlType("UUID"))
  def author = column[String]("AUTHOR")
  def content = column[String]("CONTENT")
  def color = column[String]("COLOR")
  def date = column[DateTime]("DATE")
  def positionX = column[Double]("POSITION_X")
  def positionY = column[Double]("POSITION_Y")
  def fridgeId = column[UUID]("FRIDGE_ID")
  def fridge = foreignKey("FRIDGE_FK", fridgeId, fridges)(_.id)
  def fridge_idx = index("IDX_FRIDGE_ID", fridgeId)
  def * = (id.?, author, content, color, date, positionX, positionY, fridgeId) <> (Post.tupled, Post.unapply)

  val fridges = TableQuery[Fridges]
}
