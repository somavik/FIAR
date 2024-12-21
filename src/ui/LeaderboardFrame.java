package ui;

import game.Leaderboard;
import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;

public class LeaderboardFrame extends JFrame {
    Leaderboard leaderboard;
    JPanel mainPanel;
    JPanel topPanel;

    public JLabel createLabel(String text){
        JLabel field = new JLabel(text);
        field.setPreferredSize(new Dimension(120, 60));
        field.setFont(new Font("Arial", Font.BOLD, 13));
        field.setForeground(Color.BLUE);
        field.setOpaque(true);
        field.setHorizontalAlignment(SwingConstants.CENTER); // Szöveg középre igazítása vízszintesen
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1); // Fekete vonalas keret, 2 pixel vastagság
        field.setBorder(border);
        return field;
    }

    public LeaderboardFrame(){
        // Frame beállításai
        setTitle("Four in a Row");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximalizált ablak
        setLayout(new BorderLayout());

        leaderboard = new Leaderboard();
        leaderboard.loadFromFile("leaderboard.dat");

        // Top panel: Cím és háttérkép
        topPanel = new JPanel(new BorderLayout());

        // Cím hozzáadása
        JLabel titleL = new JLabel("Leaderboard", SwingConstants.CENTER);
        titleL.setFont(new Font("Arial", Font.BOLD, 50));
        titleL.setForeground(new Color(0, 0, 139));
        titleL.setOpaque(true);
        titleL.setBackground(Color.CYAN);

        titleL.setHorizontalTextPosition(SwingConstants.CENTER);
        titleL.setVerticalTextPosition(SwingConstants.CENTER);
        topPanel.add(titleL, BorderLayout.CENTER);

        // Exit gomb
        JPanel backPanel = new JPanel();
        backPanel.setBackground(Color.cyan);
        topPanel.add(backPanel, BorderLayout.WEST);
        JButton back = new JButton("<");
        back.setPreferredSize(new Dimension(45, 45));
        back.setMargin(new Insets(5, 5, 5, 5)); // Térköz
        back.addActionListener(new MmActionListener(this)); 
        backPanel.add(back);

        add(topPanel, BorderLayout.NORTH);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.CYAN);
        // GridBagConstraints konfigurálása
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0, 0, 0); // Margók a komponensek körül
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Ne nyújtsa ki automatikusan

        JLabel rank = createLabel("Rank");
        rank.setBackground(Color.BLUE);
        rank.setForeground(Color.white);
        mainPanel.add(rank, gbc);
        gbc.gridx++;
        JLabel player = createLabel("Player name");
        player.setBackground(Color.BLUE);
        player.setForeground(Color.white);
        mainPanel.add(player, gbc);
        gbc.gridx++;
        JLabel score = createLabel("Score");
        score.setBackground(Color.BLUE);
        score.setForeground(Color.white);
        mainPanel.add(score, gbc);
        gbc.gridwidth = 1;

        // Ciklus ami kirajzolja a leaderboard tartalmát táblázat formájában
        for (Map.Entry<String, Integer> entry : leaderboard.getPlayers().entrySet()) {
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel number = createLabel(Integer.toString(gbc.gridy));
            mainPanel.add(number, gbc);
            gbc.gridx++;
            JLabel t = createLabel(entry.getKey());
            mainPanel.add(t, gbc);
            gbc.gridx++;
            JLabel s = createLabel(Integer.toString(entry.getValue()));
            mainPanel.add(s, gbc);
        }

        add(mainPanel, BorderLayout.CENTER);
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

}
