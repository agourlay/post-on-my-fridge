package pomf.domain.model

import pomf.domain.config.Profile
import pomf.util.XssFilter
import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.DateTime

case class Fridge(name: String, description: String = "", creationDate: DateTime, modificationDate: DateTime, id: Option[Long] = None){
  require(!name.isEmpty, "fridge name must not be empty")
  require(!XssFilter.containsScript(name), "name must not contain script tags")
  require(!XssFilter.containsScript(description), "description must not contain script tags")
}

case class FridgeRest(name: String, description: String, creationDate: DateTime, modificationDate: DateTime, id: Option[Long], posts: List[Post])

trait FridgeComponent { this: Profile =>
  import profile.simple._

  object Fridges extends Table[Fridge]("FRIDGE") {
    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME", O.NotNull)
    def description = column[String]("DESCRIPTION", O.NotNull)
    def creationDate = column[DateTime]("CREATION_DATE", O.NotNull)
    def modificationDate = column[DateTime]("MODIFICATION_DATE", O.NotNull)

    def * = name ~ description ~ creationDate ~ modificationDate ~ id <> (Fridge.apply _, Fridge.unapply _)

    // Query Definition
    val autoInc = name ~ description  ~ creationDate ~ modificationDate returning id into { case (c, i) => Fridge(c._1, c._2, c._3, c._4, i) }
    def findAll = for (x <- (Fridges)) yield x
    def forInsert = name ~ description  ~ creationDate ~ modificationDate <>
      ({ (n, d, cd, md) => Fridge(n, d, cd, md, None) }, { x: Fridge => Some((x.name, x.description, x.creationDate, x.modificationDate)) })
      
    // Query Execution
    def findAllFridge(implicit session: Session): List[Fridge] = Query(Fridges).sortBy(_.name).list()

    def insert(fridge: Fridge)(implicit session: Session): Fridge = autoInc.insert(fridge.name, fridge.description, fridge.creationDate, fridge.modificationDate)

    def update(fridge: Fridge)(implicit session: Session) = Fridges.where(_.id === fridge.id).update(fridge)

    def updateModificationDate(fridgeId: String)(implicit session: Session) = {
      val fridgeOpt = findByName(fridgeId)
      if (fridgeOpt.isDefined) {
        val fridge = fridgeOpt.get
        update(Fridge(fridge.name, fridge.description, fridge.creationDate, new DateTime(), fridge.id))
      }  
    }

    def findByName(fridgeName: String)(implicit session: Session):Option[Fridge] = Query(Fridges).filter(_.name === fridgeName).firstOption    
    
    def searchByNameLike(term:String)(implicit session: Session):List[String] = {
      val query = for {
    	  fridge <- Fridges if fridge.name like "%"+term+"%"
      } yield (fridge.name)
      query.list
    }

    def count(implicit session: Session) = Query(Fridges).list.length

  }
}
