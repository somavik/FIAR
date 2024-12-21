package game;
import components.Table;
import java.awt.*;

// Neki mindig 0 lesz a difficulty-a (típuslekérdezés-t segíti)
public class Human extends Player {
    public Human(String name, Color color) {
        super(name, color); // Meghívja a Player konstruktorát
    }

    @Override
    public int getDifficultyLevel() {
        return 0;
    }

    // Azért kell, hogy lehessen példányosítani, de nem használatos  
    @Override
    public int makeMove(Table table){
        return 0;
    }

    // Esetleg további metódusok a Human specifikus funkciókra
    @Override
    public String toString() {
        return "Human{name='" + getName() + "', score=" + getScore() + ", color='" + getColor() + "'}";
    }
}