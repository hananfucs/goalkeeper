package com.hf.goalkeeper.core.managers;

import java.util.ArrayList;

/**
 * Created by hanan on 23/08/17.
 */

public interface DBManager {
    void saveMatch(StatisticsManager.Match match);
    StatisticsManager.Match getMatch(String matchId);
    ArrayList<StatisticsManager.Match> getMatchesForPlayer(String playerId);
}
