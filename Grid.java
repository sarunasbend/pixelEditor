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

    private boolean xMirror = false;
    private boolean yMirror = false;

    public Grid(int xPanel, int yPanel, int pixelSize, int gridWidth, int gridHeight){
        this.xPanel = xPanel;
        this.yPanel = yPanel;
        this.pixelSize = pixelSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.pixels = new Pixel[gridHeight][gridWidth];
        createPixelGrid();
    }

    public int getWidth(){return this.gridWidth * this.pixelSize;}
    public int getHeight(){return this.gridHeight * this.pixelSize;}
    public void printAll(Graphics2D g2d){this.pixelGrid.printAll(g2d);}
    
    private void createPixelGrid(){
        this.pixelGrid = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawGrid(g2d);
                g2d.setRenderingHints(renderingHints);
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
                if (xMirror && yMirror){
                    if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                        //adds x and y mirror tiles
                        paintXYMirror();
                    }
                } else if (xMirror && !yMirror){
                    if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                        //adds x mirror tiles
                        paintXMirror(); 
                    }
                } else if (!xMirror && yMirror){
                    if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                        //adds y mirror tiles
                        paintYMirror();
                    }
                } else if (!xMirror && !yMirror){
                    if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                        //adds unmirrored tiles
                        pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize);
                        pixelGrid.repaint(); 
                    } 
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
                    if (xMirror && yMirror){
                        if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                            //adds x and y mirror tiles
                            paintXYMirror();
                        }
                    } else if (xMirror && !yMirror){
                        if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                            //adds x mirror tiles
                            paintXMirror(); 
                        }
                    } else if (!xMirror && yMirror){
                        if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                            //adds y mirror tiles
                            paintYMirror();
                        }
                    } else if (!xMirror && !yMirror){
                        if ((mouseX/pixelSize < gridWidth) && (mouseY/pixelSize < gridHeight) && (mouseX > -1) && (mouseY > -1)){
                            //adds unmirrored tiles
                            pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize);
                            pixelGrid.repaint(); 
                        } 
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

    //tools
    public void clearGrid(){
        for (int i = 0; i < gridHeight; i++){
            for (int j = 0; j < gridHeight; j++){
                pixels[i][j] = null;
            }
        }
        pixelGrid.repaint();
    }

    public void replaceColour(){

    }

    public void setXMirror(){this.xMirror = !this.xMirror;}

    public void setYMirror(){this.yMirror = !this.yMirror;}

    public void paintXYMirror(){
        pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize); //normal
        pixels[(gridHeight - 1) - (mouseX/pixelSize)][mouseY/pixelSize] = new Pixel(currentColour,mouseX, (gridHeight * pixelSize) - mouseY, pixelSize, pixelSize); 
        pixels[mouseX/pixelSize][(gridWidth - 1) - (mouseY/pixelSize)] = new Pixel(currentColour, (gridWidth * pixelSize) - mouseX, mouseY, pixelSize, pixelSize);
        pixels[(gridHeight - 1) - (mouseX/pixelSize)][(gridWidth - 1) - (mouseY/pixelSize)] = new Pixel(currentColour, (gridWidth * pixelSize) - mouseX, (gridHeight * pixelSize) - mouseY, pixelSize, pixelSize);
        pixelGrid.repaint(); 
    }

    public void paintXMirror(){
        pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize); //normal
        pixels[mouseX/pixelSize][(gridWidth - 1) - (mouseY/pixelSize)] = new Pixel(currentColour, (gridWidth * pixelSize) - mouseX, mouseY, pixelSize, pixelSize);
        pixelGrid.repaint();
    }

    public void paintYMirror(){
        pixels[mouseX/pixelSize][mouseY/pixelSize] = new Pixel(currentColour, mouseX, mouseY, pixelSize, pixelSize); //normal
        pixels[(gridHeight - 1) - (mouseX/pixelSize)][mouseY/pixelSize] = new Pixel(currentColour,mouseX, (gridHeight * pixelSize) - mouseY, pixelSize, pixelSize); //
        pixelGrid.repaint();
    }

    public void rotateClockwise(){
    }

    public void rotateAnticlockwise(){
    }

    public void undoLastAction(){
    }
}
