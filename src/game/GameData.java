package game;

import java.awt.Color;
import java.io.*;

// Minden adatot eltárol ami szükséges egy játék betöltéséhez
public class GameData implements Serializable{
    private final int row;
    private final int column;
    private final String p1Name;
    private final int p1Score;
    private final Color p1Color;
    private final int p1Diff;
    private final String p2Name;
    private final int p2Score;
    private final Color p2Color;
    private final int p2Diff;
    private final Color[][] mx;
    private final boolean firstP;

    public GameData(int row, int column, String p1Name, int p1Score, Color p1Color, int p1Diff,String p2Name, int p2Score ,Color p2Color, int p2Diff, Color[][] mx, boolean firstP){
        this.row = row;
        this.column = column;
        this.p1Name = p1Name;
        this.p1Score = p1Score;
        this.p1Color = p1Color;
        this.p1Diff = p1Diff;
        this.p2Name = p2Name;
        this.p2Score = p2Score;
        this.p2Color = p2Color;
        this.p2Diff = p2Diff;
        this.mx = mx;
        this.firstP=firstP;
    }

    // Szerializáló be-ki töltő
    public void saveGameData(GameData gameData, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(gameData); 
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game data: " + e.getMessage());
        }
    }

    public GameData loadGameData(String filename) {
        GameData gameData = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            gameData = (GameData) in.readObject();
            System.out.println("Game loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game data: " + e.getMessage());
        }
        return gameData;
    }

     // Getterek
     public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getP1Name() {
        return p1Name;
    }

    public int getP1Score() {
        return p1Score;
    }

    public Color getP1Color() {
        return p1Color;
    }

    public int getP1Diff() {
        return p1Diff;
    }

    public String getP2Name() {
        return p2Name;
    }

    public int getP2Score() {
        return p2Score;
    }

    public Color getP2Color() {
        return p2Color;
    }

    public int getP2Diff() {
        return p2Diff;
    }

    public Color[][] getMx() {
        return mx;
    }

    public boolean isFirstP() {
        return firstP;
    }

    // Csak a teszteléshez kell
    public void setColorAt(int r, int c, Color c1) {
        mx[r][c] = c1;
    }
}
