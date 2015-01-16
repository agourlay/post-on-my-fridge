package pomf

import pomf.configuration.Configuration
import pomf.core.{ BootedCore, CoreActors }

object Boot extends App with Configuration with BootedCore with CoreActors {}