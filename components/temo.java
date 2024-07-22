package components;

import javax.swing.*;
import java.awt.*;

/*
 * Temporary class for the structure of the program 
 */
public class temo extends JFrame {
    private JPanel[] windows; //holds all the frames for the program (modes)
    private int mode = 0; //current mode (Home, menu, editor)
    private int xPadding = 0; //width inset
    private int frameWidth; //window width
    private int yPadding = 0; //height inset
    private int frameHeight; //window height

    public temo(JPanel homePanel, JPanel menuPanel){
        this.windows = new JPanel[3];
        this.windows[0] = homePanel;
        this.windows[1] = menuPanel;
        this.windows[2] = null;
        setInsets();
        initFrame();
    }

    private void setInsets(){
        setSize(600, 683);
        pack();
        Insets insets = getInsets();
        this.xPadding = insets.left + insets.right;
        this.yPadding = insets.top + insets.bottom;
        
    }

    private void initFrame(){
        setSize(600 + this.xPadding, 683 + this.yPadding);
        add(this.windows[this.mode]);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void setMode(int mode){
        remove(this.windows[this.mode]);
        this.mode = mode;
        add(this.windows[mode]);
    }
}

//for home/menu resolution will be fixed to 600x683 (not considering insets)

/* NOTES
 * When you create a frame of size 400x400, it will not just be 400x400 frame, the 
 * os will create a 400x400 window, with the frame width = window width - width inset,
 * for the frame height = window height - height inset
 */