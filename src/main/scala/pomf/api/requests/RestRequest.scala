package pomf.api.request

import akka.actor._
import akka.pattern._
import akka.actor.SupervisorStrategy.Stop

import scala.util._
import scala.concurrent.Future

import spray.routing._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.json._
import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.metrics.Instrumented
import pomf.configuration._
import pomf.api.exceptions.RequestTimeoutException

abstract class RestRequest(ctx : RequestContext)(implicit breaker: CircuitBreaker) extends Actor with ActorLogging with Instrumented {

  val system = context.system
  implicit val executionContext = context.dispatcher
  val timeout = akka.util.Timeout(Settings(system).Timeout)

  context.setReceiveTimeout(timeout.duration)

  val timerCtx = metrics.timer("timer").timerContext()
  
  override def receive : Receive = handleTimeout

  def handleTimeout : Receive = {
    case ReceiveTimeout => requestOver(new RequestTimeoutException())
    case Failure(e)     => requestOver(e)
    case e : Exception  => requestOver(e)
  }

  def requestOver[T](payload: T)(implicit marshaller: ToResponseMarshaller[T]) = {
    ctx.complete{
      breaker.withCircuitBreaker { 
        payload match {
          case e: Exception => Future.failed(e)
          case _ => Future.successful(payload)
        }
      }
    }
    timerCtx.stop()
    self ! PoisonPill
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e => {
        ctx.complete(e)
        timerCtx.stop()
        Stop
      }
    }  
}