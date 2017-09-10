package com.hf.goalkeeper.core.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by hanan on 23/08/17.
 */

public class LocalDBManager extends SQLiteOpenHelper implements DBManager {

    public static final String DATABASE_NAME = "GoalKeeperDB.db";

    public void resetDB() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(
                "delete * from matches_table ");
        db.execSQL(
                "delete * from players_table ");
        db.execSQL(
                "delete * from players_list_table ");
    }

    public static class MatchDbColumns {
        public static String TABLE_NAME = "matches_table";

        public static String MATCH_ID = "match_id";
        public static String MATCH_DATE = "match_date";
        public static String MATCH_LENGTH = "match_length";
    }

    public static class PlayersDbColumns {
        public static String TABLE_NAME = "players_table";

        public static String MATCH_ID = "match_id";
        public static String PLAYER_ID = "player_id";
        public static String GOAL_TIME = "goal_time";
        public static String WIN = "win";
        public static String TEAM = "team";

        public static class TeamColor {
            public static int BLACK = 1;
            public static int WHITE = 2;
        }
    }

    public static class PlayersListDbColumns {
        public static String TABLE_NAME = "players_list_table";

        public static String PLAYER_ID = "player_id";
        public static String PLAYER_FIRST = "player_first";
        public static String PLAYER_LAST = "player_last";
    }


    public LocalDBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table IF NOT EXISTS matches_table " +
                        "(match_id text, match_date long, match_length integer)"
        );
        db.execSQL(
                "create table IF NOT EXISTS players_table " +
                        "(match_id text, player_id text, goal_time integer, win boolean, team integer)"
        );

        db.execSQL(
                "create table IF NOT EXISTS players_list_table " +
                        "(player_id text, player_first text, player_last text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS matches_table");
        db.execSQL("DROP TABLE IF EXISTS players_table");
        db.execSQL("DROP TABLE IF EXISTS players_list_table");
        onCreate(db);
    }



    @Override
    public void saveMatch(StatisticsManager.Match match) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MatchDbColumns.MATCH_ID, match.id);
        contentValues.put(MatchDbColumns.MATCH_DATE, match.matchDate.getTime());
        contentValues.put(MatchDbColumns.MATCH_LENGTH, match.matchLength);
        db.insert(MatchDbColumns.TABLE_NAME, null, contentValues);

        saveScorres(match);
    }

    @Override
    public StatisticsManager.Match getMatch(String matchId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MatchDbColumns.TABLE_NAME + " where " + MatchDbColumns.MATCH_ID + "='" + matchId + "'", null);
        if (!cursor.moveToFirst())
            return null;
        StatisticsManager.Match match = new StatisticsManager.Match();
        match.id = matchId;
        match.matchLength = cursor.getInt(cursor.getColumnIndex(MatchDbColumns.MATCH_LENGTH));
        match.matchDate =  new Date(cursor.getLong(cursor.getColumnIndex(MatchDbColumns.MATCH_DATE)));
        cursor.close();

        Cursor playerCursor = db.rawQuery("select * from " + PlayersDbColumns.TABLE_NAME + " where " + PlayersDbColumns.MATCH_ID + "='" + matchId + "'", null);
        match.goals = getMatchGoals(playerCursor);
        match.blackTeam = getTeamInMatch(playerCursor, PlayersDbColumns.TeamColor.BLACK);
        match.whiteTeam= getTeamInMatch(playerCursor, PlayersDbColumns.TeamColor.WHITE);
        playerCursor.close();
        return match;
    }

    @Override
    public ArrayList<StatisticsManager.Match> getMatchesForPlayer(String playerId) {
        ArrayList<StatisticsManager.Match> matches = new ArrayList<>();
        HashSet<String> matchIds = new HashSet<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PlayersDbColumns.TABLE_NAME + " where " + PlayersDbColumns.PLAYER_ID + "='" + playerId + "'", null);
        if (!cursor.moveToFirst())
            return null;
        do {
            String matchId = cursor.getString(cursor.getColumnIndex(PlayersDbColumns.MATCH_ID));
            if (!matchIds.contains(matchId));
                matchIds.add(matchId);
        }while (cursor.moveToNext());
        for (String matchId : matchIds) {
            matches.add(getMatch(matchId));
        }
        return matches;
    }

    private ArrayList<PlayerManager.Player> getTeamInMatch(Cursor playerCursor, int color) {
        HashSet<String> added = new HashSet<>();
        ArrayList<PlayerManager.Player> players = new ArrayList<>();
        if (!playerCursor.moveToFirst())
            return null;
        do {
            String playerId = playerCursor.getString(playerCursor.getColumnIndex(PlayersDbColumns.PLAYER_ID));
            int playerColor = playerCursor.getInt(playerCursor.getColumnIndex(PlayersDbColumns.TEAM));
            if (added.contains(playerId) || playerColor != color)
                continue;
            PlayerManager.Player player = new PlayerManager.Player(getPlayerName(playerId), playerId);
            players.add(player);
        } while (playerCursor.moveToNext());
        return players;
    }

    private String getPlayerName(String playerId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PlayersListDbColumns.TABLE_NAME + " where " + PlayersListDbColumns.PLAYER_ID + "='" + playerId + "'", null);
        if (!cursor.moveToFirst())
            return null;
        return cursor.getString(cursor.getColumnIndex(PlayersListDbColumns.PLAYER_FIRST)) + " " + cursor.getString(cursor.getColumnIndex(PlayersListDbColumns.PLAYER_LAST));
    }

    private ArrayList<StatisticsManager.Goal> getMatchGoals(Cursor playerCursor) {
        ArrayList<StatisticsManager.Goal> goals = new ArrayList<>();
        if (!playerCursor.moveToFirst())
            return null;
        do {
            if (playerCursor.getInt(playerCursor.getColumnIndex(PlayersDbColumns.GOAL_TIME)) > 0) {
                StatisticsManager.Goal goal = new StatisticsManager.Goal();
                goal.second = playerCursor.getInt(playerCursor.getColumnIndex(PlayersDbColumns.GOAL_TIME));
                goal.scorrer = new PlayerManager.Player(null, playerCursor.getString(playerCursor.getColumnIndex(PlayersDbColumns.PLAYER_ID)));
                goals.add(goal);
            }

        } while (playerCursor.moveToNext());
        return goals;
    }

    private void saveScorres(StatisticsManager.Match match) {
        SQLiteDatabase db = this.getWritableDatabase();
        HashSet<String> added = new HashSet<>();

        //Add scorrers
        for (StatisticsManager.Goal goal : match.goals) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayersDbColumns.MATCH_ID, match.id);
            contentValues.put(PlayersDbColumns.GOAL_TIME, (goal.second));
            contentValues.put(PlayersDbColumns.PLAYER_ID, goal.scorrer.id);
            contentValues.put(PlayersDbColumns.WIN, didPlayerWin(match, goal));
            contentValues.put(PlayersDbColumns.TEAM, getPlayerTeam(match, goal));
            added.add(goal.scorrer.id);
            db.insert(MatchDbColumns.TABLE_NAME, null, contentValues);
        }

        //Add Blacks
        for (PlayerManager.Player player : match.blackTeam) {
            if (added.contains(player.id))
                continue;
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayersDbColumns.MATCH_ID, match.id);
            contentValues.put(PlayersDbColumns.GOAL_TIME, -1);
            contentValues.put(PlayersDbColumns.PLAYER_ID, player.id);
            db.insert(MatchDbColumns.TABLE_NAME, null, contentValues);
        }

        //add whites
        for (PlayerManager.Player player : match.whiteTeam) {
            if (added.contains(player.id))
                continue;
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayersDbColumns.MATCH_ID, match.id);
            contentValues.put(PlayersDbColumns.GOAL_TIME, -1);
            contentValues.put(PlayersDbColumns.PLAYER_ID, player.id);
            db.insert(MatchDbColumns.TABLE_NAME, null, contentValues);
        }
    }

    private boolean didPlayerWin(StatisticsManager.Match match, StatisticsManager.Goal goal) {
        return match.winner == getPlayerTeam(match, goal);
    }

    private int getPlayerTeam(StatisticsManager.Match match, StatisticsManager.Goal goal) {
        PlayerManager.Player scorrer = goal.scorrer;
        for (PlayerManager.Player player : match.blackTeam) {
            if (scorrer.id.equals(player))
                return PlayersDbColumns.TeamColor.BLACK;
        }


        return PlayersDbColumns.TeamColor.WHITE;
    }
}
