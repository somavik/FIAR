package ui;
import game.Game;
import game.Human;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class TwoPlayers extends JFrame{
    private final JPanel mainP;
    private final JPanel topPanel;
    private final JTextField player1;
    private final JTextField player2;
    private final JComboBox<String> color;
    private final JTextField row;
    private final JTextField collumn;
    
    // PLay Human
    class PHActionListener implements ActionListener{
        JFrame parentFrame;

        public PHActionListener(TwoPlayers parentFrame) {
                    this.parentFrame = parentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String p1Name;
            if(player1.getText().isEmpty()){
                p1Name = "Guest1"; 
            }
            else{
                p1Name = player1.getText();
            }
            Color p1Color;
            String selectedeColor = (String) color.getSelectedItem();
            if(selectedeColor.equals("Random color")){
                Random rand = new Random();
                int randi = rand.nextInt(2);
                p1Color = (randi==1) ?  Color.red : Color.yellow;
            }
            else{
                p1Color = Color.red;
            }

            String p2Name;
            if(player2.getText().isEmpty()){
                p2Name = "Guest2"; 
            }
            else{
                p2Name = player2.getText();
            }
            Color p2Color = (p1Color.equals(Color.red)) ? Color.yellow : Color.red;

            Human p1 = new Human(p1Name, p1Color);
            Human p2 = new Human(p2Name, p2Color);

            Game game = new Game(p1, p2);

            int rowS;
            int collS;
            if(row.getText().isEmpty() || collumn.getText().isEmpty()){
                rowS=6;
                collS=7;
            }
            else{
                rowS = Integer.parseInt(row.getText());
                collS = Integer.parseInt(collumn.getText());
            }
            
            if(rowS<=12 && collS <=22 && rowS!=0 && collS!=0 && (rowS>3 || collS>3)){
                TableFrame tableframe = new TableFrame(rowS, collS, game, parentFrame);
                tableframe.setVisible(true);
                setVisible(false);
            }
            else{
                JTextField error = new JTextField("Valid méretet adj meg!");
                error.setFont(new Font("Arial", Font.BOLD, 20));
                error.setOpaque(true);
                error.setBackground(Color.red);
                add(error, BorderLayout.SOUTH);
                error.setVisible(true);
            }
        }
    }

    class MmActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu mm = new MainMenu();
            mm.setVisible(true);
            setVisible(false);
        }
        
    }

    public TwoPlayers() {
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

        JButton mm = new JButton("<");
        mm.setPreferredSize(new Dimension(45, 45));
        topPanel.add(mm, BorderLayout.WEST);
        mm.addActionListener(new MmActionListener());

        // Menü sáv beállítása
        JMenuBar topMenuBar = new JMenuBar();   
        JMenu menu = new JMenu("    Options    "); 
        JMenuItem menui = new JMenuItem("Main Menu");  
        menui.addActionListener(new MmActionListener());  
        menu.add(menui);  
        topMenuBar.add(menu);  
        topPanel.add(topMenuBar, BorderLayout.EAST);  
        
        // Fő panel létrehozása
        mainP = new JPanel(new GridBagLayout());
        mainP.setBackground(Color.CYAN);
        add(mainP, BorderLayout.CENTER);

        // textfieldek létrehozása
        JLabel player1Name = createLabel("P1 name:", Color.red);
        JLabel player2Name = createLabel("P2 name:", Color.yellow);
        JLabel chooseColor = createLabel("Choose Color:", new Color(193, 28, 132));
        JLabel tableSize = createLabel("Choose table size:", new Color(200, 21, 139));
        JLabel x = new JLabel("X");
        x.setFont(new Font("Arial", Font.BOLD, 13));
        x.setPreferredSize(new Dimension(30, 30));
        x.setOpaque(true);
        x.setBackground(Color.cyan);
        x.setHorizontalAlignment(SwingConstants.CENTER);

        // Textfieldek, combobox
        player1 = new JTextField();
        player1.setPreferredSize(new Dimension(115, 30));
        player2 = new JTextField();
        player2.setPreferredSize(new Dimension(115, 30));
        String[] tt = {"Selected", "Random color"};
        color = new JComboBox<>(tt);
        color.setPreferredSize(new Dimension(115, 30));
        row = new JTextField();
        row.setPreferredSize(new Dimension(30, 30));
        collumn = new JTextField();
        collumn.setPreferredSize(new Dimension(30, 30));

        // GridBagConstraints konfigurálása
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; 

        // Gombok elhelyezése
        mainP.add(player1Name, gbc);
        gbc.gridx++;
        mainP.add(player1, gbc);
        gbc.gridy++;

        gbc.gridx--;
        mainP.add(player2Name, gbc);
        gbc.gridx++;
        mainP.add(player2, gbc);
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
        gbc.insets = new Insets(20, 0, 20, 45); // Margók a komponensek körül
        mainP.add(x, gbc);
        gbc.gridx++;
        mainP.add(collumn, gbc);

        // Play gomb hozzáadása
        JButton playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(150, 60)); 
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(new Color(150, 20, 100));
        playButton.setOpaque(true);
        playButton.addActionListener(new PHActionListener(this));
         
        // A gomb pozícionálása
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2; // Két oszlop szélesség
        gbc.insets = new Insets(50, 0, 0, 20); 
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

