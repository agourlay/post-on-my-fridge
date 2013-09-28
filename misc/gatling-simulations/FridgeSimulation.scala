import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._
import java.util.Random

class FridgeSimulation extends Simulation {

	val httpConf = httpConfig
			.baseURL("http://fridge.arnaud-gourlay.info")
			.acceptHeader("text/plain,*/*; q=0.01")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("en-US,en;q=0.4")
			.connection("keep-alive")
			.userAgentHeader("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0")

	val headers_req = Map(
			"Cache-Control" -> """no-cache""",
			"Content-Type" -> """application/json; charset=UTF-8""",
			"Pragma" -> """no-cache""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_SSE = Map(
			"Accept" -> """text/event-stream""",
			"Cache-Control" -> """no-cache""",
			"Pragma" -> """no-cache"""
	)

	def getAllFridges = http("retrieve all fridges")
					    .get("/fridges/")
						.headers(headers_req)

	def retrieveAndSetUserToken = http("retrieve user token on ${fridge_id}")
						         .get("/token/")
						         .headers(headers_req)
						         .check(bodyString.saveAs("user_token"))

	def createPostOnFridge = http("create post on ${fridge_id}")
							.post("/posts/")
							.headers(headers_req)
							.queryParam("""token""", """${user_token}""")
							.body("""{"author":"${user_name}","content":"New post -> edit me with a double click!","color":"#f7f083","positionX":0.5,"positionY":0.04,"fridgeId":"${fridge_id}","date":"2013-04-08T12:40:48"}""")
							.check(jsonPath("id")saveAs("post_id"))

	def suscribeToNotification = http("subscribe to fridge ${fridge_id}")
								 .get("/stream/fridge/${fridge_id}?token=${user_token}")
								 .headers(headers_SSE)

	def retrieveChatHistory = http("retrieve chat history ${fridge_id}")
					.get("/messages/${fridge_id}")
					.headers(headers_req)

	def retrieveFridgeContent = http("retrieve fridge ${fridge_id}")
					.get("/fridges/${fridge_id}")
					.headers(headers_req)			

	def sendMessage = http("send chat message on ${fridge_id}")
					.post("/messages/${fridge_id}")
					.headers(headers_req)
					.queryParam("""token""", """${user_token}""")
					.body("""{"user":"${user_name}","message":"Hello from Gatling blasting fridge ${fridge_id}","timestamp":1365329980732}""")

	def searchForFridge = http("search for fridge term ${next_fridge_id}")
					.get("/search/fridge/")
					.headers(headers_req)
					.queryParam("""term""", """${next_fridge_id}""")

	def movePostAround = http("moving post ${post_id} on fridge ${fridge_id}")
					.put("/posts/")
					.headers(headers_req)
					.queryParam("""token""", """${user_token}""")
					.body("""{"author":"Anonymous","content":"New post -> edit me with a double click!","color":"#f7f083","positionX":0.8627935723114957,"positionY":0.04,"fridgeId":"${fridge_id}","id":${post_id},"date":"2013-04-08T12:40:48"}""")
					.asJSON

	def changePostColor = http("change post ${post_id} color on fridge ${fridge_id}")
					.put("/posts/")
					.headers(headers_req)
					.queryParam("""token""", """${user_token}""")
					.body("""{"author":"Anonymous","content":"New post -> edit me with a double click!","color":"#8386f8","positionX":0.8627935723114957,"positionY":0.04,"fridgeId":"${fridge_id}","date":"2013-04-08T12:40:48","id":${post_id}}""")
					.asJSON

	def changePostContent = http("change post ${post_id} content on fridge ${fridge_id}")
					.put("/posts/")
					.headers(headers_req)
					.queryParam("""token""", """${user_token}""")
					.body("""{"author":"Anonymous","content":"Gatling rocks!","color":"#8386f8","positionX":0.8627935723114957,"positionY":0.04,"fridgeId":"${fridge_id}","date":"2013-04-08T12:40:48","id":${post_id}}""")
					.asJSON
			

	def deletePost = http("delete post ${post_id} from fridge ${fridge_id}")
					.delete("/posts/${post_id}")
					.headers(headers_req)
					.queryParam("""token""", """${user_token}""")

	val fridgeChoice = Array("gatling1","gatling2","gatling3")

	val rand = new Random(System.currentTimeMillis())

	val scn = scenario("Some users having fun")
		.exec(getAllFridges)
		.pause(2 seconds)
	    .exec(session => session.setAttribute("fridge_id", fridgeChoice(rand.nextInt(fridgeChoice.length))))
	    .exec(session => session.setAttribute("user_name","Joe"))
	    .exec(session => session.setAttribute("next_fridge_id", fridgeChoice(rand.nextInt(fridgeChoice.length))))
	    .exec(retrieveAndSetUserToken)
	    .pause(2 seconds)
	    .exec(retrieveChatHistory)
	    .pause(2 seconds)
	    //.exec(suscribeToNotification)
	    .pause(2 seconds)
	    .exec(retrieveFridgeContent)
	    .pause(2 seconds)
	    .exec(createPostOnFridge)
	    .pause(2 seconds)
	    .exec(sendMessage)
	    .pause(2 seconds)
	    .exec(movePostAround)
	    .pause(2 seconds)
	    .exec(changePostColor)
	    .pause(2 seconds)
	    .exec(changePostContent)
	    .pause(2 seconds)
	    .exec(deletePost)
	    .pause(2 seconds)		
	    .exec(searchForFridge)

	setUp(scn.users(150).ramp(2).protocolConfig(httpConf))
}
