package com.hf.goalkeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hanan on 17/02/17.
 */

public class GamePlayActivity extends AppCompatActivity implements GameContract.ViewHandler{
    private static final String GAME_MINUTES = "game_minutes";
    private static final String GAME_SECONDS = "game_seconds";
    private static final String EXT_MINUTES = "ext_minutes";
    private static final String EXT_SECONDS= "ext_seconds";

    private TextView mMatchTimeMin;
    private TextView mMatchTimeSec;

    private Button mResumeButton;
    private Button mStopButton;
    private Button mPauseButton;


    private RecyclerView mBlackTeamList;
    private RecyclerView mWhiteTeamList;

    private TeamListAdapter mBlackAdapter;
    private TeamListAdapter mWhiteAdapter;

    private RecyclerView mWhiteTeamScorrers;
    private RecyclerView mBlackTeamScorrers;

    private ScorrerListAdapter mBlackScorrersAdapter;
    private ScorrerListAdapter mWhiteScorrersAdapter;

    private Mapper mMapper;
    private GameContract.UserActionsListener mActionsListener;

    public static Intent getIntent(Context context, int gameMinutes, int gameSeconds, int extMinutes, int extSeconds) {
        Intent intent = new Intent(context, GamePlayActivity.class);

        intent.putExtra(GAME_MINUTES, gameMinutes);
        intent.putExtra(GAME_SECONDS, gameSeconds);
        intent.putExtra(EXT_MINUTES, extMinutes);
        intent.putExtra(EXT_SECONDS, extSeconds);

        return intent;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_gameplay);
        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        setLayout();
        mActionsListener = new TimeManager(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int gameMinutes = getIntent().getIntExtra(GAME_MINUTES, 8);
        int gameSeconds = getIntent().getIntExtra(GAME_SECONDS, 0);
        int extMinutes = getIntent().getIntExtra(EXT_MINUTES, 2);
        int extSeconds = getIntent().getIntExtra(EXT_SECONDS, 0);
        mActionsListener.userStartedGame(gameMinutes, gameSeconds, extMinutes, extSeconds);

        mBlackAdapter.displayMatchTeams();
        mWhiteAdapter.displayMatchTeams();
    }

    private void setLayout() {
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


        mBlackTeamScorrers = (RecyclerView) findViewById(R.id.blackScorrers);
        mWhiteTeamScorrers = (RecyclerView) findViewById(R.id.whiteScorrers);

        mBlackScorrersAdapter = new ScorrerListAdapter((StatisticsManager) mMapper.getValueForKey(StatisticsManager.class), PlayerManager.BLACK_TEAM);
        mWhiteScorrersAdapter = new ScorrerListAdapter((StatisticsManager) mMapper.getValueForKey(StatisticsManager.class), PlayerManager.WHITE_TEAM);

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
    }

    @Override
    public void updateMatchTime(int minutes, int seconds) {
        String minutesString = String.valueOf(minutes);
        final String minutesStringF;
        if (minutesString.length() == 1) {
            minutesString = "0"+minutesString;
        }
        minutesStringF = minutesString;
        String secondsString = String.valueOf(seconds);
        final String secondsStringF;
        if (secondsString.length() == 1) {
            secondsString = "0" + secondsString;
        }
        secondsStringF = secondsString;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMatchTimeMin.setText(minutesStringF);
                mMatchTimeSec.setText(secondsStringF);
            }
        });
    }

    @Override
    public void updateExtTime(int minutes, int seconds) {

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
        finish();
    }
}
