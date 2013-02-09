package pomf.domain

import java.util.Date

case class Post(author: String,
    content: String,
    color:String,
	date:Date,
	positionX:Double,
	positionY:Double,
	dueDate:Option[Date] = None,
	fridgeId:String,
    id: Option[Long] = None
)

trait PostComponent { this: Profile =>
  import profile.simple._
  
  implicit val dateMapper = MappedTypeMapper.base[java.util.Date, java.sql.Date] (
  x => new java.sql.Date(x.getTime),
  x => new java.util.Date(x.getTime))

  object Posts extends Table[Post]("POST") {
    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def author = column[String]("AUTHOR", O.NotNull)
    def content = column[String]("CONTENT", O.NotNull)
    def color = column[String]("COLOR", O.NotNull)
    def date = column[Date]("DATE", O.NotNull)
    def positionX = column[Double]("POSITION_X", O.NotNull)
    def positionY = column[Double]("POSITION_Y", O.NotNull)
    def dueDate = column[Option[Date]]("DUE_DATE")
    def fridgeId = column[String]("FRIDGE_ID", O.NotNull)
   
    def * = author ~ content ~ color ~ date ~ positionX ~ positionY ~ dueDate ~ fridgeId ~ id <> (Post.apply _, Post.unapply _)
   
    // Query Definition
    val autoInc = author ~ content ~ color ~ date ~ positionX ~ positionY ~ dueDate ~ fridgeId returning id into { case (c, i) => Post(c._1, c._2,c._3, c._4,c._5, c._6,c._7,c._8, i) }
    def findAll = for (x <- ((Posts))) yield x
    def forInsert = author ~ content ~ color ~ date ~ positionX ~ positionY ~ dueDate ~ fridgeId <>
      ({ (a,ct,cl,d,ps,py,dd,fi) => Post(a,ct,cl,d,ps,py,dd,fi, None) }, { x: Post => Some((x.author, x.content, x.color, x.date, x.positionX, x.positionY, x.dueDate, x.fridgeId)) })
      
    // Query Execution
    def findAllPost(implicit session: Session): List[Post] = Query(Posts).sortBy(_.id).list()
    def insert(x: Post)(implicit session: Session): Post = {
      autoInc.insert(x.author, x.content, x.color, x.date, x.positionX, x.positionY, x.dueDate, x.fridgeId)
    }
    
    def findPostByFridge(fridgeName : String )(implicit session: Session) = Query(Posts).filter(_.fridgeId === fridgeName).list 
  }
}