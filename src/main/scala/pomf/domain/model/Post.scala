package pomf.domain.model

import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.DateTime
import pomf.domain.config.Profile
import spray.json.DefaultJsonProtocol
import pomf.util.XssFilter

case class Post(author: String,
    content: String,
    color:String,
	  date:DateTime,
	  positionX:Double,
	  positionY:Double,
	  dueDate:Option[DateTime] = None,
	  fridgeId:String,
    id: Option[Long] = None
){
  require(!author.isEmpty, "author must not be empty")
  require(!XssFilter.containsScript(author), "author must not contain script tags")
  require(!XssFilter.containsScript(content), "content must not contain script tags")
  require(!fridgeId.isEmpty, "fridgeId must not be empty")
  require(!XssFilter.containsScript(fridgeId), "fridgeId must not contain script tags")
}

trait PostComponent { this: Profile =>
  import profile.simple._  

  object Posts extends Table[Post]("POST") {    
    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def author = column[String]("AUTHOR", O.NotNull)
    def content = column[String]("CONTENT", O.NotNull)
    def color = column[String]("COLOR", O.NotNull)
    def date = column[DateTime]("DATE", O.NotNull)
    def positionX = column[Double]("POSITION_X", O.NotNull)
    def positionY = column[Double]("POSITION_Y", O.NotNull)
    def dueDate = column[Option[DateTime]]("DUE_DATE")
    def fridgeId = column[String]("FRIDGE_ID", O.NotNull)
   
    def * = author ~ content ~ color ~ date ~ positionX ~ positionY ~ dueDate ~ fridgeId ~ id <> (Post.apply _, Post.unapply _)
   
    // Query Definition
    val autoInc = author ~ content ~ color ~ date ~ positionX ~ positionY ~ dueDate ~ fridgeId returning id into { case (c, i) => Post(c._1, c._2,c._3, c._4,c._5, c._6,c._7,c._8, i) }
    def findAll = for (x <- ((Posts))) yield x
    def forInsert = author ~ content ~ color ~ date ~ positionX ~ positionY ~ dueDate ~ fridgeId <>
      ({ (a,ct,cl,d,ps,py,dd,fi) => Post(a,ct,cl,d,ps,py,dd,fi, None) }, { x: Post => Some((x.author, x.content, x.color, x.date, x.positionX, x.positionY, x.dueDate, x.fridgeId)) })
      
    // Query Execution
    def findAllPost(implicit session: Session): List[Post] = Query(Posts).sortBy(_.id).list()
    
    def insert(x: Post)(implicit session: Session): Post = autoInc.insert(x.author, x.content, x.color, x.date, x.positionX, x.positionY, x.dueDate, x.fridgeId)
    
    def findPostByFridge(fridgeName : String )(implicit session: Session) = Query(Posts).filter(_.fridgeId === fridgeName).list 
    
    def updatePost(postToUpdate : Post)(implicit session: Session): Option[Post] = {
      val q =  for { p <- Posts if p.id === postToUpdate.id } yield p.author ~ p.content ~ p.color ~ p.date ~ p.positionX ~ p.positionY ~ p.dueDate ~ p.fridgeId ~ p.id
      Post.unapply(postToUpdate) match{
       case Some(post) =>  q.update(post) ; Some(postToUpdate)
       case None => None
      }
    }
    
    def getPost(idPost : Long)(implicit session: Session):Option[Post] =  Query(Posts).filter(_.id === idPost).firstOption 
    
    def deletePost(idPost : Long)(implicit session: Session):Long =  {
      (for(p <- Posts if p.id === idPost) yield p).delete
      idPost 
    }
    
    def deleteOutdatedPost(implicit session: Session) = {
        val now = new DateTime()
       (for(p <- Posts if p.dueDate < now) yield p).delete 
    }

    def count(implicit session: Session) = Query(Posts).list.length

  }
}
