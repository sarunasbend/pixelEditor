import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/** Temporary class for testing the pixel editor functionality
 * @author Sarunas Bendoraitis
 */
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

    private JButton clearButton;

    private JButton xMirror;
    private JButton yMirror;

    private JButton export;

    private JButton replaceColour;

    private Image importedImage;

    public GUI(){
        setSize(new Dimension(800, 800));

        this.importedImage = new ImageIcon("exports.png").getImage();

        this.grid = new Grid(importedImage, 0, 0, 10, 400, 400);
        this.pixelGrid = this.grid.getGridPanel();

        this.redButton = new JButton("RED");
        this.redButton.setBounds(0, 400, 100, 100);
        this.redButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                red+=20;
                if (red > 255){red = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.blueButton = new JButton("BLUE");
        this.blueButton.setBounds(100, 400, 100, 100);
        this.blueButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                blue+=20;
                if (blue > 255){blue = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.greenButton = new JButton("GREEN");
        this.greenButton.setBounds(200, 400, 100, 100);
        this.greenButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                green+=20;
                if (green > 255){green = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.clearButton = new JButton("CLEAR");
        this.clearButton.setBounds(0, 550, 100, 100);
        this.clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.clearGrid();
            }
        });

        this.xMirror = new JButton("xMirror");
        this.xMirror.setBounds(0,500,25, 25);
        this.xMirror.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.setXMirror();
            }            
        });

        this.yMirror = new JButton("yMirror");
        this.yMirror.setBounds(25, 500, 25, 25);
        this.xMirror.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.setYMirror();
            }            
        });

        this.export = new JButton("EXPORT");
        this.export.setBounds(50, 500, 50, 50);
        this.export.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                exportGridAsPNG(pixelGrid);
            }          
        });

        this.replaceColour = new JButton("REPLACE");
        this.replaceColour.setBounds(0, 700, 100, 50);
        this.replaceColour.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.replaceColour(new Color(0,0,0), new Color(255, 0, 0));
            }
        });


        setLayout(null);
        add(pixelGrid);
        /*add(redButton);
        add(blueButton);
        add(greenButton);
        add(pixelGrid);
        add(clearButton);
        add(xMirror);
        add(yMirror);
        add(export);
        add(replaceColour);*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }
    
    public void exportGridAsPNG(JPanel panel){
        BufferedImage image = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = image.createGraphics();
        this.grid.printAll(g2d);

        try {
            ImageIO.write(image, "png", new File("exports.png"));
            System.out.println("Panel saved as PNG");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
