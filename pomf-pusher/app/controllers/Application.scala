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

object Application extends Controller {
  
    val asJson: Enumeratee[Notification, JsValue] = Enumeratee.map[Notification] {
    notification => toJson ( Map (
      "fridgeId" -> toJson(notification.fridgeName),
      "command" -> toJson(notification.command),
      "user" -> toJson(notification.user),
      "message" -> toJson(notification.message),
      "timestamp" -> toJson(notification.timestamp)
    ) )
  }

  /**
   * Stream of server send events
   */
  def stream(fridgeName:String) = Action {
    Ok.stream(PomfNotificationService.getStream(fridgeName) &> asJson ><> EventSource()).as("text/event-stream")
  }
}