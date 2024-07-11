import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Grid {
    public class Pixel{
        private Color pixelColour;
        private Rectangle rectangle;

        public Pixel(Color pixeColor, int x, int y, int width, int height){
            this.pixelColour = pixeColor;
            this.rectangle = new Rectangle(x, y, width, height);
        }
        public Color getPixelColour(){return this.pixelColour;}
        public Rectangle getRectangle(){return this.rectangle;}
    }

    private JPanel pixelGrid;
    private int xPanel;
    private int yPanel;

    private int pixelSize;
    private int gridWidth;
    private int gridHeight;

    private int mouseX = -1;
    private int mouseY = -1;
    private boolean isMouseHeldDown = false;

    private final RenderingHints renderingHints  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-aliasing

    private Pixel[][] pixels;
    private Color currentColour = new Color(0, 0, 0);

    public Grid(int xPanel, int yPanel, int pixelSize, int gridWidth, int gridHeight){
        this.xPanel = xPanel;
        this.yPanel = yPanel;
        this.pixelSize = pixelSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.pixels = new Pixel[gridHeight][gridWidth];
        createPixelGrid();
    }

    private void createPixelGrid(){
        this.pixelGrid = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawGrid(g2d);
                g2d.setClip(xPanel, yPanel, gridWidth * pixelSize, gridHeight * pixelSize); //might have to change to specify bounds
                if ((mouseX >= 0) && (mouseY >= 0)){
                    for (int i = 0; i < gridHeight; i++){
                        for (int j = 0; j < gridWidth; j++){
                            if (pixels[i][j] != null){
                               Rectangle pixel = pixels[i][j].getRectangle();
                                g2d.setColor(pixels[i][j].getPixelColour());
                                g2d.drawRect(pixel.x, pixel.y, pixel.width, pixel.height);
                                g2d.fillRect(pixel.x, pixel.y, pixel.width, pixel.height); 
                            }  
                        }
                    }
                }
            }
        };

        this.pixelGrid.setSize(this.gridWidth * this.pixelSize, this.gridHeight * this.pixelSize);
        this.pixelGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event){
                isMouseHeldDown = true;
                mouseX = ((event.getX() / pixelSize) * pixelSize);
                mouseY = ((event.getY() / pixelSize) * pixelSize);
                if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                    pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize);
                    pixelGrid.repaint(); 
                }
            }
            @Override
            public void mouseReleased(MouseEvent event){
                isMouseHeldDown = false;
            }
        });

        this.pixelGrid.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event){
                if (isMouseHeldDown){
                    mouseX = ((event.getX() / pixelSize) * pixelSize);
                    mouseY = ((event.getY() / pixelSize) * pixelSize);
                    if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                        pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize);
                        pixelGrid.repaint(); 
                    }
                }
            }            
        });
    }

    private void drawGrid(Graphics2D g2d){
        for (int i = xPanel; i < (gridWidth * pixelSize) + xPanel; i+=pixelSize){
            g2d.drawLine(i, yPanel, i, yPanel + (gridHeight * pixelSize));
        }

        for (int j = yPanel; j < (gridHeight * pixelSize) + yPanel; j+=pixelSize){
            g2d.drawLine(xPanel, j, xPanel + (gridWidth * pixelSize), j);
        }
    }

    public JPanel getGridPanel(){return this.pixelGrid;}

    public void setPixelColor(Color pixeColor){this.currentColour = pixeColor;}
}
