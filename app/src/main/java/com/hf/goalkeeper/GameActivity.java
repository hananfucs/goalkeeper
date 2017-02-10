package com.hf.goalkeeper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by hanan on 05/02/17.
 */

public class GameActivity extends AppCompatActivity {
    private EditText mGameMinTimeText;
    private EditText mGameSecTimeText;
    private EditText mExtMinTimeText;
    private EditText mExtSecTimeText;

    private Button mStartButton;

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

        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        mPlayerManager = (PlayerManager) mMapper.getValueForKey(PlayerManager.class);
        setLayout();
    }

    private void setLayout() {
        mContext = this;

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
                } else {
                    startMatch();
                }
            }
        });
    }

    private void startMatch() {
        mBlackAdapter.displayMatchTeams();
        mWhiteAdapter.displayMatchTeams();
        //update lists and their status
        //start clock
        //adjust layout
        mStartButton.setText("Pause");
    }


}
