import javax.swing.*;

import components.EditorTitle;
import components.EraserToolLabel;
import components.PenToolLabel;

import java.awt.*;

public class GUI extends JFrame {
    private int width;
    private int height;
    private Insets insets;
    private int pixelSize;
    private int canvasWidth;
    private int canvasHeight;

    private int canvasXPadding;
    private int canvasYPadding;

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
        Canvas blankCanvas = new Canvas(0, 0,pixelSize , canvasWidth, canvasHeight);
        JPanel canvas = blankCanvas.getCanvas();
        canvas.setLocation(canvasXPadding, canvasYPadding);

        JPanel left = new JPanel();
        left.setBounds(0, 0, 200, 800);
        left.setBackground(Color.RED);
        left.setLayout(null);
        addButtons(left); //TEMPORARY METHOD TO TEST FUNCTIONALITY

        JPanel middle = new JPanel();
        middle.setBounds(200, 0, 800, 800);
        middle.setBackground(Color.BLUE);
        middle.setLayout(null);
        middle.add(canvas);

        JPanel right = new JPanel();
        right.setBounds(1000, 0, 200, 800);
        right.setBackground(Color.GREEN);
        JLabel mouse = blankCanvas.getMouseLabel();
        mouse.setBounds(0, 100, 50, 50);
        right.add(mouse);

        add(left);
        add(middle);
        add(right);
    }

    public void addButtons(JPanel left){
        EditorTitle title = new EditorTitle(200, 100, "components\\resources\\EditorTitle.png");
        JLabel temp = title.getEditorTitle();
        temp.setBounds(0,0,200,100);
        PenToolLabel temp2 = new PenToolLabel(100, 100, "components\\resources\\PenTool.png");
        JLabel penTool = temp2.getPenToolLabel();
        penTool.setBounds(0,100,100,50);
        EraserToolLabel temp3 = new EraserToolLabel(100,50, "components\\\\resources\\\\EraserTool.png");
        JLabel eraserTool = temp3.getEraserToolLabel();
        eraserTool.setBounds(100,100,100,50);
        left.add(temp);
        left.add(penTool);
        left.add(eraserTool);
    }
}