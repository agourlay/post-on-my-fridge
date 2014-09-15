package pomf.configuration

import java.io.File

trait Configuration {
  val externalConfPath = "../conf/pomf.conf"
  if (new File(externalConfPath).exists()) System.setProperty("config.file", externalConfPath)
}