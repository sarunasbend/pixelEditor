import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import java.awt.*;

public class oldHome extends JFrame {
    // private HomePanel tempName;
    private JPanel PhotoPanel;

    public oldHome(){
        // this.tempName = new HomePanel(0, 0, 0, 0);
        // this.PhotoPanel = tempName.getHomePanel();
        initFrame();
    }

    private void initFrame(){
        add(this.PhotoPanel);
        //FrameImage backgroundImage = new FrameImage("resources/bg.gif", 724, 824);
        //setContentPane(backgroundImage);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(613,720));
        setBackground(new Color(0,0,0));
        setVisible(true);
        setResizable(false);
    }

    private GUI temp;

    private void createPixel(){
        // this.temp = new GUI();
        dispose();
    }
}
