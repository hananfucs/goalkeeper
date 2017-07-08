package com.hf.goalkeeper.viewes.support;

import com.hf.goalkeeper.core.managers.StatisticsManager;

/**
 * Created by hanan on 11/02/17.
 */

public interface GameContract {

    interface ViewHandler {
        void updateMatchTime(int minutes, int seconds);
        void updateExtTime(int minutes, int seconds);

        void matchStarted();
        void matchPaused();
        void matchEnded();

        void goalScored(StatisticsManager.Goal goal);
    }

    interface UserActionsListener {
        void userStartedGame(int gameMinutes, int gameSeconds, int extMinutes, int extSeconds);
        void userPausedGame();
        void userResumedGame();
        void userStoppedGame();

        int getGameStatus();
    }
}
