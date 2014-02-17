import AssemblyKeys._

assemblySettings

net.virtualvoid.sbt.graph.Plugin.graphSettings

jarName in assembly := "pomf-api.jar"

test in assembly := {}

organization := "com.agourlay"

name := "pomf-api"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4-RC3"

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
  val akkaVersion   = "2.3.0-RC3"
  val sprayVersion  = "1.3-RC1"
  Seq(
       "io.spray"               %   "spray-can"         % sprayVersion               withSources() 
      ,"io.spray"               %   "spray-routing"     % sprayVersion               withSources()
      ,"io.spray"               %   "spray-caching"     % sprayVersion               withSources()
      ,"io.spray"               %   "spray-testkit"     % sprayVersion    % "test"   withSources()
      ,"io.spray"               %%  "spray-json"        % "1.2.5"                    withSources()
      ,"com.typesafe.akka"      %%  "akka-actor"        % akkaVersion                withSources()
      ,"com.typesafe.akka"      %%  "akka-slf4j"        % akkaVersion                withSources()
      ,"com.typesafe.akka"      %%  "akka-testkit"      % akkaVersion     % "test"   withSources()
      ,"joda-time"              %   "joda-time"         % "2.3"                      withSources()
      ,"org.joda"               %   "joda-convert"      % "1.6"                      withSources()
      ,"com.github.tototoshi"   %%  "slick-joda-mapper" % "0.4.1"                    withSources()
      ,"com.typesafe.slick"     %%  "slick"             % "1.0.1"                    withSources()
      ,"com.h2database"         %   "h2"                % "1.3.175"       
      ,"org.postgresql"         %   "postgresql"        % "9.3-1100-jdbc41"          withSources()
      ,"ch.qos.logback"         %   "logback-classic"   % "1.1.1"                    withSources()
      ,"org.specs2"             %%  "specs2-core"       % "2.3.8"         % "test"   withSources()  
  )
}

seq(Revolver.settings: _*)
