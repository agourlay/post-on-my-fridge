package pomf

import pomf.configuration.Configuration
import pomf.core.{ BootedCore, CoreActors }
import pomf.api.Api

object Boot extends App with Configuration with BootedCore with CoreActors with Api {}