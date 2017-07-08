package com.hf.goalkeeper.viewes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hf.goalkeeper.viewes.support.PlayerListContract;
import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.managers.PlayerManager;

/**
 * Created by hanan on 05/02/17.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final PlayerManager mPlayerManager;
    private PlayerListContract.ViewHandler mViewHnadler;


    public PlayerAdapter(PlayerListContract.ViewHandler viewHandler, PlayerManager playerManager) {
        mViewHnadler = viewHandler;
        mPlayerManager = playerManager;
    }

    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlayerAdapter.ViewHolder holder, final int position) {
        holder.mTextView.setText(mPlayerManager.getPlayerByLocation(position).name);
        holder.mRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHnadler.showAddPlayerDialog(position, mPlayerManager.getPlayerByLocation(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayerManager.getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public RelativeLayout mRow;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.player_name_text);
            mRow = (RelativeLayout) v.findViewById(R.id.row_background);
        }
    }

}
