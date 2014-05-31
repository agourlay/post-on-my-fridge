package pomf.domain.model

import com.github.tototoshi.slick.PostgresJodaSupport._
import org.joda.time.DateTime
import pomf.util.XssFilter
import scala.slick.driver.PostgresDriver.simple._

case class Post(id: Option[Long] = None,
    author: String,
    content: String,
    color:String,
	  date:DateTime,
	  positionX:Double,
	  positionY:Double,
	  dueDate:Option[DateTime] = None,
	  fridgeId:Long
){
  require(!author.isEmpty, "author must not be empty")
  require(!XssFilter.containsScript(author), "author must not contain script tags")
  require(!XssFilter.containsScript(content), "content must not contain script tags")
}

class Posts (tag: Tag) extends Table[Post](tag, "POSTS"){
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def author = column[String]("AUTHOR", O.NotNull)
  def content = column[String]("CONTENT", O.NotNull)
  def color = column[String]("COLOR", O.NotNull)
  def date = column[DateTime]("DATE", O.NotNull)
  def positionX = column[Double]("POSITION_X", O.NotNull)
  def positionY = column[Double]("POSITION_Y", O.NotNull)
  def dueDate = column[Option[DateTime]]("DUE_DATE")
  def fridgeId = column[Long]("FRIDGE_ID", O.NotNull)
  def fridge = foreignKey("FRIDGE_FK",  fridgeId, fridges)(_.id) 
  def * = (id.?, author, content, color, date, positionX, positionY, dueDate, fridgeId) <> (Post.tupled, Post.unapply)

  val fridges = TableQuery[Fridges]
}
