package pomf.service.rest
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
import spray.json.DefaultJsonProtocol
import pomf.domain.model.Post
import pomf.domain.model.FridgeRest
import pomf.domain.model.Fridge
import pomf.domain.config.ProductionDB
import pomf.domain.config.DBConfig

class PomfServiceActor extends Actor with PomfRouteService with ProductionDB {

  def actorRefFactory = context

  def receive = runRoute(pomfRoute)

}

object JsonImplicits extends DefaultJsonProtocol with DateMarshalling {
  implicit val impPost = jsonFormat9(Post)
  implicit val impFridge = jsonFormat3(Fridge)
  implicit val impFridgeRest = jsonFormat4(FridgeRest)
}

trait PomfRouteService extends HttpService { this: DBConfig =>
  import JsonImplicits._

  val pomfRoute =
    pathPrefix("api") {
     path("fridge" / Rest) { fridgeName =>
        get {
          complete(m.getFridgeRest(fridgeName))
        }           
      } ~
      path("fridge"){
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
      }~
      pathPrefix("rss") {
         path("fridge" / Rest) { fridgeName =>
           get {
            complete(m.getFridgeRss(fridgeName))
          }           
        }
     }~
     pathPrefix("search") {
         path("fridge"){
           parameters("term") { term =>
             get {
              complete(m.searchByNameLike(term))
             }           
           }
         }
     }   
  }   
}