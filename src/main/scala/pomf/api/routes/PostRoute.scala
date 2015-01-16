package pomf.api.route

import akka.actor.{ Actor, ActorRef, Props, ActorContext }
import akka.http.marshallers.sprayjson.SprayJsonSupport
import akka.http.marshalling.Marshaller._
import akka.http.unmarshalling.Unmarshal
import akka.http.server._
import Directives._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.api.request._

object PostRoute {

  def build(crudService: ActorRef)(implicit context: ActorContext) =
    path("posts") {
      post {
        parameters("token") { token ⇒
          entity(as[Post]) { post ⇒
            ctx ⇒ context.actorOf(CreatePost.props(post, token, ctx, crudService))
          }
        }
      } ~
        put {
          parameters("token") { token ⇒
            entity(as[Post]) { post ⇒
              ctx ⇒ context.actorOf(UpdatePost.props(post, token, ctx, crudService))
            }
          }
        }
    } ~
      path("posts" / JavaUUID) { postId ⇒
        get { ctx ⇒
          context.actorOf(GetPost.props(postId, ctx, crudService))
        } ~
          delete {
            parameters("token") { token ⇒
              ctx ⇒ context.actorOf(DeletePost.props(postId, token, ctx, crudService))
            }
          }
      }
}