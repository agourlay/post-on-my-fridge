package pomf.api.route

import akka.actor._
import akka.pattern._

import scala.concurrent.duration._
import scala.concurrent.Future

import spray.httpx.encoding._
import spray.routing._
import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.httpx.encoding._
import DefaultJsonProtocol._

import pomf.service.TokenServiceProtocol

class TokenRoute(tokenService : ActorRef)(implicit context: ActorContext) extends Directives {

  implicit def executionContext = context.dispatcher
  implicit val timeout = akka.util.Timeout(5 seconds)	

  val route = 
    path("token") {
      get {
        complete{
          (tokenService ? TokenServiceProtocol.RequestToken).mapTo[String]
        }
      }
    }        
}