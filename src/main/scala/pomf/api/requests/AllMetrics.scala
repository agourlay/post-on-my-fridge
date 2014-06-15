package pomf.api.request

import akka.actor._

import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.json._

import DefaultJsonProtocol._

import pomf.metrics.MetricsReporterProtocol._
import pomf.metrics.MetricsReporterProtocol

class AllMetrics(ctx : RequestContext, metricsRepo: ActorRef) extends RestRequest(ctx) {

  metricsRepo ! MetricsReporterProtocol.All

  override def receive = waitingMetrics orElse handleTimeout

  def waitingMetrics : Receive = {
    case MetricsReport(metrics) => requestOver(metrics)
  }
}

object AllMetrics {
   def props(ctx : RequestContext, metricsRepo: ActorRef) 
     = Props(classOf[AllMetrics], ctx, metricsRepo).withDispatcher("requests-dispatcher")
}