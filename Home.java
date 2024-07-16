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

public class Home extends JFrame {
    private JPanel PhotoPanel;
    private boolean hover = false;
    private Image idleImage;
    private Image hoverImage;

    public Home(){
        initTemp();
        initFrame();
    }

    private void initTemp(){
        this.PhotoPanel = new JPanel() {
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if (hover){
                    g.drawImage(hoverImage, 0, 0, this);
                } else {
                    g.drawImage(idleImage, 0, 0, this);
                }
            }
        };

        idleImage = new ImageIcon("resources/home/idle.gif").getImage();
        hoverImage = new ImageIcon("resources/home/hover.png").getImage();
        
        this.PhotoPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e){
                int x = e.getX();
                int y = e.getY();

                if (x >= 185 && x <= 320 && y >= 325 && y <= 450){
                    if (!hover){
                        hover = true;
                        repaint();
                    }
                } else {
                    if (hover){
                        hover = false;
                        repaint();
                    }
                }
            }
        });

        this.PhotoPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                int x = e.getX();
                int y = e.getY();

                if (x >= 185 && x <= 320 && y >= 325 && y <= 450){
                    createPixel();
                }
            }
        });

        this.PhotoPanel.setBounds(0,0, 603, 683);
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
        this.temp = new GUI();
        dispose();
    }
}
