package com.hf.goalkeeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hanan on 15/02/17.
 */

public class ScorrerListAdapter extends RecyclerView.Adapter<ScorrerListAdapter.ViewHolder> {
    private StatisticsManager mStatsManager;
    private int mTeam;

    public ScorrerListAdapter(StatisticsManager statsManager, int team) {
        mTeam = team;
        mStatsManager = statsManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scorrer_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatisticsManager.Goal goal = mStatsManager.getGoals(mTeam).get(position);
        holder.mTextView.setText(getGoalTest(goal));
    }

    private String getGoalTest(StatisticsManager.Goal goal) {
        return goal.scorrer.name + " (" + goal.minute + ":" + goal.second + ")";
    }

    @Override
    public int getItemCount() {
        return mStatsManager.getGoals(mTeam).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.player_name_text);
        }
    }

}
