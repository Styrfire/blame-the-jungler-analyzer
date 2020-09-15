package com.analyzer.utility

import org.junit.Ignore
import org.slf4j.LoggerFactory
import spock.lang.Specification

class JsonUtilityTest extends Specification
{
	private static logger = LoggerFactory.getLogger(TeamMatchRetrieverIntegrationTest.class)

	@Ignore
	def "test createTeamChampionWinRateJsonMock"()
	{
		given:

		when:
			String json = JsonUtility.createTeamChampionWinRateJsonMock()
		then:
			logger.info(json)
	}
}
