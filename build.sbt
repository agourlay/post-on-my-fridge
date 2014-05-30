import com.typesafe.sbt.SbtNativePackager.Universal

import com.typesafe.sbt.packager.Keys._

packageArchetype.java_application

incOptions := incOptions.value.withNameHashing(true)

mappings in Universal += {
  file("src/main/resources/application.conf") -> "conf/pomf.conf"
}

scriptClasspath += "../conf/pomf.conf"

organization := "com.agourlay"

name := "pomf"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions := Seq(
  "-unchecked",
  "-Xlint",
  "-deprecation",
  "-encoding","utf8",
  "-Ywarn-dead-code",
  "-language:_",
  "-feature")

resolvers ++= Seq(
   "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/"
  ,"sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  ,"typesafe release"   at "http://repo.typesafe.com/typesafe/releases/"
  ,"typesafe repo"      at "http://repo.typesafe.com/typesafe/repo/"
  ,"typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  ,"maven central"      at "http://repo1.maven.org/maven2/"
  ,"akka repo"          at "http://repo.akka.io/"
  ,"akka snapshots"     at "http://repo.akka.io/snapshots"
  ,"spray repo"         at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaVersion   = "2.3.3"
  val sprayVersion  = "1.3.1"
  Seq(
       "io.spray"               %   "spray-can"         % sprayVersion               withSources() 
      ,"io.spray"               %   "spray-routing"     % sprayVersion               withSources()
      ,"io.spray"               %   "spray-caching"     % sprayVersion               withSources()
      ,"io.spray"               %%  "spray-json"        % "1.2.6"                    withSources()
      ,"com.typesafe.akka"      %%  "akka-actor"        % akkaVersion                withSources()
      ,"com.typesafe.akka"      %%  "akka-slf4j"        % akkaVersion                withSources()
      ,"joda-time"              %   "joda-time"         % "2.3"                      withSources()
      ,"org.joda"               %   "joda-convert"      % "1.6"                      withSources()
      ,"com.github.tototoshi"   %%  "slick-joda-mapper" % "1.1.0"                    withSources()
      ,"com.typesafe.slick"     %%  "slick"             % "2.0.2"                    withSources()
      ,"org.postgresql"         %   "postgresql"        % "9.3-1101-jdbc41"          withSources()
      ,"ch.qos.logback"         %   "logback-classic"   % "1.1.2"                    withSources() 
  )
}

seq(Revolver.settings: _*)
