import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.*;

public class GUI extends JFrame{
    private Grid grid;
    private JPanel pixelGrid;
    private JButton redButton;
    private int red;
    private JButton greenButton;
    private int green;
    private JButton blueButton;
    private int blue;
    private Color currentColor;

    public GUI(){
        setSize(new Dimension(1000, 1000));
        this.grid = new Grid(100, 100, 20, 20, 20);
        this.pixelGrid = this.grid.getGridPanel();
        this.redButton = new JButton("COLOUR");
        this.redButton.setBounds(0, 400, 100, 100);
        this.redButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                red+=20;
                if (red > 255){red = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.blueButton = new JButton("COLOUR");
        this.blueButton.setBounds(100, 400, 100, 100);
        this.blueButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                blue+=20;
                if (blue > 255){blue = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.greenButton = new JButton("COLOUR");
        this.greenButton.setBounds(200, 400, 100, 100);
        this.greenButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                green+=20;
                if (green > 255){green = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });
        

        setLayout(null);
        add(redButton);
        add(blueButton);
        add(greenButton);
        add(pixelGrid);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }
}
