package com.analyzer.utility

import com.riot.api.RiotApi
import com.riot.dto.Match.Match
import org.junit.Ignore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import java.sql.Timestamp

class TeamMatchRetrieverIntegrationTest extends Specification
{
	private static logger = LoggerFactory.getLogger(TeamMatchRetrieverIntegrationTest.class)

	@Ignore
	def "test getTeamMatches"()
	{
		given:
			RiotApi api = new RiotApi(System.getProperty("api.key"))
			Timestamp timestamp
			int numOfWeeksTimestamp = 5

			if (numOfWeeksTimestamp == -1)
				timestamp = new Timestamp(1578488400000L) // timestamp from beginning of season
			else
				timestamp = new Timestamp(System.currentTimeMillis() - (604800000L*numOfWeeksTimestamp)) // timestamp from numOfWeeks ago

		when:
			List<Match> matches = TeamMatchRetriever.getTeamMatches(api, "cPkWNSpBp7c3IdQi712Zp9TfDyn27rn20EpbgGVPs3HCvb4", 10, timestamp.getTime(), 440, "RW0", "Zann Starfire", "jstocs5", "Léttérs", "Biscuit Joint")
		then:
			logger.info(matches.size() + " games found!")
			matches != null
	}
}
