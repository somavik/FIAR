package game;

import components.Table;
import java.awt.*;

public abstract class Bot extends Player {
    protected  int difficultyLevel;

    public Bot(String name, Color color, int difficultyLevel) {
        super(name, color); // Meghívja a Player konstruktorát
        this.difficultyLevel = difficultyLevel;
    }

    // Ő nem tud lépni
    @Override
    public abstract int makeMove(Table table);

    // Getter, setter, printer
    @Override
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public String toString() {
        return "Bot{name='" + getName() + "', score=" + getScore() + 
               ", color='" + getColor() + "', difficultyLevel='" + difficultyLevel + "'}";
    }
}

