package pomf.core.actors

import akka.actor._
import pomf.core.metrics.Instrumented

trait CommonActor extends Actor with ActorLogging with Instrumented {

  override def postRestart(reason: Throwable): Unit = {
    log.debug(s"Restarted actor: ${self.path}")
    super.postRestart(reason)
  }
}

