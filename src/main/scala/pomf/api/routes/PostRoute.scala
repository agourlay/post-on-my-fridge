package pomf.api.route

import akka.actor._

import spray.routing._
import spray.httpx.SprayJsonSupport._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.api.request._


class PostRoute(crudService : ActorRef)(implicit context: ActorContext) extends Directives {

  val route = 
    path("posts") {
      post {
        parameters("token") { token =>
          entity(as[Post]) { post =>
            ctx => context.actorOf(CreatePost.props(post, token, ctx, crudService))
          }
        }
      } ~
      put {
        parameters("token") { token =>
          entity(as[Post]) { post =>
            ctx => context.actorOf(UpdatePost.props(post, token, ctx, crudService))
          }
        }
      } ~
      path("posts" / LongNumber) { postId =>
        get { ctx =>
          context.actorOf(GetPost.props(postId, ctx, crudService))
        } ~
        delete { 
          parameters("token") { token =>
            ctx => context.actorOf(DeletePost.props(postId, token, ctx, crudService))
          }
        }
      } 
    }     
}