package com.hf.goalkeeper.viewes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.managers.GameStatsHolder;
import com.hf.goalkeeper.core.managers.StatisticsManager;

/**
 * Created by hanan on 15/02/17.
 */

public class ScorrerListAdapter extends RecyclerView.Adapter<ScorrerListAdapter.ViewHolder> {
    private GameStatsHolder mStatsManager;
    private int mTeam;

    public ScorrerListAdapter(GameStatsHolder statsManager, int team) {
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
        holder.mCancelButton.setOnClickListener(getCancelGoalListener(holder.getLayoutPosition()));
    }

    private String getGoalTest(StatisticsManager.Goal goal) {
        String goalMinute = String.valueOf(goal.minute);
        if (goalMinute.length() == 1)
            goalMinute = "0" + goalMinute;

        String goalSecond = String.valueOf(goal.second);
        if (goalSecond.length() == 1)
            goalSecond = "0" + goalSecond;
        return goal.scorrer.name + " (" + goalMinute + ":" + goalSecond+ ")";
    }

    private View.OnClickListener getCancelGoalListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatsManager.cancelGoal(position, mTeam);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mStatsManager.getGoals(mTeam).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView mCancelButton;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.scorrerText);
            mCancelButton = (ImageView) v.findViewById(R.id.cancel_goal);
        }
    }

}
