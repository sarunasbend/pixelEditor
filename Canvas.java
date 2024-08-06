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

public class Canvas {
    public class Pixel {
        private int originalX; //track position to return to after zooming
        private int originalY;
        private int globalPixelSize;
        private Color pixelColour; 
        private Rectangle rectangle;

        public Pixel(int x, int y, int size, Color colour){
            originalX = x;
            originalY = y;
            globalPixelSize = size;
            pixelColour = colour;
            rectangle = new Rectangle(x,y,size,size);
        }

        //getters
        public int getOriginalX(){return this.originalX;}
        public int getOriginalY(){return this.originalY;}
        public int getglobalPixelSize(){return this.globalPixelSize;}
        public Color getPixelColour(){return this.pixelColour;}
        public Rectangle getRectangle(){return this.rectangle;}
        //setters
        public void setPixelColour(Color colour){pixelColour = colour;}
    }

    private JPanel canvasPanel;
    private int xPanel; //where the panel will be placed, needed for graphics 
    private int yPanel; 
    private int canvasWidth;
    private int canvasHeight;
    private int globalPixelSize = 20; //TEMP VALUE
    
    private int[][][] imagePixelData; //array of pixel RGB
    private int userImageHeight; //height of imported image
    private int userImageWidth; //width of imported image
    private boolean isUserImageDrawn = false;

    private final RenderingHints renderingHints  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-aliasing

    private Pixel[][] pixels;
    private Color currentColour = new Color(0, 0, 0);

    private int mouseClickX = -1;
    private int mouseClickY = -1;

    private int mouseMoveX = -1;
    private int mouseMoveY = -1;

    private boolean hasMouseMoved = false;
    private boolean hasMousePressed = false;
    private boolean isMouseHeldDown = false;
    private Rectangle hoverRectangle;

    private boolean xMirror = false;
    private boolean yMirror = false;

    //blank canvas constructor
    //pixel size is determined before the creation of the program
    public Canvas(int xP, int yP, int pS, int cW, int cH){
        xPanel = xP;
        yPanel = yP;
        globalPixelSize = pS;
        canvasWidth = cW;
        canvasHeight = cH;
        this.pixels = new Pixel[canvasHeight][canvasWidth]; //height and width is the resolution
        createPixelCanvas();
    }

    private void createPixelCanvas(){
        this.canvasPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawcanvas(g2d);
                g2d.setRenderingHints(renderingHints);
                g2d.setClip(xPanel, yPanel, canvasWidth * globalPixelSize, canvasHeight * globalPixelSize); //might have to change to specify bounds
                if ((mouseClickX >= 0) && (mouseClickY >= 0)){
                    for (int i = 0; i < canvasHeight; i++){
                        for (int j = 0; j < canvasWidth; j++){
                            if (pixels[i][j] != null){
                                // Rectangle pixel = pixels[i][j].getRectangle();
                                g2d.setColor(pixels[i][j].getPixelColour());
                                g2d.drawRect(pixel.x, pixel.y, pixel.width, pixel.height);
                                g2d.fillRect(pixel.x, pixel.y, pixel.width, pixel.height); 
                                
                            }  
                        }
                    }
                }

                // if (hasMouseMoved){
                //     g2d.setColor(Color.red);
                //     g2d.drawRect((mouseMoveX/ globalPixelSize) * globalPixelSize, (mouseClickY / globalPixelSize) * globalPixelSize, globalPixelSize, globalPixelSize);
                //     hasMouseMoved = false;
                //     g2d.setColor(currentColour);
                //     repaint();
                // }

                
            }
        };

        this.canvasPanel.setSize(this.canvasWidth * this.globalPixelSize, this.canvasHeight * this.globalPixelSize);
        this.canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event){
                isMouseHeldDown = true;
                mouseClickX = mouseMoveX = ((event.getX() / globalPixelSize) * globalPixelSize);
                mouseClickY = mouseMoveY = ((event.getY() / globalPixelSize) * globalPixelSize);
                hoverRectangle = new Rectangle(mouseMoveX, mouseMoveY, globalPixelSize, globalPixelSize);
                if (xMirror && yMirror){
                    if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                        //adds x and y mirror tiles
                        // paintXYMirror();
                    }
                } else if (xMirror && !yMirror){
                    if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                        //adds x mirror tiles
                        // paintXMirror(); 
                    }
                } else if (!xMirror && yMirror){
                    if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                        //adds y mirror tiles
                        // paintYMirror();
                    }
                } else if (!xMirror && !yMirror){
                    if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                        //adds unmirrored tiles
                        pixels[mouseClickX/globalPixelSize][mouseClickY/globalPixelSize] = new Pixel( mouseClickX, mouseClickY, globalPixelSize, currentColour);
                        canvasPanel.repaint(); 
                    } 
                }
                
            }
            @Override
            public void mouseReleased(MouseEvent event){
                isMouseHeldDown = false;
            }
        });

        this.canvasPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event){
                // mouseClickX  = mouseMoveX = ((event.getX() / globalPixelSize) * globalPixelSize);
                // mouseClickY = mouseMoveY = ((event.getY() / globalPixelSize) * globalPixelSize);
                // hoverRectangle = new Rectangle(mouseMoveX, mouseMoveY, globalPixelSize, globalPixelSize);
                if (isMouseHeldDown){
                    mouseClickX  = mouseMoveX = ((event.getX() / globalPixelSize) * globalPixelSize);
                    mouseClickY = mouseMoveY = ((event.getY() / globalPixelSize) * globalPixelSize);
                    hoverRectangle = new Rectangle(mouseMoveX, mouseMoveY, globalPixelSize, globalPixelSize);
                    if (xMirror && yMirror){
                        if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                            //adds x and y mirror tiles
                            // paintXYMirror();
                        }
                    } else if (xMirror && !yMirror){
                        if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                            //adds x mirror tiles
                            // paintXMirror(); 
                        }
                    } else if (!xMirror && yMirror){
                        if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                            //adds y mirror tiles
                            // paintYMirror();
                        }
                    } else if (!xMirror && !yMirror){
                        if ((mouseClickX/globalPixelSize < canvasWidth) && (mouseClickY/globalPixelSize < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                            //adds unmirrored tiles
                            pixels[mouseClickX/globalPixelSize][mouseClickY/globalPixelSize] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                            canvasPanel.repaint(); 
                        } 
                    }
                }
            }
        });
    }
    

    private void drawcanvas(Graphics2D g2d){
        for (int i = xPanel; i < (canvasWidth * globalPixelSize) + xPanel; i+=globalPixelSize){
            g2d.drawLine(i, yPanel, i, yPanel + (canvasHeight * globalPixelSize));
        }

        for (int j = yPanel; j < (canvasHeight * globalPixelSize) + yPanel; j+=globalPixelSize){
            g2d.drawLine(xPanel, j, xPanel + (canvasWidth * globalPixelSize), j);
        }
    }

    public JPanel getCanvas(){return this.canvasPanel;}
}