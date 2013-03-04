package pomf.service.caching

import akka.actor.Actor
import com.redis.RedisClient
import scala.concurrent._
import ExecutionContext.Implicits.global
import pomf.domain.config.DBConfig


trait PomfCachingService  {
  
 this: DBConfig =>
  def putInCache(key: String, value : Any):Future[Boolean] = future{
    cache.set(key, value)
    cache.expire(key, 1000)
  }
  
  def getFromCache(key: String) = future{
    cache.get(key)
  }
  
}