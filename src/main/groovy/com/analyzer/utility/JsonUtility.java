package com.analyzer.utility;

import com.analyzer.dto.ChampionWinRate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class JsonUtility
{
	private static Logger logger = LoggerFactory.getLogger(JsonUtility.class);

	// accepts a player position map where position is the key, and name is the value
	public static String createTeamChampionWinRateJson(String topName, String jungleName, String midName, String botName, String supportName, Map<String, List<ChampionWinRate>> positionChampionWinRates)
	{
		JsonArray positions = new JsonArray();

		if (topName == null || positionChampionWinRates.get(topName) == null ||
				jungleName == null || positionChampionWinRates.get(jungleName) == null ||
				midName == null || positionChampionWinRates.get(midName) == null ||
				botName == null || positionChampionWinRates.get(botName) == null ||
				supportName == null || positionChampionWinRates.get(supportName) == null)
		{
			logger.error("A team member's name and/or champion's win rate is null!");
			return null;
		}

		// create top json
		JsonArray topStats = new JsonArray();
		for (ChampionWinRate championWinRate : positionChampionWinRates.get(topName))
		{
			BigDecimal gamesWon = BigDecimal.valueOf(championWinRate.getNumOfWins());
			BigDecimal gamesPlayed = BigDecimal.valueOf(championWinRate.getNumOfGames());
			topStats.add(championWinRate.getChampionName() + ": " +
					gamesWon.divide(gamesPlayed, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) +
					"% (" + gamesWon + "/" + gamesPlayed + ")");
		}

		JsonObject top = new JsonObject();
		top.addProperty("position", "Top");
		top.addProperty("name", topName);
		top.add("stats", topStats);

		positions.add(top);

		// create jungle json
		JsonArray jungleStats = new JsonArray();
		for (ChampionWinRate championWinRate : positionChampionWinRates.get(jungleName))
		{
			BigDecimal gamesWon = BigDecimal.valueOf(championWinRate.getNumOfWins());
			BigDecimal gamesPlayed = BigDecimal.valueOf(championWinRate.getNumOfGames());
			jungleStats.add(championWinRate.getChampionName() + ": " +
					gamesWon.divide(gamesPlayed, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) +
					"% (" + gamesWon + "/" + gamesPlayed + ")");
		}

		JsonObject jungle = new JsonObject();
		jungle.addProperty("position", "Jungle");
		jungle.addProperty("name", jungleName);
		jungle.add("stats", jungleStats);

		positions.add(jungle);

		// create mid json
		JsonArray midStats = new JsonArray();
		for (ChampionWinRate championWinRate : positionChampionWinRates.get(midName))
		{
			BigDecimal gamesWon = BigDecimal.valueOf(championWinRate.getNumOfWins());
			BigDecimal gamesPlayed = BigDecimal.valueOf(championWinRate.getNumOfGames());
			midStats.add(championWinRate.getChampionName() + ": " +
					gamesWon.divide(gamesPlayed, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) +
					"% (" + gamesWon + "/" + gamesPlayed + ")");
		}

		JsonObject mid = new JsonObject();
		mid.addProperty("position", "Mid");
		mid.addProperty("name", midName);
		mid.add("stats", midStats);

		positions.add(mid);

		// create bot json
		JsonArray botStats = new JsonArray();
		for (ChampionWinRate championWinRate : positionChampionWinRates.get(botName))
		{
			BigDecimal gamesWon = BigDecimal.valueOf(championWinRate.getNumOfWins());
			BigDecimal gamesPlayed = BigDecimal.valueOf(championWinRate.getNumOfGames());
			botStats.add(championWinRate.getChampionName() + ": " +
					gamesWon.divide(gamesPlayed, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) +
					"% (" + gamesWon + "/" + gamesPlayed + ")");
		}

		JsonObject bot = new JsonObject();
		bot.addProperty("position", "Bot");
		bot.addProperty("name", botName);
		bot.add("stats", botStats);

		positions.add(bot);

		// create support json
		JsonArray supportStats = new JsonArray();
		for (ChampionWinRate championWinRate : positionChampionWinRates.get(supportName))
		{
			BigDecimal gamesWon = BigDecimal.valueOf(championWinRate.getNumOfWins());
			BigDecimal gamesPlayed = BigDecimal.valueOf(championWinRate.getNumOfGames());
			supportStats.add(championWinRate.getChampionName() + ": " +
					gamesWon.divide(gamesPlayed, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) +
					"% (" + gamesWon + "/" + gamesPlayed + ")");
		}

		JsonObject support = new JsonObject();
		support.addProperty("position", "Support");
		support.addProperty("name", supportName);
		support.add("stats", supportStats);

		positions.add(support);

		JsonObject teamChampionWinRatesJson = new JsonObject();
		teamChampionWinRatesJson.add("positions", positions);

		return  teamChampionWinRatesJson.toString();
	}

	public static String createTeamChampionWinRateJsonMock()
	{
		JsonArray topStatsMock = new JsonArray();
		topStatsMock.add("Shen: 66% (4/2)");
		topStatsMock.add("Galio: 50% (1/1)");

		JsonObject top = new JsonObject();
		top.addProperty("position", "Top");
		top.addProperty("name", "RW0");
		top.add("stats", topStatsMock);

		JsonArray jungleStatsMock = new JsonArray();
		jungleStatsMock.add("Zac: 66% (4/2)");
		jungleStatsMock.add("Olaf: 0% (0/3)");

		JsonObject jungle = new JsonObject();
		jungle.addProperty("position", "Jungle");
		jungle.addProperty("name", "Zann Starfire");
		jungle.add("stats", jungleStatsMock);

		JsonArray midStatsMock = new JsonArray();
		midStatsMock.add("Orianna: 66% (4/2)");
		midStatsMock.add("Galio: 50% (1/1)");

		JsonObject mid = new JsonObject();
		mid.addProperty("position", "Mid");
		mid.addProperty("name", "jstocs5");
		mid.add("stats", midStatsMock);

		JsonArray botStatsMock = new JsonArray();
		botStatsMock.add("Senna: 100% (4/0)");
		botStatsMock.add("Caitlyn: 50% (1/1)");

		JsonObject bot = new JsonObject();
		bot.addProperty("position", "Bot");
		bot.addProperty("name", "Léttérs");
		bot.add("stats", botStatsMock);

		JsonArray supportStatsMock = new JsonArray();
		supportStatsMock.add("Nami: 60% (3/2)");
		supportStatsMock.add("Thresh: 50% (1/1)");

		JsonObject support = new JsonObject();
		support.addProperty("position", "Support");
		support.addProperty("name", "Biscuit Joint");
		support.add("stats", supportStatsMock);

		JsonArray positionsMock = new JsonArray();
		positionsMock.add(top);
		positionsMock.add(jungle);
		positionsMock.add(mid);
		positionsMock.add(bot);
		positionsMock.add(support);

		JsonObject teamChampionWinRatesJsonMock = new JsonObject();
		teamChampionWinRatesJsonMock.add("positions", positionsMock);

		return  teamChampionWinRatesJsonMock.toString();
	}
}
