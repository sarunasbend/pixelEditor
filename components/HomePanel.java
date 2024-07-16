package components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.*;

public class HomePanel {
    private JPanel homePanel;
    private boolean createPixelEditor = false;

    private int xPadding;
    private int panelWidth;
    private int yPadding;
    private int panelHeight;

    private Image idleImage;
    private Image hoverImage;
    private boolean isHover = false;

    public HomePanel(int xPadding, int panelWidth, int yPadding, int panelHeight){
        this.xPadding = xPadding;
        this.panelWidth = panelWidth;
        this.yPadding = yPadding;
        this.panelHeight = panelHeight;

        setIdleImage("resources/home/idle.gif");
        setHoverImage("resources/home/hover.png");
        setHomePanel();
    }

    private void setIdleImage(String path){
        this.idleImage = new ImageIcon(path).getImage();
    }

    private void setHoverImage(String path){
        this.hoverImage = new ImageIcon(path).getImage();
    }

    private void setHomePanel(){
        this.homePanel = new JPanel() {
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if (isHover){
                    g.drawImage(hoverImage, xPadding, yPadding, this);
                } else {
                    g.drawImage(idleImage, xPadding, yPadding, this);
                }
            }
        };

        this.homePanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e){
                int x = e.getX();
                int y = e.getY();

                if (x >= 185 && x <= 320 && y >= 325 && y <= 450){
                    if (!isHover){
                        isHover = true;
                        homePanel.repaint();
                    }
                } else {
                    if (isHover){
                        isHover = false;
                        homePanel.repaint();
                    }
                }
            }      
        });

        this.homePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                int x = e.getX();
                int y = e.getY();

                if (x >= 185 && x <= 320 && y >= 325 && y <= 450){
                    setCreatePixelEditor();
                }
            }
        });
    }

    public void setCreatePixelEditor(){
        System.out.println("IT WORKED!!!");
        this.createPixelEditor = true;
    }

    public boolean getCreatePixelEditor(){
        return this.createPixelEditor;
    }

    public JPanel getHomePanel(){
        return this.homePanel;
    }
}
