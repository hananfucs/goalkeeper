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
    private TimerThread currentGameThred;
    
    public int gameStatus() {
        return mGameState;
    }

    public TimeManager(GameContract.ViewHandler viewHandler) {
        mViewHandler = viewHandler;
    }

    @Override
    public void userStartedGame(int gameMinutes, int gameSeconds, int extMinutes, int extSeconds) {
        currentGameThred = new TimerThread(gameMinutes, gameSeconds);
        currentGameThred.start();
        mGameState = GAME_ONGOING;
    }

    @Override
    public void userPausedGame() {

    }

    @Override
    public void userResumedGame() {

    }

    @Override
    public void userEndedGame() {

    }

    private class TimerThread extends Thread{
        private int mSeconds;
        private int mMinutes;
        private boolean mPause;

        public TimerThread(int minutes, int seconds) {
            mMinutes = minutes;
            mSeconds = seconds;
        }

        public void pauseTime() {
            mPause = true;
        }

        public void resumeTime() {
            mPause = false;
        }

        @Override
        public void run() {
            while (!mPause) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mSeconds = mSeconds - 1;

                if (mSeconds == 0 && mMinutes == 0) {
                    Log.d("HHH", mMinutes + ":" + mSeconds);
                    return;
                }

                if (mSeconds == -1) {
                    mMinutes = mMinutes - 1;
                    mSeconds = 59;
                }
                Log.d("HHH", mMinutes + ":" + mSeconds);
            }
        }
    }

}
