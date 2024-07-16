import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Home extends JFrame {
    private JPanel PhotoPanel;
    private boolean hover = false;
    
    public Home(){
        initPhotoPanel();
        initFrame();
    }

    private void initPhotoPanel(){
        this.PhotoPanel = new JPanel();
        this.PhotoPanel.setBounds(0,0,724, 824);
        ImageIcon gif = new ImageIcon("resources/home/idle.gif");
        JLabel gifLabel = new JLabel(gif);
        this.PhotoPanel.add(gifLabel);

        this.PhotoPanel.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e){
                int x = e.getX();
                int y = e.getY();

                if (x >= 0 && x <= 100 && y >= 0 && y <= 100){
                    hover = true;
                } else {
                    hover = false;
                }

                if (hover){
                    System.out.println("yes");
                    ImageIcon hover = new ImageIcon("resources/home/hover.gif");
                    gifLabel.setIcon(hover);
                }
            }            
        });
    }

    private void initFrame(){
        add(this.PhotoPanel);
        //FrameImage backgroundImage = new FrameImage("resources/bg.gif", 724, 824);
        //setContentPane(backgroundImage);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 860));
        setBackground(new Color(0,0,0));
        setVisible(true);
        setResizable(false);
    }
}
