package pomf.api.request

import akka.actor._
import akka.actor.SupervisorStrategy.Stop
import akka.http.server._
import akka.http.model._
import akka.http.model.headers._
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.unmarshalling.Unmarshal
import akka.http.marshalling._

import scala.util._
import scala.collection.immutable.Seq
import scala.concurrent.Future

import spray.json._
import spray.json.DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.core.actors.CommonActor
import pomf.configuration._
import pomf.api.exceptions.RequestTimeoutException

abstract class RestRequest(ctx: RequestContext) extends CommonActor {

  val system = context.system
  implicit val executionContext = context.dispatcher
  val timeout = akka.util.Timeout(Settings(system).Timeout)

  context.setReceiveTimeout(timeout.duration)

  val timerCtx = metrics.timer("request").timerContext()

  def receive = {
    case ReceiveTimeout ⇒ requestOver(new RequestTimeoutException())
    case Failure(e)     ⇒ requestOver(e)
    case e: Exception   ⇒ requestOver(e)
  }

  private def closeThings() {
    timerCtx.stop()
    self ! PoisonPill
  }

  def requestOver[T: ToResponseMarshallable](payload: T) = {
    ctx.complete(HttpResponse(entity = payload))
    closeThings()
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e ⇒ {
        ctx.complete(e)
        timerCtx.stop()
        Stop
      }
    }
}

object RestRequest {
  def perRequest[A](props: Props, message: Any)(implicit context: ActorContext) = {
    (context.actorOf(RestRequest.props(ctx, props)) ? message).mapTo[A]
  }
}