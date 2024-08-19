package components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PenToolLabel {
    private JLabel toolLabel;
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;

    public PenToolLabel(int width, int height, String imagePath){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        setEditorTitle();
    }

    private void setEditorTitle(){
        imageIcon = new ImageIcon(imagePath);
        toolLabel = new JLabel(imageIcon);
        toolLabel.setSize(width, height);
    }

    public JLabel getPenToolLabel(){
        return this.toolLabel;
    }
}
