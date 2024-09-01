import javax.sound.sampled.Line;
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
    private JPanel palettePanel;
    private JPanel volatilePanel;
    private int xPanel; 
    private int yPanel; 
    private int canvasWidth;
    private int canvasHeight;
    private int globalPixelSize = 20; 
    
    private final RenderingHints renderingHints  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-aliasing

    private Pixel[][] pixels;
    private Color currentColour = new Color(0, 0, 0);
    private Color[] permanentColourPalette; //colours that user selected using the RGB slider will be added
    private int permanentColourIndex = 0;
    private Color[] volatileColourPalette;  //colours that the colour picker selects, temporary colours
    private int volatileColourIndex = 0;
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

    //blank canvas constructor
    //pixel size is determined before the creation of the program
    public Canvas(int xP, int yP, int pS, int cW, int cH){
        xPanel = 0;
        yPanel = 0;
        globalPixelSize = pS;
        canvasWidth = cW;
        canvasHeight = cH;
        this.pixels = new Pixel[canvasHeight][canvasWidth]; //height and width is the resolution
        this.permanentColourPalette = new Color[16]; //palette is able to store 16 colours
        this.volatileColourPalette = new Color[8]; //last 6 colour picked 
        initPixels();
        createPixelCanvas();
        createMouseLabel();  
        createVolatilePalette();
        createPermanentPalette();
        canvasPanel.repaint();
    }

    private void createPixelCanvas(){
        this.canvasPanel = new JPanel(){
            //main method of the program, allows painting of pixels
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
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
                            Rectangle hover;
                            switch (brushSize){
                                case 1:
                                    hover = new Rectangle(temp.x, temp.y, temp.width, temp.height);
                                    break;
                                case 2:
                                    hover = new Rectangle(temp.x, temp.y, temp.width * 2, temp.height * 2);
                                    break;
                                case 3:
                                    hover = new Rectangle(temp.x - globalPixelSize, temp.y - globalPixelSize, temp.width * 3, temp.height * 3);
                                    break;
                                case 4:
                                    hover = new Rectangle(temp.x - globalPixelSize, temp.y - globalPixelSize, temp.width * 4, temp.height * 4);
                                    break;
                                default:
                                    hover = null;
                                    break;
                            }
                            g2d.setColor(Color.RED);
                            g2d.drawRect(hover.x, hover.y, hover.width, hover.height);
                            g2d.setColor(transparentColour);
                            g2d.fillRect(hover.x, hover.y, hover.width, hover.height);
                            g2d.setColor(currentColour);
                        }
                    }
                }
                // // int index = 0;
                // for (int i = xPanel; i <= (canvasWidth * globalPixelSize); i+=globalPixelSize){
                //     g2d.setColor(Color.BLACK);
                //     g2d.drawLine(i, yPanel, i, yPanel + (canvasHeight * globalPixelSize));
                // }
                // //horizontal lines
                // for (int j = yPanel; j <= (canvasHeight * globalPixelSize); j+=globalPixelSize){
                //     g2d.drawLine(xPanel, j, xPanel + (canvasWidth * globalPixelSize), j);
                // }
                // g2d.setColor(currentColour);
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
                            if (brushSize == 2){
                                brushTwo(indexY, indexX, currentColour);
                            } else if (brushSize == 3){
                                brushThree(indexY, indexX, currentColour);
                            } else if (brushSize == 4){
                                brushFour(indexY, indexX, currentColour);
                            }
                            canvasPanel.repaint();
                            break;
                        case 1:
                            pixels[indexY][indexX].setPixelColour(Color.WHITE);
                            if (brushSize == 2){
                                brushTwo(indexY, indexX, Color.WHITE);
                            } else if (brushSize == 3){
                                brushThree(indexY, indexX, Color.WHITE);
                            } else if (brushSize == 4){
                                brushFour(indexY, indexX, Color.WHITE);
                            }
                            canvasPanel.repaint(); 
                            break;
                        case 2: //colourpicker
                            currentColour = pixels[indexY][indexX].getPixelColour();
                            if (volatileColourIndex < 8){
                                volatileColourPalette[volatileColourIndex] = currentColour;
                                volatileColourIndex++;
                                if (volatileColourIndex == 8){volatileColourIndex = 0;}
                                volatilePanel.repaint();
                            }
                            break;
                        case 3: //bucketfill
                            Color replacedColour = pixels[indexY][indexX].getPixelColour();
                            if (!currentColour.equals(replacedColour)){
                                depthFirstSearch(indexY, indexX, replacedColour);
                            }
                            canvasPanel.repaint();
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
                                //even width canvas
                                if (indexX < midpoint) {
                                    mouseClickX = (canvasWidth * globalPixelSize) - mouseClickX - globalPixelSize;
                                    pixels[indexY][canvasWidth - 1 - indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                }
                            } else {
                                //odd width canvas
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
                                    pixels[i][j].setPixelColour(Color.WHITE);
                                }
                            }
                            break;
                        default:
                            System.out.println("No mode selected");
                    }
                }
                canvasPanel.repaint();
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
                                if (brushSize == 2){
                                    brushTwo(indexY, indexX, currentColour);
                                } else if (brushSize == 3){
                                    brushThree(indexY, indexX, currentColour);
                                } else if (brushSize == 4){
                                    brushFour(indexY, indexX, currentColour);
                                }
                                canvasPanel.repaint();
                                break;
                            case 1: //eraser
                                pixels[indexY][indexX].setPixelColour(Color.WHITE);
                                if (brushSize == 2){
                                    brushTwo(indexY, indexX, Color.WHITE);
                                } else if (brushSize == 3){
                                    brushThree(indexY, indexX, Color.WHITE);
                                } else if (brushSize == 4){
                                    brushFour(indexY, indexX, Color.WHITE);
                                }
                                canvasPanel.repaint(); 
                                break;
                            case 2: //colourpicker //NOT REALLY NEEDED AS WHO WOULD DRAG A COLOUR PICKER?!
                                currentColour = pixels[indexY][indexX].getPixelColour();
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
                                    if (indexX < midpoint) {
                                        mouseClickX = (canvasWidth * globalPixelSize) - mouseClickX - globalPixelSize;
                                        pixels[indexY][canvasWidth - 1 - indexX] = new Pixel(mouseClickX, mouseClickY, globalPixelSize, currentColour);
                                    }
                                } else {
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
                                        pixels[i][j].setPixelColour(Color.WHITE);
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

    private void brushTwo(int y, int x, Color paintColour){
        if (x + 1 < canvasWidth){pixels[y][x + 1] = new Pixel((x + 1) * globalPixelSize, y * globalPixelSize, globalPixelSize, paintColour);}
        if (y + 1 < canvasHeight){pixels[y + 1][x] = new Pixel(x * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((x + 1 < canvasWidth) && (y + 1 < canvasHeight)){pixels[y + 1][x + 1] = new Pixel((x + 1) * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
    }

    private void brushThree(int y, int x, Color paintColour){
        if ((y - 1 >= 0) && (x - 1 >= 0)){pixels[y - 1][x - 1] = new Pixel((x - 1) * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}
        if (y - 1 >= 0){pixels[y - 1][x] = new Pixel(x * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((y - 1 >= 0) && (x + 1 < canvasWidth)){pixels[y - 1][x + 1] = new Pixel((x + 1) * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}
        if (x - 1 >= 0){pixels[y][x - 1] = new Pixel((x - 1) * globalPixelSize, y * globalPixelSize, globalPixelSize, paintColour);}
        if (x + 1 < canvasWidth){pixels[y][x + 1] = new Pixel((x + 1) * globalPixelSize, y * globalPixelSize, globalPixelSize, paintColour);}
        if ((y + 1 < canvasHeight) && (x - 1 >= 0)){pixels[y + 1][x - 1] = new Pixel((x - 1) * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
        if (y + 1 < canvasHeight){pixels[y + 1][x] = new Pixel(x * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((x + 1 < canvasWidth) && (y + 1 < canvasHeight)){pixels[y + 1][x + 1] = new Pixel((x + 1) * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
    }

    private void brushFour(int y, int x, Color paintColour){
        if ((y - 1 >= 0) && (x - 1 >= 0)){pixels[y - 1][x - 1] = new Pixel((x - 1) * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}
        if (y - 1 >= 0){pixels[y - 1][x] = new Pixel(x * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((y - 1 >= 0) && (x + 1 < canvasWidth)){pixels[y - 1][x + 1] = new Pixel((x + 1) * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((y - 1 >= 0) && (x + 2 < canvasWidth)){pixels[y - 1][x + 2] = new Pixel((x + 2) * globalPixelSize, (y - 1) * globalPixelSize, globalPixelSize, paintColour);}

        if (x - 1 >= 0){pixels[y][x - 1] = new Pixel((x - 1) * globalPixelSize, y * globalPixelSize, globalPixelSize, paintColour);}
        if (x + 1 < canvasWidth){pixels[y][x + 1] = new Pixel((x + 1) * globalPixelSize, y * globalPixelSize, globalPixelSize, paintColour);}
        if (x + 2 < canvasWidth){pixels[y][x + 2] = new Pixel((x + 2) * globalPixelSize, y * globalPixelSize, globalPixelSize, paintColour);}

        if ((x + 1 < canvasWidth) && (y + 1 < canvasHeight)){pixels[y + 1][x + 1] = new Pixel((x + 1) * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
        if (y + 1 < canvasHeight){pixels[y + 1][x] = new Pixel(x * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((y + 1 < canvasHeight) && (x - 1 >= 0)){pixels[y + 1][x - 1] = new Pixel((x - 1) * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}
        if ((x + 2 < canvasWidth) && (y + 1 < canvasHeight)){pixels[y + 1][x + 2] = new Pixel((x + 2) * globalPixelSize, (y + 1) * globalPixelSize, globalPixelSize, paintColour);}

        if ((x + 1 < canvasWidth) && (y + 2 < canvasHeight)){pixels[y + 2][x + 1] = new Pixel((x + 1) * globalPixelSize, (y + 2) * globalPixelSize, globalPixelSize, paintColour);}
        if (y + 2 < canvasHeight){pixels[y + 2][x] = new Pixel(x * globalPixelSize, (y + 2) * globalPixelSize, globalPixelSize, paintColour);}
        if ((y + 2 < canvasHeight) && (x - 1 >= 0)){pixels[y + 2][x - 1] = new Pixel((x - 1) * globalPixelSize, (y + 2) * globalPixelSize, globalPixelSize, paintColour);}
        if ((x + 2 < canvasWidth) && (y + 2 < canvasHeight)){pixels[y + 2][x + 2] = new Pixel((x + 2) * globalPixelSize, (y + 2) * globalPixelSize, globalPixelSize, paintColour);}

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

    //flood fill algorithm for bucket fill tool
    private void depthFirstSearch(int indexY, int indexX, Color replacedColour){
        if ((indexY >= canvasHeight) || (indexX >= canvasWidth) || (indexY < 0) || (indexX < 0)){
            return;
        } else if (!replacedColour.equals(pixels[indexY][indexX].getPixelColour())){
            return;
        } else {
            pixels[indexY][indexX].setPixelColour(currentColour);
            depthFirstSearch(indexY + 1, indexX, replacedColour);
            depthFirstSearch(indexY - 1, indexX, replacedColour);
            depthFirstSearch(indexY, indexX + 1, replacedColour);
            depthFirstSearch(indexY, indexX - 1, replacedColour);
        }
    }

    private void initPixels(){
        for (int i = 0; i < this.canvasHeight; i++){
            for (int j = 0; j < this.canvasWidth; j++){
                pixels[i][j] = new Pixel(j * globalPixelSize, i * globalPixelSize, globalPixelSize, Color.WHITE);
            }
        }
    }

    public void setBrushSize(int brushSize){this.brushSize = brushSize;}

    public void setCurrentColour(Color newColour){
        this.currentColour = newColour;
    }

    public void addToPermanentPalette(Color colour){
        for (int i = 0; i < 16; i++){
            if (permanentColourPalette[i] == null){
                permanentColourPalette[i] = colour;
                palettePanel.repaint();
                break;
            } else {
                if (permanentColourPalette[i].equals(colour)){
                    break;
                }
            }
        }
    }

    public void removeFromPermanentPalette(){
        for (int i = 0; i < 16; i++){
            if (permanentColourPalette[i] != null){
               if (permanentColourPalette[i].equals(currentColour)){
                    permanentColourPalette[i] = null;
                    palettePanel.repaint();
                    break;
                } 
            }
        
        }
    }

    private void createVolatilePalette(){
        volatilePanel = new JPanel(null){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setClip(0,0,200,25);
                int index = 0;
                for (int i = 0; i < 200; i+=25){
                    g2d.setColor(volatileColourPalette[index]);
                    g2d.drawRect(i, 0, 25, 25);
                    g2d.fillRect(i, 0, 25, 25);
                    index++;
                }
            }
        };
        volatilePanel.setSize(200,25);
    }

    public JPanel getVolatilePanel(){return this.volatilePanel;}

    private void createPermanentPalette(){
        palettePanel = new JPanel(null){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
                int index = 0;
                for (int i = 0; i < 50; i+=25){
                    for (int j = 0; j < 200; j+=25){
                        if (permanentColourPalette[index] != null){
                            g2d.setColor(permanentColourPalette[index]);
                            g2d.drawRect(j, i, 25, 25);
                            g2d.fillRect(j, i, 25, 25);
                            g2d.setColor(Color.WHITE);
                            index++;
                        }
                    }
                }
            }
        };

        palettePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                int x = e.getX() / 25;
                int y = e.getY() / 25;
                int index = (y == 0) ? x : x + 8;
                if (permanentColourPalette[index] != null ){setCurrentColour(permanentColourPalette[index]);};
            }
        });
    }

    public JPanel getColourPalette(){return this.palettePanel;}
}