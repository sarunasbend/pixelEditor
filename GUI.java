import javax.swing.*;

import components.BucketFillToolLabel;
import components.ColourPickerToolLabel;
import components.EditorTitle;
import components.EraserToolLabel;
import components.PenToolLabel;
import components.RGBPanel;
import components.RedoToolLabel;
import components.TrashToolLabel;
import components.UndoToolLabel;
import components.XMirrorToolLabel;
import components.YMirrorToolLabel;
import components.ZoomInToolLabel;
import components.ZoomOutToolLabel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    private Canvas blankCanvas;

    private int width;
    private int height;
    private Insets insets;
    private int pixelSize;
    private int canvasWidth;
    private int canvasHeight;

    private int canvasXPadding;
    private int canvasYPadding;

    private EditorTitle editorTitle;
    private PenToolLabel penTool;
    private EraserToolLabel eraserTool;
    private ColourPickerToolLabel colourPickerTool;
    private BucketFillToolLabel bucketFillTool;
    private RedoToolLabel redoTool;
    private UndoToolLabel undoTool;
    private XMirrorToolLabel xMirrorTool;
    private YMirrorToolLabel yMirrorTool;
    private TrashToolLabel trashTool;
    private ZoomInToolLabel zoomInTool;
    private ZoomOutToolLabel zoomOutTool;

    private int currentCanvasMode;
    private int previousCanvasMode = 0;

    public GUI(int width, int height, int canvasWidth, int canvasHeight) {
        this.width = width; //resolution of application
        this.height = height;
        this.canvasWidth = canvasWidth; //canvas size
        this.canvasHeight = canvasHeight;
        //calculates the pixel size for the canvas, pixel size is smaller result from calculation
        this.pixelSize = ((800/canvasHeight) < (800/canvasWidth)) ? (800/canvasHeight) : (800/canvasWidth); 
        this.canvasXPadding = (800 - (pixelSize * canvasWidth)) / 2; //centers the canvas 
        this.canvasYPadding = (800 - (pixelSize * canvasHeight)) / 2;

        setForeground(Color.WHITE);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true); //program needs to be visible before insets are calculated
        setSize(width, height);
        addComponents(); //adds components
        setInsets(); //sets the size of the JFrame to actually be width x height
        highlightTool();
    }

    /**
     * When creating a JFrame of n x m dimensions, it won't actually be n x m size but it will rather be the whole window tab size.
     * Insets are the width and height taken up by the OS to display the program 
     */
    private void setInsets() {
        this.insets = getInsets();
        setSize(width + insets.left + insets.right, height + insets.bottom + insets.top);
    }

    private void addComponents() {
        blankCanvas = new Canvas(canvasHeight, canvasWidth, pixelSize);
        System.out.println(pixelSize);
        JPanel canvas = blankCanvas.getCanvas();
        canvas.setLocation(canvasXPadding, canvasYPadding);

        JPanel left = new JPanel();
        left.setBounds(0, 0, 200, 800);
        left.setBackground(Color.BLACK);
        left.setLayout(null);
        addLeftButtons(left); //TEMPORARY METHOD TO TEST FUNCTIONALITY
        addActionListeners();

        JPanel middle = new JPanel();
        middle.setBounds(200, 0, 800, 800);
        middle.setBackground(Color.BLUE);
        middle.setLayout(null);
        middle.add(canvas);

        JPanel right = new JPanel();
        right.setBounds(1000, 0, 200, 800);
        right.setBackground(Color.BLACK);
        right.setLayout(null);
        addRightButtons(right);

        add(left);
        add(middle);
        add(right);
    }

    /*
     * Adds all the buttons for the essential tool buttons (and title label)
     * All elements are added to the left panel of the JFrame 
     * Modular to an extent (until I run out of space within JPanel)
     */
    public void addLeftButtons(JPanel left){
        editorTitle = new EditorTitle(200, 100, "components\\resources\\EditorTitle.png", "components\\\\resources\\\\EditorTitleHover.gif");
        editorTitle.setBounds(0,0,200,100);
        
        penTool = new PenToolLabel(100, 50, "components\\resources\\PenTool.png", "components\\resources\\PenToolHighlight.png");
        penTool.setBounds(0,200,100,50);

        eraserTool = new EraserToolLabel(100, 50, "components\\resources\\EraserTool.png", "components\\resources\\EraserToolHighlight.png");
        eraserTool.setBounds(100,200,100,50);

        colourPickerTool = new ColourPickerToolLabel(100, 50, "components\\resources\\ColourPickerTool.png", "components\\resources\\ColourPickerToolHighlight.png");
        colourPickerTool.setBounds(0,250,100,50);

        bucketFillTool = new BucketFillToolLabel(100, 50, "components\\resources\\BucketFillTool.png", "components\\resources\\BucketFillToolHighlight.png");
        bucketFillTool.setBounds(100,250,100,50);

        redoTool = new RedoToolLabel(100, 50, "components\\resources\\RedoTool.png" ,"components\\resources\\RedoToolHighlight.png");
        redoTool.setBounds(0,300,100,50);

        undoTool = new UndoToolLabel(100, 50, "components\\resources\\UndoTool.png", "components\\resources\\UndoToolHighlight.png");
        undoTool.setBounds(100,300,100,50);

        xMirrorTool = new XMirrorToolLabel(100, 50, "components\\resources\\XMirrorTool.png", "components\\resources\\XMirrorToolHighlight.png");
        xMirrorTool.setBounds(0,350,100,50);

        yMirrorTool = new YMirrorToolLabel(100, 50, "components\\resources\\YMirrorTool.png", "components\\resources\\YMirrorToolHighlight.png");
        yMirrorTool.setBounds(100,350,100,50);

        trashTool = new TrashToolLabel(100, 50, "components\\resources\\TrashTool.png", "components\\resources\\TrashToolHighlight.png");
        trashTool.setBounds(0,400,100,50);
        
        zoomInTool = new ZoomInToolLabel(100, 50, "components\\resources\\ZoomInTool.png" , "components\\resources\\ZoomInToolHighlight.png");
        zoomInTool.setBounds(0,450,100,50);

        zoomOutTool = new ZoomOutToolLabel(100, 50, "components\\resources\\ZoomOutTool.png" , "components\\resources\\ZoomOutToolHighlight.png");
        zoomOutTool.setBounds(100,450,100,50);

        JPanel brushSize1 = new JPanel();
        brushSize1.setBackground(new Color(250, 0 , 0));
        brushSize1.setBounds(0,150,50,50);
        brushSize1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                System.out.println("1");
                blankCanvas.setBrushSize(1);
            }            
        });

        JPanel brushSize2 = new JPanel();
        brushSize2.setBackground(new Color(200, 0 , 0));
        brushSize2.setBounds(50,150,50,50);
        brushSize2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                System.out.println("2");
                blankCanvas.setBrushSize(2);
            }
        });

        JPanel brushSize3 = new JPanel();
        brushSize3.setBackground(new Color(150, 0 , 0));
        brushSize3.setBounds(100,150,50,50);
        brushSize3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                System.out.println("3");
                blankCanvas.setBrushSize(3);
            }            
        });

        JPanel brushSize4 = new JPanel();
        brushSize4.setBackground(new Color(100, 0 , 0));
        brushSize4.setBounds(150,150,50,50);
        brushSize4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                blankCanvas.setBrushSize(4);
            }            
        });

        //adds all elements to Panel
        left.add(editorTitle);
        left.add(penTool);
        left.add(eraserTool);
        left.add(bucketFillTool);
        left.add(colourPickerTool);
        left.add(redoTool);
        left.add(undoTool);
        left.add(xMirrorTool);
        left.add(yMirrorTool);
        left.add(trashTool);
        left.add(zoomInTool);
        left.add(zoomOutTool);
        left.add(brushSize1);
        left.add(brushSize2);
        left.add(brushSize3);
        left.add(brushSize4);
    }

    //TEMPORARY MOUSELISTENERS IN THIS CLASS
    public void addRightButtons(JPanel right){
        JLabel mouse = blankCanvas.getMouseLabel();
        mouse.setBounds(50, 700, 150, 100);

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("components/resources/CustomFont.ttf"));
            customFont = customFont.deriveFont(24f);
            mouse.setFont(customFont);
            mouse.setForeground(Color.YELLOW);
        } catch (FontFormatException | IOException e){
            e.printStackTrace();
        }
        
        JPanel miniMap = new JPanel();
        miniMap.setBackground(Color.RED);
        miniMap.setBounds(0,0,200,200);
        
        // JLabel label = new JLabel("Colour Palette");
        // label.setBounds(0,200,200,50);
        // label.setForeground(Color.WHITE);
        
        // JPanel colourPalette = new JPanel();
        // colourPalette.setBounds(0,250,200,100);
        // colourPalette.setBackground(Color.WHITE);

        // JSlider redSlider = new JSlider(JSlider.VERTICAL, 0,255,128);
        // redSlider.setBounds(0,350,50,100);

        // JLabel red = new JLabel("255");
        // red.setBounds(150,350,50,50);

        // JSlider greenSlider = new JSlider(JSlider.VERTICAL, 0,255,128);
        // greenSlider.setBounds(50,350,50,100);

        // JSlider blueSlider = new JSlider(JSlider.VERTICAL,0,255,128);
        // blueSlider.setBounds(100,350, 50,100);

        // JButton add = new JButton("ADD");
        // add.setBounds(0,150,100,100);
        // add.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e){
        //         blankCanvas.addToPermanentPalette(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
        //     }
        // });

        // JButton remove = new JButton("REM");
        // remove.setBounds(100,150,100,100);
        // remove.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e){
        //         blankCanvas.removeFromPermanentPalette();
        //     }
        // });

        // JPanel colourPalette = blankCanvas.getColourPalette();
        // colourPalette.setBounds(0, 250,200,50);

        // JPanel colourPalette = new JPanel();
        // colourPalette.setBackground(Color.GREEN);
        // colourPalette.setBounds(0,200,200,100);
        // colourPalette.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent event){
        //         int red = redSlider.getValue();
        //         int green = greenSlider.getValue();
        //         int blue = blueSlider.getValue();
        //         blankCanvas.setCurrentColour(new Color(red, green, blue));
        //         System.out.println(red + " , " + green + " , " + blue);
        //     }            
        // });

        // JPanel palette = blankCanvas.getVolatilePanel();
        // palette.setBounds(0,150,200,100);
        
        RGBPanel rgbPanel = new RGBPanel(200, 350, "components/resources/RGBPanel.png");
        JLayeredPane pane = rgbPanel.getPane();
        pane.setBounds(0,200,200,350);

        // right.add(palette);
        right.add(miniMap);
        // right.add(colourPalette);
        right.add(pane);
        // right.add(redSlider);
        // // right.add(red);
        // right.add(greenSlider);
        // right.add(blueSlider);
        // right.add(add);
        // right.add(remove);
        // right.add(colourPalette);
        right.add(mouse);
        // right.add(label);
    }

    private void addActionListeners(){
        editorTitle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                editorTitle.setSelectedTitle();
            }            
        });

        penTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                penTool.setSelectedTool();
                currentCanvasMode = 0;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        eraserTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                eraserTool.setSelectedTool();
                currentCanvasMode = 1;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        colourPickerTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                colourPickerTool.setSelectedTool();
                currentCanvasMode = 2;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        bucketFillTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                bucketFillTool.setSelectedTool();
                currentCanvasMode = 3;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        redoTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                redoTool.setSelectedTool();
                currentCanvasMode = 4;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        undoTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                undoTool.setSelectedTool();
                currentCanvasMode = 5;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
                blankCanvas.undoAction();
            }
        });

        xMirrorTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                xMirrorTool.setSelectedTool();
                currentCanvasMode = 6;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        yMirrorTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                yMirrorTool.setSelectedTool();
                currentCanvasMode = 7;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        trashTool.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                trashTool.setSelectedTool();
                currentCanvasMode = 8;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        zoomInTool.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                zoomInTool.setSelectedTool();
                currentCanvasMode = 9;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }
        });

        zoomOutTool.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                previousCanvasMode = currentCanvasMode;
                zoomOutTool.setSelectedTool();
                currentCanvasMode = 10;
                highlightTool();
                blankCanvas.setCurrentCanvasMode(currentCanvasMode);
            }            
        });
    }

    /**
     * when the user selects a tool, it should highlight the tool, and only that tool
     */
    private void highlightTool(){
        switch (previousCanvasMode){
            case 0: //pen
                penTool.setSelectedTool();
                break;
            case 1: //eraser
                eraserTool.setSelectedTool();
                break;
            case 2: //colourpicker
                colourPickerTool.setSelectedTool();
                break;
            case 3: //bucketfill
                bucketFillTool.setSelectedTool();
                break;
            case 4: //redotool
                redoTool.setSelectedTool();
                break;
            case 5: //undotool
                undoTool.setSelectedTool();
                break;
            case 6: //xMirror
                xMirrorTool.setSelectedTool();
                break;
            case 7: //yMirror
                yMirrorTool.setSelectedTool();
                break;
            case 8: //trash
                trashTool.setSelectedTool();
                break;
            case 9: //zoomin
                zoomInTool.setSelectedTool();
                break;
            case 10: //zoomout
                zoomOutTool.setSelectedTool();
                break;
            default:
                currentCanvasMode = 0;
                break;
        }
    }
}

