package components;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RGBPanel {
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;

    private JLayeredPane pane;
    
    private JPanel panel;
    private JLabel foreGround;

    private JSlider rSlider;
    private JSlider gSlider;
    private JSlider bSlider;

    private JLabel rLabel;
    private JLabel gLabel;
    private JLabel bLabel;

    private Font customFont;
    private JTextField hexcode;

    public RGBPanel(int width, int height, String imagePath){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        createImageIcon();
        createPane();
        createSliders();
        createTextField(25, 275, 150, 25, "components/resources/CustomFont.ttf");
        createLabels();
        addToPanel();
    }
    
    private void createImageIcon(){
        imageIcon = new ImageIcon(imagePath);
        foreGround = new JLabel(imageIcon);
        foreGround.setBounds(0,0,200,350);
    }

    private void createPane(){
        pane = new JLayeredPane();
        //input hexcodes
    }

    private void addToPanel(){
        panel = new JPanel(null);
        panel.setBackground(Color.BLACK);
        panel.setBounds(0,0,200,350);

        panel.add(rLabel);
        panel.add(gLabel);
        panel.add(bLabel);
        panel.add(rSlider);
        panel.add(gSlider);
        panel.add(bSlider);
        panel.add(hexcode);
        pane.add(panel, JLayeredPane.DEFAULT_LAYER);
        pane.add(foreGround, JLayeredPane.MODAL_LAYER);

    }

    private void createSliders(){
        //rgb sliders
        rSlider = createSlider(25,50,50,200, new Color(255,0,0),new Color(0,0,0));
        gSlider = createSlider(75, 50, 50, 200, new Color(0,255,0),new Color(0,0,0));
        bSlider = createSlider(125, 50, 50, 200, new Color(0,0,255), new Color(0,0,0));
    }

    private void createLabels(){
        //labels for rgb text
        rLabel = createLabel(35,25,50,25,"RED", Color.WHITE);
        gLabel = createLabel(75,25,50,25,"GREEN", Color.WHITE);
        bLabel = createLabel(130,25,50,25,"BLUE", Color.WHITE);
    }

    //rgb custom sliders
    private  JSlider createSlider(int x, int y, int width, int height, Color startColour, Color endColour){
        GradientSlider newSlider = new GradientSlider(startColour, endColour);
        newSlider.setBounds(x, y, width, height);
        newSlider.setBackground(Color.BLACK);
        return newSlider;
    }
    
    //rgb labels
    private JLabel createLabel(int x, int y, int width, int height, String text, Color colour){
        JLabel newLabel = new JLabel(text);
        newLabel.setBounds(x, y, width, height);
        newLabel.setForeground(colour);
        customFont = customFont.deriveFont(16f);
        newLabel.setFont(customFont);
        return newLabel;
    }

    private JTextField createTextField(int x, int y, int width, int height, String fontPath){
        hexcode = new JTextField("#");
        hexcode.setBounds(x, y, width, height);
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            customFont = customFont.deriveFont(14f);
            hexcode.setFont(customFont);
            hexcode.setForeground(Color.WHITE);
        } catch (FontFormatException | IOException e){
            e.printStackTrace();
        }
        hexcode.setBackground(null);
        hexcode.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        hexcode.setHorizontalAlignment(JTextField.CENTER);
        return hexcode;
    }

    //get color object
    public Color getRGB(){
        int red = rSlider.getValue();
        int green = gSlider.getValue();
        int blue = bSlider.getValue();
        return new Color(red, green, blue);
    }

    //gets hexadecimal code for colour
    public String getHex(){
        int red = rSlider.getValue();
        int green = gSlider.getValue();
        int blue = bSlider.getValue();
        return "#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue); 
    }

    public JLayeredPane getPane(){return this.pane;}

}   
