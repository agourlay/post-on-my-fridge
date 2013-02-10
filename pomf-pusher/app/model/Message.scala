package model

import play.api.libs.iteratee.Enumerator
import java.io.File
import java.io.InputStream
import java.io.ByteArrayInputStream

case class Message (command :String, payload:String)

object MessageEnumerator{
  
  def messageStream() : Enumerator[Message] = {
    Enumerator(Seq(Message("message","lol"), Message("post delete","id 3")):_*)
  }
}