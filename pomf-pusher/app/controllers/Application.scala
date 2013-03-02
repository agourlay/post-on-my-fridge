package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json.JsValue
import libs.json.Json._
import libs.EventSource
import model.Message
import play.api.libs.iteratee.Enumerator
import model.MessageEnumerator

object Application extends Controller {
  
    val asJson: Enumeratee[Message, JsValue] = Enumeratee.map[Message] {
    message => toJson ( Map (
      "command" -> toJson(message.command),
      "payload" -> toJson(message.payload)
    ) )
  }

  /**
   * Stream of server send events
   */
  def stream(fridgeName:String) = Action {
    Ok.stream(MessageEnumerator.messageStream &> asJson ><> EventSource()).as("text/event-stream")
  }
}