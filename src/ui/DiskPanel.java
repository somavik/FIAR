package ui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DiskPanel extends JPanel {
    private final int col;
    private Color diskColor; // Tárolja, hogy a cellában van-e korong és annak színét
    private final TableFrame tableF;
    // Segédváltozók
    int lastCol=-1; // utolsó egér pozíciója
    int pressedC=-1; // utolsó oszlop ahol le lett nyomva az egér

    // New game és rematch esetén kell
    public void newFrame(){
        TableFrame tableframe = new TableFrame(tableF.row, tableF.column, tableF.game, tableF.previousFrame);
        tableframe.setVisible(true);
        tableF.dispose();
    }

    // New game
    class NGActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            tableF.action=false;
            newFrame(); // new game-re kattintva előjön az új játék felülete, megcserélt színekkel és a nyertes pontszámát megnövelve
        }
    }

    // Rematch
    class RActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            tableF.game.resetScores();
            tableF.action=false;
            newFrame();
        }
    }

    // Leaderboard
    class LActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            tableF.action=false;
            tableF.leaderboard.saveToFile("leaderboard.dat");
            tableF.leaderboard.printLeaderboard();
        }
    }

    public void addPanel(String text, Color actionC) {
        // GridBagConstraints konfigurálása
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0); // Margók a komponensek körül
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JLabel winner = new JLabel(text);
        winner.setFont(new Font("Arial", Font.BOLD, 20));
        winner.setForeground(actionC);
        tableF.centerPanel.add(winner, gbc);
    }

    // Segédfg hasonló button csinálására
    public JButton makeButton(String text, ActionListener ac){
        JButton newB = new JButton(text);
        newB.setPreferredSize(new Dimension(300, 60));
        newB.setFont(new Font("Arial", Font.BOLD, 20));
        newB.setForeground(Color.WHITE);
        newB.setBackground(new Color(150, 20, 100));
        newB.setOpaque(true);
        newB.addActionListener(ac);
        return newB;
    }


    public void thereIsAWinner(Color actionC){
        // Ha ember nyert
        if(tableF.game.getPlayerByColor(actionC).getDifficultyLevel()==0){
            // Felkerül a leaderboard-ra, a bot difficulty-ával megnövekedett pontszámával
            tableF.leaderboard.addOrUpdatePlayer(tableF.game.getPlayerByColor(actionC).getName(), tableF.game.getOtherPlayerByColor(actionC).getDifficultyLevel());
        }
       
        // Történés kiírása
        String winText = "4 in a row,";
        winText += tableF.game.getPlayerByColor(actionC).getName();
        winText += ((tableF.game.getPlayerByColor(actionC).getDifficultyLevel()>0) ? " (bot) " : " ");
        winText += "won the game!";

        addPanel(winText, actionC);

        // Alsó panel hozzáadása
        tableF.bottomPanel = new JPanel(new FlowLayout());
        tableF.add(tableF.bottomPanel, BorderLayout.SOUTH);

        // Új gombok hozzáadása
        JButton rematch = makeButton("Rematch", new RActionListener());
        tableF.bottomPanel.add(rematch);

        JButton leaderboard = makeButton("Save to leaderboard", new LActionListener());
        tableF.bottomPanel.add(leaderboard);

        JButton mm = makeButton("Quit to main menu", new MmActionListener(tableF));
        tableF.bottomPanel.add(mm);

        // Szín csere
        tableF.game.switchColors();
    }

    public boolean check(Color actionC) {
        if (tableF.table.hasFourInARow(actionC)) {
            tableF.action = true;
            tableF.removeAllListeners(); // most már ne lehessen korongokat elhelyezni
            
            // Pontszám növelése
            tableF.game.getPlayerByColor(actionC).increaseScore();

            if(tableF.game.isThereAWinner()){
                thereIsAWinner(actionC);
            }
            else{
                // Iylenkor csak 1 game-et nyert, nem az egész meccset
                String winText = "4 in a row,";
                winText += tableF.game.getPlayerByColor(actionC).getName();
                winText += ((tableF.game.getPlayerByColor(actionC).getDifficultyLevel()>0) ? " (bot) " : " ");
                winText += "won!";
                
                addPanel(winText, actionC);

                // Alsó panel hozzáadása
                tableF.bottomPanel = new JPanel(new FlowLayout());
                tableF.add(tableF.bottomPanel, BorderLayout.SOUTH);

                // Új gomb hozzáadása
                JButton newG = makeButton("Next game", new NGActionListener());
                tableF.bottomPanel.add(newG);

                // Szín csere
                tableF.game.switchColors();
            }
            // Keret frissítése
            tableF.revalidate(); // Elrendezés újraszámolása
            tableF.repaint(); // Rajz újrarajzolása
        }
        else if(tableF.table.isTableFull()){
            // Draw esete
            tableF.action = true;
            tableF.removeAllListeners(); // most már ne lehessen korongokat elhelyezni
            // Színek váltása
            tableF.game.switchColors();

            addPanel("Draw!", Color.BLUE);
    
            // Alsó panel hozzáadása
            tableF.bottomPanel = new JPanel(new FlowLayout());
            tableF.add(tableF.bottomPanel, BorderLayout.SOUTH);
    
            // Új gomb hozzáadása
            JButton newG = makeButton("New game", new NGActionListener());
            tableF.bottomPanel.add(newG);
    
            // Keret frissítése
            tableF.revalidate(); // Elrendezés újraszámolása
            tableF.repaint(); // Rajz újrarajzolása
        }
        return tableF.table.hasFourInARow(actionC); // megtörténtek a fentiek?
    }

    public boolean handleMove(boolean hasFourInARow, Color actionC){
        actionC = (tableF.firstP) ? Color.red : Color.yellow;
        if(!hasFourInARow && tableF.game.getPlayerByColor(actionC).getDifficultyLevel()>0){ // ha nem volt győzés, akkor léptetjük a bot-ot
            int move = tableF.game.getPlayerByColor(actionC).makeMove(tableF.table);
            tableF.addDisk(move, actionC);
            tableF.firstP = !tableF.firstP; 
            return check(actionC); // győzött a bot?
        }
        return false;
    }

    class DMouseAdapter extends MouseAdapter {
        // Ezzel a színnel lesznek meghívva  a függvények
        Color actionC;
        // Segéd színek:
        Color cb = Color.gray.brighter(); 
        Color c = Color.gray;  
        @Override
        public void mouseEntered(MouseEvent e) {
            // Szín beállítása annak megfelelően, hogy ki jön
            actionC = (tableF.firstP) ? Color.red : Color.yellow;
            // Aktuális panel színe:
            Color actC = tableF.panelmx[0][DiskPanel.this.col].getDiskColor();
            // Csak akkor lesz átszínezve a az oszlop, ha nem szürke, tehát nem nyomták le ott a gombot
            if(!actC.equals(c) && !actC.equals(cb)){
                tableF.addDiskPrev(DiskPanel.this.col, actionC);
            }
            // Különben nincsen színezés
            lastCol = DiskPanel.this.col; // frissítjük az utolsó ismert oszlopot
            tableF.releasedC = DiskPanel.this.col;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Aktuális szín
            Color actC = tableF.panelmx[0][DiskPanel.this.col].getDiskColor();
            // Hasonló mint az enter-nél, csak ekkor nincs színezés
            if(!actC.equals(c) && !actC.equals(cb)){
                tableF.addDiskPrev(DiskPanel.this.col, Color.white);
            }
            lastCol = -1; // az egér nincs oszlop felett
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // Játékosváltás, ez azért kell hogy a mouseEntered a másik színnel színezzen, ez nem egy valós váltás 
            tableF.firstP = !tableF.firstP; 
            // Nyomva tartáskor szürke színű előnézet
            tableF.addDiskPrev(DiskPanel.this.col, Color.gray);
            // Oszlopok frissítése
            lastCol=DiskPanel.this.col;
            pressedC=DiskPanel.this.col;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Játékosváltás, mert a mousepressedben váltottunk, ezt visszacsináljuk
            tableF.firstP = !tableF.firstP; 
            actionC = (tableF.firstP) ? Color.red : Color.yellow;

            // Ellenőrizzük, hogy az oszlop tele van-e
            if (tableF.table.isColumnFull(DiskPanel.this.col)) {
                tableF.addDiskPrev(tableF.releasedC, actionC); // ha tele van, akkor nem történik rakás, előnézet a jelenlegi oszlopra vváltozatlan színnel
                return;
            }
            //ha nem volt return
            tableF.addDisk(DiskPanel.this.col, actionC); // Korong elhelyezése
            boolean hasFourInARow = check(actionC);

            tableF.firstP = !tableF.firstP; 
            if(handleMove(hasFourInARow, actionC)){
                hasFourInARow=true;
            }

            actionC = (tableF.firstP) ? Color.red : Color.yellow;
            if(hasFourInARow){
                tableF.addDiskPrev(tableF.releasedC, Color.white); // ha volt 4 in a row, akkor letisztitjuk a táblát a previewtól, hogy szépen jelenjen meg
            }
            else if(pressedC==lastCol){ // különben, ha most ugyanott vagyunk mint ahol lenyomtuk a gombot,
                tableF.addDiskPrev(DiskPanel.this.col, actionC); // akkor kirajzoljuk a következő lehetséges rakást
            }
            else{
                tableF.addDiskPrev(DiskPanel.this.col, Color.white); // különben nem
                tableF.addDiskPrev(tableF.releasedC, actionC);
            }   
            pressedC=-1; // most nincs lenyomva gomb
        }
    }

    public DiskPanel(int c, TableFrame tableF) {
        this.col = c;
        this.diskColor = Color.WHITE; // kezdetben nincs korong
        this.tableF = tableF;
        // Adapter beállítása
        if(tableF!=null){
            DMouseAdapter mouseAdapter = new DMouseAdapter();
            addMouseListener(mouseAdapter);
        }
    }

    public void setDiskColor(Color color) {
        this.diskColor = color;
        repaint(); // ha változik a szín, újrarajzoljuk a panelt
    }

    public Color getDiskColor() {
        return this.diskColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Külső terület beállítása
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());  // A panel teljes területe kék 
        // Belső terület(karika) beállítása
        if (diskColor != Color.WHITE) {
            g.setColor(diskColor);
        }
        else{
            g.setColor(Color.WHITE);
        }
        // Arányos kör rajzolása a panel közepére
        int diameter = (int)(Math.min(getWidth(), getHeight()) * 0.8);  // A kisebb dimenzió 80%-a
        int x = (getWidth() - diameter) / 2;  // Középre helyezés a panel szélessége mentén
        int y = (getHeight() - diameter) / 2; // Középre helyezés a panel magassága mentén
        g.fillOval(x, y, diameter, diameter); // Rajzolunk egy kört
    }
}