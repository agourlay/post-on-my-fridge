package pomf.service.caching

import akka.actor.Actor
import com.redis.RedisClient

class PomfCachingActor extends Actor with PomfCachingService {

  val r = new RedisClient("50.30.35.9", 2787)
  r.auth("24ae0c3b7f83ee6b550cc16b9fbed4a7")
   

  def receive = {
    case (key,value) => r.set(key, value); r.expire(key, 1000)
    case _ => println("received unknown message")
  }
  
}

trait PomfCachingService {

}