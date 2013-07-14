package pomf.domain.model

import pomf.util.XssFilter

case class ChatMessage(user : String, message : String, timestamp : Long ) {
  require(!user.isEmpty, "user must not be empty")
  require(!XssFilter.containsScript(user), "user must not contain script tags")
  require(!message.isEmpty, "message must not be empty")
  require(!XssFilter.containsScript(message), "message must not contain script tags")
}