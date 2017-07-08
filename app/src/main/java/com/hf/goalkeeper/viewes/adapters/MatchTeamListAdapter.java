package com.hf.goalkeeper.viewes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.managers.PlayerManager;
import com.hf.goalkeeper.core.managers.StatisticsManager;

/**
 * Created by hanan on 18/02/17.
 */

public class MatchTeamListAdapter extends TeamListAdapter {
    private StatisticsManager mStatisticsManager;

    public MatchTeamListAdapter(PlayerManager playerManager, int team, StatisticsManager statisticsManager) {
        super(playerManager, team);
        mMatchTeam = mManager.getTeam(mTeam);
        mStatisticsManager = statisticsManager;
    }

    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v, PLAYER_VIEWHOLDER_GAME);
        return vh;
    }

    @Override
    public void onBindViewHolder(final TeamListAdapter.ViewHolder holder, final int position) {
        holder.setPlayer(mMatchTeam.get(position));
        holder.mTextView.setText(mMatchTeam.get(position).name);
        holder.mCheckbox.setVisibility(View.GONE);
        holder.setOnClickListener(getPlayerRowClickListener(holder));
    }

    @Override
    public int getItemCount() {
        return mMatchTeam.size();

    }

    @Override
    View.OnClickListener getPlayerRowClickListener(final ViewHolder viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mStatisticsManager.goalScored(mTeam, viewHolder.mPlayer);
            }
        };
    }

}
