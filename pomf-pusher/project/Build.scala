import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "pomf-pusher"
  val appVersion      = "1.0-SNAPSHOT"
 
  val appDependencies = Seq(
  	"com.typesafe.akka"  %%  "akka-remote"     % "2.1.4",
  	"com.typesafe.akka"  %%  "akka-slf4j"      % "2.1.4"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-deprecation","-unchecked","-feature") 
  )

}
