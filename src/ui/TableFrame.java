package ui;
import components.Table;
import game.Game;
import game.GameData;
import game.Leaderboard;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.*;

public class TableFrame extends JFrame {
    JFrame previousFrame; // Előzö frame (< -hez kell)
    Game game; // Játék adatai 
    Table table; // Tábla állapota
    DiskPanel [][]panelmx; // Tábla állapota a grafikus intetface-en 
    int row; // Table-ből átvett adatok
    int column;
    Leaderboard leaderboard; // Hogy ki lehessen menteni a játék végén
    // Felépítő panelek
    JPanel topPanel;
    JPanel centerPanel;
    JPanel bottomPanel;
    // segéd változók
    boolean firstP; // Piros jön-e?
    int releasedC; // Melyik oszlopban engedték el?
    boolean action = false; // Nyerés vagy döntetlen van? Ekkor nem szabad elmenteni a játékot

    // < gomb
    class BActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            previousFrame.setVisible(true);
            setVisible(false);
        }
    }

    // Save
    class SActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            // Ha nincs győzés vagy döntetlen, el lehet menteni
            if(!action){
                // Szükséges adatok lekérése
                int row = table.row; 
                int column = table.column;
                String p1Name = game.getPlayers()[0].getName();
                int p1Score = game.getPlayers()[0].getScore();
                Color p1Color = game.getPlayers()[0].getColor();
                int p1Diff = game.getPlayers()[0].getDifficultyLevel();
                String p2Name = game.getPlayers()[1].getName();
                int p2Score = game.getPlayers()[1].getScore();
                Color p2Color = game.getPlayers()[1].getColor();
                int p2Diff = game.getPlayers()[1].getDifficultyLevel();
                Color[][] mx = table.mx;
                boolean first = firstP;
                //Ezt mind egy gameData-ba
                GameData gameData = new GameData(row, column, p1Name, p1Score, p1Color, p1Diff, p2Name, p2Score , p2Color, p2Diff, mx,first);

                // JFileChooser létrehozása
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Game");

                File defaultDirectory = new File("saves");
                fileChooser.setCurrentDirectory(defaultDirectory);

                // A mentés helyének kiválasztása
                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    // Ellenőrizzük, hogy van-e kiterjesztés, ha nincs, adjuk hozzá a .dat-ot
                    if (!filePath.endsWith(".dat")) {
                        filePath += ".dat";
                    }
                    // Játék mentése
                    gameData.saveGameData(gameData, filePath);
                }
            }
        }
    }
    
    // Alapértelmezett ctor, ha nincs megadva fistP-re érték
    public TableFrame(int r, int c, Game game, JFrame pFrame){
        this(r, c, game, pFrame, true);  
        // Mindig igaz, hogy a piros kezd, de
        // lehet ez másképp, ha betöltünk egy játékot amiben a sárga jön épp
    } 

    public TableFrame(int r, int c, Game game, JFrame pFrame, boolean first) {
        // Frame beállításai
        setTitle("Four in a Row");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.CYAN);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screenSize);

        firstP = first; // először az első játékos kezd (ha nincs megadva más betöltéskor)
        this.game = game;
        this.row = r;
        this.column = c;
        leaderboard = new Leaderboard();
        this.leaderboard.loadFromFile("leaderboard.dat");
        this.leaderboard.printLeaderboard();
        previousFrame = pFrame;
        table = new Table(r, c); // Az alap táblát létrehozzuk és beállítjuk

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.CYAN);
        add(topPanel, BorderLayout.NORTH);

        // Exit gomb
        JButton back = new JButton("<");
        back.setPreferredSize(new Dimension(45, 45));
        topPanel.add(back, BorderLayout.WEST); // Balra kerül
        back.addActionListener(new BActionListener());

        JPanel playersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playersPanel.setBackground(Color.CYAN);
        topPanel.add(playersPanel);

        // Mindkét játékoshoz tartozik egy diskpanel az ő színével
        DiskPanel panel1 = new DiskPanel(-1, null);
        panel1.setDiskColor(game.getPlayers()[0].getColor());
        panel1.setBackground(Color.cyan);
        panel1.setPreferredSize(new Dimension(30, 30));
        playersPanel.add(panel1);

        Font font = new Font("Arial", Font.BOLD, 13);
        String player1Name = game.getPlayers()[0].getName() + (game.getPlayers()[0].getDifficultyLevel()==0 ? "" : "(bot)");
        JLabel player1 = new JLabel(player1Name + ":");
        player1.setFont(font);
        playersPanel.add(player1);

        JLabel player1Score = new JLabel(String.valueOf(game.getPlayers()[0].getScore()));
        player1Score.setFont(font);
        playersPanel.add(player1Score);

        JLabel spacer = new JLabel("                            ");
        playersPanel.add(spacer);

        DiskPanel panel2 = new DiskPanel(-1, null);
        panel2.setDiskColor(game.getPlayers()[1].getColor());
        panel2.setBackground(Color.cyan);
        panel2.setPreferredSize(new Dimension(30, 30));
        playersPanel.add(panel2);

        String player2Name = game.getPlayers()[1].getName() + (game.getPlayers()[1].getDifficultyLevel()==0 ? "" : "(bot)");
        JLabel player2 = new JLabel(player2Name + ":");
        player2.setFont(font);
        playersPanel.add(player2);

        JLabel player2Score = new JLabel(String.valueOf(game.getPlayers()[1].getScore()));
        player2Score.setFont(font);
        playersPanel.add(player2Score);

        JMenuBar topMenuBar = new JMenuBar();   // Menü sáv létrehozása
        JMenu menu = new JMenu("    Options    ");  // Menü létrehozása
        JMenuItem menui = new JMenuItem("Main Menu");  // Menüelem létrehozása
        menui.addActionListener(new MmActionListener(this));  // Akció hozzáadása
        JMenuItem save = new JMenuItem("Save");  
        save.addActionListener(new SActionListener());  
        menu.add(menui);  // Menüelemek hozzáadása a menühöz
        menu.add(save);
        topMenuBar.add(menu);  // Menü hozzáadása a menüsávhoz
        topPanel.add(topMenuBar, BorderLayout.EAST);  // Menü sáv hozzáadása a topPanel-hez

        // JPanel, ahol a táblát kirajzoljuk
        JPanel mainPanel = new JPanel(new GridLayout(row, column));
        Dimension panelSize = new Dimension(column * 100, row * 100);
        Dimension panelSizeMin = new Dimension(column * 70, row * 70);
        // A panel fix méretének beállítása
        mainPanel.setPreferredSize(panelSize);
        mainPanel.setMinimumSize(panelSizeMin);

        panelmx = new DiskPanel[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                panelmx[i][j] = new DiskPanel(j, this); // Minden cella egy egyedi DiskPanel
                panelmx[i][j].setDiskColor(Color.WHITE);
                mainPanel.add(panelmx[i][j]);
            }
        }
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.cyan);

        // GridBagConstraints konfigurálása
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0); // Margók a komponensek körül
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Ne nyújtsa ki automatikusan

        gbc.gridy++;
        centerPanel.add(mainPanel,gbc);
        add(centerPanel);  // Központi tartalom

        // Ha a bot piros és a piros kezd, akkor ő lép először
        if(game.getPlayerByColor(Color.red).getDifficultyLevel()!=0 && firstP){
            int move = game.getPlayerByColor(Color.red).makeMove(table);
            addDisk(move, Color.RED);
            firstP=!firstP; // játékos váltás
        }

        // Kezdetben üres, win és draw -kor kerül rá tartalom
        bottomPanel = null;

        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    // Korong hozzáadása egy oszlophoz
    public void addDisk(int column, Color color) {
        for (int r = row-1; r >= 0; r--) { // Alulról felfelé keresünk helyet
            if (table.mx[r][column] == Color.WHITE) { // Üres helyet keresünk
                table.mx[r][column] = color;
                panelmx[r][column].setDiskColor(color); // Frissítjük a panelt a változással
                break;
            }
        }
    }

    // Load-nál van szerepe
    public void loadTable(Color [][] mix){
        for(int r=0; r<row; r++){
            for(int c=0; c<column; c++){
                if(!mix[r][c].equals(Color.white)){
                    panelmx[r][c].setDiskColor(mix[r][c]); 
                    table.mx[r][c]=mix[r][c];
                }
            }
        }
    }

    public void addDiskPrev(int column, Color color) {
        // Ha tele van az oszlop ret
        if(table.mx[0][column] != Color.white){
            return;
        }
        Color bc; // brighter color
        if(color == Color.white){
            bc = color; 
        }
        else if(color == Color.gray){
            bc = color.brighter();
        }
        else {
            bc = (firstP) ? new Color(255, 173, 176) : new Color(255, 255,160);
        }

        for (int r = row-1; r >=0; r--) { 
            if (table.mx[r][column] == Color.WHITE) { // Üres helyet keresünk
                panelmx[r][column].setDiskColor(color); // A legutolsó sorban az eredeti szín jeleni meg
                while(r >0){
                    panelmx[r-1][column].setDiskColor(bc); // A fentiekben világosabb 
                    r--;
                }
            }
        }
    }

    // Win és draw-kor kell, hogy ne lehessen piszkálni a táblát már
    public void removeAllListeners() {
        for (int i = 0; i < panelmx.length; i++) {
            for (int j = 0; j < panelmx[i].length; j++) {
                for (MouseListener listener : panelmx[i][j].getMouseListeners()) {
                    panelmx[i][j].removeMouseListener(listener);
                }
            }
        }
    }

    public Table getTable() {
        return table;
    }

    public DiskPanel[][] getPanelmx() {
        return panelmx;
    }
    
}
