package pomf.api.streaming

import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import Directives._

object ServerSentEventSupport {
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