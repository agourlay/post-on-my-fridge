package pomf.service
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
import pomf.domain.model.ChatMessage
import pomf.domain.model.ChatMessage
import pomf.domain.model.Notification
import spray.routing.directives.ParamDefMagnet.apply
import akka.pattern.ask
import scala.concurrent.duration._
import akka.actor.Status.Success

class ApiHttpActor extends ApiRouteService with Actor  {

  def actorRefFactory = context

  def receive = runRoute(pomfRoute)

}

object JsonImplicits extends DefaultJsonProtocol with DateMarshalling {
  implicit val impPost = jsonFormat9(Post)
  implicit val impFridge = jsonFormat3(Fridge)
  implicit val impFridgeRest = jsonFormat4(FridgeRest)
  implicit val impChatMessage = jsonFormat3(ChatMessage)
  implicit val impNotif = jsonFormat5(Notification)
}

trait ApiRouteService extends HttpService { 
  import JsonImplicits._

  implicit def executionContext = actorRefFactory.dispatcher
  private implicit val timeout = akka.util.Timeout(60.seconds)
  val crudService = actorRefFactory.actorFor("pomf-crud-service")
  
  val pomfRoute =
    pathPrefix("api") {
      path("fridge" / Rest) { fridgeName =>
        get {
          complete{
            (crudService ? fridgeName).mapTo[FridgeRest]
            }
        }
      } ~
        path("fridge") {
          post {
            entity(as[Fridge]) { fridge =>
              complete{
               (crudService ? fridge).mapTo[Fridge]
              }
            }
          }
        } ~
        path("post") {
          post {
            parameters("token") { token =>
              entity(as[Post]) { post =>
                complete{
                  (crudService ? CrudServiceActor.CreatePost(post, token)).mapTo[Post]
                }
              }
            }
          } ~
            put {
              parameters("token") { token =>
                entity(as[Post]) { post =>
                  complete{
                  (crudService ? CrudServiceActor.UpdatePost(post, token)).mapTo[Post]
                }
                }
              }
            }
        } ~
        path("post" / LongNumber) { postId =>
          get {
            complete{
             (crudService ? postId).mapTo[Option[Post]]
            }
          } ~
            delete {
              parameters("token") { token =>
                complete{
                 (crudService ! (postId, token))
                 }
              }
            }
        } ~
        pathPrefix("rss") {
          path("fridge" / Rest) { fridgeName =>
            get {
              complete(getFridgeRss(fridgeName))
            }
          }
        } ~
        pathPrefix("search") {
          path("fridge") {
            parameters("term") { term =>
              get {
                complete(searchByNameLike(term))
              }
            }
          }
        } ~
        path("message" / Rest) { fridgeName =>
          post {
            parameters("token") { token =>
              entity(as[ChatMessage]) { message =>
                complete(addChatMessage(fridgeName, message, token))
              }
            }
          } ~
            get {
              complete(retrieveChatHistory(fridgeName))
            }
        } ~
        path("token") {
          get {
            complete(TokenSupport.nextSessionId)
          }
        }
    }
}