package pomf.core

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer

trait Core {
  implicit def system: ActorSystem
  implicit def materializer: FlowMaterializer
}

trait BootedCore extends Core {
  implicit lazy val system = ActorSystem("pomf")
  implicit lazy val materializer = FlowMaterializer()
  sys.addShutdownHook(system.shutdown())
}