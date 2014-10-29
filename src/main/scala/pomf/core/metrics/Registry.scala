package pomf.core.metrics

import com.codahale.metrics._
import com.codahale.metrics.health.HealthCheckRegistry

import nl.grons.metrics.scala._

object PomfRegistry {
  val metricRegistry = new MetricRegistry()
  val healthCheckRegistry = new HealthCheckRegistry()
}

trait Instrumented extends InstrumentedBuilder with CheckedBuilder with FutureMetrics {
  val metricRegistry = PomfRegistry.metricRegistry
  val registry = PomfRegistry.healthCheckRegistry
  override lazy val metricBaseName = MetricName(getClass)
}