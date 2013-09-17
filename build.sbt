import AssemblyKeys._

assemblySettings

jarName in assembly := "pomf-api.jar"

test in assembly := {}

net.virtualvoid.sbt.graph.Plugin.graphSettings

organization := "com.agourlay"

name := "pomf-api"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions := Seq(
  "-unchecked",
  "-Xlint",
  "-deprecation",
  "-encoding","utf8",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-feature")

resolvers ++= Seq(
   "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/"
  ,"sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  ,"typesafe release"   at "http://repo.typesafe.com/typesafe/releases/"
  ,"typesafe repo"      at "http://repo.typesafe.com/typesafe/repo/"
  ,"typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  ,"maven central"      at "http://repo1.maven.org/maven2/"
  ,"akka repo"          at "http://repo.akka.io/"
  ,"spray repo"         at "http://repo.spray.io/"
  ,"spray nightly"      at "http://nightlies.spray.io/"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

libraryDependencies ++= Seq(
   "io.spray"           %   "spray-can"        % "1.2-20130912"               withSources() 
  ,"io.spray"           %   "spray-routing"    % "1.2-20130912"               withSources()
  ,"io.spray"           %   "spray-caching"    % "1.2-20130912"               withSources()
  ,"io.spray"           %   "spray-testkit"    % "1.2-20130912"    % "test"   withSources()
  ,"io.spray"           %%  "spray-json"       % "1.2.5"                      withSources()
  ,"com.typesafe.akka"  %%  "akka-actor"       % "2.3-20130916-200212"        withSources()
  ,"com.typesafe.akka"  %%  "akka-slf4j"       % "2.3-20130916-200212"        withSources()
  ,"com.typesafe.akka"  %%  "akka-persistence" % "2.3-SNAPSHOT"               withSources()
  ,"com.typesafe.slick" %%  "slick"            % "1.0.1"                      withSources()
  ,"com.h2database"     %   "h2"               % "1.3.173"       
  ,"org.postgresql"     %   "postgresql"       % "9.2-1003-jdbc4"             withSources()
  ,"ch.qos.logback"     %   "logback-classic"  % "1.0.13"                     withSources()
  ,"junit"              %   "junit"            % "4.11"            % "test"   withSources()
  ,"org.specs2"         %%  "specs2"           % "2.2"             % "test"   withSources()  
)

seq(Revolver.settings: _*)
