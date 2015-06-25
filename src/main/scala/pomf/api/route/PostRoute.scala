package pomf.api.route

import akka.actor.ActorContext
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.marshalling.Marshaller._
import akka.http.scaladsl.server._
import Directives._

import java.util.UUID

import akka.stream.ActorMaterializer
import pomf.api.endpoint.JsonSupport
import pomf.domain.CrudService
import pomf.domain.model.Post
import pomf.core.configuration._

object PostRoute extends JsonSupport {

  def build(crudService: CrudService)(implicit context: ActorContext, fm: ActorMaterializer) = {
    implicit val timeout = akka.util.Timeout(Settings(context.system).Timeout)
    implicit val ec = context.dispatcher

    path("posts") {
      post {
        parameters("token") { token: String ⇒
          entity(as[Post]) { post: Post ⇒
            onSuccess(crudService.addPost(post, token)) { created: Post ⇒
              complete(ToResponseMarshallable(OK -> created))
            }
          }
        }
      } ~
        put {
          parameters("token") { token: String ⇒
            entity(as[Post]) { post: Post ⇒
              onSuccess(crudService.updatePost(post, token)) { updated: Post ⇒
                complete(ToResponseMarshallable(OK -> updated))
              }
            }
          }
        }
    } ~
      path("posts" / JavaUUID) { postId: UUID ⇒
        get {
          onSuccess(crudService.getPost(postId)) { post: Post ⇒
            complete(ToResponseMarshallable(OK -> post))
          }
        } ~
          delete {
            parameters("token") { token: String ⇒
              onSuccess(crudService.deletePost(postId, token)) { msg: String ⇒
                complete(ToResponseMarshallable(OK -> msg))
              }
            }
          }
      }
  }
}