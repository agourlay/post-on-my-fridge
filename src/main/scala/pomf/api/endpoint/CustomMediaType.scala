package pomf.api.endpoint

import spray.http._
import spray.http.MediaTypes._

object CustomMediaType {
    val EventStreamType = register(
	    MediaType.custom(
	    	mainType = "text",
	    	subType = "event-stream",
	    	compressible = true,
	    	binary = false
	    )
	)
}