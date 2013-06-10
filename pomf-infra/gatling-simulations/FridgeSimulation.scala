
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


	val headers_1 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
			"Cache-Control" -> """max-age=0"""
	)

	val headers_2 = Map(
			"Cache-Control" -> """max-age=0""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_3 = Map(
			"Accept" -> """*/*""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_5 = Map(
			"Accept" -> """image/png,image/*;q=0.8,*/*;q=0.5"""
	)

	val headers_6 = Map(
			"Accept" -> """text/plain, */*; q=0.01""",
			"Cache-Control" -> """no-cache""",
			"Content-Type" -> """application/json; charset=UTF-8""",
			"Pragma" -> """no-cache""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_8 = Map(
			"Accept" -> """text/event-stream""",
			"Cache-Control" -> """no-cache""",
			"Pragma" -> """no-cache"""
	)

	val headers_9 = Map(
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_12 = Map(
			"Accept" -> """*/*""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_13 = Map(
			"Cache-Control" -> """no-cache""",
			"Content-Type" -> """application/json; charset=UTF-8""",
			"Pragma" -> """no-cache""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_14 = Map(
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	def retrieveAndSetUserToken = http("retrieve user token on ${fridge_id}")
						         .get("/api/token/")
						         .headers(headers_3)
						         .check(bodyString.saveAs("user_token"))

	def createPostOnFridge = http("create post on ${fridge_id}")
							.post("/api/post/")
							.headers(headers_13)
							.queryParam("""token""", """${user_token}""")
							.body("""{"author":"${user_name}","content":"New post -> edit me with a double click!","color":"#f7f083","positionX":0.5,"positionY":0.04,"fridgeId":"${fridge_id}","date":"2013-04-08T12:40:48"}""")
							.check(jsonPath("id")saveAs("post_id"))

    def suscribeToNotification = http("subscribe to fridge ${fridge_id}")
								 .get("/stream/${fridge_id}/${user_token}")
								 .headers(headers_8)

	def retrieveChatHistory = http("retrieve chat history ${fridge_id}")
					.get("/api/message/${fridge_id}")
					.headers(headers_2)

	def retrieveFridgeContent = http("retrieve fridge ${fridge_id}")
					.get("/api/fridge/${fridge_id}")
					.headers(headers_3)			

	def sendMessage = http("send chat message on ${fridge_id}")
					.post("/api/message/${fridge_id}")
					.headers(headers_6)
					.queryParam("""token""", """${user_token}""")
					.body("""{"user":"${user_name}","message":"Hello from Gatling blasting fridge ${fridge_id}","timestamp":1365329980732}""")

	def searchForFridge = http("search for fridge term ${next_fridge_id}")
					.get("/api/search/fridge/")
					.headers(headers_9)
					.queryParam("""term""", """${next_fridge_id}""")

	def movePostAround = http("moving post ${post_id} on fridge ${fridge_id}")
					.put("/api/post/")
					.headers(headers_3)
					.queryParam("""token""", """${user_token}""")
					.body("""{"author":"Anonymous","content":"New post -> edit me with a double click!","color":"#f7f083","positionX":0.8627935723114957,"positionY":0.04,"fridgeId":"${fridge_id}","id":${post_id},"date":"2013-04-08T12:40:48"}""")
					.asJSON

	def changePostColor = http("change post ${post_id} color on fridge ${fridge_id}")
					.put("/api/post/")
					.headers(headers_3)
					.queryParam("""token""", """${user_token}""")
					.body("""{"author":"Anonymous","content":"New post -> edit me with a double click!","color":"#8386f8","positionX":0.8627935723114957,"positionY":0.04,"fridgeId":"${fridge_id}","date":"2013-04-08T12:40:48","id":${post_id}}""")
					.asJSON

	def changePostContent = http("change post ${post_id} content on fridge ${fridge_id}")
					.put("/api/post/")
					.headers(headers_3)
					.queryParam("""token""", """${user_token}""")
					.body("""{"author":"Anonymous","content":"Gatling rocks!","color":"#8386f8","positionX":0.8627935723114957,"positionY":0.04,"fridgeId":"${fridge_id}","date":"2013-04-08T12:40:48","id":${post_id}}""")
					.asJSON
			

	def deletePost = http("delete post ${post_id} from fridge ${fridge_id}")
					.delete("/api/post/${post_id}")
					.headers(headers_14)
					.queryParam("""token""", """${user_token}""")


	val fridgeChoice = Array("gatling1","gatling2")
	val rand = new Random(System.currentTimeMillis())

	val scn = scenario("Fridge use case")
	    .exec(session => session.setAttribute("fridge_id", fridgeChoice(rand.nextInt(fridgeChoice.length))))
	    .pause(10 milliseconds)
	    .exec(session => session.setAttribute("user_name","Joe"))
	    .pause(10 milliseconds)
	    .exec(session => session.setAttribute("next_fridge_id", fridgeChoice(rand.nextInt(fridgeChoice.length))))
	    .pause(10 milliseconds)
	    .exec(retrieveAndSetUserToken)
	    .pause(20 milliseconds)
		.exec(retrieveChatHistory)
		.pause(20 milliseconds)
		//.exec(suscribeToNotification)
		.pause(20 milliseconds)
		.exec(retrieveFridgeContent)
		.pause(2)
		.exec(createPostOnFridge)
		.pause(2)
        .exec(sendMessage)
		.pause(2)
		.exec(movePostAround)
		.pause(2)
		.exec(changePostColor)
		.pause(2)
		.exec(changePostContent)
		.pause(2)
		.exec(deletePost)
		.pause(2)		
		.exec(searchForFridge)

	setUp(scn.users(150).ramp(10).protocolConfig(httpConf))
}