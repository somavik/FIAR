package game;
import components.Table;
import java.awt.*;

public abstract class Player {
    protected  String name;
    protected  int score;
    protected  Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.score = 0; 
    }

    // Getterek, setterek, printer
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void increaseScore() {
        this.score++;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', score=" + score + ", color='" + color + "'}";
    }

    // Később lesznek definiálva
    public abstract int makeMove(Table table);

    public abstract int getDifficultyLevel();
}

