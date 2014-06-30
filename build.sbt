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

scalaVersion := "2.11.1"

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
  val akkaV         = "2.3.4"
  val sprayV        = "1.3.1"
  val sprayJsonV    = "1.2.6"
  val scalaMetricsV = "3.2.0_a2.3"
  val metricsV      = "3.0.2"
  val jodaTimeV     = "2.3"
  val jodaConvertV  = "1.6"
  val slickV        = "2.1.0-M2" 
  val slickJodaV    = "1.2.0-SNAPSHOT"
  val postgresqlV   = "9.3-1101-jdbc41"
  val logbackV      = "1.1.2"
  val dbcpV         = "1.4"
  Seq(
     "io.spray"             %%  "spray-can"                % sprayV     
    ,"io.spray"             %%  "spray-routing-shapeless2" % sprayV             
    ,"io.spray"             %%  "spray-json"               % sprayJsonV                
    ,"com.typesafe.akka"    %%  "akka-actor"               % akkaV        
    ,"com.typesafe.akka"    %%  "akka-slf4j"               % akkaV          
    ,"joda-time"            %   "joda-time"                % jodaTimeV                
    ,"org.joda"             %   "joda-convert"             % jodaConvertV                
    ,"com.github.tototoshi" %%  "slick-joda-mapper"        % slickJodaV             
    ,"com.typesafe.slick"   %%  "slick"                    % slickV             
    ,"org.postgresql"       %   "postgresql"               % postgresqlV    
    ,"ch.qos.logback"       %   "logback-classic"          % logbackV
    ,"commons-dbcp"         %   "commons-dbcp"             % dbcpV
    ,"nl.grons"             %%  "metrics-scala"            % scalaMetricsV        
    ,"com.codahale.metrics" %   "metrics-graphite"         % metricsV                  
  )
}

seq(Revolver.settings: _*)
