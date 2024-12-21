package test;

import components.Table;
import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Play;
import ui.TableFrame;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

class GameTest {
    // Teszteléshez szükséges adattagok
    private Game game;
    private Human p1;
    private Bot p2;
    private Table table;
    private Leaderboard leaderboard;
    private File tempFileL;
    private File tempFileGD;
    private GameData gameData;
    private TableFrame tableFrame;

    @BeforeEach
    void setUp() {
        // init
        p1 = new Human("Player1", Color.RED);
        p2 = new Beginner("Bot", Color.YELLOW, 1);
        game = new Game(p1, p2);
        table = new Table(6, 7);
        leaderboard = new Leaderboard();
        leaderboard.addOrUpdatePlayer("Alice", 10);
        leaderboard.addOrUpdatePlayer("Bob", 15);
        try{
            tempFileL = File.createTempFile("leaderboard", ".dat");
            tempFileGD = File.createTempFile("gameData", ".dat");
        }
        catch (IOException e) {
            fail("Error reading from file: " + e.getMessage());
        }
        tempFileL.deleteOnExit();
        tempFileGD.deleteOnExit();

        Color[][] mx = new Color[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                mx[i][j] = Color.WHITE; // üres
            }
        }
        gameData = new GameData(6, 7, "Alice", 3, Color.RED, 1, "Bob", 5, Color.YELLOW, 2, mx, true);
        tableFrame = new TableFrame(6, 7, game, new Play());
    }

    @Test
    void testGetPlayerByColorRed() {
        Player player = game.getPlayerByColor(Color.RED);
        assertEquals(p1, player, "Player with Red color should be Player1.");
    }

    @Test
    void testGetPlayerByColorYellow() {
        Player player = game.getPlayerByColor(Color.YELLOW);
        assertEquals(p2, player, "Player with Yellow color should be Bot.");
    }

    @Test
    void testGetOtherPlayerByColorYellow() {
        Player player = game.getOtherPlayerByColor(Color.YELLOW);
        assertEquals(p1, player, "The other player of Red should be Player1.");
    }

    @Test
    void testSwitchColor() {
        game.switchColors();
        assertEquals(p1.getColor(), Color.YELLOW, "The player of Yellow should be Player1.");
        assertEquals(p2.getColor(), Color.RED, "The player of Red should be Bot1.");
    }

    @Test
    void testWinningConditions() {
        // Vízszintes nyerés
        table.setColorAt(0, 0, Color.RED);
        table.setColorAt(0, 1, Color.RED);
        table.setColorAt(0, 2, Color.RED);
        table.setColorAt(0, 3, Color.RED);

        assertTrue(table.hasFourInARow(Color.RED), "Horizontal win should be detected.");

        // Függőleges nyerés
        table.setColorAt(3, 1, Color.RED);
        table.setColorAt(3, 1, Color.RED);
        table.setColorAt(4, 1, Color.RED);
        table.setColorAt(5, 1, Color.RED);

        assertTrue(table.hasFourInARow(Color.RED), "Vertical win should be detected.");

        // Átlós nyerés
        table.setColorAt(5, 2, Color.RED);
        table.setColorAt(4, 3, Color.RED);
        table.setColorAt(3, 4, Color.RED);
        table.setColorAt(2, 5, Color.RED);

        assertTrue(table.hasFourInARow(Color.RED), "Diagonal win (bottom-left to top-right) should be detected.");

        // Másik írányba
        table.setColorAt(5, 6, Color.RED);
        table.setColorAt(4, 5, Color.RED);
        table.setColorAt(3, 4, Color.RED);
        table.setColorAt(2, 3, Color.RED);

        assertTrue(table.hasFourInARow(Color.RED), "Diagonal win (bottom-right to top-left) should be detected.");
    }

    @Test
    void testIsTableFull() {
        // Tábla tele töltése
        for (int c = 0; c < 7; c++) {
            for (int r = 0; r < 6; r++) {
                table.setColorAt(r, c, Color.RED);
            }
        }

        assertTrue(table.isTableFull(), "The table should be full after all columns are filled.");
    }

    @Test
    void testLoadFromFile() {
        leaderboard.saveToFile(tempFileL.getAbsolutePath());

        // Új Leaderboard betöltése a tempfile-ből (ahova ki lett mentve az eredeti)
        Leaderboard newLeaderboard = new Leaderboard();
        newLeaderboard.loadFromFile(tempFileL.getAbsolutePath());

        // newLeaderboard játékosai = eredeti játékosai (elvileg)
        Map<String, Integer> loadedPlayers = newLeaderboard.getPlayers();
        assertEquals(2, loadedPlayers.size(), "Leaderboard should contain 2 players after loading.");
        assertTrue(loadedPlayers.containsKey("Alice"), "Alice should be in the leaderboard.");
        assertTrue(loadedPlayers.containsKey("Bob"), "Bob should be in the leaderboard.");
        assertEquals(10, loadedPlayers.get("Alice"), "Alice's score should be 10 after loading.");
        assertEquals(15, loadedPlayers.get("Bob"), "Bob's score should be 15 after loading.");
    }

    @Test
    void testLoadGameData() {
        // Mentés
        gameData.saveGameData(gameData, tempFileGD.getAbsolutePath());

        // Betöltés
        GameData loadedGameData = gameData.loadGameData(tempFileGD.getAbsolutePath());

        // Siker?
        assertNotNull(loadedGameData, "Loaded game data should not be null.");

        // Elvileg megegyeznek az adatok
        assertEquals(gameData.getRow(), loadedGameData.getRow(), "Rows should match.");
        assertEquals(gameData.getColumn(), loadedGameData.getColumn(), "Columns should match.");
        assertEquals(gameData.getP1Name(), loadedGameData.getP1Name(), "Player 1's name should match.");
        assertEquals(gameData.getP1Score(), loadedGameData.getP1Score(), "Player 1's score should match.");
        assertEquals(gameData.getP1Color(), loadedGameData.getP1Color(), "Player 1's color should match.");
        assertEquals(gameData.getP1Diff(), loadedGameData.getP1Diff(), "Player 1's difficulty should match.");
        assertEquals(gameData.getP2Name(), loadedGameData.getP2Name(), "Player 2's name should match.");
        assertEquals(gameData.getP2Score(), loadedGameData.getP2Score(), "Player 2's score should match.");
        assertEquals(gameData.getP2Color(), loadedGameData.getP2Color(), "Player 2's color should match.");
        assertEquals(gameData.getP2Diff(), loadedGameData.getP2Diff(), "Player 2's difficulty should match.");
        assertEquals(gameData.isFirstP(), loadedGameData.isFirstP(), "It should be player 1's turn.");
    }

    @Test
    void testLoadGameDataToTableFrame() {
        gameData.setColorAt(5, 0, Color.RED);
        gameData.setColorAt(5, 1, Color.RED);

        // Mentés
        gameData.saveGameData(gameData, tempFileGD.getAbsolutePath());

        // Betöltés
        GameData loadedGameData = gameData.loadGameData(tempFileGD.getAbsolutePath());

        // TableFrame table-be töltés
        tableFrame.loadTable(loadedGameData.getMx());

        // Disk elhelyezése
        tableFrame.addDisk(1, Color.YELLOW);

        assertEquals(tableFrame.getTable().getColorAt(5, 0),Color.red );
        assertEquals(tableFrame.getTable().getColorAt(5, 1),Color.red );
        assertEquals(tableFrame.getTable().getColorAt(4, 1),Color.yellow );
    }

    @Test
    void testDiskPrev() {
        // Kettő sárga ledobása
        tableFrame.addDisk(0, Color.YELLOW);
        tableFrame.addDisk(0, Color.YELLOW);

        // Most jöjjön egy piros
        tableFrame.addDiskPrev(0, Color.RED);

        // Táblában megjelenik a két sárga
        assertEquals(tableFrame.getTable().getColorAt(5, 0),Color.yellow);
        assertEquals(tableFrame.getTable().getColorAt(4, 0),Color.yellow);

        // A piros viszont nem
        assertEquals(tableFrame.getTable().getColorAt(3, 0),Color.white);

        // Az csak grafikusan
        assertEquals(tableFrame.getPanelmx()[3][0].getDiskColor(),Color.RED);
        // Fölfelé világosabban jelenik meg
        int r = 2;
        while(r >0){
            assertEquals(tableFrame.getPanelmx()[r][0].getDiskColor(), new Color(255, 173, 176));
            r--;
        }
    }
}
