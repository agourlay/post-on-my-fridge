package pomf

import pomf.configuration.Configuration
import pomf.core.{ BootedCore, CoreActors }
import pomf.api.{ Rest, Web }

object Boot extends App with Configuration with BootedCore with CoreActors with Rest with Web {}