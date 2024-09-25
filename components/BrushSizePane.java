package components;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;

public class BrushSizePane {
    private int width;
    private int height;

    private String brushImagePath;
    private String foreGroundPath;
    private ImageIcon imageIcon;

    private JLayeredPane pane;
    private JPanel panel;
    private JLabel foreground;
    private JLabel brushOne;
    private JLabel brushTwo;
    private JLabel brushThree;
    private JLabel brushFour;
    
    public BrushSizePane(int width, int height, String brushImagePath, String foreGroundPath){
        this.width = width;
        this.height = height;
        this.brushImagePath = brushImagePath;
        this.foreGroundPath = foreGroundPath;
        createLabels();
        createForeground();
        createPanel();
        createPane();
    }

    private void createLabels(){
        imageIcon = new ImageIcon(brushImagePath + "1.png");
        brushOne = new JLabel(imageIcon );
        brushOne.setBounds(0,0,50,75);

        imageIcon = new ImageIcon(brushImagePath + "2.png");
        brushTwo = new JLabel(imageIcon);
        brushTwo.setBounds(50,0,50,75);

        imageIcon = new ImageIcon(brushImagePath + "3.png");
        brushThree = new JLabel(imageIcon);
        brushThree.setBounds(100,0,50,75);

        imageIcon = new ImageIcon(brushImagePath + "4.png");
        brushFour = new JLabel(imageIcon);
        brushFour.setBounds(150,0,50,75);
    }

    private void createForeground(){
        imageIcon = new ImageIcon(foreGroundPath);
        foreground = new JLabel(imageIcon);
        foreground.setBounds(0,0,50,75);
    }

    private void createPanel(){
        panel = new JPanel(null);
        panel.setBounds(0, 0, width, height);
        panel.setBackground(Color.BLACK);
        panel.add(brushOne);
        panel.setOpaque(true);
        panel.add(brushTwo);
        panel.add(brushThree);
        panel.add(brushFour);
    }

    private void createPane(){
        pane = new JLayeredPane();
        pane.add(panel, JLayeredPane.DEFAULT_LAYER);
        pane.add(foreground, JLayeredPane.MODAL_LAYER);
    }

    /**
     * Class for setting the border on the selected brush size,
     */
    public void setBorder(int index){
        foreground.setBounds(index * 50, 0, 50, 75);
        pane.repaint();
    }

    public JLayeredPane getPane(){return this.pane;}

    public JLabel getBrushOne(){return this.brushOne;}

    public JLabel getBrushTwo(){return this.brushTwo;}

    public JLabel getBrushThree(){return this.brushThree;}

    public JLabel getBrushFour(){return this.brushFour;}

}
