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
	  fridgeId:String
){
  require(!author.isEmpty, "author must not be empty")
  require(!XssFilter.containsScript(author), "author must not contain script tags")
  require(!XssFilter.containsScript(content), "content must not contain script tags")
  require(!fridgeId.isEmpty, "fridgeId must not be empty")
  require(!XssFilter.containsScript(fridgeId), "fridgeId must not contain script tags")
}

class Posts (tag: Tag) extends Table[Post](tag, "POSTS"){
  def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
  def author = column[String]("AUTHOR", O.NotNull)
  def content = column[String]("CONTENT", O.NotNull)
  def color = column[String]("COLOR", O.NotNull)
  def date = column[DateTime]("DATE", O.NotNull)
  def positionX = column[Double]("POSITION_X", O.NotNull)
  def positionY = column[Double]("POSITION_Y", O.NotNull)
  def dueDate = column[Option[DateTime]]("DUE_DATE")
  def fridgeId = column[String]("FRIDGE_ID", O.NotNull)
 
  def * = (id, author, content, color, date, positionX, positionY, dueDate, fridgeId) <> (Post.tupled, Post.unapply)
}
