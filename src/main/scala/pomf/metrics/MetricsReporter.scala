package pomf.metrics

import akka.actor._

import java.util.concurrent.TimeUnit
import java.net.InetSocketAddress

import scala.collection.JavaConversions._

import spray.json.JsValue

import com.codahale.metrics._
import com.codahale.metrics.graphite._

import nl.grons.metrics.scala._

import pomf.configuration._
import pomf.metrics.MetricsReporterProtocol._

class MetricsReporter extends Actor with ActorLogging with Instrumented {

	val system = context.system

	JmxReporter.forRegistry(metricRegistry).build().start()

  log.info(s"Starting MetricsReporter to JMX")

  if (Settings(system).Graphite.Enable) {
  	val graphiteHost = Settings(system).Graphite.Host
    val graphitePort = Settings(system).Graphite.Port

  	log.info(s"Starting MetricsReporter to Graphite $graphiteHost:$graphitePort")

   	val graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort))
    val graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                                           .prefixedWith(Settings(system).Graphite.Prefix)
                                           .convertRatesTo(TimeUnit.SECONDS)
                                           .convertDurationsTo(TimeUnit.MILLISECONDS)
                                           .filter(MetricFilter.ALL)
                                           .build(graphite)

    graphiteReporter.start(1, TimeUnit.MINUTES)
  }

	def receive = {
    case All       => sender ! metricsByName(MetricsReporter.allMetrics)
    case Requests  => sender ! metricsByName(MetricsReporter.requestsMetrics)
    case Streaming => sender ! metricsByName(MetricsReporter.streamingMetrics)
    case TopicRepo => sender ! metricsByName(MetricsReporter.domainMetrics)
	}

  def metricsByName(name : String) = {
    val rawMap = metricRegistry.getMetrics().filterKeys(_.contains(name))
    MetricsReport(rawMap.toMap.mapValues(MetricsUtil.magic(_)))
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
  case object TopicRepo
  case object SubRepo
  case class MetricsReport(metrics : Map[String, JsValue])
}