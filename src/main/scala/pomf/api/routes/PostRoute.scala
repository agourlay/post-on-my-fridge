package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.routing._
import spray.json._
import spray.httpx.SprayJsonSupport._

import DefaultJsonProtocol._

import pomf.api.endpoint.JsonSupport._
import pomf.domain.model.Post
import pomf.service.CrudServiceProtocol
import pomf.api.request._


class PostRoute(crudService : ActorRef)(implicit context: ActorContext) extends Directives {

  implicit val timeout = akka.util.Timeout(5 seconds)
  implicit def executionContext = context.dispatcher

  val route = 
    path("posts") {
      post {
        parameters("token") { token =>
          entity(as[Post]) { post =>
            complete {
              (crudService ? CrudServiceProtocol.CreatePost(post, token)).mapTo[Post]
            }
          }
        }
      } ~
        put {
          parameters("token") { token =>
            entity(as[Post]) { post =>
              complete {
                (crudService ? CrudServiceProtocol.UpdatePost(post, token)).mapTo[Post]
              }
            }
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