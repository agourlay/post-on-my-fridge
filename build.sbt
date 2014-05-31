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
  val akkaV       = "2.3.3"
  val sprayV      = "1.3.1"
  val sprayJsonV  = "1.2.6"
  Seq(
       "io.spray"               %   "spray-can"         % sprayV     
      ,"io.spray"               %   "spray-routing"     % sprayV          
      ,"io.spray"               %   "spray-caching"     % sprayV              
      ,"io.spray"               %%  "spray-json"        % sprayJsonV                
      ,"com.typesafe.akka"      %%  "akka-actor"        % akkaV        
      ,"com.typesafe.akka"      %%  "akka-slf4j"        % akkaV          
      ,"joda-time"              %   "joda-time"         % "2.3"                
      ,"org.joda"               %   "joda-convert"      % "1.6"                
      ,"com.github.tototoshi"   %%  "slick-joda-mapper" % "1.1.0"              
      ,"com.typesafe.slick"     %%  "slick"             % "2.0.2"              
      ,"org.postgresql"         %   "postgresql"        % "9.3-1101-jdbc41"    
      ,"ch.qos.logback"         %   "logback-classic"   % "1.1.2"
      ,"commons-dbcp"           %   "commons-dbcp"      % "1.4"            
  )
}

seq(Revolver.settings: _*)
