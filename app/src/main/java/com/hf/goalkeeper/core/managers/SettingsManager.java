package com.hf.goalkeeper.core.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hanan on 08/07/17.
 */

public class SettingsManager {
    private static final String SHARED_PREFS_NAME = "settings";
    private static final String SHARED_PREFS_GAME_MINUTES = "game_minutes";
    private static final String SHARED_PREFS_GAME_SECONDS= "game_seconds";
    private static final String SHARED_PREFS_EXT_MINUTES = "ext_minutes";
    private static final String SHARED_PREFS_EXT_SECONDS = "ext_seconds";
    private static final String SHARED_PREFS_GOALS = "goals";

    private Context mContext;

    private int gameMinutes;
    private int gameSeconds;
    private int extMinutes;
    private int extSeconds;
    private int gameGoals;

    public SettingsManager(Context context) {
        mContext = context;
        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sharedPref = mContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        gameMinutes = sharedPref.getInt(SHARED_PREFS_GAME_MINUTES, 8);
        gameSeconds = sharedPref.getInt(SHARED_PREFS_GAME_SECONDS, 0);
        extMinutes = sharedPref.getInt(SHARED_PREFS_EXT_MINUTES, 2);
        extSeconds = sharedPref.getInt(SHARED_PREFS_EXT_SECONDS, 0);
        gameGoals = sharedPref.getInt(SHARED_PREFS_GOALS, 2);
    }

    public void saveSettings() {
     SharedPreferences sharedPref = mContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedPref.edit();
     editor.putInt(SHARED_PREFS_GAME_MINUTES, gameMinutes);
     editor.putInt(SHARED_PREFS_GAME_SECONDS, gameSeconds);
     editor.putInt(SHARED_PREFS_EXT_MINUTES, extMinutes);
     editor.putInt(SHARED_PREFS_EXT_SECONDS, extSeconds);
     editor.putInt(SHARED_PREFS_GOALS, gameGoals);
     editor.apply();
    }

    public int getGameMinutes() {
        return gameMinutes;
    }

    public int getGameSeconds() {
        return gameSeconds;
    }

    public void setGameSeconds(int gameSeconds) {
        this.gameSeconds = gameSeconds;
    }

    public int getExtMinutes() {
        return extMinutes;
    }

    public void setExtMinutes(int extMinutes) {
        this.extMinutes = extMinutes;
    }

    public int getExtSeconds() {
        return extSeconds;
    }

    public void setExtSeconds(int extSeconds) {
        this.extSeconds = extSeconds;
    }

    public int getGameGoals() {
        return gameGoals;
    }

    public void setGameGoals(int gameGoals) {
        this.gameGoals = gameGoals;
    }

    public void setGameMinutes(int gameMinutes) {
        this.gameMinutes = gameMinutes;
    }
}
