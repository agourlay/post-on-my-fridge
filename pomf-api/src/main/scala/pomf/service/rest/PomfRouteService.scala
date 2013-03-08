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
import pomf.service.PomfProdServiceLayer
import pomf.service.PomfActionService
import pomf.domain.model.Notification
import pomf.domain.model.ChatMessage

class PomfHttpActor extends Actor with PomfRouteService with PomfProdServiceLayer {

  def actorRefFactory = context

  def receive = runRoute(pomfRoute)

}

object JsonImplicits extends DefaultJsonProtocol with DateMarshalling {
  implicit val impPost = jsonFormat9(Post)
  implicit val impFridge = jsonFormat3(Fridge)
  implicit val impFridgeRest = jsonFormat4(FridgeRest)
  implicit val impChatMessage = jsonFormat3(ChatMessage)
}

trait PomfRouteService extends HttpService { this: PomfActionService =>
  import JsonImplicits._

  val pomfRoute =
    pathPrefix("api") {
     path("fridge" / Rest) { fridgeName =>
        get {
          detachTo(singleRequestServiceActor) {
            complete(getFridgeRest(fridgeName))
          }      
        }           
      } ~
      path("fridge"){
        post {
          detachTo(singleRequestServiceActor) {
                entity(as[Fridge]) { fridge =>
                	complete(addFridge(fridge))
                }
          	}     
          }
      } ~ 
      path("post") {
        post {
          detachTo(singleRequestServiceActor) {
            entity(as[Post]) { post =>
            	complete(addPost(post))
            }
          }        
        } ~
          put {
	        detachTo(singleRequestServiceActor) {
	          entity(as[Post]) { post =>
	            complete(updatePost(post))
	          } 
	        }          
          }
      } ~
      path("post" / LongNumber) { postId =>
        get {
          detachTo(singleRequestServiceActor) {
            complete(getPost(postId))
          }        
        } ~
          delete {
            detachTo(singleRequestServiceActor) {
              complete(deletePost(postId)) 
            }
          }
      }~
      pathPrefix("rss") {
         path("fridge" / Rest) { fridgeName =>
           get {
             detachTo(singleRequestServiceActor) {
               complete(getFridgeRss(fridgeName)) 
             }
          }           
        }
     }~
     pathPrefix("search") {
         path("fridge"){
           parameters("term") { term =>
             get {
               detachTo(singleRequestServiceActor) {
                  complete(searchByNameLike(term)) 
               }
             }           
           }
        }
     }~
     path("message" / Rest) { fridgeName =>
           post {
             detachTo(singleRequestServiceActor) {
               entity(as[ChatMessage]) { message =>
            	 complete(addChatMessage(fridgeName,message))
               }
             }
           }~
             get {
               detachTo(singleRequestServiceActor) {
                  complete(retrieveChatHistory(fridgeName)) 
               }
          }      
     }     
  }   
}