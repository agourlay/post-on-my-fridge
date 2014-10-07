import com.typesafe.sbt.SbtNativePackager.Universal

import com.typesafe.sbt.packager.Keys._

import scalariform.formatter.preferences._

packageArchetype.java_application

mappings in Universal += {
  file("src/main/resources/application.conf") -> "conf/pomf.conf"
}

scriptClasspath += "../conf/pomf.conf"

organization := "com.agourlay"

name := "pomf"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

scalacOptions := Seq(
  "-unchecked",
  "-Xlint",
  "-deprecation",
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-Ywarn-dead-code",
  "-language:_",
  "-feature"
)

scapegoatConsoleOutput := false

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(AlignParameters, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)
  .setPreference(RewriteArrowSymbols, true)

resolvers ++= Seq(
   "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/"
  ,"typesafe release"   at "http://repo.typesafe.com/typesafe/releases/"
  ,"typesafe repo"      at "http://repo.typesafe.com/typesafe/repo/"
  ,"maven central"      at "http://repo1.maven.org/maven2/"
  ,"akka repo"          at "http://repo.akka.io/"
  ,"spray repo"         at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaV         = "2.3.6"
  val sprayV        = "1.3.1"
  val sprayJsonV    = "1.2.6"
  val scalaMetricsV = "3.3.0_a2.3"
  val metricsV      = "3.1.0"
  val jodaTimeV     = "2.5"
  val jodaConvertV  = "1.7"
  val slickV        = "2.1.0" 
  val slickJodaV    = "1.2.0"
  val postgresqlV   = "9.3-1102-jdbc41"
  val logbackV      = "1.1.2"
  val dbcpV         = "1.4"
  Seq(
     "io.spray"              %%  "spray-can"                % sprayV     
    ,"io.spray"              %%  "spray-routing-shapeless2" % sprayV             
    ,"io.spray"              %%  "spray-json"               % sprayJsonV                
    ,"com.typesafe.akka"     %%  "akka-actor"               % akkaV        
    ,"com.typesafe.akka"     %%  "akka-slf4j"               % akkaV          
    ,"joda-time"             %   "joda-time"                % jodaTimeV                
    ,"org.joda"              %   "joda-convert"             % jodaConvertV                
    ,"com.github.tototoshi"  %%  "slick-joda-mapper"        % slickJodaV             
    ,"com.typesafe.slick"    %%  "slick"                    % slickV             
    ,"org.postgresql"        %   "postgresql"               % postgresqlV    
    ,"ch.qos.logback"        %   "logback-classic"          % logbackV
    ,"commons-dbcp"          %   "commons-dbcp"             % dbcpV
    ,"nl.grons"              %%  "metrics-scala"            % scalaMetricsV        
    ,"io.dropwizard.metrics" %   "metrics-graphite"         % metricsV                  
  )
}

seq(Revolver.settings: _*)
