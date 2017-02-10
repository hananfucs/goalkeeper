package com.hf.goalkeeper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hanan on 07/02/17.
 */

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {
    PlayerManager mManager;
    private int mTeam;
    private ArrayList<PlayerManager.Player> mMatchTeam;

    private boolean inMatch;

    public TeamListAdapter(PlayerManager playerManager, int team) {
        mManager = playerManager;
        mTeam = team;
    }

    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final TeamListAdapter.ViewHolder holder, final int position) {
        if (!inMatch) {
            holder.setPlayer(mManager.getPlayerByLocation(position));
            holder.mTextView.setText(mManager.getPlayerByLocation(position).name);
            holder.mCheckbox.setChecked(mManager.isPlayerInTeam(mManager.getPlayerByLocation(position), mTeam));
        } else {
            holder.setPlayer(mMatchTeam.get(position));
            holder.mTextView.setText(mMatchTeam.get(position).name);
            holder.mCheckbox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (inMatch)
            return mMatchTeam.size();
        return mManager.getList().size();
    }

    public void displayMatchTeams() {
        mMatchTeam = mManager.getTeam(mTeam);
        inMatch = true;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PlayerManager.Player mPlayer;
        public TextView mTextView;
        public RelativeLayout mRow;
        public CheckBox mCheckbox;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.player_name_text);
            mRow = (RelativeLayout) v.findViewById(R.id.row_background);
            mCheckbox = (CheckBox)v.findViewById(R.id.playerCheckBox);
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mManager.isPlayerInTeam(mPlayer, -1 * mTeam)) {
                        mCheckbox.setChecked(false);
                        return;
                    }

                    if (isChecked && !mManager.isPlayerInTeam(mPlayer, mTeam)) {
                        mManager.addPlayerToTeam(mPlayer, mTeam);
                    } else if (!isChecked && mManager.isPlayerInTeam(mPlayer, mTeam)) {
                        mManager.removePlayerToTeam(mPlayer, mTeam);
                    }
                }
            });
        }

        public void setPlayer(PlayerManager.Player player) {
            mPlayer = player;
        }
    }
}
