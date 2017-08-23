package com.hf.goalkeeper.viewes.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.GoalKeeperApp;
import com.hf.goalkeeper.core.Mapper;
import com.hf.goalkeeper.core.managers.GameStatsHolder;
import com.hf.goalkeeper.core.managers.PlayerManager;
import com.hf.goalkeeper.core.managers.StatisticsManager;
import com.hf.goalkeeper.core.utils.StringUtils;
import com.hf.goalkeeper.viewes.adapters.ScorrerListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by hanan on 28/07/17.
 */

public class GameSummaryActivity extends AppCompatActivity {
    public static String MATCH_TO_DISPLAY = "match_to_display";

    private Mapper mMapper;
    private TextView mMatchDate;
    private TextView mMatchScore;
    private TextView mMatchLength;
    private RecyclerView mBlackTeamList;
    private RecyclerView mWhiteTeamList;

    private ScorrerListAdapter mBlackAdapter;
    private ScorrerListAdapter mWhiteAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_game_summary);
        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        setLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        final Context cn = this;
        Thread th  = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBlackAdapter.notifyDataSetChanged();
                        mWhiteAdapter.notifyDataSetChanged();
                        Toast.makeText(cn, "updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//        th.start();
        mBlackAdapter.notifyDataSetChanged();
        mWhiteAdapter.notifyDataSetChanged();
    }

    private void setLayout() {
        mMatchDate = (TextView)findViewById(R.id.matchDate);
        mMatchScore = (TextView)findViewById(R.id.matchScore);
        mMatchLength = (TextView)findViewById(R.id.matchLength);

        StatisticsManager stats = (StatisticsManager) mMapper.getValueForKey(StatisticsManager.class);
        StatisticsManager.Match match = stats.getCurrentMatch();

        mBlackAdapter= new ScorrerListAdapter(new MatchStats(match), PlayerManager.BLACK_TEAM, true);
        mWhiteAdapter = new ScorrerListAdapter(new MatchStats(match), PlayerManager.WHITE_TEAM, true);

        mBlackTeamList = (RecyclerView)findViewById(R.id.blackTeamListSummary);
        mBlackTeamList.setAdapter(mBlackAdapter);
        mWhiteTeamList = (RecyclerView)findViewById(R.id.whiteTeamListSumary);
        mWhiteTeamList.setAdapter(mWhiteAdapter);

        RecyclerView.LayoutManager blackLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager whiteLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBlackTeamList.setLayoutManager(blackLayoutManager );
        mWhiteTeamList.setLayoutManager(whiteLayoutManager);

        SimpleDateFormat sdf = new SimpleDateFormat("DD.MM.YYYY | HH:mm");
        String dateString = sdf.format(match.matchDate);
        mMatchDate.setText(dateString);

        int blackTeamGoals = mBlackAdapter.getItemCount();
        int whiteTeamGoals = mWhiteAdapter.getItemCount();

        String score = blackTeamGoals + " - " + whiteTeamGoals;
        mMatchScore.setText(score);

        String matchLength = StringUtils.secondsToMinutes(match.matchLength) + ":" + StringUtils.secondsToSeconds(match.matchLength);
        mMatchLength.setText(matchLength);
    }

    public class MatchStats implements GameStatsHolder {
        private ArrayList<StatisticsManager.Goal> mGoals;
        private ArrayList<PlayerManager.Player> mWhiteTeam;
        private ArrayList<PlayerManager.Player> mBlackTeam;

        public MatchStats(StatisticsManager.Match match) {
            mGoals = match.goals;
            mWhiteTeam = match.whiteTeam;
            mBlackTeam = match.blackTeam;
        }

        @Override
        public ArrayList<StatisticsManager.Goal> getGoals(int team) {
            ArrayList<StatisticsManager.Goal> ret = new ArrayList<>();
            if (team == PlayerManager.BLACK_TEAM) {
                for (StatisticsManager.Goal goal : mGoals) {
                    if (scorrerIsInList(goal.scorrer, mBlackTeam))
                        ret.add(goal);
                }
            } else {
                for (StatisticsManager.Goal goal : mGoals) {
                    if (scorrerIsInList(goal.scorrer, mWhiteTeam))
                        ret.add(goal);
                }
            }
            return ret;
        }

        private boolean scorrerIsInList(PlayerManager.Player scorrer, ArrayList<PlayerManager.Player> list){
            for (PlayerManager.Player player : list) {
                if (player.id.equals(scorrer.id))
                        return true;
            }
            return false;
        }

        @Override
        public void cancelGoal(int position, int team) {

        }
    }
}
