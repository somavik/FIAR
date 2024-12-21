package game;

import java.awt.Color;
import java.io.Serializable;

public class Game implements Serializable{
    private final Player[] players = new Player[2]; // Két játékos (lehet Human vagy Bot)
    private final boolean botVHuman;

    public Game(Human p1, Bot p2) {
        this.players[0] = p1;
        this.players[1] = p2;
        botVHuman = true;
    }

    public Game(Human p1, Human p2) {
        this.players[0] = p1;
        this.players[1] = p2;
        botVHuman = false;
    }

    public Game(Player p1, Player p2, boolean bvh) {
        this.players[0] = p1;
        this.players[1] = p2;
        botVHuman = bvh; // Ebben az esetben nem tudni a játékmódot, ezért elkérjük a konstruktorban
    }

    //Segédfüggvények, amik az ActionListenerekben játszanak szerepet
    public void switchColors(){
        Color color0 = players[0].getColor();
        Color color1 = players[1].getColor();
        players[0].setColor(color1);
        players[1].setColor(color0);
    }

    public boolean isThereAWinner(){
        if(players[0].getScore()>=2 || players[1].getScore()>=2){
            return true;
        }
        else return false;
    }

    public void resetScores(){
        players[0].score=0;
        players[1].score=0;
    }

    // Getterek és printer
    public Player[] getPlayers() {
        return players;
    }

    public boolean isBotVsHuman() {
        return botVHuman;
    }

    public Player getPlayerByColor(Color color){
        if(players[0].getColor().equals(color)){
            return players[0];
        } 
        else return players[1];
    }

    public Player getOtherPlayerByColor(Color color){
        if(players[0].getColor().equals(color)){
            return players[1];
        } 
        else return players[0];
    }

    @Override
    public String toString() {
        return "Game{" +
               "players=[" + players[0] + ", " + players[1] + "], ";
    }
}