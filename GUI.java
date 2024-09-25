import javax.swing.*;

import components.BrushSizePane;
import components.BucketFillToolLabel;
import components.ColourPickerToolLabel;
import components.ColourPreview;
import components.EditorTitle;
import components.EraserToolLabel;
import components.ExportTool;
import components.InfoPane;
import components.PenToolLabel;
import components.RGBButtonPane;
import components.RGBButtonPane;
import components.RGBPanel;
import components.RedoToolLabel;
import components.TrashToolLabel;
import components.UndoToolLabel;
import components.XMirrorToolLabel;
import components.YMirrorToolLabel;
import components.ZoomInToolLabel;
import components.ZoomOutToolLabel;

import java.awt.*;
import java.awt.event.ActionListener;
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
    private int[][][] imagePixelData;

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

    private JButton export;

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

    public GUI(int width, int height, int canvasWidth, int canvasHeight, int[][][] imagePixelData){
        this.width = width; //resolution of application
        this.height = height;
        this.canvasWidth = canvasWidth; //canvas size
        this.canvasHeight = canvasHeight;
        this.imagePixelData = imagePixelData;
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
        if (imagePixelData == null){
            blankCanvas = new Canvas(canvasHeight, canvasWidth, pixelSize, "components/resources/RightBottomButtonBorder.png");
        } else {
            blankCanvas = new Canvas(canvasHeight, canvasWidth, pixelSize, "components/resources/RightBottomButtonBorder.png", imagePixelData);
        }
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

        export = new JButton("Export");
        export.setBounds(0,700,200,100);
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                ExportTool export = new ExportTool(blankCanvas.getPixelData(), canvasWidth, canvasHeight, "png");
            }
        });

        BrushSizePane brushSizes = new BrushSizePane(200, 75, "components\\resources\\BrushSize", "components\\resources\\BrushBorder.png");
        JLayeredPane brushes = brushSizes.getPane();
        brushes.setBounds(0,125,200,75);
        
        brushes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                int x = event.getX() / 50;
                if (x >= 0 && x <= 200){
                    brushSizes.setBorder(x);
                    blankCanvas.setBrushSize(x + 1);
                }
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
        left.add(export);
        left.add(brushes);
    }

    //TEMPORARY MOUSELISTENERS IN THIS CLASS
    public void addRightButtons(JPanel right){

        ColourPreview colourPreview = new ColourPreview(200, 200, "components/resources/ColourPreview.png");
        JLayeredPane preview = colourPreview.getPane();
        preview.setBackground(Color.RED);
        preview.setBounds(0,0,200,200);

        RGBPanel rgbPanel = new RGBPanel(200, 350, colourPreview, "components/resources/RGBPanel.png");
        JLayeredPane pane = rgbPanel.getPane();
        pane.setBounds(0,200,200,300);

        RGBButtonPane rgbButtonPanel = new RGBButtonPane(200, 75, "components/resources/RightMiddleBorder.png", colourPreview);
        JLayeredPane pane2 = rgbButtonPanel.getPane();
        pane2.setBounds(0,500,200,75);

        JLayeredPane pane3 = blankCanvas.getColourPalette();
        pane3.setBounds(0,575,200,100);
        
        InfoPane infoPane = new InfoPane(200, 125, "components/resources/RightInfoBorder.png", blankCanvas.getMouseLabel(), canvasWidth, canvasHeight);
        JLayeredPane pane4 = infoPane.getPane();
        pane4.setBounds(0,675,200,125);

        right.add(preview);
        right.add(pane);
        right.add(pane2);
        right.add(pane3);
        right.add(pane4);

        rgbButtonPanel.getAddButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                blankCanvas.addToColourPalette(colourPreview.getPreviewColour());
            }
        });

        rgbButtonPanel.getRemoveButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event){
                blankCanvas.removeFromColourPalette(blankCanvas.getCurrentColour());
            }
        });
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

