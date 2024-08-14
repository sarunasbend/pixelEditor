import javax.swing.*;

public class GUI extends JFrame {
    public GUI(){
        setSize(800,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas blankCanvas = new Canvas(0,0,20,20,20);
        // Converter converter = new Converter("Blue Elephant.bmp");
        // ImageCanvas imageCanvas = new ImageCanvas(0, 0, 20, converter.getWidth(), converter.getHeight(), converter.getPixels());
        Location mouse = new Location(0, 0, 100, 50, 20);
        // Create a new Thread with the tracker instance
        Thread trackerThread = new Thread(mouse);

        // Start the thread
        trackerThread.start();
        JLabel text = mouse.getText();
        text.setLocation(0,500);
        add(text);
        add(blankCanvas.getCanvas());
    }
}