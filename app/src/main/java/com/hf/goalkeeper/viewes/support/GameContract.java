package com.hf.goalkeeper.viewes.support;

import com.hf.goalkeeper.core.managers.StatisticsManager;

/**
 * Created by hanan on 11/02/17.
 */

public interface GameContract {

    interface ViewHandler {
        void updateMatchTime(int seconds);

        void matchStarted();
        void matchPaused();
        void matchEnded();
        void startedExtension();

        void goalScored(StatisticsManager.Goal goal);
        boolean shouldGoTOExtension();
    }

    interface UserActionsListener {
        void userStartedGame(int gameSeconds, int extSeconds, int matchGoals);
        void userPausedGame();
        void userResumedGame();
        void userStoppedGame();

        int getGameStatus();
    }
}
