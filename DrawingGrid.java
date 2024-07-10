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
import java.util.ArrayList;

/** Class for the pixel grid that the user will draw on 
 * @author Sarunas Bendoraitis
 */
public class DrawingGrid {
    //class that will store individual pixel information
    public class Pixel {
        private Rectangle pixel;
        private Color pixelColour;
        
        public Pixel(Rectangle pixel, Color pixelColour){
            this.pixel = pixel;
            this.pixelColour = pixelColour;
        }

        public Color getColour(){return this.pixelColour;}
        public Rectangle getRectangle(){return this.pixel;}
    }

    private JPanel drawingGridPanel;
    
    private int xPanel;
    private int yPanel;
    private int pixelSize;
    private int gridWidth;
    private int gridHeight;

    private int mouseX = -1;
    private int mouseY = -1;
    private boolean isMouseHeldDown = false;

    private final RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-aliasing
    private boolean isAntialiasingOn = false;
    private boolean isGridDrawn = false;
    private boolean isSetClipOn = false;

    private Pixel[][] gridPixels;
    private Color currentColour = new Color(0, 0, 0);

    public DrawingGrid(int xPanel, int yPanel, int pixelSize, int gridWidth, int gridHeight){
        this.xPanel = xPanel;
        this.yPanel = yPanel;
        this.pixelSize = pixelSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        initialiseGridPixels();
        createDrawingGridPanel();
    }

    private void initialiseGridPixels(){
        this.gridPixels = new Pixel[gridHeight][gridWidth];
        for (int row = 0; row < gridHeight; row++){
            for (int column = 0; column < gridWidth; column++){
                gridPixels[row][column] = new Pixel(new Rectangle(xPanel + (column * pixelSize), yPanel + (row * pixelSize), pixelSize, pixelSize), Color.white);
            }
        }
    }

    private void createDrawingGridPanel(){
        this.drawingGridPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHints(renderingHints);
                drawGrid(g2d);
                g2d.setClip(xPanel, yPanel, gridWidth * pixelSize, gridHeight * pixelSize);
                if ((mouseX >= 0) && (mouseY >= 0)){
                    for (int row = 0; row < gridHeight; row++){
                        for (int column = 0; column < gridWidth; column++){
                            g2d.setColor(gridPixels[row][column].getColour());
                            Rectangle tile = gridPixels[row][column].getRectangle();
                            g2d.drawRect(tile.x, tile.y, tile.width, tile.height);
                            g2d.fillRect(tile.x, tile.y, tile.width, tile.height);
                        }
                    } 
                }
            }
        };
        this.drawingGridPanel.setSize(this.gridWidth * this.pixelSize, this.gridHeight * this.pixelSize);
        this.drawingGridPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event){
                isMouseHeldDown = true;
                mouseX = ((event.getX() / pixelSize) * pixelSize);
                mouseY = ((event.getY() / pixelSize) * pixelSize);
                gridPixels[(mouseY / pixelSize)][(mouseX / pixelSize)] = new Pixel(new Rectangle(mouseX, mouseY, pixelSize, pixelSize), currentColour);
                drawingGridPanel.repaint();
            }

            public void mouseReleased(MouseEvent event){
                isMouseHeldDown = false;
            }
        });

        this.drawingGridPanel.addMouseMotionListener(new MouseAdapter() { //enables mouse drag painting
            public void mouseDragged(MouseEvent event) { //only valid if pressed
                if (isMouseHeldDown){
                    mouseX = ((event.getX() / pixelSize) * pixelSize);
                    mouseY = ((event.getY() / pixelSize) * pixelSize);
                    gridPixels[(mouseY / pixelSize)][(mouseX / pixelSize)] = new Pixel(new Rectangle(mouseX, mouseY, pixelSize, pixelSize), currentColour);
                    drawingGridPanel.repaint();
                }
            }
        });
    }

    private void drawGrid(Graphics2D g2d){ //initial tile grid
        for (int i = xPanel; i < (gridWidth * pixelSize) + xPanel; i+=pixelSize){
            g2d.drawLine(i, yPanel, i, yPanel + (gridHeight * pixelSize));
        }

        for (int j = yPanel; j < (gridHeight * pixelSize) + yPanel; j+=pixelSize){
            g2d.drawLine(xPanel, j, xPanel + (gridWidth * pixelSize), j);
        }
    }

    public JPanel getDrawingGridPanel(){return this.drawingGridPanel;}

}
