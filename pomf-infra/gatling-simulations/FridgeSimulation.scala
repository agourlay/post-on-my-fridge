
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

class FridgeSimulation extends Simulation {

	val httpConf = httpConfig
			.baseURL("http://fridge.arnaud-gourlay.info")
			.acceptHeader("application/json, text/javascript, */*; q=0.01")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
			.connection("keep-alive")
			.userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:20.0) Gecko/20100101 Firefox/20.0")


	val headers_1 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
			"Cache-Control" -> """max-age=0""",
			"If-Modified-Since" -> """Sun, 31 Mar 2013 09:55:21 GMT"""
	)

	val headers_2 = Map(
			"Cache-Control" -> """max-age=0""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_3 = Map(
			"Accept" -> """*/*""",
			"Cache-Control" -> """max-age=0""",
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


	val scn = scenario("Fridge test")
		.exec(http("index")
					.get("/")
					.headers(headers_1)
					.check(status.is(304))
			)
		.pause(2)
		.exec(http("retrieve chat history gatling1")
					.get("/api/message/gatling1")
					.headers(headers_2)
			)
		.pause(12 milliseconds)
		.exec(http("retrieve user token on gatling1")
					.get("/api/token/")
					.headers(headers_3)
			)
		.pause(19 milliseconds)
		.exec(http("retrieve fridge gatling1")
					.get("/api/fridge/gatling1")
					.headers(headers_3)
			)
		.pause(20 milliseconds)
		.exec(http("subscribe to fridge update")
				.get("/stream/gatling1/s7o4f9vbrbf1hgoabph1hq28ls")
				.headers(headers_8)
			)
		.pause(3)
		.exec(http("send chat message on gatling1")
					.post("/api/message/gatling1")
					.headers(headers_6)
					.queryParam("""token""", """s7o4f9vbrbf1hgoabph1hq28ls""")
						.body("""{"user":"joe","message":"first message on gatling1","timestamp":1365329980732}""").asJSON
			)
		.pause(3)
		.exec(http("send chat message 2 on gatling1")
					.post("/api/message/gatling1")
					.headers(headers_6)
					.queryParam("""token""", """s7o4f9vbrbf1hgoabph1hq28ls""")
						.body("""{"user":"joe","message":"second message on gatling1","timestamp":1365329984455}""").asJSON
			)
		.pause(4)
		.exec(http("search for fridge term ga")
					.get("/api/search/fridge/")
					.headers(headers_9)
					.queryParam("""term""", """ga""")
			)
		.pause(195 milliseconds)
		.exec(http("search for fridge term gat")
					.get("/api/search/fridge/")
					.headers(headers_9)
					.queryParam("""term""", """gat""")
			)
		.pause(1)
		.exec(http("retrieve chat history gatling2")
					.get("/api/message/gatling2")
					.headers(headers_9)
			)
		.pause(11 milliseconds)
		.exec(http("retrieve user token on gatling2")
					.get("/api/token/")
					.headers(headers_12)
			)
		.exec(http("retrieve fridge gatling2")
					.get("/api/fridge/gatling2")
					.headers(headers_12)
			)
		.pause(11 milliseconds)
		.exec(http("subscribe to fridge gatling2")
				.get("/stream/gatling1/s7o4f9vbrbf1hgoabph1hq29ls")
				.headers(headers_8)
			)
		.pause(6)
		.exec(http("send chat message on gatling2")
					.post("/api/message/gatling2")
					.headers(headers_6)
					.queryParam("""token""", """s7o4f9vbrbf1hgoabph1hq29ls""")
						.body("""{"user":"joe","message":"first message on gatling2","timestamp":1365330002982}""").asJSON
			)
		.pause(4)
		.exec(http("search for fridge term ga")
					.get("/api/search/fridge/")
					.headers(headers_9)
					.queryParam("""term""", """ga""")
			)
		.pause(786 milliseconds)
		.exec(http("search for fridge term gat")
					.get("/api/search/fridge/")
					.headers(headers_9)
					.queryParam("""term""", """gat""")
			)
		.pause(4)
		.exec(http("retrieve chat history gatling1")
					.get("/api/message/gatling1")
					.headers(headers_9)
			)
		.pause(786 milliseconds)
		.exec(http("retrieve user token on gatling1")
					.get("/api/token/")
					.headers(headers_12)
			)
		.pause(18 milliseconds)
		.exec(http("retrieve fridge gatling1")
					.get("/api/fridge/gatling1")
					.headers(headers_12)
			)

	setUp(scn.users(500).ramp(100).protocolConfig(httpConf))
}