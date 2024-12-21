package ui;
import game.Beginner;
import game.Game;
import game.GameData;
import game.Human;
import game.Intermediate;
import game.Player;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class MainMenu extends JFrame {
    private final JPanel mainP;
    private final JLabel titleL;

    // Play
    class PActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Play p = new  Play();
            p.setVisible(true);
            setVisible(false);
        }
    }

    // Two players
    class TPActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            TwoPlayers p = new  TwoPlayers();
            p.setVisible(true);
            setVisible(false);
        }
    }

    // Leaderboard
    class LActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            LeaderboardFrame lf = new LeaderboardFrame();
            lf.setVisible(true);
            setVisible(false);
        }
    }

    // Load
    class LoadActionListener implements ActionListener{
        JFrame pFrame;
        public LoadActionListener(JFrame parentFrame) {
            this.pFrame = parentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            GameData gameData = new GameData(0, 0, "", 0, Color.BLACK,0, "", 0, Color.BLACK,0, new Color[0][0], true);

            // JFileChooser létrehozása
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Game");

            // Saves directory-ból indul
            File defaultDirectory = new File("saves");
            fileChooser.setCurrentDirectory(defaultDirectory);

            // A betöltés helyének kiválasztása
            int userSelection = fileChooser.showOpenDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                String filePath = fileToLoad.getAbsolutePath();

                // GameData betöltése
                gameData = gameData.loadGameData(filePath);

                // Kiírás, hogy sikeresen betöltődött a játék
                if (gameData != null) {
                    pFrame.setVisible(false); // Ha igen, akkor nem kell a Main Menu
                } else {
                    System.out.println("Error: Unable to load the game data.");
                }
            }

            // TableFrame megalkotása a betöltött adatokból
            // Players->Game->TableFrame
            Player player1;
            switch (gameData.getP1Diff()){
                case 0:
                    player1 = new Human(gameData.getP1Name(), gameData.getP1Color());
                    player1.setScore(gameData.getP1Score()); 
                    break;
                case 1:
                    player1 = new Beginner(gameData.getP1Name(), gameData.getP1Color(), gameData.getP1Diff());
                    player1.setScore(gameData.getP1Score()); 
                    break;
                default:
                    player1 = new Intermediate(gameData.getP1Name(), gameData.getP1Color(), gameData.getP1Diff());
                    player1.setScore(gameData.getP1Score()); 
                    break;
            }

            Player player2;
            switch (gameData.getP2Diff()){
                case 0:
                    player2 = new Human(gameData.getP2Name(), gameData.getP2Color());
                    player2.setScore(gameData.getP2Score()); 
                    break;
                case 1:
                    player2 = new Beginner(gameData.getP2Name(), gameData.getP2Color(), gameData.getP2Diff());
                    player2.setScore(gameData.getP2Score()); 
                    break;
                default:
                    player2 = new Intermediate(gameData.getP2Name(), gameData.getP2Color(), gameData.getP2Diff());
                    player2.setScore(gameData.getP2Score()); 
                    break;
            }

            boolean bvh= false;
            if(player1.getDifficultyLevel()>0 || player2.getDifficultyLevel()>0){
                bvh = true;
            }

            Game game = new Game(player1,player2, bvh);

            JFrame parentF;
            if(bvh){
                parentF = new Play(); 
            }
            else{
                parentF = new TwoPlayers();
            }

            TableFrame tf = new TableFrame(gameData.getRow(),gameData.getColumn(), game, parentF, gameData.isFirstP());
            tf.loadTable(gameData.getMx()); // Már színes korongog kirajzolása
            tf.revalidate();
            tf.repaint();
            tf.setVisible(true);
        }
    }

    public MainMenu() {
        setTitle("Four in a row");
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Color.CYAN);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screenSize);
        
        // Cím hozzáadása (felül)
        titleL = new JLabel("Four in a row", SwingConstants.CENTER);
        titleL.setFont(new Font("Arial", Font.BOLD, 50));
        titleL.setForeground(new Color(0, 0, 139));
        titleL.setOpaque(true); // Opaque kell a háttérszínhez
        titleL.setBackground(Color.CYAN);
        add(titleL, BorderLayout.NORTH);

        // Háttérkép betöltése és méretezése
        ImageIcon originalIcon = new ImageIcon("mm2.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(1548, 177, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        titleL.setIcon(scaledIcon);
        titleL.setHorizontalTextPosition(SwingConstants.CENTER);
        titleL.setVerticalTextPosition(SwingConstants.CENTER);

        // Fő panel létrehozása
        mainP = new JPanel(new GridBagLayout());
        mainP.setBackground(Color.CYAN);
        add(mainP, BorderLayout.CENTER);

        // Gombok létrehozása
        JButton play = createButton("Play", new Color(100, 150, 255));
        play.addActionListener(new PActionListener());
        JButton twoPlayers = createButton("Two players", new Color(60, 125, 231));
        twoPlayers.addActionListener(new TPActionListener());
        JButton load = createButton("Load", new Color(75, 54, 157));
        load.addActionListener(new LoadActionListener(this));
        JButton leaderBoard = createButton("Leaderboard", new Color(112, 54, 157));
        leaderBoard.addActionListener(new LActionListener());
        JButton exit = createButton("Exit", new Color(193, 28, 132));
        exit.addActionListener(e -> dispose()); // Bezárja az ablakot

        // GridBagConstraints konfigurálása
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Margók a komponensek körül
        gbc.anchor = GridBagConstraints.CENTER;

        // Gombok elhelyezése
        mainP.add(play, gbc);
        gbc.gridy++;
        mainP.add(twoPlayers, gbc);
        gbc.gridy++;
        mainP.add(load, gbc);
        gbc.gridy++;
        mainP.add(leaderBoard, gbc);
        gbc.gridy++;
        mainP.add(exit, gbc);

        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    // Segéd fg hasonló gombok létrehozásására
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(115, 60));
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBackground(bgColor);
        return button;
    }

}