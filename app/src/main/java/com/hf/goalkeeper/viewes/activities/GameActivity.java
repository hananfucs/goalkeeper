package com.hf.goalkeeper.viewes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.utils.StringUtils;
import com.hf.goalkeeper.viewes.adapters.TeamListAdapter;
import com.hf.goalkeeper.core.GoalKeeperApp;
import com.hf.goalkeeper.core.Mapper;
import com.hf.goalkeeper.core.managers.PlayerManager;
import com.hf.goalkeeper.core.managers.SettingsManager;

/**
 * Created by hanan on 05/02/17.
 */

public class GameActivity extends AppCompatActivity {
    private TextView mGameMinTimeText;
    private TextView mGameSecTimeText;
    private TextView mExtMinTimeText;
    private TextView mExtSecTimeText;

    private ConstraintLayout mEditTimeLayout;
    private ConstraintLayout mShowTimeLayout;

    private Button mStartButton;
    private ImageButton mEditSettingsButton;

    private RecyclerView mBlackTeamList;
    private RecyclerView mWhiteTeamList;

    private TeamListAdapter mBlackAdapter;
    private TeamListAdapter mWhiteAdapter;
    private Mapper mMapper;
    private PlayerManager mPlayerManager;

    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_game);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        setLayout();
        mPlayerManager = (PlayerManager) mMapper.getValueForKey(PlayerManager.class);
    }

    private void setLayout() {
        mContext = this;

        mEditTimeLayout = (ConstraintLayout)findViewById(R.id.editTimeLayout);
        mShowTimeLayout = (ConstraintLayout)findViewById(R.id.showTimeLayout);

        mGameMinTimeText = (TextView) findViewById(R.id.gameTimeMinEditText);
        mGameSecTimeText = (TextView) findViewById(R.id.gameTimeSecEditText);
        mExtSecTimeText = (TextView) findViewById(R.id.extTimeSecEditText);
        mExtMinTimeText = (TextView) findViewById(R.id.extTimeMinEditText);
        SettingsManager settingsManager = (SettingsManager) mMapper.getValueForKey(SettingsManager.class);
        mGameMinTimeText.setText(StringUtils.getFormatedNumber(settingsManager.getGameMinutes()));
        mGameSecTimeText.setText(StringUtils.getFormatedNumber(settingsManager.getGameSeconds()));
        mExtMinTimeText.setText(StringUtils.getFormatedNumber(settingsManager.getExtMinutes()));
        mExtSecTimeText.setText(StringUtils.getFormatedNumber(settingsManager.getExtSeconds()));

                mBlackTeamList = (RecyclerView) findViewById(R.id.blackTeamList);
        mWhiteTeamList = (RecyclerView) findViewById(R.id.whiteTeamList);

        mBlackAdapter = new TeamListAdapter((PlayerManager) mMapper.getValueForKey(PlayerManager.class), PlayerManager.BLACK_TEAM);
        mWhiteAdapter = new TeamListAdapter((PlayerManager) mMapper.getValueForKey(PlayerManager.class), PlayerManager.WHITE_TEAM);

        mBlackTeamList.setAdapter(mBlackAdapter);
        mWhiteTeamList.setAdapter(mWhiteAdapter);

        RecyclerView.LayoutManager blackLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager whiteLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBlackTeamList.setLayoutManager(blackLayoutManager );
        mWhiteTeamList.setLayoutManager(whiteLayoutManager);

        mBlackAdapter.notifyDataSetChanged();
        mWhiteAdapter.notifyDataSetChanged();

        mStartButton = (Button) findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPlayerManager.areTeamsEqual() || mPlayerManager.areTeamsEmpty()) {
                    Toast.makeText(mContext, "Teams are not equal, can't start game", Toast.LENGTH_SHORT).show();
                    return;
                }
                startMatch();
            }
        });
        mEditSettingsButton = (ImageButton) findViewById(R.id.edit_settings_button);
        mEditSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startMatch() {
        Intent gamePlayIntent = GamePlayActivity.getIntent(this);
        startActivity(gamePlayIntent);
    }
}
