package com.hf.goalkeeper.core.managers;

import java.util.ArrayList;

/**
 * Created by hanan on 15/02/17.
 */

public class StatisticsManager {
    private ArrayList<Goal> currentWhiteGoals = new ArrayList<>();
    private ArrayList<Goal> currentBlackGoals = new ArrayList<>();
    private TimeManager mTimeManager;

    public void setTimeManager(TimeManager timeManager) {
        mTimeManager = timeManager;
    }

    public ArrayList<Goal> getBlackGoals() {
        return currentBlackGoals;
    }

    public ArrayList<Goal> getWhiteGoals() {
        return currentWhiteGoals;
    }

    public void goalScored(int team, PlayerManager.Player player) {
        Goal goal = new Goal();
        goal.second = mTimeManager.getCurrentSecond()%60;
        goal.minute = mTimeManager.getCurrentSecond()/60;
        goal.isExtension = mTimeManager.isInExt();
        goal.scorrer = player;

        if (team == PlayerManager.BLACK_TEAM)
            currentBlackGoals.add(goal);
        else if (team == PlayerManager.WHITE_TEAM)
            currentWhiteGoals.add(goal);
        int maxNumOfGoals = Math.max(currentBlackGoals.size(), currentWhiteGoals.size());
        mTimeManager.goalScored(goal, maxNumOfGoals);
    }

    public void cancelGoal(int position, int team) {
        if (team == PlayerManager.WHITE_TEAM)
            currentWhiteGoals.remove(position);
        if (team == PlayerManager.BLACK_TEAM)
            currentBlackGoals.remove(position);
        int maxNumOfGoals = Math.max(currentBlackGoals.size(), currentWhiteGoals.size());
        mTimeManager.goalScored(null, maxNumOfGoals);

    }

    public ArrayList<Goal> getGoals(int mTeam) {
        if (mTeam == PlayerManager.BLACK_TEAM)
            return currentBlackGoals;
        else
            return currentWhiteGoals;
    }

    public void resetMatchGoals() {
        currentWhiteGoals.clear();
        currentBlackGoals.clear();
    }

    public static class Goal {
        public int minute;
        public int second;
        public PlayerManager.Player scorrer;
        public boolean isExtension;
    }

    public class Match {
        public int matchSeconds;
        public int matchMinutes;
        public ArrayList<Goal> goals;
        public int winner;
    }
}
