import java.awt.Color;

import javax.swing.*;

public class GUI extends JFrame{
    private PixelGrid grid;
    
    public GUI(){
        grid = new PixelGrid(20, 800, 800);
        grid.setTileColor(new Color(255,0 ,0));
        add(grid.getPanel());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
