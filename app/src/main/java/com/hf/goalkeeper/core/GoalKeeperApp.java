package com.hf.goalkeeper.core;

import android.app.Application;

import com.hf.goalkeeper.core.managers.PlayerManager;
import com.hf.goalkeeper.core.managers.SettingsManager;
import com.hf.goalkeeper.core.managers.StatisticsManager;

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

        SettingsManager settingsManager = new SettingsManager(this);
        mMapper.setValueForKey(SettingsManager.class, settingsManager);
    }

    public Mapper getMapper() {
        return mMapper;
    }
}
