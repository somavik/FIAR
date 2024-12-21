package game;
import java.io.*;
import java.util.*;

public class Leaderboard {
    private Map<String, Integer> players; // Játékosok listája

    public Leaderboard() {
        players = new LinkedHashMap<>(); // Ha nincs adat, üres map
    }

    public void loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Map<String, Integer> loadedPlayers = (Map<String, Integer>) in.readObject();
            players.putAll(loadedPlayers); // Feltöltjük a Leaderboard-ot a fájlból beolvasott adatokkal
            System.out.println("Leaderboard loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(players); // Az aktuális játékosok mentése
            System.out.println("Leaderboard saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    // Add player to leaderboard
    public void addOrUpdatePlayer(String playerName, int score) {
        if (players.containsKey(playerName)) {
        players.put(playerName, players.get(playerName) + score);  // Frissíti a játékos score-ját
        } 
        else {
            players.put(playerName, score);  // Ha nem létezik, akkor hozzáadja az új játékost a score-val
        }
        sort();
    }

    // Az eredeti map-et rendezett lista alapján újratöltjük
    public void sort() {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(players.entrySet());
        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Érték szerint csökkenő sorrend
    
        // Töröljük az eredeti térképet és hozzáadjuk a rendezett elemeket
        players.clear();
        for (Map.Entry<String, Integer> entry : entryList) {
            players.put(entry.getKey(), entry.getValue());
        }
    }

    // Getter, printer
    public Map<String, Integer> getPlayers() {
        return players;
    }

    public void printLeaderboard() {
        System.out.println("Leaderboard:");
        for (Map.Entry<String, Integer> entry : players.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " points");
        }
    }
}

