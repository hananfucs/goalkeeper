package com.hf.goalkeeper.core.managers;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hanan on 28/07/17.
 */

public interface GameStatsHolder {
    ArrayList<StatisticsManager.Goal> getGoals(int team);
    void cancelGoal(int position, int team);
}
