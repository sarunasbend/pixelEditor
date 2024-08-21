package components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class UndoToolLabel extends JLabel {
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;

    public UndoToolLabel(int width, int height, String imagePath){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        setEditorTitle();
    }

    private void setEditorTitle(){
        imageIcon = new ImageIcon(imagePath);
        setIcon(imageIcon);
        setSize(width, height);
    }
}
