package pomf.api.route

import akka.actor._

import spray.httpx.encoding._
import spray.routing._

class FilesRoute(implicit context: ActorContext) extends Directives {

  val route = 
    pathSingleSlash{
      encodeResponse(Gzip){
        getFromResource("frontend/web/index.html")   
      }
    } ~
    encodeResponse(Gzip){
      getFromResourceDirectory("frontend/web")
    }        
}