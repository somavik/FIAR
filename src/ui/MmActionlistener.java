package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// Main menu-t hozza elő, gyakran kell
class MmActionListener implements ActionListener{
    JFrame pFrame;
    public MmActionListener(JFrame parentFrame) {
        this.pFrame = parentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainMenu mm = new MainMenu();
        mm.setVisible(true);
        pFrame.setVisible(false);
    }
}
    
