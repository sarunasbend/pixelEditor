package components;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InfoPane {
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;
    private JLabel foreGround;

    private JLayeredPane pane;
    private JPanel panel;
    private JLabel mouseLocation;
    private JLabel dimensions;

    private Font labelFont;
    
    
    public InfoPane(int width, int height, String imagePath, JLabel mouseLocation, int canvasWidth, int canvasHeight){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        this.mouseLocation = mouseLocation;
        createImageIcon();
        createFont();
        createDimensionLabel(canvasWidth, canvasHeight);
        createMouseLocationLabel();
        createPane();
        addToPanel();
    }

    private void createImageIcon(){
        imageIcon = new ImageIcon(imagePath);
        foreGround = new JLabel(imageIcon);
        foreGround.setBounds(0,0, width, height);
    }

    private void createFont(){
            try {
            labelFont = Font.createFont(Font.TRUETYPE_FONT, new File("components/resources/CustomFont.ttf"));
            labelFont = labelFont.deriveFont(20f);
        } catch (FontFormatException | IOException e){
            e.printStackTrace();
        }
    }

    private void createDimensionLabel(int canvasWidth, int canvasHeight){
        dimensions = new JLabel(canvasWidth + "x" + canvasHeight);
        dimensions.setFont(labelFont);
        dimensions.setForeground(Color.WHITE);
        dimensions.setBounds(70,25,100,25);
    }

    private void createMouseLocationLabel(){
        mouseLocation.setFont(labelFont);
        mouseLocation.setBounds(60,50,100,50);
        mouseLocation.setForeground(Color.WHITE);
    }

    
    private void createPane(){
        pane = new JLayeredPane();
    }

    private void addToPanel(){
        panel = new JPanel(null);
        panel.setBackground(Color.BLACK);
        panel.setBounds(0,0,width,height);

        panel.add(dimensions);
        panel.add(mouseLocation);

        pane.add(panel, JLayeredPane.DEFAULT_LAYER);
        pane.add(foreGround, JLayeredPane.MODAL_LAYER);
    }

    public JLayeredPane getPane(){return this.pane;}
}
