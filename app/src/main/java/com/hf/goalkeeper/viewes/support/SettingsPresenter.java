package com.hf.goalkeeper.viewes.support;

import android.os.Bundle;

import com.hf.goalkeeper.viewes.dialogs.SettingsDialog;
import com.hf.goalkeeper.core.Mapper;
import com.hf.goalkeeper.core.managers.SettingsManager;
import com.hf.goalkeeper.core.utils.StringUtils;

/**
 * Created by hanan on 01/07/17.
 */

public class SettingsPresenter implements SettingsContract.SettingsUiActionsListener {
    private final Mapper mMapper;
    private SettingsManager mSettingsManager;


    private SettingsContract.SettingsViewHandler mViewHandler;

    public SettingsPresenter(Mapper mapper) {
        mMapper = mapper;
        mSettingsManager = (SettingsManager) mMapper.getValueForKey(SettingsManager.class);
    }

    @Override
    public void setViewHandler(SettingsContract.SettingsViewHandler viewHandler) {
        mViewHandler = viewHandler;
    }

    @Override
    public void openPopUp(SettingsDialog.DialogType type) {
        SettingsDialog settingsDialog = new SettingsDialog();
        Bundle args = new Bundle();
        args.putSerializable(SettingsDialog.DIALOG_TYPE, type);
        args.putInt(SettingsDialog.GAME_MINUTES, mSettingsManager.getGameMinutes());
        args.putInt(SettingsDialog.GAME_SECONDS, mSettingsManager.getGameSeconds());
        args.putInt(SettingsDialog.EXT_MINUTES, mSettingsManager.getExtMinutes());
        args.putInt(SettingsDialog.EXT_SECONDS, mSettingsManager.getExtSeconds());
        args.putInt(SettingsDialog.GAME_GOALS, mSettingsManager.getGameGoals());

        settingsDialog.setArguments(args);
        settingsDialog.setListener(this);
        mViewHandler.openSettingPopup(settingsDialog);
    }

    @Override
    public void updateTimeSetting(SettingsDialog.DialogType type, int minutes, int seconds) {
        SettingsManager settingsManager = (SettingsManager) mMapper.getValueForKey(SettingsManager.class);
        if (type == SettingsDialog.DialogType.MATCH_TIME) {
            settingsManager.setGameMinutes(minutes);
            settingsManager.setGameSeconds(seconds);
            mViewHandler.updateSettingsDisplay();
        } else {
            settingsManager.setExtMinutes(minutes);
            settingsManager.setExtSeconds(seconds);
            mViewHandler.updateSettingsDisplay();
        }
    }

    @Override
    public void updateGoals(int numOfGoals) {
        SettingsManager settingsManager = (SettingsManager) mMapper.getValueForKey(SettingsManager.class);
        settingsManager.setGameGoals(numOfGoals);
        mViewHandler.updateSettingsDisplay();
    }

    @Override
    public String getGameTime() {
        return StringUtils.getFormatedTime(mSettingsManager.getGameMinutes(), mSettingsManager.getGameSeconds());
    }

    @Override
    public String getExtTime() {
        return StringUtils.getFormatedTime(mSettingsManager.getExtMinutes(), mSettingsManager.getExtSeconds());
    }

    @Override
    public String getGoalsCount() {
        return String.valueOf(mSettingsManager.getGameGoals());
    }

    @Override
    public void saveSettings() {
        mSettingsManager.saveSettings();
    }
}
