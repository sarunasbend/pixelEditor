package components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class EraserToolLabel {
    private JLabel toolLabel;
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;

    public EraserToolLabel(int width, int height, String imagePath){
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

    public JLabel getEraserToolLabel(){
        return this.toolLabel;
    }
}
