import javax.swing.*;
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
        this.width = width;
        this.height = height;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.pixelSize = ((800/canvasHeight) < (800/canvasWidth)) ? (800/canvasHeight) : (800/canvasWidth); //calculates the pixel size for the canvas
        this.canvasXPadding = (800 - (pixelSize * canvasWidth)) / 2;
        this.canvasYPadding = (800 - (pixelSize * canvasHeight)) / 2;

        
        setForeground(Color.GREEN);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setSize(width, height);
        addComponents();
        setInsets();
        
    }

    private void setInsets() {
        this.insets = getInsets();
        setSize(width + insets.left + insets.right, height + insets.bottom + insets.top);
        System.out.println(width + insets.left + insets.right + " : " + height + insets.bottom + insets.top);
        System.out.println(width);
        System.out.println(height);
        System.out.println(insets.left);
        System.out.println(insets.right);
        System.out.println(insets.top);
        System.out.println(insets.bottom);
    }

    private void addComponents() {
        
        // Assuming Canvas is a custom class you have implemented
        Canvas blankCanvas = new Canvas(0, 0,pixelSize , canvasWidth, canvasHeight);
        JPanel canvas = blankCanvas.getCanvas();
        canvas.setLocation(canvasXPadding, canvasYPadding);

        JPanel left = new JPanel();
        left.setBounds(0, 0, 200, 800);
        left.setBackground(Color.RED);

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
}