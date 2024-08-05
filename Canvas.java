import javax.swing.JPanel;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 *  Class that will be the main pixel editor for the user.
 *  This class will have two constructors, blank canvas, image canvas.
 */
public class Canvas {
    public class Pixel {
        private int x;
        private int y;
        private int pixelSize;
        private Color pixelColour;
        private Rectangle rectangle;
       
        public Pixel(int x, int y, int pixelSize, Color pixeColour, Rectangle rectangle){
            
        }
    }

    public Canvas(){}


    private JPanel canvas;

    //testing implementation
    public Canvas(int[][][] imageArray, int width, int height){
        this.canvas = new JPanel() {
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                int x = 0;
                int y = 0;
                for (int i = 0 ; i < height; i++){
                    y = 0;
                    for (int j = 0; j < width; j++){
                        g.setColor(new Color(imageArray[i][j][0],imageArray[i][j][1],imageArray[i][j][2]));
                        g.drawRect(x, y, 20, 20);
                        g.fillRect(x, y, 20, 20);
                        y = y + 20;
                    }
                    x = x + 20;
                }
            }
        };
    };

    public JPanel getCanvas(){return this.canvas;}
}