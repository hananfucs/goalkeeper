package com.hf.goalkeeper.viewes.support;

import com.hf.goalkeeper.viewes.dialogs.SettingsDialog;

/**
 * Created by hanan on 01/07/17.
 */

public interface SettingsContract {
    interface SettingsViewHandler {
        void updateSettingsDisplay();
        void openSettingPopup(SettingsDialog dialog);
    }

    interface SettingsUiActionsListener {
        void setViewHandler(SettingsViewHandler viewHandler);
        void openPopUp(SettingsDialog.DialogType type);
        void updateTimeSetting(SettingsDialog.DialogType type, int minutes, int seconds);
        void updateGoals(int numOfGoals);
        String getGameTime();
        String getExtTime();
        String getGoalsCount();
        void saveSettings();
    }
}
