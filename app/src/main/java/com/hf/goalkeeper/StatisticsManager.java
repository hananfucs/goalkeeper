package com.hf.goalkeeper;

import java.util.ArrayList;

/**
 * Created by hanan on 15/02/17.
 */

public class StatisticsManager {
    private ArrayList<Goal> currentWhiteGoals = new ArrayList<>();
    private ArrayList<Goal> currentBlackGoals = new ArrayList<>();

    public ArrayList<Goal> getBlackGoals() {
        return currentBlackGoals;
    }

    public ArrayList<Goal> getWhiteGoals() {
        return currentWhiteGoals;
    }

    public void goalScored(int team, PlayerManager.Player player, int minutes, int seconds) {
        Goal goal = new Goal();
        goal.minute = minutes;
        goal.second = seconds;
        goal.scorrer = player;

        if (team == PlayerManager.BLACK_TEAM)
            currentBlackGoals.add(goal);
        else if (team == PlayerManager.WHITE_TEAM)
            currentWhiteGoals.add(goal);
    }

    public ArrayList<Goal> getGoals(int mTeam) {
        if (mTeam == PlayerManager.BLACK_TEAM)
            return currentBlackGoals;
        else
            return currentWhiteGoals;
    }

    public static class Goal {
        public int minute;
        public int second;
        public PlayerManager.Player scorrer;
    }

    public class Match {
        public int matchSeconds;
        public int matchMinutes;
        public ArrayList<Goal> goals;
        public int winner;
    }
}
