package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel {
    private JPanel menuPanel;
    private int xPadding;
    private int panelWidth;
    private int yPadding;
    private int panelHeight;
    
    private Image menuImage;

    public MenuPanel(int xPadding, int panelWidth, int yPadding, int panelHeight){
        this.xPadding = xPadding;
        this.panelWidth = panelWidth;
        this.yPadding = yPadding;
        this.panelHeight = panelHeight;

        setMenuImage("resources/menu/menu.png");
        setMenuPanel();
    }

    private void setMenuImage(String path){
        this.menuImage = new ImageIcon(path).getImage();
    }

    private void setMenuPanel(){
        this.menuPanel = new JPanel() {
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(menuImage, 0, 0, this); //changed to 0 temp
            }
        };

        this.menuPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + " : " + y);
            }
        });
    }

    public JPanel getMenuPanel(){
        return this.menuPanel;
    }


}
