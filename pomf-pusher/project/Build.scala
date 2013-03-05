import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "pomf-pusher"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.github.sstone"  %%  "amqp-client" % "1.1"    ,   
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
