package com.analyzer.web.controller;

import com.analyzer.utility.TeamMatchRetriever;
import com.google.gson.Gson;
import com.riot.api.RiotApi;
import com.riot.dto.Match.Match;
import com.riot.dto.Match.MatchList;
import com.riot.dto.Match.MatchTimeline;
import com.riot.dto.Summoner.Summoner;
import com.riot.exception.RiotApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
class BlameTheJunglerController
{
	@Value("${queue}")
	private int queue;

	private RiotApi api;
	private Timestamp timestamp;

	private static Logger logger = LoggerFactory.getLogger(BlameTheJunglerController.class);

	@Inject
	BlameTheJunglerController(@Value("${timestamp.numOfWeeks:-1}") int numOfWeeksTimestamp)
	{
		this.api = new RiotApi(System.getProperty("api.key"));
		logger.info("numOfWeeksTimestamp = " + numOfWeeksTimestamp);

		if (numOfWeeksTimestamp == -1)
			this.timestamp = new Timestamp(1578488400000L); // timestamp from beginning of season
		else
			this.timestamp = new Timestamp(System.currentTimeMillis() - (604800000L*numOfWeeksTimestamp)); // timestamp from numOfWeeks ago
	}

	@RequestMapping("/")
	String helloWorld()
	{
		return "Hello world from the Blame the Jungler Analyzer!";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/playerStats", produces = MediaType.APPLICATION_JSON_VALUE)
	String playerStats(@RequestParam String top, @RequestParam String jungle, @RequestParam String mid,
					   @RequestParam String bot, @RequestParam String support)
	{
		logger.info("top = " + top);
		logger.info("jungle = " + jungle);
		logger.info("mid = " + mid);
		logger.info("bot = " + bot);
		logger.info("support = " + support);

		Summoner summoner;
		MatchList matchList;

		try
		{
			// get jungler's match list (this can be any position, I just chose jungle because it's me!
			summoner = api.getSummonerByName(jungle);


			List<Match> teamMatches = TeamMatchRetriever.getTeamMatches(api, summoner.getAccountId(), 10, timestamp.getTime(), 440, top, jungle, mid, bot, support);
		}
		catch (RiotApiException e)
		{
			logger.error(e.getMessage());
			return e.getMessage();
		}
		return "{\n" +
				"\t\"positions\": [\n" +
				"\t\t{\n" +
				"\t\t\t\"position\":\"Top\",\n" +
				"\t\t\t\"name\":\"RW0\",\n" +
				"\t\t\t\"stats\":\n" +
				"\t\t\t[\n" +
				"\t\t\t\t\"KDA: 6.0 (4/2/8)\",\n" +
				"\t\t\t\t\"KP: 68%\",\n" +
				"\t\t\t\t\"DMG/M: 324\",\n" +
				"\t\t\t\t\"CSD @ 15: +1.2\",\n" +
				"\t\t\t\t\"Solo K: 2\",\n" +
				"\t\t\t\t\"ISO D: 0\"\n" +
				"\t\t\t]\n" +
				"\t\t},\n" +
				"\t\t{\n" +
				"\t\t\t\"position\":\"Jungle\",\n" +
				"\t\t\t\"name\":\"Zann Starfire\",\n" +
				"\t\t\t\"stats\":\n" +
				"\t\t\t[\n" +
				"\t\t\t\t\"KDA: 6.0 (4/2/8)\",\n" +
				"\t\t\t\t\"KP: 68%\",\n" +
				"\t\t\t\t\"DMG %: 11%\",\n" +
				"\t\t\t\t\"FBP: 44%\",\n" +
				"\t\t\t\t\"K+A @ 15: +2.2\",\n" +
				"\t\t\t\t\"GD @ 15: 0.7k\",\n" +
				"\t\t\t\t\"XP D @ 15: 0.4k\"\n" +
				"\t\t\t]\n" +
				"\t\t},\n" +
				"\t\t{\n" +
				"\t\t\t\"position\":\"Mid\",\n" +
				"\t\t\t\"name\":\"jstocs5\",\n" +
				"\t\t\t\"stats\":\n" +
				"\t\t\t[\n" +
				"\t\t\t\t\"KDA: 6.0 (4/2/8)\",\n" +
				"\t\t\t\t\"KP: 68%\",\n" +
				"\t\t\t\t\"DMG/M: 324\",\n" +
				"\t\t\t\t\"CSD @ 15: +1.2\",\n" +
				"\t\t\t\t\"Solo K: 2\",\n" +
				"\t\t\t\t\"ISO D: 0\"\n" +
				"\t\t\t]\n" +
				"\t\t},\n" +
				"\t\t{\n" +
				"\t\t\t\"position\":\"Bot\",\n" +
				"\t\t\t\"name\":\"Léttérs\",\n" +
				"\t\t\t\"stats\":\n" +
				"\t\t\t[\n" +
				"\t\t\t\t\"KDA: 6.0 (4/2/8)\",\n" +
				"\t\t\t\t\"KP: 68%\",\n" +
				"\t\t\t\t\"DMG/M: 524\",\n" +
				"\t\t\t\t\"CSD @ 15: +1.2\"\n" +
				"\t\t\t]\n" +
				"\t\t},\n" +
				"\t\t{\n" +
				"\t\t\t\"position\":\"Support\",\n" +
				"\t\t\t\"name\":\"Biscuit Joint\",\n" +
				"\t\t\t\"stats\":\n" +
				"\t\t\t[\n" +
				"\t\t\t\t\"KDA: 6.0 (4/2/8)\",\n" +
				"\t\t\t\t\"KP: 68%\",\n" +
				"\t\t\t\t\"VS/M: 1.1\",\n" +
				"\t\t\t\t\"ISO D: 4\"\n" +
				"\t\t\t]\n" +
				"\t\t}\n" +
				"\t]\n" +
				"}";
	}
}
