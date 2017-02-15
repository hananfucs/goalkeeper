package com.hf.goalkeeper;

import android.util.Log;

/**
 * Created by hanan on 11/02/17.
 */

public class TimeManager implements GameContract.UserActionsListener{
    public static final int GAME_NOT_STARTED = 0;
    public static final int GAME_ONGOING = 1;
    public static final int GAME_IN_EXTENSION = 2;
    public static final int GAME_PAUSED= 3;

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
    public void userStartedGame(int gameMinutes, int gameSeconds, int extMinutes, int extSeconds) {
        mViewHandler.matchStarted();
        mViewHandler.updateMatchTime(gameMinutes, gameSeconds);
        currentGameThread = new TimerThread(gameMinutes, gameSeconds);
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
    }

    private class TimerThread extends Thread{
        private int mSeconds;
        private int mMinutes;
        private boolean mPause;
        private boolean mOngoing;

        public TimerThread(int minutes, int seconds) {
            mMinutes = minutes;
            mSeconds = seconds;
            mOngoing = true;
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

                if (mSeconds == 0 && mMinutes == 0) {
                    Log.d("HHH", mMinutes + ":" + mSeconds);
                    mViewHandler.updateMatchTime(mMinutes, mSeconds);
                    return;
                }

                if (mSeconds == -1) {
                    mMinutes = mMinutes - 1;
                    mSeconds = 59;
                }
                mViewHandler.updateMatchTime(mMinutes, mSeconds);
            }
        }
    }

}
