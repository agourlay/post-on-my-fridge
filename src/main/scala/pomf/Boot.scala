package pomf

import akka.actor._
import akka.io.IO

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import pomf.configuration._
import pomf.core._
import pomf.api._

import spray.can.Http

object Boot extends App with Configuration with BootedCore with CoreActors with Rest with Web {
 
  val log: Logger = LoggerFactory.getLogger("boot")
  log.info(" +--------------------+")
  log.info(" |  Fridge starting   |")
  log.info(" |--------------------|")
  log.info(" |                    |")
  log.info(" |                    |")
  log.info("xx                    |")
  log.info("x|                    |")
  log.info("xx                    |")
  log.info(" |                    |")
  log.info(" +--------------------+")
  log.info(" |                    |")
  log.info("xx                    |")
  log.info("x|                    |")
  log.info("xx                    |")
  log.info(" |                    |")
  log.info(" |                    |")
  log.info(" +--------------------+")  
}