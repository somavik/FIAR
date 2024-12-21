package ui;
import game.Beginner;
import game.Bot;
import game.Game;
import game.Human;
import game.Intermediate;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Play extends JFrame{
    private final JPanel mainP;
    private final JPanel topPanel;
    private final JTextField player;
    private final JComboBox<String> bot;
    private final JComboBox<String> color;
    private final JTextField row;
    private final JTextField column;

    // PLay Bot
    class PBActionListener implements ActionListener{
        // Szükség van az előző frame-re, hogy a back (<) gombbal visszalehessen lépni az előbbi frame-re
        private final JFrame parentFrame;
        public PBActionListener(Play parentFrame) {
            this.parentFrame = parentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Játék megalkotása a kiválasztott értékek alapján
            String humanName;
            if(player.getText().isEmpty()){
                humanName = "Guest"; // Alapértelmezett
            }
            else{
                humanName = player.getText();
            }
            Color humanColor;
            String selectedeColor = (String) color.getSelectedItem();
            switch (selectedeColor){
                case "Random color":
                    Random rand = new Random();
                    int randi = rand.nextInt(2);
                    humanColor = (randi==1) ?  Color.red : Color.yellow;
                    break;
                case "Red":
                    humanColor = Color.red;
                    break;
                default:
                    humanColor = Color.yellow;
                    break;
            }

            String botName = (String)bot.getSelectedItem();
            Color botColor = (humanColor.equals(Color.red)) ? Color.yellow : Color.red;
            int botDiff = (botName.equals("Beginner")) ? 1 : 4;

            Human humanP = new Human(humanName, humanColor);
            Bot botP;
            if(botDiff==1){
                botP = new Beginner(botName, botColor, botDiff); // Beginnernek 1
            }
            else{
                botP = new Intermediate(botName, botColor, botDiff); // Intermediate-nak 4 a nehézsége
            }
            Game game = new Game(humanP, botP);

            int rowS;
            int collS;
            if(row.getText().isEmpty() || column.getText().isEmpty()){
                rowS=6;
                collS=7;
            }
            else{
                rowS = Integer.parseInt(row.getText());
                collS = Integer.parseInt(column.getText());
            }
            
            if(rowS<=12 && collS <=22 && rowS!=0 && collS!=0 && (rowS>3 || collS>3)){
                TableFrame tableframe = new TableFrame(rowS, collS, game, parentFrame); // Ha validak a méretek, kezdetét veszi a játék
                tableframe.setVisible(true);
                setVisible(false);
            }
            else{
                JTextField error = new JTextField("Valid méretet adj meg!"); // Különben hibaüzenet
                error.setFont(new Font("Arial", Font.BOLD, 20));
                error.setOpaque(true);
                error.setBackground(Color.red);
                add(error, BorderLayout.SOUTH);
                error.setVisible(true);
                parentFrame.revalidate();
            }
        }
    }

    public Play() {
        setTitle("Four in a row");
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Color.CYAN);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screenSize);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.CYAN);
        add(topPanel, BorderLayout.NORTH);

        // Vissza button
        JButton mm = new JButton("<");
        mm.setPreferredSize(new Dimension(45, 45));
        topPanel.add(mm, BorderLayout.WEST);
        mm.addActionListener(new MmActionListener(this));

        // Menü sáv beállítása
        JMenuBar topMenuBar = new JMenuBar();   
        JMenu menu = new JMenu("    Options    "); 
        JMenuItem menui = new JMenuItem("Main Menu");  
        menui.addActionListener(new MmActionListener(this));  
        menu.add(menui);  
        topMenuBar.add(menu);  
        topPanel.add(topMenuBar, BorderLayout.EAST);  
        
        // Fő panel létrehozása
        mainP = new JPanel(new GridBagLayout());
        mainP.setBackground(Color.CYAN);
        add(mainP, BorderLayout.CENTER);

        // Label-el létrehozása
        JLabel playerName = createLabel("Player name:", new Color(75, 54, 157));
        JLabel chooseBot = createLabel("Choose bot:", new Color(112, 54, 157));
        JLabel chooseColor = createLabel("Choose Color:", new Color(193, 28, 132));
        JLabel tableSize = createLabel("Choose table size:", new Color(200, 21, 139));
        JLabel x = new JLabel("X");
        x.setFont(new Font("Arial", Font.BOLD, 13));
        x.setPreferredSize(new Dimension(30, 30));
        x.setOpaque(true);
        x.setBackground(Color.cyan);
        x.setHorizontalAlignment(SwingConstants.CENTER);

        // Textfield és comboboxok
        player = new JTextField();
        player.setPreferredSize(new Dimension(115, 30));
        String[] t = {"Beginner", "Intermediate"};
        bot = new JComboBox<>(t);
        bot.setPreferredSize(new Dimension(115, 30));
        String[] tt = {"Red", "Yellow", "Random color"};
        color = new JComboBox<>(tt);
        color.setPreferredSize(new Dimension(115, 30));
        row = new JTextField();
        row.setPreferredSize(new Dimension(30, 30));
        column = new JTextField();
        column.setPreferredSize(new Dimension(30, 30));

        // GridBagConstraints konfigurálása
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 20, 10); // Margók a komponensek körül
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Ne nyújtsa ki automatikusan

        // Gombok elhelyezése
        mainP.add(playerName, gbc);
        gbc.gridx++;
        mainP.add(player, gbc);
        gbc.gridy++;

        gbc.gridx--;
        mainP.add(chooseBot, gbc);
        gbc.gridx++;
        mainP.add(bot, gbc);
        gbc.gridy++;

        gbc.gridx--;
        mainP.add(chooseColor, gbc);
        gbc.gridx++;
        mainP.add(color, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainP.add(tableSize, gbc);
        gbc.gridx++;
        mainP.add(row, gbc);
        gbc.gridx++;
        gbc.insets = new Insets(20, 0, 20, 45); // Margók a komponensek körül, mert ez az x nem akar viselkedni
        mainP.add(x, gbc);
        gbc.gridx++;
        mainP.add(column, gbc);

        // Play gomb hozzáadása
        JButton playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(150, 60)); 
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(new Color(150, 20, 100));
        playButton.setOpaque(true);
        playButton.addActionListener(new PBActionListener(this));
         
        // A gomb pozícionálása
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2; // Két oszlop szélesség
        gbc.insets = new Insets(50, 0, 0, 20); // Extra margó fent
        mainP.add(playButton, gbc);

        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    

    private JLabel createLabel(String text, Color bgColor) {
        JLabel field = new JLabel(text);
        field.setPreferredSize(new Dimension(120, 60));
        field.setFont(new Font("Arial", Font.BOLD, 13));
        field.setForeground(Color.WHITE);
        field.setOpaque(true);
        field.setBackground(bgColor);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        return field;
    }
}
