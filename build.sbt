import scalariform.formatter.preferences._

organization := "com.agourlay"

name := "pomf"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-Ywarn-dead-code",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-feature"
)

enablePlugins(JavaAppPackaging)

mappings in Universal += {
  file("src/main/resources/application.conf") -> "conf/pomf.conf"
}

scriptClasspath += "../conf/pomf.conf"

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(AlignParameters, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)
  .setPreference(RewriteArrowSymbols, true)

resolvers += "hseeberger at bintray" at "http://dl.bintray.com/hseeberger/maven"

libraryDependencies ++= {
  val akkaV         = "2.3.9"
  val akkaHttpV     = "1.0-M4"
  val akkaSseV      = "0.7.0"
  val sprayJsonV    = "1.3.1"
  val scalaMetricsV = "3.3.0_a2.3"
  val metricsV      = "3.1.1"
  val jodaTimeV     = "2.7"
  val jodaConvertV  = "1.7"
  val slickV        = "2.1.0"
  val slickJodaV    = "1.2.0"
  val postgresqlV   = "9.4-1201-jdbc41"
  val logbackV      = "1.1.2"
  val hikaricpV     = "2.3.3"
  Seq(                
     "com.typesafe.akka"     %%  "akka-actor"                        % akkaV
    ,"com.typesafe.akka"     %%  "akka-slf4j"                        % akkaV
    ,"com.typesafe.akka"     %%  "akka-http-experimental"            % akkaHttpV
    ,"com.typesafe.akka"     %%  "akka-http-spray-json-experimental" % akkaHttpV
    ,"de.heikoseeberger"     %%  "akka-sse"                          % akkaSseV
    ,"io.spray"              %%  "spray-json"                        % sprayJsonV
    ,"joda-time"             %   "joda-time"                         % jodaTimeV
    ,"org.joda"              %   "joda-convert"                      % jodaConvertV
    ,"com.github.tototoshi"  %%  "slick-joda-mapper"                 % slickJodaV
    ,"com.typesafe.slick"    %%  "slick"                             % slickV
    ,"org.postgresql"        %   "postgresql"                        % postgresqlV
    ,"ch.qos.logback"        %   "logback-classic"                   % logbackV
    ,"com.zaxxer"            %   "HikariCP-java6"                    % hikaricpV
    ,"nl.grons"              %%  "metrics-scala"                     % scalaMetricsV
    ,"io.dropwizard.metrics" %   "metrics-graphite"                  % metricsV
  )
}

seq(Revolver.settings: _*)
