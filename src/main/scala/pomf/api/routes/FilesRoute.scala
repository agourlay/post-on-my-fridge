package pomf.api.route

import akka.actor._

import spray.routing._
import spray.http.HttpHeaders._
import spray.http.CacheDirectives._

class FilesRoute(implicit context: ActorContext) extends Directives {

  val route =
    pathSingleSlash {
      respondWithHeader(`Cache-Control`(`public`, `max-age`(60L * 60L * 24L * 31L))) {
        getFromResource("frontend/web/dist/index.html")
      }
    } ~
      respondWithHeader(`Cache-Control`(`public`, `max-age`(60L * 60L * 24L * 31L))) {
        getFromResourceDirectory("frontend/web/dist")
      }
}