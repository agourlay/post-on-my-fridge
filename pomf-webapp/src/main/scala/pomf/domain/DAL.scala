package pomf.domain

import scala.slick.driver.ExtendedProfile
import scala.slick.session.Session
import scala.slick.driver.H2Driver
import scala.slick.driver.SQLiteDriver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait Profile {
  val profile: ExtendedProfile
}

class DAL(override val profile: ExtendedProfile) extends FridgeComponent with PostComponent with Profile {
  import profile.simple._

  val logger: Logger = LoggerFactory.getLogger("pomf.domain");
  logger.info("Model class instantiated")

  def ddls = List(Fridges.ddl, Posts.ddl)

  def create(implicit session: Session): Unit = {
    try {
      ddls.foreach(_.create)
    } catch {
      case e: Exception => logger.info("Could not create database.... assuming it already exists")
    }
  }

  def drop(implicit session: Session): Unit = {
    try {
      ddls.foreach(_.drop)
    } catch {
      case e: Exception => logger.info("Could not drop database")
    }
  }

  def purge(implicit session: Session) = { drop; create }
}