import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.*;

public class GUI extends JFrame{
    private DrawingGrid grid;
    private JPanel pixelGrid;
    private JButton colourSelector;
    private Color currentColor;
    private int red;
    private int green;
    private int blue;

    public GUI(){
        setSize(new Dimension(1000, 1000));
        this.grid = new DrawingGrid(100, 100, 20, 20, 20);
        this.pixelGrid = this.grid.getDrawingGridPanel();
        /*this.colourSelector = new JButton("COLOUR");
        this.colourSelector.setBounds(0, 400, 100, 100);
        this.colourSelector.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                currentColor = new Color(red, green, blue);
                red+=20;
                green+=20;
                blue+=20;
                grid.setTileColor(currentColor);
            }
        });*/

        setLayout(null);
        //add(colourSelector);
        add(pixelGrid);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }
}
