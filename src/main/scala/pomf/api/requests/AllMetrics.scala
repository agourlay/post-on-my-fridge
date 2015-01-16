package pomf.api.request

import akka.actor.{ Actor, ActorRef, Props }
import akka.http.server._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

import spray.json._

import DefaultJsonProtocol._

import pomf.core.metrics.MetricsReporterProtocol._
import pomf.core.metrics.MetricsReporterProtocol

class AllMetrics(ctx: RequestContext, metricsRepo: ActorRef) extends RestRequest(ctx) {
  metricsRepo ! MetricsReporterProtocol.All

  override def receive = super.receive orElse waitingMetrics

  def waitingMetrics: Receive = {
    case MetricsReport(metrics) ⇒ requestOver(metrics)
  }
}

object AllMetrics {
  def props(ctx: RequestContext, metricsRepo: ActorRef) = Props(classOf[AllMetrics], ctx, metricsRepo)
}