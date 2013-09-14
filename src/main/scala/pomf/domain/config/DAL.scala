package pomf.domain.config

import scala.slick.driver.ExtendedProfile

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import pomf.domain.model.FridgeComponent
import pomf.domain.model.PostComponent

trait Profile {
  val profile: ExtendedProfile
}

class DAL(override val profile: ExtendedProfile) extends FridgeComponent with PostComponent with Profile {
  import profile.simple._

  val logger: Logger = LoggerFactory.getLogger("domain")
  logger.info("Model class instantiated")

  def ddls = List(Posts.ddl,Fridges.ddl)

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