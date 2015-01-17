package pomf.core.metrics

import akka.actor._

import java.util.concurrent.TimeUnit

import scala.collection.JavaConversions._

import spray.json.JsValue

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricFilter
import com.codahale.metrics.{ Counter ⇒ JCounter }
import com.codahale.metrics.{ Meter ⇒ JMeter }
import com.codahale.metrics.{ Timer ⇒ JTimer }
import com.codahale.metrics.{ Gauge ⇒ JGauge }
import com.codahale.metrics.{ Histogram ⇒ JHistogram }
import com.codahale.metrics.graphite._

import nl.grons.metrics.scala._

import pomf.api.endpoint.JsonSupport
import pomf.configuration.Settings
import pomf.core.actors.CommonActor
import pomf.core.metrics.MetricsReporterProtocol._

class MetricsReporter extends CommonActor with JsonSupport {

  val system = context.system

  JmxReporter.forRegistry(metricRegistry).build().start()

  log.info("Starting MetricsReporter to JMX")

  if (Settings(system).Graphite.Enable) {
    val graphiteHost = Settings(system).Graphite.Host
    val graphitePort = Settings(system).Graphite.Port

    log.info(s"Starting MetricsReporter to Graphite $graphiteHost:$graphitePort")

    val graphite = new Graphite(graphiteHost, graphitePort)
    val graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
      .prefixedWith(Settings(system).Graphite.Prefix)
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .filter(MetricFilter.ALL)
      .build(graphite)

    graphiteReporter.start(1, TimeUnit.MINUTES)
  }

  def receive = {
    case All       ⇒ sender ! metricsByName(MetricsReporter.allMetrics)
    case Requests  ⇒ sender ! metricsByName(MetricsReporter.requestsMetrics)
    case Streaming ⇒ sender ! metricsByName(MetricsReporter.streamingMetrics)
    case Domain    ⇒ sender ! metricsByName(MetricsReporter.domainMetrics)
  }

  def metricsByName(name: String) = {
    val rawMap = metricRegistry.getMetrics().filterKeys(_.contains(name))
    MetricsReport(rawMap.toMap.mapValues(toJsValue(_)))
  }

  def toJsValue(java: Any): JsValue = java match {
    case j: JTimer      ⇒ formatTimer.write(new Timer(j))
    case j: JGauge[Int] ⇒ formatGauge.write(new Gauge(j))
    case j: JMeter      ⇒ formatMeter.write(new Meter(j))
    case j: JCounter    ⇒ formatCounter.write(new Counter(j))
    case j: JHistogram  ⇒ formatHisto.write(new Histogram(j))
    case _              ⇒ throw new RuntimeException(s"Cannot format metric")
  }
}

object MetricsReporter {
  def props = Props(classOf[MetricsReporter])
  val allMetrics = "pomf"
  val requestsMetrics = "pomf.api.request"
  val streamingMetrics = "pomf.api.streaming"
  val domainMetrics = "pomf.domain"
}

object MetricsReporterProtocol {
  case object All
  case object Requests
  case object Streaming
  case object Domain
  case class MetricsReport(metrics: Map[String, JsValue])
}