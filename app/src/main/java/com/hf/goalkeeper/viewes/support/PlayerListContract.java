package com.hf.goalkeeper.viewes.support;

import com.hf.goalkeeper.core.managers.PlayerManager;

/**
 * Created by hanan on 05/02/17.
 */

public interface PlayerListContract {

    public interface ViewHandler {
        void showAddPlayerDialog(final int position, final PlayerManager.Player player);
    }

}
