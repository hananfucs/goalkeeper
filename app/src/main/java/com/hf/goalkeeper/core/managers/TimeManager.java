package com.hf.goalkeeper.core.managers;

import com.hf.goalkeeper.viewes.support.GameContract;

/**
 * Created by hanan on 11/02/17.
 */

public class TimeManager implements GameContract.UserActionsListener {
    public static final int GAME_NOT_STARTED = 0;
    public static final int GAME_ONGOING = 1;
    public static final int GAME_IN_EXTENSION = 2;
    public static final int GAME_PAUSED= 3;

    private int mMatchSeconds;
    private int mMatchGoals;

    private GameContract.ViewHandler mViewHandler;
    private int mGameState = GAME_NOT_STARTED;
    private TimerThread currentGameThread;

    @Override
    public int getGameStatus() {
        return mGameState;
    }

    public TimeManager(GameContract.ViewHandler viewHandler) {
        mViewHandler = viewHandler;
    }

    @Override
    public void userStartedGame(int gameSeconds, int extSeconds, int matchGoals) {
        mMatchSeconds= gameSeconds;
        mMatchGoals = matchGoals;
        if (mGameState == GAME_NOT_STARTED) {
            currentGameThread = new TimerThread(gameSeconds);
            currentGameThread = new TimerThread(gameSeconds, extSeconds);
            currentGameThread.start();
            mGameState = GAME_ONGOING;
            mViewHandler.matchStarted();
            mViewHandler.updateMatchTime(gameSeconds);
        } else {
            mViewHandler.updateMatchTime(mMatchSeconds - currentGameThread.getSecond());
        }
    }

    @Override
    public void userPausedGame() {
        if (currentGameThread != null)
            currentGameThread.pauseTime();
        mGameState = GAME_PAUSED;
        mViewHandler.matchPaused();
    }

    @Override
    public void userResumedGame() {
        if (currentGameThread != null)
            currentGameThread.resumeTime();
        mGameState = GAME_ONGOING;
        mViewHandler.matchStarted();
    }

    @Override
    public void userStoppedGame() {
        if (currentGameThread != null)
            currentGameThread.stopGame();
        mViewHandler.matchEnded();
        mGameState = GAME_NOT_STARTED;
        currentGameThread = null;
    }

    public int getCurrentSecond() {
        return currentGameThread.getSecond();
    }

    public boolean isInExt() {
        return false;
    }

    public void goalScored(StatisticsManager.Goal goal, int maxNumOfGoals) {
        mViewHandler.goalScored(goal);
        if (maxNumOfGoals == mMatchGoals || mGameState == GAME_IN_EXTENSION) {
            userStoppedGame();
        }
    }

    private class TimerThread extends Thread{
        private int mMatchSeconds;
        private int mMatchInitialTime;

        private int mExtSeconds;
        private int mExtInitialTime;

        private boolean mPause;
        private boolean mOngoing;

        public TimerThread(int seconds) {
            mMatchSeconds = seconds;
            mMatchInitialTime = seconds;
            mOngoing = true;
        }

        public TimerThread(int matchSeconds, int extSeconds) {
            mMatchSeconds = matchSeconds;
            mMatchInitialTime = matchSeconds;
            mExtSeconds = extSeconds;
            mExtInitialTime = extSeconds;
            mOngoing = true;
        }

        public int getSecond() {
            if (mGameState == GAME_IN_EXTENSION)
                return mMatchInitialTime + mExtInitialTime - mMatchSeconds;
            else
                return mMatchInitialTime - mMatchSeconds;
        }

        public void pauseTime() {
            mPause = true;
        }

        public void resumeTime() {
            mPause = false;
        }

        public void stopGame() {
            mOngoing = false;
        }

        @Override
        public void run() {
            while (mOngoing) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (mPause) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mMatchSeconds = mMatchSeconds - 1;

                if (mMatchSeconds == 0) {
                    mViewHandler.updateMatchTime(mMatchSeconds);
                    if (!mViewHandler.shouldGoTOExtension())
                        userStoppedGame();
                    if (mExtSeconds != 0) {
                        mGameState = GAME_IN_EXTENSION;
                        mMatchSeconds = mExtInitialTime;
                        mExtSeconds = 0;
                    }
                    else {
                        //finish game
                        return;
                    }
                    mViewHandler.startedExtension();
                    continue;
                }

                mViewHandler.updateMatchTime(mMatchSeconds);
            }
        }
    }

}
