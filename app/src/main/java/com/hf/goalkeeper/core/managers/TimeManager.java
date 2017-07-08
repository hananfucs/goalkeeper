package com.hf.goalkeeper.core.managers;

import android.util.Log;

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
    private int mExtMinutes;
    private int mExtSeconds;

    private GameContract.ViewHandler mViewHandler;
    private int mGameState;
    private TimerThread currentGameThread;

    @Override
    public int getGameStatus() {
        return mGameState;
    }

    public TimeManager(GameContract.ViewHandler viewHandler) {
        mViewHandler = viewHandler;
    }

    @Override
    public void userStartedGame(int gameSeconds, int extMinutes, int extSeconds) {
        mViewHandler.matchStarted();
        mViewHandler.updateMatchTime(gameSeconds);

        mMatchSeconds= gameSeconds;
        mExtMinutes = extMinutes;
        mExtSeconds = extSeconds;

        currentGameThread = new TimerThread(gameSeconds);
        currentGameThread.start();
        mGameState = GAME_ONGOING;
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

    public void goalScored(StatisticsManager.Goal goal) {
        mViewHandler.goalScored(goal);
    }

    private class TimerThread extends Thread{
        private int mSeconds;
        private int mInitialTime;
        private boolean mPause;
        private boolean mOngoing;

        public TimerThread(int seconds) {
            mSeconds = seconds;
            mInitialTime = seconds;
            mOngoing = true;
        }

        public int getSecond() {
            return mInitialTime - mSeconds;
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

                mSeconds = mSeconds - 1;

                if (mSeconds == 0) {
                    mViewHandler.updateMatchTime(mSeconds);
                    return;
                }

                mViewHandler.updateMatchTime(mSeconds);
            }
        }
    }

}