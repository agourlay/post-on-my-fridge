package pomf.rest
import pomf.domain._
import pomf.util._

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.routing.Directive.pimpApply
import spray.routing.directives.CompletionMagnet.fromObject
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.json.DefaultJsonProtocol._

class PomfServiceActor extends Actor with PomfService with ProductionDB {

  def actorRefFactory = context

  def receive = runRoute(pomfRoute)

}

import spray.json.DefaultJsonProtocol

object JsonImplicits extends DefaultJsonProtocol with DateMarshalling {
  implicit val impPost = jsonFormat9(Post)
  implicit val impFridge = jsonFormat3(Fridge)
  implicit val impFridgeRest = jsonFormat4(FridgeRest)
}

trait PomfService extends HttpService { this: DBConfig =>
  import JsonImplicits._

  val pomfRoute =
    path("") {
      get {
        getFromResource("index.html")
      }
    } ~
      pathPrefix("css") {
        getFromResourceDirectory("css")
      } ~
      pathPrefix("js") {
        getFromResourceDirectory("js")
      } ~
      pathPrefix("img") {
        getFromResourceDirectory("img")
      } ~
      pathPrefix("templates") {
        getFromResourceDirectory("templates")
      } ~
      path("fridge" / Rest) { fridgeName =>
        get {
          complete(m.getFridgeRest(fridgeName))
        } ~
          post {
            entity(as[Fridge]) { fridge =>
              complete(m.addFridge(fridge))
            }
          }
      } ~
      path("post") {
        post {
          entity(as[Post]) { post =>
            complete(m.addPost(post))
          }
        } ~
          put {
            entity(as[Post]) { post =>
              complete(m.updatePost(post))
            }
          }
      } ~
      path("post" / LongNumber) { postId =>
        get {
          complete(m.getPost(postId))
        } ~
          delete {
            complete(m.deletePost(postId))
          }
      }
}