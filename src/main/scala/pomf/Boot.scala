package pomf

import pomf.core.configuration.Configuration
import pomf.core.{ Core, CoreComponents }

object Boot extends App with Configuration with Core with CoreComponents {

  def stop() = {
    system.shutdown()
  }
}