import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.*;

public class GUI extends JFrame{
    private PixelGrid grid;
    private JButton colourSelector;
    private Color currentColor;
    private int red;
    private int green;
    private int blue;
    
    public GUI(){
        setSize(new Dimension(800, 900));
        grid = new PixelGrid(20, 800, 800);
        createColourSelector();
        grid.setTileColor(new Color(255,0 ,0));
        add(grid.getPanel());
        //add(colourSelector);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void createColourSelector(){
        this.colourSelector = new JButton("COLOUR");
        this.colourSelector.setBounds(0, 0, 800, 20);
        this.colourSelector.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                currentColor = new Color(red, green, blue);
                red+=20;
                green+=20;
                blue+=20;
                grid.setTileColor(currentColor);
            }
        });

    }
}
