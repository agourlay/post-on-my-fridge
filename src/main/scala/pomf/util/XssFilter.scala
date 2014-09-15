package pomf.util

object XssFilter {

  val patterns: List[String] = List("/>",
    "</script>",
    "javascript:",
    "vbscript:",
    "<META",
    "<object",
    "onmouseover",
    "onblur",
    "onfocus",
    "onclick",
    "onload")

  def containsScript(txt: String): Boolean = {
    patterns.exists(txt.contains(_))
  }
}