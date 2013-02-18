package pomf.service.messaging

import org.junit.runner.RunWith
import org.specs2.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProducerSpec extends Specification with PomfMessagingService {
  def is =
    "Producer should:" ^
      p ^
      "Setup connection"
      "connection ?"  ^
      end
 
}