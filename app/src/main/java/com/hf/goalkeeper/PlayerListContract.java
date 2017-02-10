package com.hf.goalkeeper;

/**
 * Created by hanan on 05/02/17.
 */

public interface PlayerListContract {

    public interface ViewHandler {
        void showAddPlayerDialog(final int position, final PlayerManager.Player player);
    }

}
