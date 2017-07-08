package com.hf.goalkeeper.viewes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.managers.PlayerManager;

import java.util.ArrayList;

/**
 * Created by hanan on 07/02/17.
 */

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {
    final static int PLAYER_VIEWHOLDER_SELECTOR = 0;
    final static int PLAYER_VIEWHOLDER_GAME = 1;

    PlayerManager mManager;
    int mTeam;
    ArrayList<PlayerManager.Player> mMatchTeam;

    private boolean inMatch;

    public TeamListAdapter(PlayerManager playerManager, int team) {
        mManager = playerManager;
        mTeam = team;
    }

    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v, PLAYER_VIEWHOLDER_SELECTOR);
        return vh;
    }

    @Override
    public void onBindViewHolder(final TeamListAdapter.ViewHolder holder, final int position) {
//        if (!inMatch) {
            holder.setPlayer(mManager.getPlayerByLocation(position));
            holder.mTextView.setText(mManager.getPlayerByLocation(position).name);
            holder.mCheckbox.setChecked(mManager.isPlayerInTeam(mManager.getPlayerByLocation(position), mTeam));
            holder.mCheckbox.setVisibility(View.VISIBLE);
            holder.setOnClickListener(getPlayerRowClickListener(holder));
//        } else {
//            holder.setPlayer(mMatchTeam.get(position));
//            holder.mTextView.setText(mMatchTeam.get(position).name);
//            holder.mCheckbox.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
//        if (inMatch)
//            return mMatchTeam.size();
        return mManager.getList().size();
    }
//
//    public void displayMatchTeams() {
//        mMatchTeam = mManager.getTeam(mTeam);
//        inMatch = true;
//        notifyDataSetChanged();
//    }

    public void displayAllPlayers() {
        inMatch = false;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PlayerManager.Player mPlayer;
        public TextView mTextView;
        public RelativeLayout mRow;
        public CheckBox mCheckbox;
        private final int mType;

        public ViewHolder(View v, int type) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.player_name_text);
            mRow = (RelativeLayout) v.findViewById(R.id.row_background);
            mCheckbox = (CheckBox)v.findViewById(R.id.playerCheckBox);
            this.mType = type;
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   checkeboxChecked(isChecked);
                }
            });

        }

        public void checkeboxChecked(boolean value) {
            if (mManager.isPlayerInTeam(mPlayer, -1 * mTeam)) {
                mCheckbox.setChecked(false);
                return;
            }

            if (value && !mManager.isPlayerInTeam(mPlayer, mTeam)) {
                mManager.addPlayerToTeam(mPlayer, mTeam);
            } else if (!value && mManager.isPlayerInTeam(mPlayer, mTeam)) {
                mManager.removePlayerToTeam(mPlayer, mTeam);
            }
        }

        public void setPlayer(PlayerManager.Player player) {
            mPlayer = player;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mRow.setOnClickListener(listener);
        }
    }

    View.OnClickListener getPlayerRowClickListener(final ViewHolder viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.checkeboxChecked(!viewHolder.mCheckbox.isChecked());
                viewHolder.mCheckbox.setChecked(!viewHolder.mCheckbox.isChecked());
            }
        };
    }
}
