package pomf.api.endpoint

import akka.actor.ActorLogging
import akka.http.model.HttpResponse
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.http.server._
import pomf.core.metrics.Instrumented
import pomf.domain.{ ChatRoomNotFoundException, FridgeAlreadyExistsException, FridgeNotFoundException, PostNotFoundException }

trait RestFailureHandler extends Instrumented {
  this: ActorLogging ⇒

  val postNotFound = metrics.meter("PostNotFoundException")
  val fridgeNotFound = metrics.meter("FridgeNotFoundException")
  val chatRoomNotFound = metrics.meter("ChatRoomNotFoundException")
  val fridgeAlreadyExists = metrics.meter("FridgeAlreadyExistsException")
  val illegalArgument = metrics.meter("IllegalArgumentException")
  val requestTimeout = metrics.meter("RequestTimeoutException")
  val otherException = metrics.meter("OtherException")

  implicit val exceptionHandler = ExceptionHandler {

    case e: PostNotFoundException ⇒
      extractUri { uri ⇒
        postNotFound.mark()
        log.warning("Request to {} could not be handled normally -> post does not exist", uri)
        complete(HttpResponse(NotFound, entity = s"Post ${e.postId} not found : please check post id correctness\n"))
      }

    case e: FridgeNotFoundException ⇒
      extractUri { uri ⇒
        fridgeNotFound.mark()
        log.warning("Request to {} could not be handled normally -> fridge does not exist", uri)
        complete(HttpResponse(NotFound, entity = s"Fridge ${e.fridgeId} not found : please check fridge id correctness\n"))
      }

    case e: ChatRoomNotFoundException ⇒
      extractUri { uri ⇒
        chatRoomNotFound.mark()
        log.warning("Request to {} could not be handled normally -> chatRoom does not exist", uri)
        complete(HttpResponse(NotFound, entity = s"ChatRoom ${e.fridgeId} not found : please check fridge id correctness\n"))
      }

    case e: FridgeAlreadyExistsException ⇒
      extractUri { uri ⇒
        fridgeAlreadyExists.mark()
        log.warning("Request to {} could not be handled normally -> fridge {} already exists", uri, e.fridgeId)
        complete(HttpResponse(Conflict, entity = s"Fridge ${e.fridgeId} already exist\n"))
      }

    case e: IllegalArgumentException ⇒
      extractUri { uri ⇒
        illegalArgument.mark()
        log.error("Request to {} could not be handled normally -> IllegalArgumentException", uri)
        log.error("IllegalArgumentException : {} ", e)
        complete(HttpResponse(InternalServerError, entity = e.getMessage))
      }

    case e: Exception ⇒
      extractUri { uri ⇒
        otherException.mark()
        log.error("Request to {} could not be handled normally -> unknown exception", uri)
        log.error("unknown exception : {} ", e)
        complete(HttpResponse(InternalServerError, entity = "An unexpected error occured \n"))
      }
  }
}