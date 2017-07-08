package com.hf.goalkeeper.viewes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.viewes.support.SettingsContract;
import com.hf.goalkeeper.viewes.dialogs.SettingsDialog;
import com.hf.goalkeeper.viewes.support.SettingsPresenter;
import com.hf.goalkeeper.core.GoalKeeperApp;

/**
 * Created by hanan on 26/06/17.
 */

public class GameSettingsActivity extends AppCompatActivity implements SettingsContract.SettingsViewHandler {
    private TextView mMatchTime;
    private TextView mExtTime;
    private TextView mGoalsCount;

    private SettingsContract.SettingsUiActionsListener mSettingsActionListener;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_match_settings_new);
        mSettingsActionListener = new SettingsPresenter(((GoalKeeperApp)getApplication()).getMapper());
        mSettingsActionListener.setViewHandler(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMatchTime = (TextView) findViewById(R.id.match_time_text);
        mExtTime = (TextView) findViewById(R.id.ext_time_text);
        mGoalsCount = (TextView) findViewById(R.id.goals_count_text);
        updateSettingsDisplay();
        mMatchTime.setOnClickListener(getSettingsClickListener(SettingsDialog.DialogType.MATCH_TIME));
        mExtTime.setOnClickListener(getSettingsClickListener(SettingsDialog.DialogType.EXT_TIME));
        mGoalsCount.setOnClickListener(getSettingsClickListener(SettingsDialog.DialogType.GOALS_COUNT));
    }

    private View.OnClickListener getSettingsClickListener(final SettingsDialog.DialogType type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsActionListener.openPopUp(type);
            }
        };
    }

    @Override
    public void updateSettingsDisplay() {
        mMatchTime.setText(mSettingsActionListener.getGameTime());
        mExtTime.setText(mSettingsActionListener.getExtTime());
        mGoalsCount.setText(mSettingsActionListener.getGoalsCount());

    }

    @Override
    public void openSettingPopup(SettingsDialog dialog) {
        dialog.show(getFragmentManager(), "");
    }

    @Override
    public void onPause() {
        super.onPause();
        mSettingsActionListener.saveSettings();
    }

}
