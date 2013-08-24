package pomf.domain.model

import pomf.domain.config.Profile
import pomf.util.XssFilter

case class Fridge(name: String, description: String = "", id: Option[Long] = None){
  require(!name.isEmpty, "fridge name must not be empty")
  require(!XssFilter.containsScript(name), "name must not contain script tags")
  require(!XssFilter.containsScript(description), "description must not contain script tags")
}

case class FridgeRest(name: String, description: String, id: Option[Long], posts: List[Post])

trait FridgeComponent { this: Profile =>
  import profile.simple._

  object Fridges extends Table[Fridge]("FRIDGE") {
    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME", O.NotNull)
    def description = column[String]("DESCRIPTION", O.NotNull)

    def * = name ~ description ~ id <> (Fridge.apply _, Fridge.unapply _)

    // Query Definition
    val autoInc = name ~ description returning id into { case (c, i) => Fridge(c._1, c._2, i) }
    def findAll = for (x <- (Fridges)) yield x
    def forInsert = name ~ description <>
      ({ (n, d) => Fridge(n, d, None) }, { x: Fridge => Some((x.name, x.description)) })
      
    // Query Execution
    def findAllFridge(implicit session: Session): List[Fridge] = Query(Fridges).sortBy(_.name).list()

    def insert(fridge: Fridge)(implicit session: Session): Fridge = autoInc.insert(fridge.name, fridge.description)

    def update(fridge: Fridge)(implicit session: Session) = Fridges.where(_.id === fridge.id).update(fridge)

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
