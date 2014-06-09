package pomf.api.request

import akka.actor._
import akka.actor.SupervisorStrategy.Stop

import scala.util.Failure

import spray.routing._

import pomf.metrics.Instrumented
import pomf.configuration._
import pomf.api.exceptions.RequestTimeoutException
import pomf.api.request.RestRequestProtocol._

abstract class RestRequest(ctx : RequestContext) extends Actor with Instrumented {

  val system = context.system
  implicit val executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(Settings(system).Timeout)

  val timeoutScheduler = system.scheduler.scheduleOnce(timeout.duration, self, RestRequestProtocol.RequestTimeout)
  
  val timerCtx = metrics.timer("timer").timerContext()
  
  override def receive : Receive = handleTimeout

  def handleTimeout : Receive = {
    case RequestTimeout => {
      ctx.complete(new RequestTimeoutException())
      requestOver()
    }
    case Failure(e) => {
      ctx.complete(e)
      requestOver()
    }
    case e : Exception => {
      ctx.complete(e)
      requestOver()
    }   
  }

  def requestOver() = {
    timerCtx.stop()
    self ! PoisonPill
  }

  override def postStop() = {
    timeoutScheduler.cancel()
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

object RestRequestProtocol {
  case object RequestTimeout
}