package pomf.api.endpoint

import akka.actor._

import spray.routing._
import spray.httpx.encoding._

import scala.concurrent.duration._
import scala.concurrent.Future

import pomf.api.route._
import pomf.api.exceptions.RestFailureHandler
import pomf.core.CoreActors
import pomf.metrics.MetricsReporter

class ApiEndpoint(coreActors : CoreActors) extends HttpEndpoint with Actor {
  implicit def actorRefFactory = context    
  def receive = runRoute(routes(coreActors))
}

trait HttpEndpoint extends HttpService with RestFailureHandler {
 
  def routes(coreActors : CoreActors ) (implicit context: ActorContext) = {
    val crudService = coreActors.crudService
    val chatRepo = coreActors.chatRepo
    val tokenService = coreActors.tokenService
    val metricsReporter = coreActors.metricsReporter

    val chat = new ChatRoute(chatRepo).route
    val files = new FilesRoute().route 
    val fridge = new FridgeRoute(crudService).route    
    val post = new PostRoute(crudService).route    
    val search = new SearchRoute(crudService).route   
    val stats = new StatsRoute(crudService, metricsReporter).route
    val streaming = new StreamingRoute().route     
    val token = new TokenRoute(tokenService).route             

    encodeResponse(Gzip){
      chat ~ files ~ fridge ~ post ~ search ~ stats ~ streaming ~ token 
    }  
  } 
}

object ApiEndpoint {
  def props(coreActors : CoreActors) = Props(classOf[ApiEndpoint], coreActors)
}