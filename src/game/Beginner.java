package game;
import components.Table;
import java.awt.*;
import java.util.Random;

// Neki mindig 1 lesz a difficulty-a
public class Beginner extends Bot {

    public Beginner(String name, Color color, int difficultyLevel) {
        super(name, color, difficultyLevel); // Meghívja a Bot konstruktorát
    }

    @Override
    public int makeMove(Table table) {
        Random rand = new Random();
        int col;
        do {
            col = rand.nextInt(table.column); // Véletlenszerű oszlop
        } while (!isValidMove(table, col)); // Csak érvényes oszlopokra
        return col;
    }

    private boolean isValidMove(Table table, int col) {
        // Ellenőrizze, hogy az oszlopban van-e még hely
        return table.mx[0][col] == Color.WHITE;
    }
}
