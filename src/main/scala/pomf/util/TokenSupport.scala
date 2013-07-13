package pomf.util

import java.security.SecureRandom
import java.math.BigInteger

object TokenSupport {
 
  val  random :SecureRandom = new SecureRandom()
  def nextSessionId = new BigInteger(130, random).toString(32)
  
}