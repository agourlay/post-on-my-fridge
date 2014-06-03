package pomf.api.exceptions 

import spray.util.LoggingContext
import spray.routing._
import spray.http._
import HttpHeaders._

import akka.pattern.AskTimeoutException

import pomf.service._

trait RestFailureHandling {
  this: HttpService =>

	implicit def omnibusExceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
  	
		case e : PostNotFoundException  =>
	    	requestUri { uri =>
	        	log.warning("Request to {} could not be handled normally -> post does not exist", uri)
	    	  	complete(StatusCodes.NotFound, s"Post ${e.postId} not found : please check post id correctness\n")
	    	}

	    case e : FridgeNotFoundException  =>
	    	requestUri { uri =>
	        	log.warning("Request to {} could not be handled normally -> fridge does not exist", uri)
	    	  	complete(StatusCodes.NotFound, s"Fridge ${e.fridgeId} not found : please check fridge id correctness\n")
	    	}	

	    case e : IllegalArgumentException  => 
	      	requestUri { uri =>
		        log.error("Request to {} could not be handled normally -> IllegalArgumentException", uri)
		        log.error("IllegalArgumentException : {} ", e)
		        complete(StatusCodes.InternalServerError, e.getMessage)
	    	}  

	    case e : AskTimeoutException  => 
	      	requestUri { uri =>
		        log.error("Request to {} could not be handled normally -> AskTimeoutException", uri)
		        log.error("AskTimeoutException : {} ", e)
		        complete(StatusCodes.InternalServerError, e.getMessage)
	    	}  

	  	case e : Exception  =>
	    	requestUri { uri =>
		        log.error("Request to {} could not be handled normally -> unknown exception", uri)
		        log.error("unknown exception : {} ", e)
		    	complete(StatusCodes.InternalServerError, "An unexpected error occured \n")
	    	}
	}      
}  