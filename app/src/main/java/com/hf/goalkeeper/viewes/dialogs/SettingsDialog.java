package com.hf.goalkeeper.viewes.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.viewes.support.SettingsContract;

/**
 * Created by hanan on 01/07/17.
 */

public class SettingsDialog extends DialogFragment implements View.OnClickListener{
    public static final String DIALOG_TITLE = "dialog_title";
    public static final String DIALOG_TYPE = "dialog_type";
    public static final String GAME_MINUTES = "game_minutes";
    public static final String GAME_SECONDS= "game_seconds";
    public static final String EXT_MINUTES = "ext_minutes";
    public static final String EXT_SECONDS = "ext_seconds";
    public static final String GAME_GOALS = "goals";
    public enum DialogType {MATCH_TIME, EXT_TIME, GOALS_COUNT};
    private static final String[] SECONDS_VALUES = new String[] { "00", "15", "30", "45" };

    private SettingsContract.SettingsUiActionsListener mSettingsPresenter;
    private DialogType mType;

    private NumberPicker mMinutesPicker;
    private NumberPicker mSecondsPicker;
    private NumberPicker mGoalsPicker;

    private Button mOKButton;
    private Button mCancelButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings_dialog_layout);
        dialog.show();

        mMinutesPicker = (NumberPicker) dialog.findViewById(R.id.minutes_picker);
        mMinutesPicker.setMaxValue(30);
        mMinutesPicker.setMinValue(0);
        mSecondsPicker = (NumberPicker) dialog.findViewById(R.id.seconds_picker);
        mSecondsPicker.setMinValue(1);
        mSecondsPicker.setMaxValue(4);
        mSecondsPicker.setDisplayedValues(SECONDS_VALUES);

        mGoalsPicker = (NumberPicker) dialog.findViewById(R.id.goals_picker);
        mGoalsPicker.setMaxValue(30);
        mGoalsPicker.setMinValue(1);

        Bundle args = getArguments();
        mType = (DialogType) args.get(DIALOG_TYPE);

        mOKButton = (Button) dialog.findViewById(R.id.settings_dialog_ok);
        mOKButton.setOnClickListener(this);
        mCancelButton = (Button) dialog.findViewById(R.id.settings_dialog_cancel);
        mCancelButton.setOnClickListener(this);

        TextView title = (TextView)dialog.findViewById(R.id.setting_type_text);

        setTitle(mType, title);
        setValues(mType, args);


        if (mType == DialogType.GOALS_COUNT) {
            RelativeLayout goalsLayout = (RelativeLayout) dialog.findViewById(R.id.goals_settings_layout);
            goalsLayout.setVisibility(View.VISIBLE);

            RelativeLayout timeLayout = (RelativeLayout) dialog.findViewById(R.id.time_settings_layout);
            timeLayout.setVisibility(View.GONE);
        }

        return dialog;

    }

    private void setValues(DialogType type, Bundle args) {
        switch (type) {
            case EXT_TIME:
                mMinutesPicker.setValue(args.getInt(EXT_MINUTES));
                mSecondsPicker.setValue(args.getInt(EXT_SECONDS)/15 +1);
                break;
            case MATCH_TIME:
                mMinutesPicker.setValue(args.getInt(GAME_MINUTES));
                mSecondsPicker.setValue(args.getInt(GAME_SECONDS)/15 + 1);
                break;
            case GOALS_COUNT:
                mGoalsPicker.setValue(args.getInt(GAME_GOALS));
                break;
        }
    }

    private void setTitle(DialogType type, TextView title) {

        switch (type) {
            case EXT_TIME:
                title.setText("Extra Time:");
                break;
            case MATCH_TIME:
                title.setText("Match Time:");
                break;
            case GOALS_COUNT:
                title.setText("Goals:");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.settings_dialog_ok) {
            if (mType == DialogType.GOALS_COUNT)
                mSettingsPresenter.updateGoals(mGoalsPicker.getValue());
            else {
                int seconds = Integer.parseInt(SECONDS_VALUES[mSecondsPicker.getValue() - 1]);
                mSettingsPresenter.updateTimeSetting(mType, mMinutesPicker.getValue(), seconds);
            }
            dismiss();
        } else if (v.getId() == R.id.settings_dialog_cancel) {
            dismiss();
        }
    }

    public void setListener(SettingsContract.SettingsUiActionsListener actionsListener) {
        mSettingsPresenter = actionsListener;
    }
}

