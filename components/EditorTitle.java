package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditorTitle extends JLabel {
    private int width;
    private int height;
    private String imagePath;
    private ImageIcon imageIcon;
    private String selectedPath;
    private ImageIcon selectedIcon;
    private boolean selected = false;

    public EditorTitle(int width, int height, String imagePath, String selctedPath){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        this.selectedPath = selctedPath;
        setEditorTitle();
    }

    private void setEditorTitle(){
        imageIcon = new ImageIcon(imagePath);
        selectedIcon = new ImageIcon(selectedPath);
        setIcon(imageIcon);
        setSize(width, height);
    }

    //mouse listener will be in Editor GUI class, this method is called if user presses the title 
    public void setSelectedTitle(){
        selected = !selected;
        if (selected){
            setIcon(selectedIcon);
        } else {
            setIcon(imageIcon);
        }
    }

    //returns whether or not the tool has been selcted
    public boolean getSelected(){return this.selected;}
}
