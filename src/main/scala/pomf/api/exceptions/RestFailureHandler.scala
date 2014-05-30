package pomf.api.exceptions 

import spray.util.LoggingContext
import spray.routing._
import spray.http._
import HttpHeaders._

trait RestFailureHandling {
  this: HttpService =>

	implicit def omnibusExceptionHandler(implicit log: LoggingContext) = ExceptionHandler {
  	
	    case e : IllegalArgumentException  => 
	      requestUri { uri =>
	        log.error("Request to {} could not be handled normally -> IllegalArgumentException", uri)
	        log.error("IllegalArgumentException : {} ", e)
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