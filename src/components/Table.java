package components;

import java.awt.Color;
import java.io.Serializable;

public class Table implements Serializable{
    public final Color[][] mx;  // a mx nem, de a benne tárolt színek változtathatók
    public final int row;
    public final int column;

    public Table(int r, int c) {
        row = r;
        column = c;
        mx = new Color[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                mx[i][j] = Color.WHITE; // Alapértelmezett,üres szín
            }
        }
    }

    public boolean hasFourInARow(Color player) {
        // 1. Sorok ellenőrzése (vízszintes)
        for (int r = 0; r < row; r++) {
            for (int c = 0; c <= column - 4; c++) {
                if (mx[r][c].equals(player) && mx[r][c + 1].equals(player) &&
                    mx[r][c + 2].equals(player) && mx[r][c + 3].equals(player)) {
                    return true;
                }
            }
        }
        // 2. Oszlopok ellenőrzése (függőleges)
        for (int c = 0; c < column; c++) {
            for (int r = 0; r <= row - 4; r++) {
                if (mx[r][c].equals(player) && mx[r + 1][c].equals(player) &&
                    mx[r + 2][c].equals(player) && mx[r + 3][c].equals(player)) {
                    return true;
                }
            }
        }
        // 3. Átlók ellenőrzése (balról-jobbra le)
        for (int r = 0; r <= row - 4; r++) {
            for (int c = 0; c <= column - 4; c++) {
                if (mx[r][c].equals(player) && mx[r + 1][c + 1].equals(player) &&
                    mx[r + 2][c + 2].equals(player) && mx[r + 3][c + 3].equals(player)) {
                    return true;
                }
            }
        }
        // 4. Átlók ellenőrzése (jobbról-balra le)
        for (int r = 0; r <= row - 4; r++) {
            for (int c = 3; c < column; c++) {
                if (mx[r][c].equals(player) && mx[r + 1][c - 1].equals(player) &&
                    mx[r + 2][c - 2].equals(player) && mx[r + 3][c - 3].equals(player)) {
                    return true;
                }
            }
        }
        // Ha sehol sincs négy egy sorban
        return false;
    }

    public boolean isColumnFull(int col) {
        return mx[0][col] != Color.white; // Ha az oszlop tetején már van korong, akkor tele van
    }

    public boolean isTableFull() {
        for (int c = 0; c < column; c++){
            if(!isColumnFull(c)) return false;  // Ha van olyan oszlop aminek a tetején nincs korong, akkor még van hely
        }
        return true;
    }

    // Getter, setter, printer
    public Color getColorAt(int r, int c){
        return mx[r][c];
    }

    public void setColorAt(int r, int c, Color col){
        mx[r][c]=col;
    }

    public void printBoard() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print((mx[i][j] == Color.WHITE ? "." : "O") + " ");
            }
            System.out.println();
        }
    }
}
