import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import javax.swing.JLabel;

public class Location implements Runnable {
    private JLabel text;
    private int xPanel;
    private int yPanel;
    private int width;
    private int height;
    private int pixelSize;
    
    public Location(int xPanel, int yPanel, int width, int height, int pS){
        text = new JLabel("0 : 0");
        this.xPanel = xPanel;
        this.yPanel = yPanel;
        this.width = width;
        this.height = height;
        this.pixelSize = pS;
        text.setSize(new Dimension(width, height));
    }

    public void setPixelSize(int pS){
        pixelSize = pS;
    }

    @Override
    public void run() {
        // Infinite loop to constantly get mouse position
        while (true) {
            // Get the PointerInfo instance which holds the current location of the mouse
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo != null) {
                // Get the Point instance representing the location
                Point location = pointerInfo.getLocation();
                // Print the location (x, y coordinates)
                text.setText(location.x + " : " + location.y);
            }

            // Sleep for a short interval to avoid excessive CPU usage
            try {
                Thread.sleep(100); // 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public JLabel getText(){return this.text;}
}