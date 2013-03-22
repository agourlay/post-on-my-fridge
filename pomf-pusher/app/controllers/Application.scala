package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json.JsValue
import libs.json.Json._
import libs.EventSource
import play.api.libs.iteratee.Enumerator
import model.Notification
import service.PomfNotificationService
import play.api.libs.json.Json
import play.api.libs.json.JsValueDeserializer
import play.api.libs.json.JsString
import play.api.libs.json.JsObject

object Application extends Controller {
  
  def filterNotToken(token: String): Enumeratee[JsObject, JsObject] = Enumeratee.filter[JsObject] {
    notification => notification.\("token").as[String] != token
  }
    
  val removeToken: Enumeratee[JsObject, JsObject] = Enumeratee.map[JsObject] {
    notification => Json.obj(
    		"fridgeName" -> notification.\("fridgeName"),
    		"command" -> notification.\("command"),
    		"payload" -> notification.\("payload"),
    		"timestamp" -> notification.\("timestamp")
    )
  }
  
  def toJsValue: Enumeratee[JsObject, JsValue] = Enumeratee.map[JsObject] {
    notification => notification
  }

  /**
   * Stream of server send events
   */
  def stream(fridgeName:String, token: String) = Action {
    println("Get Stream for fridge "+fridgeName+" with token "+token)
    Ok.stream(PomfNotificationService.getStream(fridgeName) &> filterNotToken(token) &> removeToken &> toJsValue ><> EventSource()).as("text/event-stream")
  }
}