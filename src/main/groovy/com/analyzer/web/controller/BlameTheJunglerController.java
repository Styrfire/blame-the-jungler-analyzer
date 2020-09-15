package com.analyzer.web.controller;

import com.analyzer.dto.ChampionWinRate;
import com.analyzer.service.StaticDataService;
import com.analyzer.utility.JsonUtility;
import com.analyzer.utility.TeamMatchRetriever;
import com.riot.api.RiotApi;
import com.riot.dto.Match.Match;
import com.riot.dto.Match.Participant;
import com.riot.dto.Summoner.Summoner;
import com.riot.exception.RiotApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
class BlameTheJunglerController
{
	@Value("${queue}")
	private int queue;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private RiotApi api;
	private Timestamp timestamp;

	private static Logger logger = LoggerFactory.getLogger(BlameTheJunglerController.class);

	@Inject
	BlameTheJunglerController(NamedParameterJdbcTemplate namedParameterJdbcTemplate, @Value("${timestamp.numOfWeeks:-1}") int numOfWeeksTimestamp)
	{
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

		try
		{
			// get jungler's match list (this can be any position, I just chose jungle because it's me!
			summoner = api.getSummonerByName(jungle);


			List<Match> teamMatches = TeamMatchRetriever.getTeamMatches(api, summoner.getAccountId(), 10, timestamp.getTime(), queue, top, jungle, mid, bot, support);
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

	@RequestMapping(method = RequestMethod.GET, value = "/teamChampionWinRates", produces = MediaType.APPLICATION_JSON_VALUE)
	String teamChampionWinRates(@RequestParam String top, @RequestParam String jungle, @RequestParam String mid,
					   @RequestParam String bot, @RequestParam String support)
	{
		logger.info("top = " + top);
		logger.info("jungle = " + jungle);
		logger.info("mid = " + mid);
		logger.info("bot = " + bot);
		logger.info("support = " + support);

		Summoner summoner;
		Map<String, List<ChampionWinRate>> positionChampionWinRates = new HashMap<>();
		int winningTeamId;
		String[] playerNames = {top, jungle, mid, bot, support};
		boolean championFound = false;

		StaticDataService staticDataService = new StaticDataService(namedParameterJdbcTemplate);

		try
		{
			// get jungler's match list (this can be any position, I just chose jungle because it's me!
			summoner = api.getSummonerByName(jungle);

			List<Match> teamMatches = TeamMatchRetriever.getTeamMatches(api, summoner.getAccountId(), 10, timestamp.getTime(), queue, top, jungle, mid, bot, support);

			if (teamMatches == null)
			{
				logger.error("teamMatches is null!");
				return null;
			}

			for (Match teamMatch : teamMatches)
			{
				if (teamMatch.getTeams().get(0).getWin().equals("Win"))
					winningTeamId = teamMatch.getTeams().get(0).getTeamId();
				else
					winningTeamId = teamMatch.getTeams().get(1).getTeamId();

				for (String playerName : playerNames)
				{
					Participant participant = TeamMatchRetriever.getPlayerParticipantInfo(playerName, teamMatch);
					if (participant == null)
					{
						logger.error("Participant info for " + playerName + " is null!");
						return null;
					}

					if (positionChampionWinRates.containsKey(playerName))
					{
						for (ChampionWinRate championWinRate : positionChampionWinRates.get(playerName))
						{
							if (championWinRate.getChampionName().equals(staticDataService.getChampionByKey(participant.getChampionId()).getName()))
							{
								championFound = true;
								championWinRate.setNumOfGames(championWinRate.getNumOfGames() + 1);
								if (participant.getTeamId() == winningTeamId)
									championWinRate.setNumOfWins(championWinRate.getNumOfWins() + 1);

								break;
							}
						}

						if (championFound)
						{
							championFound = false;
						}
						else
						{
							ChampionWinRate championWinRate = new ChampionWinRate();
							championWinRate.setChampionName(staticDataService.getChampionByKey(participant.getChampionId()).getName());
							championWinRate.setNumOfGames(1);
							if (participant.getTeamId() == winningTeamId)
								championWinRate.setNumOfWins(1);
							else
								championWinRate.setNumOfWins(0);
							positionChampionWinRates.get(playerName).add(championWinRate);
						}
					}
					else
					{
						ChampionWinRate championWinRate = new ChampionWinRate();
						championWinRate.setChampionName(staticDataService.getChampionByKey(participant.getChampionId()).getName());
						championWinRate.setNumOfGames(1);
						if (participant.getTeamId() == winningTeamId)
							championWinRate.setNumOfWins(1);
						else
							championWinRate.setNumOfWins(0);

						List<ChampionWinRate> championWinRates = new ArrayList<>();
						championWinRates.add(championWinRate);

						positionChampionWinRates.put(playerName, championWinRates);
					}
				}
			}
		}
		catch (RiotApiException e)
		{
			logger.error(e.getMessage());
			return e.getMessage();
		}

		String result = JsonUtility.createTeamChampionWinRateJson(top, jungle, mid, bot, support, positionChampionWinRates);
//		String result = JsonUtility.createTeamChampionWinRateJsonMock();
		logger.info("teamChampionWinRates JSON = " + result);

		return result;
	}
}
