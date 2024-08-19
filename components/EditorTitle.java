package components;

import javax.swing.*;
import java.awt.*;

public class EditorTitle {
    private JLabel title;
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;

    public EditorTitle(int width, int height, String imagePath){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        setEditorTitle();
    }

    private void setEditorTitle(){
        imageIcon = new ImageIcon(imagePath);
        title = new JLabel(imageIcon);
        title.setSize(width, height);
    }

    public JLabel getEditorTitle(){
        return this.title;
    }
}
