import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Canvas{
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
        public void setGlobalPixelSize(int size){globalPixelSize = size;}
        
        public void setLocation(int x, int y){
            originalX = x; originalY = y;
            rectangle = new Rectangle(x,y,globalPixelSize, globalPixelSize);
        }
    }

    private JPanel canvasPanel;
    private JLabel mouseLocation;
    private JPanel palettePanel;
    private JPanel volatilePanel;

    private int canvasHeight;
    private int canvasWidth;
    private Pixel[][] pixels;
    private int pixelSize;
    private Color currentColour = Color.BLACK;

    private Color[] permanentColourPalette; //colours that user selected using the RGB slider will be added
    private int permanentColourIndex = 0;
    private Color[] volatileColourPalette;  //colours that the colour picker selects, temporary colours
    private int volatileColourIndex = 0;
    private Color transparentColour = new Color(100,100,100,50);
    private int brushSize = 1; //brush sizes can either by 1, 4, 9 - determines the number of pixels coloured with one click
    private int currentCanvasMode = 0; //this is the tool currently selected, only one can be selected (with the exception of x/y mirror tools being combined)
    private Rectangle hoverRectangle;

    private int viewportWidth = 0;
    private int viewportHeight = 0;
    private int viewportStartX = 0; //range of indexes that will be shown with zoom
    private int viewportStartY = 0;
    private int viewportEndX;
    private int viewportEndY;
    private int zoomFactor = 2; //each zoom in is a factor of 2
    private int currentZoom = 1; //tracks current zoom

    private int mouseClickX = -1;
    private int mouseClickY = -1;
    private int mouseMoveX = -1; //track mouse movement for hover rectangle
    private int mouseMoveY = -1;

    private boolean isMouseHeldDown = false;

    private Stack<String> undoStack; //tracks the last N actions that have altered the canvas
    private ArrayList<String> currentAction; //tracks the current action being performed, adds to top of stack at end of action

    public Canvas(int height, int width, int pixelsize){
        canvasHeight = height;
        canvasWidth = width;
        pixelSize = pixelsize;
        viewportEndX = width;
        viewportEndY = height;
        pixels = new Pixel[height][width];
        undoStack = new Stack<>();
        undoStack.setSize(20);
        currentAction = new ArrayList<>();
        initPixels(); //change pixels to none null
        initPaint(); //paint component
        initCanvas(); //jpanel adjustments
        createMouseLabel();
        createPermanentPalette();
        createVolatilePalette();
        addMouseListener(); //mouse actions
        canvasPanel.repaint();
    }

    private void initPixels(){
        for (int i = 0; i < canvasHeight; i++){
            for (int j = 0; j < canvasWidth; j++){
                pixels[i][j] = new Pixel(j * pixelSize, i * pixelSize, pixelSize, Color.WHITE);
            }
        }
    }

    private void initPaint(){
        this.canvasPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setClip(0, 0, 800, 800);
                if ((mouseClickX >= 0) && (mouseClickY >= 0)){
                    for (int i = viewportStartY; i < viewportEndY; i++){
                        for (int  j = viewportStartX ; j < viewportEndX; j++){
                            Rectangle pixel = pixels[i][j].getRectangle();    
                            g.setColor(pixels[i][j].getPixelColour());
                            g.drawRect(pixel.x, pixel.y, pixelSize, pixelSize);
                            g.fillRect(pixel.x, pixel.y, pixelSize, pixelSize);

                        }
                    }
                }
                if ((mouseMoveY/pixelSize + viewportStartY < canvasHeight) && (mouseMoveX/pixelSize + viewportStartX < canvasWidth) && (mouseMoveX > -1) && (mouseMoveY > -1)){
                    Rectangle temp = pixels[mouseMoveY/pixelSize + viewportStartY][mouseMoveX/pixelSize + + viewportStartX].getRectangle();                        
                    Rectangle hover;
                    switch (brushSize){
                        case 1:
                            hover = new Rectangle(temp.x, temp.y, temp.width, temp.height);
                            break;
                        case 2:
                            hover = new Rectangle(temp.x, temp.y, temp.width * 2, temp.height * 2);
                            break;
                        case 3:
                            hover = new Rectangle(temp.x - pixelSize, temp.y - pixelSize, temp.width * 3, temp.height * 3);
                            break;
                        case 4:
                            hover = new Rectangle(temp.x - pixelSize, temp.y - pixelSize, temp.width * 4, temp.height * 4);
                            break;
                        default:
                            hover = null;
                            break;
                    }
                    g.setColor(Color.RED);
                    g.drawRect(hover.x, hover.y, hover.width, hover.height);
                    g.setColor(transparentColour);
                    g.fillRect(hover.x, hover.y, hover.width, hover.height);
                    g.setColor(currentColour);
                }
            }
        };
    }

    private void initCanvas(){
        canvasPanel.setSize(canvasWidth * pixelSize, canvasHeight * pixelSize);
    }

    private void addMouseListener(){
        canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event){
                isMouseHeldDown = true;
                currentAction.add(Integer.toString(currentCanvasMode));
                currentAction.add(Integer.toString(currentColour.getRed()) + ":" + Integer.toString(currentColour.getGreen()) + ":" + Integer.toString(currentColour.getBlue()));
                mouseClickX = mouseMoveX = event.getX(); //mouseClick is only necessary for finding out the index of pixel
                mouseClickY = mouseMoveY = event.getY(); //mouseMove is needed for the hover rectangle
                int indexX = (mouseClickX / pixelSize) + viewportStartX; //finds index of placed pixel, pixel size will change with the zoom of the canvas
                int indexY = (mouseClickY / pixelSize) + viewportStartY; //viewPort is for when it is zoomed in

                if ((indexX < canvasWidth) && (indexY < canvasHeight) && (mouseClickX > - 1) && (mouseClickY > -1)){
                    // pixels[indexY][indexX].setPixelColour(currentColour);
                    int midpoint;
                    switch (currentCanvasMode){
                        case 0:
                            pixels[indexY][indexX].setPixelColour(currentColour);
                            if (!currentAction.get(currentAction.size() - 1).equals(indexX + ":" + indexY + ",")){
                                currentAction.add(indexX + ":" + indexY + ",");
                            }
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
                            pixels[indexY][indexX].setPixelColour(currentColour);
                            if (brushSize == 2){
                                brushTwo(indexY, indexX, currentColour);
                            } else if (brushSize == 3){
                                brushThree(indexY, indexX, currentColour);
                            } else if (brushSize == 4){
                                brushFour(indexY, indexX, currentColour);
                            }
                            midpoint = canvasHeight / 2;
                            if (canvasHeight % 2 == 0){
                                if (indexY < midpoint){
                                    mouseClickY = (canvasHeight * pixelSize) - mouseClickY - pixelSize;
                                    pixels[canvasHeight - 1 - indexY][indexX].setPixelColour(currentColour);
                                    if (brushSize == 2){
                                        brushTwo(canvasHeight - 1 - indexY, indexX, currentColour);
                                    } else if (brushSize == 3){
                                        brushThree(canvasHeight - 1 - indexY, indexX, currentColour);
                                    } else if (brushSize == 4){
                                        brushFour(canvasHeight - 1 - indexY, indexX, currentColour);
                                    }
                                }
                            } else {
                                if (indexY < midpoint){
                                    mouseClickY = (canvasHeight * pixelSize) - mouseClickY - pixelSize;
                                    pixels[canvasHeight - 1 - indexY][indexX].setPixelColour(currentColour);
                                    if (brushSize == 2){
                                        brushTwo(canvasHeight - 1 - indexY, indexX, currentColour);
                                    } else if (brushSize == 3){
                                        brushThree(canvasHeight - 1 - indexY, indexX, currentColour);
                                    } else if (brushSize == 4){
                                        brushFour(canvasHeight - 1 - indexY, indexX, currentColour);
                                    }
                                } else if (indexY > midpoint){
                                    mouseClickY = (canvasHeight - 1 - indexY) * pixelSize;
                                    pixels[midpoint - (indexY - midpoint)][indexX].setPixelColour(currentColour);
                                    if (brushSize == 2){
                                        brushTwo(midpoint - (indexY - midpoint), indexX, currentColour);
                                    } else if (brushSize == 3){
                                        brushThree(midpoint - (indexY - midpoint), indexX, currentColour);
                                    } else if (brushSize == 4){
                                        brushFour(midpoint - (indexY - midpoint), indexX, currentColour);
                                    }
                                }
                            }
                            canvasPanel.repaint();
                            break;
                        case 7: //ymirror
                            pixels[indexY][indexX].setPixelColour(currentColour);
                            if (brushSize == 2){
                                brushTwo(indexY, indexX, currentColour);
                            } else if (brushSize == 3){
                                brushThree(indexY, indexX, currentColour);
                            } else if (brushSize == 4){
                                brushFour(indexY, indexX, currentColour);
                            }
                            midpoint = canvasWidth / 2;
                            if (canvasWidth % 2 == 0) {
                                //even width canvas
                                if (indexX < midpoint) {
                                    mouseClickX = (canvasWidth * pixelSize) - mouseClickX - pixelSize;
                                    pixels[indexY][canvasWidth - 1 - indexX].setPixelColour(currentColour);
                                    if (brushSize == 2){
                                        brushTwo(indexY, canvasWidth - 1 - indexX, currentColour);
                                    } else if (brushSize == 3){
                                        brushThree(indexY, canvasWidth - 1 - indexX, currentColour);
                                    } else if (brushSize == 4){
                                        brushFour(indexY, canvasWidth - 1 - indexX, currentColour);
                                    }
                                }
                            } else {
                                //odd width canvas
                                if (indexX < midpoint) {
                                    mouseClickX = (canvasWidth * pixelSize) - mouseClickX - pixelSize;
                                    pixels[indexY][canvasWidth - 1 - indexX].setPixelColour(currentColour);
                                    if (brushSize == 2){
                                        brushTwo(indexY, canvasWidth - 1 - indexX, currentColour);
                                    } else if (brushSize == 3){
                                        brushThree(indexY, canvasWidth - 1 - indexX, currentColour);
                                    } else if (brushSize == 4){
                                        brushFour(indexY, canvasWidth - 1 - indexX, currentColour);
                                    }
                                } else if (indexX > midpoint) {
                                    mouseClickX = (canvasWidth - 1 - indexX) * pixelSize;
                                    pixels[indexY][midpoint - (indexX - midpoint)].setPixelColour(currentColour);
                                    if (brushSize == 2){
                                        brushTwo(indexY, midpoint - (indexX - midpoint), currentColour);
                                    } else if (brushSize == 3){
                                        brushThree(indexY, midpoint - (indexX - midpoint), currentColour);
                                    } else if (brushSize == 4){
                                        brushFour(indexY, midpoint - (indexX - midpoint), currentColour);
                                    }
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
                        case 9:
                            ZoomIn(indexX, indexY);
                            break;
                        case 10:
                            ZoomOut(indexX, indexY);
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
                pushToStack();
            }
        });

        canvasPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event){
                if (isMouseHeldDown){
                    mouseClickX = mouseMoveX = event.getX();
                    mouseClickY = mouseMoveY = event.getY();
                    int indexX = (mouseClickX / pixelSize) + viewportStartX; //finds index of placed pixel, pixel size will change with the zoom of the canvas
                    int indexY = (mouseClickY / pixelSize) + viewportStartY; //viewPort is for when it is zoomed in

                    if ((indexX < canvasWidth) && (indexY < canvasHeight) && (mouseClickX > - 1) && (mouseClickY > -1)){
                        // pixels[indexY][indexX].setPixelColour(currentColour);
                        int midpoint;
                        switch (currentCanvasMode){
                            case 0: //pen
                                if (!currentAction.get(currentAction.size() - 1).equals(indexX + ":" + indexY + ",")){ //prevents multiple additions of same action
                                    currentAction.add(indexX + ":" + indexY + ",");
                                }
                                pixels[indexY][indexX].setPixelColour(currentColour);
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
                                if (!currentAction.get(currentAction.size() - 1).equals(indexX + ":" + indexY + ",")){ //prevents multiple additions of same action
                                    currentAction.add(indexX + ":" + indexY + ",");
                                }
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
                                pixels[indexY][indexX].setPixelColour(currentColour);
                                if (!currentAction.get(currentAction.size() - 1).equals(indexX + ":" + indexY + ",")){ //prevents multiple additions of same action
                                    currentAction.add(indexX + ":" + indexY + ",");
                                }
                                if (brushSize == 2){
                                    brushTwo(indexY, indexX, currentColour);
                                } else if (brushSize == 3){
                                    brushThree(indexY, indexX, currentColour);
                                } else if (brushSize == 4){
                                    brushFour(indexY, indexX, currentColour);
                                }
                                midpoint = canvasHeight / 2;
                                if (canvasHeight % 2 == 0){
                                    if (indexY < midpoint){
                                        mouseClickY = (canvasHeight * pixelSize) - mouseClickY - pixelSize;
                                        pixels[canvasHeight - 1 - indexY][indexX].setPixelColour(currentColour);
                                        if (brushSize == 2){
                                            brushTwo(canvasHeight - 1 - indexY, indexX, currentColour);
                                        } else if (brushSize == 3){
                                            brushThree(canvasHeight - 1 - indexY, indexX, currentColour);
                                        } else if (brushSize == 4){
                                            brushFour(canvasHeight - 1 - indexY, indexX, currentColour);
                                        }
                                    }
                                } else {
                                    if (indexY < midpoint){
                                        mouseClickY = (canvasHeight * pixelSize) - mouseClickY - pixelSize;
                                        pixels[canvasHeight - 1 - indexY][indexX].setPixelColour(currentColour);
                                        if (brushSize == 2){
                                            brushTwo(canvasHeight - 1 - indexY, indexX, currentColour);
                                        } else if (brushSize == 3){
                                            brushThree(canvasHeight - 1 - indexY, indexX, currentColour);
                                        } else if (brushSize == 4){
                                            brushFour(canvasHeight - 1 - indexY, indexX, currentColour);
                                        }
                                    } else if (indexY > midpoint){
                                        mouseClickY = (canvasHeight - 1 - indexY) * pixelSize;
                                        pixels[midpoint - (indexY - midpoint)][indexX].setPixelColour(currentColour);
                                        if (brushSize == 2){
                                            brushTwo(midpoint - (indexY - midpoint), indexX, currentColour);
                                        } else if (brushSize == 3){
                                            brushThree(midpoint - (indexY - midpoint), indexX, currentColour);
                                        } else if (brushSize == 4){
                                            brushFour(midpoint - (indexY - midpoint), indexX, currentColour);
                                        }
                                    }
                                }
                                canvasPanel.repaint();
                                break;
                            case 7: //xmirror   
                                pixels[indexY][indexX].setPixelColour(currentColour);
                                if (!currentAction.get(currentAction.size() - 1).equals(indexX + ":" + indexY + ",")){ //prevents multiple additions of same action
                                    currentAction.add(indexX + ":" + indexY + ",");
                                }
                                if (brushSize == 2){
                                    brushTwo(indexY, indexX, currentColour);
                                } else if (brushSize == 3){
                                    brushThree(indexY, indexX, currentColour);
                                } else if (brushSize == 4){
                                    brushFour(indexY, indexX, currentColour);
                                }
                                midpoint = canvasWidth / 2;
                                if (canvasWidth % 2 == 0) {
                                    //even width canvas
                                    if (indexX < midpoint) {
                                        mouseClickX = (canvasWidth * pixelSize) - mouseClickX - pixelSize;
                                        pixels[indexY][canvasWidth - 1 - indexX].setPixelColour(currentColour);
                                        if (brushSize == 2){
                                            brushTwo(indexY, canvasWidth - 1 - indexX, currentColour);
                                        } else if (brushSize == 3){
                                            brushThree(indexY, canvasWidth - 1 - indexX, currentColour);
                                        } else if (brushSize == 4){
                                            brushFour(indexY, canvasWidth - 1 - indexX, currentColour);
                                        }
                                    }
                                } else {
                                    //odd width canvas
                                    if (indexX < midpoint) {
                                        mouseClickX = (canvasWidth * pixelSize) - mouseClickX - pixelSize;
                                        pixels[indexY][canvasWidth - 1 - indexX].setPixelColour(currentColour);
                                        if (brushSize == 2){
                                            brushTwo(indexY, canvasWidth - 1 - indexX, currentColour);
                                        } else if (brushSize == 3){
                                            brushThree(indexY, canvasWidth - 1 - indexX, currentColour);
                                        } else if (brushSize == 4){
                                            brushFour(indexY, canvasWidth - 1 - indexX, currentColour);
                                        }
                                    } else if (indexX > midpoint) {
                                        mouseClickX = (canvasWidth - 1 - indexX) * pixelSize;
                                        pixels[indexY][midpoint - (indexX - midpoint)].setPixelColour(currentColour);
                                        if (brushSize == 2){
                                            brushTwo(indexY, midpoint - (indexX - midpoint), currentColour);
                                        } else if (brushSize == 3){
                                            brushThree(indexY, midpoint - (indexX - midpoint), currentColour);
                                        } else if (brushSize == 4){
                                            brushFour(indexY, midpoint - (indexX - midpoint), currentColour);
                                        }
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
                            case 9:
                                ZoomIn(indexX, indexY);
                                break;
                            case 10:
                                ZoomOut(indexX, indexY);
                                break;
                            default: 
                                System.out.println("No mode selected");
                        }
                    }
                    canvasPanel.repaint();
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent event){
                mouseMoveX = ((event.getX() / pixelSize) * pixelSize);
                mouseMoveY = ((event.getY() / pixelSize) * pixelSize);
                setMouseLocationText(mouseMoveX / pixelSize, mouseMoveY / pixelSize);
                canvasPanel.repaint(); //so that it is consistently updated
            }
        });
    }

    private void brushTwo(int y, int x, Color paintColour){
        if (x + 1 < canvasWidth){
            pixels[y][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + y + ",");
        }
        if (y + 1 < canvasHeight){
            pixels[y + 1][x].setPixelColour(paintColour);
            currentAction.add(x + ":" + (y + 1) + ",");
        }
        if ((x + 1 < canvasWidth) && (y + 1 < canvasHeight)){
            pixels[y + 1][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y + 1) + ",");
        }
    }

    private void brushThree(int y, int x, Color paintColour){
        if ((y - 1 >= 0) && (x - 1 >= 0)){
            pixels[y - 1][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y - 1) + ",");
        }
        if (y - 1 >= 0){
            pixels[y - 1][x].setPixelColour(paintColour);
            currentAction.add((x) + ":" + (y - 1) + ",");

        }
        if ((y - 1 >= 0) && (x + 1 < canvasWidth)){
            pixels[y - 1][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y - 1) + ",");
        }
        if (x - 1 >= 0){
            pixels[y][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y) + ",");
        }
        if (x + 1 < canvasWidth){
            pixels[y][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y) + ",");
        }
        if ((y + 1 < canvasHeight) && (x - 1 >= 0)){
            pixels[y + 1][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y + 1) + ",");
        }
        if (y + 1 < canvasHeight){
            pixels[y + 1][x].setPixelColour(paintColour);
            currentAction.add((x) + ":" + (y + 1) + ",");
        }
        if ((x + 1 < canvasWidth) && (y + 1 < canvasHeight)){
            pixels[y + 1][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y + 1) + ",");
        }
    }

    private void brushFour(int y, int x, Color paintColour){
        if ((y - 1 >= 0) && (x - 1 >= 0)){
            pixels[y - 1][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y - 1) + ",");
        }
        if (y - 1 >= 0){
            pixels[y - 1][x].setPixelColour(paintColour);
            currentAction.add((x) + ":" + (y - 1) + ",");
        }
        if ((y - 1 >= 0) && (x + 1 < canvasWidth)){
            pixels[y - 1][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y - 1) + ",");
        }
        if ((y - 1 >= 0) && (x + 2 < canvasWidth)){
            pixels[y - 1][x + 2].setPixelColour(paintColour);
            currentAction.add((x + 2) + ":" + (y - 1) + ",");
        }

        if (x - 1 >= 0){
            pixels[y][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y) + ",");
        }
        if (x + 1 < canvasWidth){
            pixels[y][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y) + ",");
        }
        if (x + 2 < canvasWidth){
            pixels[y][x + 2].setPixelColour(paintColour);
            currentAction.add((x + 2) + ":" + (y) + ",");
        }

        if ((x + 1 < canvasWidth) && (y + 1 < canvasHeight)){
            pixels[y + 1][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y + 1) + ",");

        }
        if (y + 1 < canvasHeight){
            pixels[y + 1][x].setPixelColour(paintColour);
            currentAction.add((x) + ":" + (y + 1) + ",");
        }
        if ((y + 1 < canvasHeight) && (x - 1 >= 0)){
            pixels[y + 1][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y + 1) + ",");
        }
        if ((x + 2 < canvasWidth) && (y + 1 < canvasHeight)){
            pixels[y + 1][x + 2].setPixelColour(paintColour);
            currentAction.add((x + 2) + ":" + (y + 1) + ",");
        }

        if ((x + 1 < canvasWidth) && (y + 2 < canvasHeight)){
            pixels[y + 2][x + 1].setPixelColour(paintColour);
            currentAction.add((x + 1) + ":" + (y + 2) + ",");
        }
        if (y + 2 < canvasHeight){
            pixels[y + 2][x].setPixelColour(paintColour);
            currentAction.add((x) + ":" + (y + 2) + ",");
        }
        if ((y + 2 < canvasHeight) && (x - 1 >= 0)){
            pixels[y + 2][x - 1].setPixelColour(paintColour);
            currentAction.add((x - 1) + ":" + (y + 2) + ",");
        }
        if ((x + 2 < canvasWidth) && (y + 2 < canvasHeight)){
            pixels[y + 2][x + 2].setPixelColour(paintColour);
            currentAction.add((x + 2) + ":" + (y + 2) + ",");
        }
    }

    public void createMouseLabel(){
        this.mouseLocation = new JLabel("[ 0 : 0 ]");
        this.mouseLocation.setSize(100,50);
    }

    public void setMouseLocationText(int mouseX, int mouseY){
        this.mouseLocation.setText("[ " + mouseX + " : " + mouseY + " ]");
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

    //called upon deletion closure of the JFrame.
    public void deleteUndoStack(){
    }

    public void undoAction(){
        String poppedAction[] = (undoStack.pop()).split(";");
        int mode = Integer.parseInt(poppedAction[0]);
        String rgbValues[] = poppedAction[1].split(":");
        Color replacedColour = new Color(Integer.parseInt(rgbValues[0]), Integer.parseInt(rgbValues[1]), Integer.parseInt(rgbValues[2]));
        String indexes[] = poppedAction[2].split(",");
        switch (mode) {
            case 0:
                for (int i = 0; i < indexes.length; i++){
                    String temp[] = indexes[i].split(":");
                    int x = Integer.parseInt(temp[0]);
                    int y = Integer.parseInt(temp[1]);
                    pixels[y][x] = new Pixel(x * pixelSize, y * pixelSize, pixelSize, Color.WHITE);
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                break;
        }
        canvasPanel.repaint();
        setCurrentColour(replacedColour);
    }

    private void redoAction(){

    }

    /*
     * same as the canvas mode, these are the opcode for the actions taken
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
    private void pushToStack(){
        String action = currentAction.getFirst() + ";";
        action = action + currentAction.get(1) + ";";
        for (int i = 2; i < currentAction.size(); i++){
            action = action + currentAction.get(i);
        }
        // System.out.println(temp);
        undoStack.push(action);
        // printStack();
        currentAction.clear();
    }

    public void setGlobalPixel(int size){
        this.pixelSize = size;
    }

    //TEMPORARY METHOD TO CHECK FUNCTIONALITY
    private void printStack(){
        for (String x : undoStack){
            System.out.println(x);
        }
        System.out.println();
    }    
    
    public void ZoomIn(int x, int y){
        viewportHeight = canvasHeight / zoomFactor;
        viewportWidth = canvasWidth / zoomFactor;
        
        viewportStartY = Math.max(0,y - (viewportHeight / 2));
        viewportStartX = Math.max(0,x - (viewportWidth / 2));

        viewportEndY = Math.min(canvasHeight,y + (viewportHeight / 2));
        viewportEndX = Math.min(canvasWidth,x + (viewportWidth / 2));
        
        currentZoom *= zoomFactor;
        pixelSize = Math.max((canvasWidth * pixelSize) / (viewportEndX - viewportStartX), (canvasHeight * pixelSize) / (viewportEndY - viewportStartY));

        for (int i = viewportStartY; i < viewportEndY; i++){
            for (int j = viewportStartX; j < viewportEndX; j++){
                pixels[i][j].setGlobalPixelSize(pixelSize);

                int newX = (j - viewportStartX) * pixelSize;
                int newY = (i - viewportStartY) * pixelSize;
                pixels[i][j].setLocation(newX, newY);
            }
        }
        System.out.println(pixelSize);
        canvasPanel.repaint();
    }

    public void ZoomOut(int centerX, int centerY){
        if (currentZoom > 1) {
            viewportHeight = Math.min(canvasHeight, viewportHeight * zoomFactor);
            viewportWidth = Math.min(canvasWidth, viewportWidth * zoomFactor);

            viewportStartY = Math.max(0, centerY - (viewportHeight / 2));
            viewportStartX = Math.max(0, centerX - (viewportWidth / 2));

            viewportEndY = Math.min(canvasHeight, centerY + (viewportHeight / 2));
            viewportEndX = Math.min(canvasWidth, centerX + (viewportWidth / 2));

            currentZoom /= zoomFactor;

            pixelSize = Math.max(1, pixelSize / zoomFactor);

            for (int i = viewportStartY; i < viewportEndY; i++) {
                for (int j = viewportStartX; j < viewportEndX; j++) {
                    pixels[i][j].setGlobalPixelSize(pixelSize);

                    int newX = (j - viewportStartX) * pixelSize;
                    int newY = (i - viewportStartY) * pixelSize;
                    pixels[i][j].setLocation(newX, newY);
                }
            }
        }
        System.out.println(pixelSize);
        canvasPanel.repaint();
    }
}