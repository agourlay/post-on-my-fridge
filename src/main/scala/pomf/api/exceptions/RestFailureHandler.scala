package pomf.api.exceptions 

import spray.util.LoggingContext
import spray.routing._
import spray.http._
import HttpHeaders._

import akka.pattern.AskTimeoutException

import pomf.service._
import pomf.metrics.Instrumented

trait RestFailureHandling extends Instrumented {
    this: HttpService =>

    val postNotFound = metrics.meter("postNotFoundException")
    val fridgeNotFound = metrics.meter("fridgeNotFoundException")
    val chatRoomNotFound = metrics.meter("chatRoomNotFoundException")
    val fridgeAlreadyExists = metrics.meter("fridgeAlreadyExistsException")
    val illegalArgument = metrics.meter("illegalArgumentException")
    val askTimeout = metrics.meter("askTimeoutException")
    val requestTimeout = metrics.meter("requestTiemoutException")
    val otherException = metrics.meter("otherException")

	implicit def omnibusExceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
  	
		case e : PostNotFoundException  =>
	    	requestUri { uri =>
	    		postNotFound.mark()
	        	log.warning("Request to {} could not be handled normally -> post does not exist", uri)
	    	  	complete(StatusCodes.NotFound, s"Post ${e.postId} not found : please check post id correctness\n")
	    	}

	    case e : FridgeNotFoundException  =>
	    	requestUri { uri =>
	    		fridgeNotFound.mark()
	        	log.warning("Request to {} could not be handled normally -> fridge does not exist", uri)
	    	  	complete(StatusCodes.NotFound, s"Fridge ${e.fridgeId} not found : please check fridge id correctness\n")
	    	}

	    case e : ChatRoomNotFoundException  =>
	    	requestUri { uri =>
	    		chatRoomNotFound.mark()
	        	log.warning("Request to {} could not be handled normally -> chatRoom does not exist", uri)
	    	  	complete(StatusCodes.NotFound, s"ChatRoom ${e.fridgeId} not found : please check fridge id correctness\n")
	    	}			

	    case e : FridgeAlreadyExistsException =>
	        requestUri { uri => 
	        	fridgeAlreadyExists.mark()
	        	log.warning("Request to {} could not be handled normally -> fridge {} already exists", uri, e.fridgeName)
	        	complete(StatusCodes.Accepted, Location(uri):: Nil, s"Fridge ${e.fridgeName} already exist \n")
	        }    

		case e : RequestTimeoutException  =>
		    requestUri { uri =>
		    	requestTimeout.mark()
		        log.error("Request to {} could not be handled normally -> RequestTimeout", uri)
		        log.error("RequestTimeout : {} ", e)
		        complete(StatusCodes.InternalServerError, "Something is taking longer than expected, retry later \n")
		    }

	    case e : IllegalArgumentException  => 
	      	requestUri { uri =>
	      		illegalArgument.mark()
		        log.error("Request to {} could not be handled normally -> IllegalArgumentException", uri)
		        log.error("IllegalArgumentException : {} ", e)
		        complete(StatusCodes.InternalServerError, e.getMessage)
	    	}  

	    case e : AskTimeoutException  => 
	      	requestUri { uri =>
	      		askTimeout.mark()
		        log.error("Request to {} could not be handled normally -> AskTimeoutException", uri)
		        log.error("AskTimeoutException : {} ", e)
		        complete(StatusCodes.InternalServerError, e.getMessage)
	    	}  

	  	case e : Exception  =>
	    	requestUri { uri =>
	    		otherException.mark()
		        log.error("Request to {} could not be handled normally -> unknown exception", uri)
		        log.error("unknown exception : {} ", e)
		    	complete(StatusCodes.InternalServerError, "An unexpected error occured \n")
	    	}
	}      
}  