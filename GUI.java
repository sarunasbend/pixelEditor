import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/** Temporary class for testing the pixel editor functionality
 * @author Sarunas Bendoraitis
 */
public class GUI extends JFrame{
    private Grid grid;
    private JPanel pixelGrid;
    private JButton redButton;
    private int red;
    private JButton greenButton;
    private int green;
    private JButton blueButton;
    private int blue;
    private Color currentColor;

    private JButton clearButton;

    private JButton xMirror;
    private JButton yMirror;

    public GUI(){
        setSize(new Dimension(405, 700));
        this.grid = new Grid(0, 0, 20, 20, 20);
        this.pixelGrid = this.grid.getGridPanel();
        this.redButton = new JButton("RED");
        this.redButton.setBounds(0, 400, 100, 100);
        this.redButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                red+=20;
                if (red > 255){red = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.blueButton = new JButton("BLUE");
        this.blueButton.setBounds(100, 400, 100, 100);
        this.blueButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                blue+=20;
                if (blue > 255){blue = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.greenButton = new JButton("GREEN");
        this.greenButton.setBounds(200, 400, 100, 100);
        this.greenButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                green+=20;
                if (green > 255){green = 0;}
                currentColor = new Color(red, green , blue); 
                grid.setPixelColor(currentColor);
            }
        });

        this.clearButton = new JButton("CLEAR");
        this.clearButton.setBounds(0, 550, 100, 100);
        this.clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.clearGrid();
            }
        });

        this.xMirror = new JButton("xMirror");
        this.xMirror.setBounds(0,500,25, 25);
        this.xMirror.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.setXMirror();
            }            
        });

        this.yMirror = new JButton("yMirror");
        this.yMirror.setBounds(25, 500, 25, 25);
        this.xMirror.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                grid.setYMirror();
            }            
        });
        

        setLayout(null);
        add(redButton);
        add(blueButton);
        add(greenButton);
        add(pixelGrid);
        add(clearButton);
        add(xMirror);
        add(yMirror);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }
}
