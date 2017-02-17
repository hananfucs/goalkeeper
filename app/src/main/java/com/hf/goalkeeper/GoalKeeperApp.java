package com.hf.goalkeeper;

import android.app.Application;

/**
 * Created by hanan on 05/02/17.
 */

public class GoalKeeperApp extends Application {
    private Mapper mMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        createMapper();
    }

    private void createMapper() {
        mMapper = new Mapper();

        PlayerManager playerManager = new PlayerManager();
        mMapper.setValueForKey(PlayerManager.class, playerManager);

        StatisticsManager statisticsManager = new StatisticsManager();
        mMapper.setValueForKey(StatisticsManager.class, statisticsManager);
    }

    public Mapper getMapper() {
        return mMapper;
    }
}
