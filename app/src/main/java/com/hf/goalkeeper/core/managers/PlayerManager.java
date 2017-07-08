package com.hf.goalkeeper.core.managers;

import java.util.ArrayList;

/**
 * Created by hanan on 05/02/17.
 */

public class PlayerManager {
    public static final int BLACK_TEAM = -1;
    public static final int WHITE_TEAM = 1;

    private ArrayList<Player> mBlackTeam = new ArrayList<>();
    private ArrayList<Player> mWhiteTeam = new ArrayList<>();

    public boolean areTeamsEqual() {
        return mBlackTeam.size() == mWhiteTeam.size();
    }

    public void addPlayerToTeam(Player player, int team) {
        if (team == BLACK_TEAM) {
            mBlackTeam.add(player);
        } else {
            mWhiteTeam.add(player);
        }
    }

    public void removePlayerToTeam(Player player, int team) {
        boolean removed;
        if (team == BLACK_TEAM) {
            removed = mBlackTeam.remove(player);
        } else {
            removed = mWhiteTeam.remove(player);
        }
    }

    public boolean isPlayerInTeam(Player player, int team) {
        if (team == BLACK_TEAM)
            return mBlackTeam.contains(player);
        else
            return mWhiteTeam.contains(player);
    }


    public PlayerManager() {
        PlayerManager.Player player1 = new PlayerManager.Player("Hanan", "1");
        PlayerManager.Player player2 = new PlayerManager.Player("Avron", "2");
        PlayerManager.Player player3 = new PlayerManager.Player("Zivron", "3");
        PlayerManager.Player player4 = new PlayerManager.Player("Didos", "4");
        PlayerManager.Player player5 = new PlayerManager.Player("Jasper", "5");
        PlayerManager.Player player6 = new PlayerManager.Player("Andres", "6");
        PlayerManager.Player player7 = new PlayerManager.Player("Harel", "7");
        PlayerManager.Player player8 = new PlayerManager.Player("Itamar", "8");
        PlayerManager.Player player9 = new PlayerManager.Player("Bini", "9");
        PlayerManager.Player player10 = new PlayerManager.Player("Aviel", "10");
        PlayerManager.Player player11 = new PlayerManager.Player("Noam", "11");
        PlayerManager.Player player12 = new PlayerManager.Player("Ori", "12");
        PlayerManager.Player player13 = new PlayerManager.Player("Roey", "13");
        PlayerManager.Player player14 = new PlayerManager.Player("Nimrod", "14");
        PlayerManager.Player player15 = new PlayerManager.Player("Ezer", "15");
        addPlayer(player1);
        addPlayer(player2);
        addPlayer(player3);
        addPlayer(player4);
        addPlayer(player5);
        addPlayer(player6);
        addPlayer(player7);
        addPlayer(player8);
        addPlayer(player9);
        addPlayer(player10);
        addPlayer(player11);
        addPlayer(player12);
        addPlayer(player13);
        addPlayer(player14);
        addPlayer(player15);
    }

    private ArrayList<Player> mPlayerList = new ArrayList<>();

    public void addPlayer(Player player) {
        mPlayerList.add(player);
    }

    public void addPlayer(Player player, int position) {
        mPlayerList.add(position, player);
    }

    public void removePlayer(int position) {
        mPlayerList.remove(position);
    }

    public Player getPlayerByLocation(int location) {
        return mPlayerList.get(location);
    }

    public ArrayList<Player> getList() {
        return mPlayerList;
    }

    public ArrayList<Player> getTeam(int team) {
        return team == BLACK_TEAM ? mBlackTeam : mWhiteTeam;
    }

    public static class Player {
        public Player(String name, String id) {
            this.name = name;
            this.id = id;
        }
        public String name;
        public String id;
    }


}
