package game;

import components.Table;
import java.awt.Color;

// Neki mindig 4 lesz a difficulty-a
public class Intermediate extends Bot {

    public Intermediate(String name, Color color, int difficultyLevel) {
        super(name, color, difficultyLevel); // Meghívja a Bot konstruktorát
    }

    @Override
    public int makeMove(Table table) {
        // 1. Ellenőrizzük, van-e nyerő lépés
        for (int col = 0; col < table.column; col++) {
            if (isValidMove(table, col)) {
                Table copy = simulateMove(table, col, getColor()); 
                if (copy.hasFourInARow(getColor())) {
                    return col; // Válassza a nyerő lépést
                }
            }
        }

        // 2. Blokkoljuk az ellenfelet, ha szükséges
        Color otherC = (getColor().equals(Color.red)) ? Color.yellow : Color.red;
        for (int col = 0; col < table.column; col++) {
            if (isValidMove(table, col)) {
                Table copy = simulateMove(table, col, otherC); // Az ellenfél színével
                if (copy.hasFourInARow(otherC)) {
                    return col; // Blokkoljuk az ellenfelet
                }
            }
        }

        // 3. Ha nincs nyerő vagy blokkolandó lépés, válasszon véletlenszerűen
        return new Beginner(name, color, difficultyLevel).makeMove(table);
    }

    private Table simulateMove(Table table, int col, Color color) {
        // Lemásoljuk a táblát, és szimuláljuk a lépést
        Table copy = new Table(table.row, table.column);
        for (int r = 0; r < table.row; r++) {
            System.arraycopy(table.mx[r], 0, copy.mx[r], 0, table.column); // Egyszűbb így a másolás
        }
        // Helyezze el a korongot a szimulált oszlopban
        for (int r = table.row - 1; r >= 0; r--) {
            if (copy.mx[r][col] == Color.WHITE) {
                copy.mx[r][col] = color;
                break;
            }
        }
        return copy;
    }

    private boolean isValidMove(Table table, int col) {
        return table.mx[0][col] == Color.WHITE;
    }
}
