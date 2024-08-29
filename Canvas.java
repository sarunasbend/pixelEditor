import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

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
    private JLabel mouseLocation;
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
    private Color transparentColour = new Color(100,100,100,50);
    private int brushSize = 1; //brush sizes can either by 1, 4, 9 - determines the number of pixels coloured with one click
    private int currentCanvasMode = 0; //this is the tool currently selected, only one can be selected (with the exception of x/y mirror tools being combined)

    private int mouseClickX = -1;
    private int mouseClickY = -1;

    private int mouseMoveX = -1;
    private int mouseMoveY = -1;

    // private boolean hasMouseMoved = false;
    // private boolean hasMousePressed = false;
    private boolean isMouseHeldDown = false;
    private Rectangle hoverRectangle;

    private boolean xMirror = false;
    private boolean yMirror = false;

    //blank canvas constructor
    //pixel size is determined before the creation of the program
    public Canvas(int xP, int yP, int pS, int cW, int cH){
        xPanel = 0;
        yPanel = 0;
        globalPixelSize = pS;
        canvasWidth = cW;
        canvasHeight = cH;
        this.pixels = new Pixel[canvasHeight][canvasWidth]; //height and width is the resolution
        createPixelCanvas();
        canvasPanel.repaint();
        createMouseLabel();  
    }

    private void createPixelCanvas(){
        this.canvasPanel = new JPanel(){
            //main method of the program, allows painting of pixels
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawCanvas(g2d);
                g2d.setRenderingHints(renderingHints); //anti-aliasing
                g2d.setClip(xPanel, yPanel, canvasWidth * globalPixelSize, canvasHeight * globalPixelSize); //might have to change to specify bounds
                
                if ((mouseClickX >= 0) && (mouseClickY >= 0)){
                    for (int i = 0; i < canvasHeight; i++){
                        for (int j = 0; j < canvasWidth; j++){
                            if (pixels[i][j] != null){
                                Rectangle pixel = pixels[i][j].getRectangle();    
                                g2d.setColor(pixels[i][j].getPixelColour());
                                g2d.drawRect(pixel.x, pixel.y, pixel.width, pixel.height);
                                g2d.fillRect(pixel.x, pixel.y, pixel.width, pixel.height); 
                            }

                        }
                    }
                    if ((mouseMoveY/globalPixelSize < canvasHeight) && (mouseMoveX/globalPixelSize < canvasWidth)){
                        if (pixels[mouseMoveY/globalPixelSize][mouseMoveX/globalPixelSize] != null){
                            Rectangle temp = pixels[mouseMoveY/globalPixelSize][mouseMoveX/globalPixelSize].getRectangle();
                            g2d.setColor(Color.RED);
                            g2d.drawRect(temp.x, temp.y, temp.width, temp.height);
                            g2d.setColor(transparentColour);
                            g2d.fillRect(temp.x, temp.y, temp.width, temp.height);
                            g2d.setColor(currentColour);
                        } else if (((xPanel <= mouseMoveX) && (mouseMoveX <= canvasWidth * globalPixelSize)) && ((yPanel <= mouseMoveY) && (mouseMoveY <= canvasHeight * globalPixelSize))){
                            g2d.setColor(Color.RED);
                            g2d.drawRect(hoverRectangle.x, hoverRectangle.y, hoverRectangle.width, hoverRectangle.height);
                            g2d.setColor(transparentColour);
                            g2d.fillRect(hoverRectangle.x, hoverRectangle.y, hoverRectangle.width, hoverRectangle.height);
                            g2d.setColor(currentColour);
                        }
                    }
                }
            }
        };

        this.canvasPanel.setSize(this.canvasWidth * this.globalPixelSize, this.canvasHeight * this.globalPixelSize);
        this.canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event){
                isMouseHeldDown = true;
                mouseClickX = mouseMoveX = ((event.getX() / globalPixelSize) * globalPixelSize);
                mouseClickY = mouseMoveY = ((event.getY() / globalPixelSize) * globalPixelSize);
                int indexX = mouseClickX / globalPixelSize;
                int indexY = mouseClickY / globalPixelSize;

                if ((indexX < canvasWidth) && (indexY < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                    int midpoint;                
                    switch (currentCanvasMode){
                        case 0:
                            pixels[indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                            canvasPanel.repaint(); 
                            break;
                        case 1:
                            pixels[indexY][indexX] = null;
                            canvasPanel.repaint(); 
                            break;
                        case 2: //colourpicker
                            break;
                        case 3: //bucketfill
                            break;
                        case 4: //redo
                            break;
                        case 5: //undo
                            break;
                        case 6: //xmirror
                            pixels[indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                            midpoint = canvasHeight / 2;
                            if (canvasHeight % 2 == 0){
                                if (indexY < midpoint){
                                    mouseClickY = (canvasHeight * globalPixelSize) - mouseClickY - globalPixelSize;
                                    pixels[canvasHeight - 1 - indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                }
                            } else {
                                if (indexY < midpoint){
                                    mouseClickY = (canvasHeight * globalPixelSize) - mouseClickY - globalPixelSize;
                                    pixels[canvasHeight - 1 - indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                } else if (indexY > midpoint){
                                    mouseClickY = (canvasHeight - 1 - indexY) * globalPixelSize;
                                    pixels[midpoint - (indexY - midpoint)][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                }
                            }
                            canvasPanel.repaint();
                            break;
                        case 7: //ymirror
                            pixels[indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                            midpoint = canvasWidth / 2;
                            if (canvasWidth % 2 == 0) {
                                // Even width canvas
                                if (indexX < midpoint) {
                                    mouseClickX = (canvasWidth * globalPixelSize) - mouseClickX - globalPixelSize;
                                    pixels[indexY][canvasWidth - 1 - indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                }
                            } else {
                                // Odd width canvas
                                if (indexX < midpoint) {
                                    mouseClickX = (canvasWidth * globalPixelSize) - mouseClickX - globalPixelSize;
                                    pixels[indexY][canvasWidth - 1 - indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                } else if (indexX > midpoint) {
                                    mouseClickX = (canvasWidth - 1 - indexX) * globalPixelSize;
                                    pixels[indexY][midpoint - (indexX - midpoint)] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                }
                            }
                            canvasPanel.repaint();
                            break;
                        case 8: //trash
                            for (int i = 0; i < canvasHeight; i++){
                                for (int j = 0; j < canvasWidth; j++){
                                    pixels[i][j] = null;
                                }
                            }
                            break;
                        default:
                            System.out.println("No mode selected");
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
                if (isMouseHeldDown){
                    mouseClickX  = mouseMoveX = ((event.getX() / globalPixelSize) * globalPixelSize);
                    mouseClickY = mouseMoveY = ((event.getY() / globalPixelSize) * globalPixelSize);
                    setMouseLocationText(mouseMoveX / globalPixelSize, mouseMoveY / globalPixelSize);
                    hoverRectangle = new Rectangle(mouseMoveX, mouseMoveY, globalPixelSize, globalPixelSize);
                    int indexX = mouseClickX / globalPixelSize;
                    int indexY = mouseClickY / globalPixelSize;
                    
                    if ((indexX < canvasWidth) && (indexY < canvasHeight) && (mouseClickX > -1) && (mouseClickY > -1)){
                        int midpoint;
                        switch (currentCanvasMode){
                            case 0: //pen
                                pixels[indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                canvasPanel.repaint();
                                break;
                            case 1: //eraser
                                pixels[indexY][indexX] = null;
                                canvasPanel.repaint(); 
                                break;
                            case 2: //colourpicker
                                break;
                            case 3: //bucketfill
                                break;
                            case 4: //redo
                                break;
                            case 5: //undo
                                break;
                            case 6: //xmirror
                                pixels[indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                midpoint = canvasHeight / 2;
                                if (canvasHeight % 2 == 0){
                                    if (indexY < midpoint){
                                        mouseClickY = (canvasHeight * globalPixelSize) - mouseClickY - globalPixelSize;
                                        pixels[canvasHeight - 1 - indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    }
                                } else {
                                    if (indexY < midpoint){
                                        mouseClickY = (canvasHeight * globalPixelSize) - mouseClickY - globalPixelSize;
                                        pixels[canvasHeight - 1 - indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    } else if (indexY > midpoint){
                                        mouseClickY = (canvasHeight - 1 - indexY) * globalPixelSize;
                                        pixels[midpoint - (indexY - midpoint)][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    }
                                }
                                canvasPanel.repaint();
                                break;
                            case 7: //xmirror   
                                pixels[indexY][indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                midpoint = canvasWidth / 2;
                                if (canvasWidth % 2 == 0) {
                                    // Even width canvas
                                    if (indexX < midpoint) {
                                        mouseClickX = (canvasWidth * globalPixelSize) - mouseClickX - globalPixelSize;
                                        pixels[indexY][canvasWidth - 1 - indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    }
                                } else {
                                    // Odd width canvas
                                    if (indexX < midpoint) {
                                        mouseClickX = (canvasWidth * globalPixelSize) - mouseClickX - globalPixelSize;
                                        pixels[indexY][canvasWidth - 1 - indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    } else if (indexX > midpoint) {
                                        mouseClickX = (canvasWidth - 1 - indexX) * globalPixelSize;
                                        pixels[indexY][midpoint - (indexX - midpoint)] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    }
                                }
                                canvasPanel.repaint();
                                break;
                            case 8: //trash
                                for (int i = 0; i < canvasHeight; i++){
                                    for (int j = 0; j < canvasWidth; j++){
                                        pixels[i][j] = null;
                                    }
                                }
                                break;
                            default: 
                                System.out.println("No mode selected");
                        }
                    }
                }
            }
            //tracks the position of the mouse whenever it is moved by the user
            @Override
            public void mouseMoved(MouseEvent event){
                mouseMoveX = ((event.getX() / globalPixelSize) * globalPixelSize);
                mouseMoveY = ((event.getY() / globalPixelSize) * globalPixelSize);
                setMouseLocationText(mouseMoveX / globalPixelSize, mouseMoveY / globalPixelSize);
                hoverRectangle = new Rectangle(mouseMoveX, mouseMoveY, globalPixelSize, globalPixelSize); //creates a rectangle on mouse location 
                canvasPanel.repaint(); //so that it is consistently updated
            }
        });
    }
    
    //the canvas grid
    private void drawCanvas(Graphics2D g2d){
        //vertical lines
        for (int i = xPanel; i <= (canvasWidth * globalPixelSize); i+=globalPixelSize){
            g2d.drawLine(i, yPanel, i, yPanel + (canvasHeight * globalPixelSize));
        }
        //horizontal lines
        for (int j = yPanel; j <= (canvasHeight * globalPixelSize); j+=globalPixelSize){
            g2d.drawLine(xPanel, j, xPanel + (canvasWidth * globalPixelSize), j);
        }
    }

    public void createMouseLabel(){
        this.mouseLocation = new JLabel("0 : 0");
        this.mouseLocation.setSize(100,50);
    }

    public void setMouseLocationText(int mouseX, int mouseY){
        this.mouseLocation.setText(mouseX + " : " + mouseY);
    }

    public JPanel getCanvas(){return this.canvasPanel;}
    public JLabel getMouseLabel(){return this.mouseLocation;}

    /*
     * the mode is the current tool being used, mode is represented by an integer.
     * following integers represent the following modes:
     * 0 - default brush/draw
     * 1 - eraser
     * 2 - colour picker
     * 3 - bucket fill
     * 4 - redo
     * 5 - undo
     * 6 - x-mirror
     * 7 - y-mirror
     * 8 - x/y-mirror
     * 9 - trash 
     * ...
     * more to be added
     * these are the current tool i want to implment
     */
    public void setCurrentCanvasMode(int mode){
        this.currentCanvasMode = mode;
    }

    private int getCurrentCanvasMode(){return this.currentCanvasMode;}

    //temporary 
    public void undoAction(String action){
    }
}