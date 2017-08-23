package com.hf.goalkeeper.core.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hanan on 15/02/17.
 */

public class StatisticsManager implements GameStatsHolder{
    private ArrayList<Goal> currentWhiteGoals = new ArrayList<>();
    private ArrayList<Goal> currentBlackGoals = new ArrayList<>();
    private TimeManager mTimeManager;
    private Match mCurrentMatch;

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
//        mCurrentMatch.goals.add(goal);
        int maxNumOfGoals = Math.max(currentBlackGoals.size(), currentWhiteGoals.size());
        mTimeManager.goalScored(goal, maxNumOfGoals);
    }

    @Override
    public void cancelGoal(int position, int team) {
        if (team == PlayerManager.WHITE_TEAM)
            currentWhiteGoals.remove(position);
        if (team == PlayerManager.BLACK_TEAM)
            currentBlackGoals.remove(position);
        int maxNumOfGoals = Math.max(currentBlackGoals.size(), currentWhiteGoals.size());
        mTimeManager.goalScored(null, maxNumOfGoals);

    }

    @Override
    public ArrayList<Goal> getGoals(int mTeam) {
        if (mTeam == PlayerManager.BLACK_TEAM)
            return currentBlackGoals;
        else
            return currentWhiteGoals;
    }

    private void resetMatchGoals() {
        currentWhiteGoals.clear();
        currentBlackGoals.clear();
    }

    public void matchStarted(ArrayList<PlayerManager.Player> blackTeam, ArrayList<PlayerManager.Player> whiteTeam) {
        resetMatchGoals();
        mCurrentMatch = new Match();
        mCurrentMatch.blackTeam = blackTeam;
        mCurrentMatch.whiteTeam = whiteTeam;
        mCurrentMatch.goals = new ArrayList<>();
        mCurrentMatch.matchDate = new Date(System.currentTimeMillis());
    }

    public void matchEndded() {
        mCurrentMatch.winner = getWinner();
        mCurrentMatch.matchLength = mTimeManager.getCurrentSecond();
        mCurrentMatch.goals.addAll(currentBlackGoals);
        mCurrentMatch.goals.addAll(currentWhiteGoals);

        //save Match

    }

    private int getWinner() {
        return currentWhiteGoals.size() > currentBlackGoals.size() ? PlayerManager.WHITE_TEAM : PlayerManager.BLACK_TEAM;
    }

    public Match getCurrentMatch() {
        return mCurrentMatch;
    }

    public static class Goal {
        public int minute;
        public int second;
        public PlayerManager.Player scorrer;
        public boolean isExtension;
    }

    public class Match implements Serializable{
        public ArrayList<PlayerManager.Player> blackTeam;
        public ArrayList<PlayerManager.Player> whiteTeam;
        public ArrayList<Goal> goals;
        public int winner;
        public Date matchDate;
        public int matchLength;
    }
}
