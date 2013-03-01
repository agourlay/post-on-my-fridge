package pomf.util

import pomf.domain.model.FridgeRest
import pomf.domain.model.Post

object RssSupport {

   def generateFridgeRss(fridge: FridgeRest): scala.xml.Elem = {
      val myRss = <rss version="2.0">
  <channel>
  <title>{fridge.name}</title>
  <description>{fridge.description}</description>
  <link>http://www.example.com/rss</link>
  <lastBuildDate>Mon, 05 Oct 2012 11:12:55</lastBuildDate>
  <pubDate>Tue, 06 Oct 2012 09:00:00 +0100</pubDate>
  {
   for (post <- fridge.posts) yield
   {
     <item>
     <title>{post.fridgeId}</title>
     <description>{post.content}</description>
     <link>http://www.example.com/item</link>
     <guid isPermaLink="false">123</guid>
     <author>{post.author}</author>
     <pubDate>{post.date}</pubDate>
     </item>
	}
  }
  </channel>
  </rss>
   myRss					 
   }
  
}