package components;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;

/**
 * Class to showcase what colour of the rgb slider.
 */
public class ColourPreview {
    private int width;
    private int height;
    
    private String imagePath;
    private ImageIcon imageIcon;

    private JLayeredPane pane;
    private JPanel panel;
    private JLabel previewLabel;
    private Color previewColour = new Color(126,126,126);
    private JLabel foreground;

    public ColourPreview(int width, int height, String imagePath){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        createImageIcon();
        createPreviewLabel();
        createPane();
        addToPanel();
    }

    private void createImageIcon(){
        imageIcon = new ImageIcon(imagePath);
        foreground = new JLabel(imageIcon);
        foreground.setBounds(0,0,width, height);
    }

    private void createPreviewLabel(){
        previewLabel = new JLabel();
        previewLabel.setOpaque(true);
        previewLabel.setBackground(previewColour);
        previewLabel.setBounds(0,0,width, height);
    }

    private void createPane(){
        pane = new JLayeredPane();
    }

    private void addToPanel(){
        panel = new JPanel(null);
        panel.setBounds(0,0,width,height);
        
        panel.add(previewLabel);
        pane.add(panel, JLayeredPane.DEFAULT_LAYER);
        pane.add(foreground, JLayeredPane.MODAL_LAYER);
    }

    public void setPreviewColour(Color newColour){
        previewColour = newColour;
        previewLabel.setBackground(newColour);
    }

    public Color getPreviewColour(){return this.previewColour;}

    public JLayeredPane getPane(){return this.pane;}

}
