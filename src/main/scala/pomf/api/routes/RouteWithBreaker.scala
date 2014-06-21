package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.language.postfixOps

import spray.routing._

import pomf.metrics.Instrumented
import pomf.configuration._

class RouteWithBreaker(implicit context: ActorContext) extends Directives with Instrumented {

	val system = context.system
	val timeout = akka.util.Timeout(Settings(system).Timeout)

	val openCbMeter = metrics.meter("circuit-breaker.open")
    val closeCbMeter = metrics.meter("circuit-breaker.close")
    val halfCbMeter = metrics.meter("circuit-breaker.half")

	implicit val executionContext = context.dispatcher
	implicit val breaker = new CircuitBreaker(system.scheduler,
    	maxFailures = 10,
    	callTimeout = timeout.duration,
    	resetTimeout = timeout.duration * 2).onOpen(openCbMeter.mark())
                                            .onClose(closeCbMeter.mark())
                                            .onHalfOpen(halfCbMeter.mark())   
}