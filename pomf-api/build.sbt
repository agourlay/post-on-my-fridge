import AssemblyKeys._

assemblySettings

jarName in assembly := "pomf-api.jar"

test in assembly := {}

net.virtualvoid.sbt.graph.Plugin.graphSettings

organization := "com.agourlay"

name := "pomf-api"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

resolvers ++= Seq(
   "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/"
  ,"sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  ,"typesafe release"   at "http://repo.typesafe.com/typesafe/releases/"
  ,"typesafe repo"      at "http://repo.typesafe.com/typesafe/repo/"
  ,"maven central"      at "http://repo1.maven.org/maven2/"
  ,"spray repo"         at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
   "io.spray"           %   "spray-can"       % "1.1-M8"                     withSources() 
  ,"io.spray"           %   "spray-routing"   % "1.1-M8"                     withSources()
  ,"io.spray"           %   "spray-testkit"   % "1.1-M8"          % "test"   withSources()
  ,"io.spray"           %%  "spray-json"      % "1.2.5"                      withSources()
  ,"com.typesafe.akka"  %%  "akka-actor"      % "2.1.4"                      withSources()
  ,"com.typesafe.akka"  %%  "akka-slf4j"      % "2.1.4"                      withSources()
  ,"com.typesafe.akka"  %%  "akka-remote"     % "2.1.4"                      withSources()
  ,"net.debasishg"      %%  "redisclient"     % "2.10"                       withSources()
  ,"com.typesafe.slick" %%  "slick"           % "1.0.1"                      withSources()
  ,"com.h2database"     %   "h2"              % "1.3.172"       
  ,"org.postgresql"     %   "postgresql"      % "9.2-1003-jdbc4"             withSources()
  ,"ch.qos.logback"     %   "logback-classic" % "1.0.13"                     withSources()
  ,"junit"              %   "junit"           % "4.11"            % "test"   withSources()
  ,"org.specs2"         %%  "specs2"          % "2.0"             % "test"   withSources()  
)

seq(Revolver.settings: _*)
