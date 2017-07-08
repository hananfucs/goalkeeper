package com.hf.goalkeeper.viewes.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.hf.goalkeeper.viewes.adapters.PlayerAdapter;
import com.hf.goalkeeper.viewes.support.PlayerListContract;
import com.hf.goalkeeper.R;
import com.hf.goalkeeper.core.GoalKeeperApp;
import com.hf.goalkeeper.core.Mapper;
import com.hf.goalkeeper.core.managers.PlayerManager;

public class PlayersListActivity extends AppCompatActivity  implements PlayerListContract.ViewHandler {

    private RecyclerView mPlayerList;
    private PlayerAdapter mAdapter;
    private FloatingActionButton mFab;
    private Mapper mMapper;
    private PlayerManager mPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_list);
        mMapper = ((GoalKeeperApp)getApplication()).getMapper();
        mPlayerManager = (PlayerManager) mMapper.getValueForKey(PlayerManager.class);
        setLayout();
        addPlayers();
    }

    private void setLayout() {
        mAdapter = new PlayerAdapter(this, mPlayerManager);
        mPlayerList = (RecyclerView) findViewById(R.id.player_list);
        mPlayerList.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPlayerList.setLayoutManager(mLayoutManager);
        mFab = (FloatingActionButton) findViewById(R.id.listFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPlayerDialog(-1, null);
            }
        });
    }

    private void addPlayers() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddPlayerDialog(final int position, final PlayerManager.Player player) {
        final boolean addNew = position < 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (addNew)
            builder.setTitle("Add New");
        else
            builder.setTitle("Edit");
        final EditText input = new EditText(this);
        if (player != null)
            input.setText(player.name);
        input.setFocusable(true);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addNew) {
                    mPlayerManager.addPlayer(new PlayerManager.Player(input.getText().toString(), String.valueOf(mAdapter.getItemCount() + 1)));
                } else {
                    player.name = input.getText().toString();
                    mPlayerManager.addPlayer(player, position);
                }
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        if (!addNew) {
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPlayerManager.removePlayer(position);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
