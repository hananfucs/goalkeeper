package com.hf.goalkeeper;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hanan on 05/02/17.
 */

public class GameActivity extends AppCompatActivity implements GameContract.ViewHandler{
    private EditText mGameMinTimeText;
    private EditText mGameSecTimeText;
    private EditText mExtMinTimeText;
    private EditText mExtSecTimeText;

    private TextView mMatchTImeTitle;
    private TextView mExtTImeTitle;
    private TextView mMatchTimeMin;
    private TextView mMatchTimeSec;


    private ConstraintLayout mEditTimeLayout;
    private ConstraintLayout mShowTimeLayout;

    private Button mStartButton;
    private Button mResumeButton;
    private Button mStopButton;

    private RecyclerView mBlackTeamList;
    private RecyclerView mWhiteTeamList;

    private TeamListAdapter mBlackAdapter;
    private TeamListAdapter mWhiteAdapter;
    private Mapper mMapper;
    private PlayerManager mPlayerManager;

    private GameContract.UserActionsListener mActionsListener;

    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_game);

    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener = new TimeManager(this);
        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        setLayout();
        mPlayerManager = (PlayerManager) mMapper.getValueForKey(PlayerManager.class);
    }

    private void setLayout() {
        mContext = this;

        mEditTimeLayout = (ConstraintLayout)findViewById(R.id.editTimeLayout);
        mShowTimeLayout = (ConstraintLayout)findViewById(R.id.showTimeLayout);
        mMatchTImeTitle = (TextView) findViewById(R.id.matchTimeTitle);
        mExtTImeTitle = (TextView) findViewById(R.id.extTimeTitle);
        mMatchTimeMin = (TextView) findViewById(R.id.matchTimeMin);
        mMatchTimeSec = (TextView) findViewById(R.id.matchTimeSec);

        mGameMinTimeText = (EditText) findViewById(R.id.gameTimeMinEditText);
        mGameSecTimeText = (EditText) findViewById(R.id.gameTimeSecEditText);
        mExtSecTimeText = (EditText) findViewById(R.id.extTimeSecEditText);
        mExtMinTimeText = (EditText) findViewById(R.id.extTimeMinEditText);

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
                if (!mPlayerManager.areTeamsEqual()) {
                    Toast.makeText(mContext, "Teams are not equal, can't start game", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mActionsListener.getGameStatus() == TimeManager.GAME_NOT_STARTED)
                    startMatch();
                else if (mActionsListener.getGameStatus() == TimeManager.GAME_ONGOING) {
                    mActionsListener.userPausedGame();
                } else if (mActionsListener.getGameStatus() == TimeManager.GAME_PAUSED)
                    mActionsListener.userResumedGame();

            }
        });

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
    }

    private void startMatch() {
        mBlackAdapter.displayMatchTeams();
        mWhiteAdapter.displayMatchTeams();
        int gameMinutes = Integer.valueOf(mGameMinTimeText.getText().toString());
        int gameSeconds= Integer.valueOf(mGameSecTimeText.getText().toString());
        int extMinutes = Integer.valueOf(mExtMinTimeText.getText().toString());
        int extSeconds = Integer.valueOf(mExtSecTimeText.getText().toString());
        mActionsListener.userStartedGame(gameMinutes, gameSeconds, extMinutes, extSeconds);
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
        mMatchTImeTitle.setVisibility(View.GONE);
        mExtTImeTitle.setVisibility(View.GONE);
        mEditTimeLayout.setVisibility(View.GONE);

        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        mStartButton.setVisibility(View.VISIBLE);
        mStartButton.setText("Pause");
        mShowTimeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void matchPaused() {
        mResumeButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
        mStartButton.setVisibility(View.GONE);
    }

    @Override
    public void matchEnded() {
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        mStartButton.setVisibility(View.VISIBLE);
        mStartButton.setText("Start");

        mMatchTImeTitle.setVisibility(View.VISIBLE);
        mExtTImeTitle.setVisibility(View.VISIBLE);
        mEditTimeLayout.setVisibility(View.VISIBLE);
        mShowTimeLayout.setVisibility(View.GONE);

        mBlackAdapter.displayAllPlayers();
        mWhiteAdapter.displayAllPlayers();
    }
}
