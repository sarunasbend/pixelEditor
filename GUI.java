import javax.swing.*;

public class GUI extends JFrame {
    public GUI(){
        setSize(800,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Canvas blankCanvas = new Canvas(0,0,20,20,20);
        Converter converter = new Converter("download.jpg");
        ImageCanvas imageCanvas = new ImageCanvas(0, 0, 1, converter.getWidth(), converter.getHeight(), converter.getPixels());
        add(imageCanvas.getCanvas());
    
    }
}