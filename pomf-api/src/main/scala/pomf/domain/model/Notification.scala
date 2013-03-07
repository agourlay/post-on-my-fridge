package pomf.domain.model

import spray.json.DefaultJsonProtocol

case class Notification(fridgeName : String, command :String, user : String, message : String, timestamp : Long ) 

case class ChatMessage(fridgeName : String, user : String, message : String ) 

// For later use with reverse rest on client ;)
object NotificationCmd extends Enumeration {
	type NotificationCmd = Value
	val Create = Value("Create")
	val Delete = Value("Delete") 
	val Update = Value("Update")
	val Message = Value("message") 
}