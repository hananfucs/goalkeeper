package com.hf.goalkeeper.viewes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hf.goalkeeper.core.managers.SettingsManager;
import com.hf.goalkeeper.core.utils.StringUtils;
import com.hf.goalkeeper.viewes.support.GameContract;
import com.hf.goalkeeper.viewes.adapters.MatchTeamListAdapter;
import com.hf.goalkeeper.R;
import com.hf.goalkeeper.viewes.adapters.ScorrerListAdapter;
import com.hf.goalkeeper.core.GoalKeeperApp;
import com.hf.goalkeeper.core.Mapper;
import com.hf.goalkeeper.core.managers.PlayerManager;
import com.hf.goalkeeper.core.managers.StatisticsManager;
import com.hf.goalkeeper.core.managers.TimeManager;

/**
 * Created by hanan on 17/02/17.
 */

public class GamePlayActivity extends AppCompatActivity implements GameContract.ViewHandler {

    private TextView mMatchTimeMin;
    private TextView mMatchTimeSec;

    private TextView mBlackScore;
    private TextView mWhiteScore;

    private Button mResumeButton;
    private Button mStopButton;
    private Button mPauseButton;


    private RecyclerView mBlackTeamList;
    private RecyclerView mWhiteTeamList;

    private MatchTeamListAdapter mBlackAdapter;
    private MatchTeamListAdapter mWhiteAdapter;

    private RecyclerView mWhiteTeamScorrers;
    private RecyclerView mBlackTeamScorrers;

    private ScorrerListAdapter mBlackScorrersAdapter;
    private ScorrerListAdapter mWhiteScorrersAdapter;

    private Mapper mMapper;
    private GameContract.UserActionsListener mActionsListener;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, GamePlayActivity.class);

        return intent;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_gameplay);
        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        setLayout();
        TimeManager timeManager = new TimeManager(this);
        mActionsListener = timeManager;
        StatisticsManager statisticsManager = (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class);
        statisticsManager.setTimeManager(timeManager);
        PlayerManager playerManager = (PlayerManager) mMapper.getValueForKey(PlayerManager.class);
        statisticsManager.matchStarted(playerManager.getTeam(PlayerManager.BLACK_TEAM), playerManager.getTeam(PlayerManager.WHITE_TEAM));
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingsManager settingsManager = (SettingsManager) mMapper.getValueForKey(SettingsManager.class);
        int gameMinutes = settingsManager.getGameMinutes();
        int gameSeconds = settingsManager.getGameSeconds();
        int extMinutes = settingsManager.getExtMinutes();
        int extSeconds = settingsManager.getExtSeconds();
        gameSeconds = gameSeconds + (60 * gameMinutes);
        extSeconds = extSeconds + (60 * extMinutes);
        mActionsListener.userStartedGame(gameSeconds, extSeconds, settingsManager.getGameGoals());

    }

    private void setLayout() {
        mBlackTeamList = (RecyclerView) findViewById(R.id.blackTeamListSummary);
        mWhiteTeamList = (RecyclerView) findViewById(R.id.whiteTeamListSumary);

        mBlackAdapter = new MatchTeamListAdapter((PlayerManager) mMapper.getValueForKey(PlayerManager.class), PlayerManager.BLACK_TEAM, (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class));
        mWhiteAdapter = new MatchTeamListAdapter((PlayerManager) mMapper.getValueForKey(PlayerManager.class), PlayerManager.WHITE_TEAM, (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class));

        mBlackTeamList.setAdapter(mBlackAdapter);
        mWhiteTeamList.setAdapter(mWhiteAdapter);

        RecyclerView.LayoutManager blackLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager whiteLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBlackTeamList.setLayoutManager(blackLayoutManager );
        mWhiteTeamList.setLayoutManager(whiteLayoutManager);


        mBlackTeamScorrers = (RecyclerView) findViewById(R.id.blackScorrers);
        mWhiteTeamScorrers = (RecyclerView) findViewById(R.id.whiteScorrers);

        mBlackScorrersAdapter = new ScorrerListAdapter((StatisticsManager) mMapper.getValueForKey(StatisticsManager.class), PlayerManager.BLACK_TEAM, false);
        mWhiteScorrersAdapter = new ScorrerListAdapter((StatisticsManager) mMapper.getValueForKey(StatisticsManager.class), PlayerManager.WHITE_TEAM, false);

        mBlackTeamScorrers.setAdapter(mBlackScorrersAdapter);
        mWhiteTeamScorrers.setAdapter(mWhiteScorrersAdapter);
        RecyclerView.LayoutManager blackScorrerLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager whiteScorrerLayoutManager = new LinearLayoutManager(getApplicationContext());

        mBlackTeamScorrers.setLayoutManager(blackScorrerLayoutManager);
        mWhiteTeamScorrers.setLayoutManager(whiteScorrerLayoutManager );

        mMatchTimeMin = (TextView) findViewById(R.id.matchTimeMin);
        mMatchTimeSec = (TextView) findViewById(R.id.matchTimeSec);

        mStopButton = (Button) findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.userStoppedGame();
            }
        });

        mResumeButton = (Button) findViewById(R.id.resumeButton);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.userResumedGame();
            }
        });

        mPauseButton = (Button) findViewById(R.id.pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.userPausedGame();
            }
        });

        mBlackScore = (TextView)findViewById(R.id.leftTeamScore);
        mWhiteScore = (TextView)findViewById(R.id.rightTeamScore);
    }

    @Override
    public void updateMatchTime(int seconds) {
        final String displayMinute = StringUtils.secondsToMinutes(seconds);
        final String displaySeconds = StringUtils.secondsToSeconds(seconds);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMatchTimeMin.setText(displayMinute);
                mMatchTimeSec.setText(displaySeconds);
            }
        });
    }

    @Override
    public void matchStarted() {
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void matchPaused() {
        mResumeButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.GONE);
    }

    @Override
    public void matchEnded() {
        StatisticsManager stats = (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class);
        stats.matchEndded();
        Intent intent = new Intent(this, GameSummaryActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startedExtension() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView extension = (ImageView)findViewById(R.id.extensionIndicator);
                extension.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void goalScored(StatisticsManager.Goal goal) {
        mBlackScorrersAdapter.notifyDataSetChanged();
        mWhiteScorrersAdapter.notifyDataSetChanged();
        updateScore();
    }

    @Override
    public boolean shouldGoTOExtension() {
        StatisticsManager stats = (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class);
        return stats.getBlackGoals().size() == stats.getWhiteGoals().size();
    }

    private void updateScore() {
        StatisticsManager stats = (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class);
        int blackScore = stats.getBlackGoals().size();
        int whiteScore = stats.getWhiteGoals().size();
        mBlackScore.setText(String.valueOf(blackScore));
        mWhiteScore.setText(String.valueOf(whiteScore));
    }
}
