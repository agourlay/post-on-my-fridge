package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json.JsValue
import libs.json.Json._
import play.api.libs.concurrent._
import play.api.libs.iteratee.Concurrent
import libs.EventSource
import play.api.libs.iteratee.Enumerator
import model.Notification
import scala.language.reflectiveCalls
import play.api.libs.json.Json
import play.api.libs.json.JsValueDeserializer
import play.api.libs.json.JsString
import play.api.libs.json.JsObject

object StreamController extends Controller {
  
  /**
  * Remove event with user token
  */  
  def filterNotToken(token: String): Enumeratee[JsObject, JsObject] = Enumeratee.filter[JsObject] {
    notification => notification.\("token").as[String] != token
  }
  
  /**
  * Remove event not relative to current fridge
  */  
  def filterNotCurrentFridge(fridgeName: String): Enumeratee[JsObject, JsObject] = Enumeratee.filter[JsObject] {
    notification => notification.\("fridgeName").as[String] == fridgeName
  }
  
  /**
  * Remove token & fridge name attributes
  */  
  def filterInfo: Enumeratee[JsObject, JsObject] = Enumeratee.map[JsObject] {
    notification => Json.obj(
    		"command"    -> notification.\("command"),
    		"payload"    -> notification.\("payload"),
    		"timestamp"  -> notification.\("timestamp")
    )
  }
  
  //FIXME not really nice : could by clean with implicit
  def toJsValue: Enumeratee[JsObject, JsValue] = Enumeratee.map[JsObject] { notification => notification }

  val concurrentStream = Concurrent.broadcast[JsObject] 

  /**
   * Stream for a fridge
   */
  def stream(fridgeName:String, token: String) = Action {
    println("Get Stream for fridge "+fridgeName+" with token "+token)
    Ok.stream(concurrentStream._1 &> Concurrent.buffer(100)
                                  &> filterNotToken(token)
                                  &> filterNotCurrentFridge(fridgeName)
                                  &> filterInfo 
                                  &> toJsValue ><> EventSource()).as("text/event-stream")
  }
  
  /**
   * Firehose stream
   */
  def firehose() = Action {
    println("Get Firehose")
    // do not forget to filter private fridge when implemented
    Ok.stream(concurrentStream._1 &> Concurrent.buffer(100)
                                  &> filterInfo 
                                  &> toJsValue ><> EventSource()).as("text/event-stream")
  }
}
