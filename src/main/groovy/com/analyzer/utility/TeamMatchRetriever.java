package com.analyzer.utility;

import com.riot.api.RiotApi;
import com.riot.dto.Match.Match;
import com.riot.dto.Match.MatchList;
import com.riot.dto.Match.Participant;
import com.riot.dto.Match.ParticipantIdentity;
import com.riot.exception.RiotApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TeamMatchRetriever
{
	private static Logger logger = LoggerFactory.getLogger(TeamMatchRetriever.class);

	// todo add number of games limit
	static public List<Match> getTeamMatches(RiotApi api, String accountId, int numOfGames,
											 long earliestTimeStampForGame, int queue, String topName,
											 String jungleName, String midName, String botName, String supportName)
	{
		MatchList matchList;
		Match match;
		String[] playerNames = {topName, jungleName, midName, botName, supportName};

		// used to store valid matches to return
		List<Match> matches = new ArrayList<>();

		try
		{
			int i = 0;
			boolean loop = true;
			while (loop)
			{
				// get matchlist by summoner name per 100
				matchList = api.getMatchListByAccountId(accountId, null, null,
						null, null, null, i * 100, null);

				if (matchList != null && matchList.getMatches().size() != 0)
				{
					// loop through matchList
					for (int j = 0; j < matchList.getMatches().size(); j++)
					{
						// if timestamp is older than the timestamp given, break
						if (matchList.getMatches().get(j).getTimestamp() < earliestTimeStampForGame)
						{
							logger.info("Hit timestamp older than the earliest time. Breaking loop." +
									"\nmatchList.getMatches().get(j).getTimestamp() = " + matchList.getMatches().get(j).getTimestamp());
							loop = false;
							break;
						}

						if (matchList.getMatches().get(j).getQueue() == queue)
						{
							if (queue == 400)
								logger.debug("index: " + j + " was a draft game!");
							else if (queue == 420)
								logger.debug("index: " + j + " was a ranked game!");
							else if (queue == 440)
								logger.debug("index: " + j + " was a ranked flex game!");

							match = api.getMatchByMatchId(matchList.getMatches().get(j).getGameId());

							List<Participant> participants = match.getParticipants();
							List<ParticipantIdentity> participantIdentities = match.getParticipantIdentities();

							if (allTeammatesPresentOnSameTeam(playerNames, participants, participantIdentities))
							{
								logger.info("Found team game for match id: " + matchList.getMatches().get(j).getGameId());
								matches.add(api.getMatchByMatchId(matchList.getMatches().get(j).getGameId()));
							}
							else
								logger.debug("Match not found for team");


						}
					}

					i++;
				}
				else
				{
					logger.info("matchList is empty or null!");
					break;
				}
			}
		}
		catch (RiotApiException e)
		{
			logger.error(e.getMessage());
			return null;
		}

		return matches;
	}

	private static boolean allTeammatesPresentOnSameTeam(String[] playerNames, List<Participant> participants, List<ParticipantIdentity> participantIdentities)
	{
		int teamId = 0;
		boolean playerTeamFound = false;
		boolean playerFound = false;

		// loop through each team name and make sure they all exist on the same team
		for (String playerName : playerNames)
		{
			for (ParticipantIdentity participantIdentity : participantIdentities)
			{
				if (participantIdentity.getPlayer().getSummonerName().equals(playerName))
				{
					logger.debug(playerName + " found!");
					playerFound = true;
					for (Participant participant : participants)
					{
						if (participant.getParticipantId() == participantIdentity.getParticipantId())
						{
							if (teamId == 0)
								teamId = participant.getTeamId();
							else if (participant.getTeamId() == teamId)
							{
								playerTeamFound = true;
								break;
							}
							else
							{
								logger.debug("Participant was on the other team!\nParticipan's team is " +
										participant.getTeamId() + " and teamId = " + teamId);
								return false;
							}
						}
					}
				}

				if (playerTeamFound)
				{
					playerTeamFound = false;
					break;
				}
			}

			if (!playerFound)
				return false;
			else
				playerFound = false;
		}

		return true;
	}
}
