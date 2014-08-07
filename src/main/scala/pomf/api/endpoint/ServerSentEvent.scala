package pomf.api.endpoint

import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import Directives._

object ServerSentEvent {
    val EventStreamType = register(
	    MediaType.custom(
	    	mainType = "text",
	    	subType = "event-stream",
	    	compressible = true,
	    	binary = false
	    )
	)
	def lastEventId = optionalHeaderValueByName("Last-Event-ID") | parameter("lastEventId"?)
}