import javax.swing.*;

public class GUI extends JFrame {
    public GUI(){
        setSize(1000, 1000);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Converter converter = new Converter("Blue Elephant.bmp");
        Canvas newCanvas = new Canvas(converter.getPixels(), converter.getWidth(), converter.getHeight());

        add(newCanvas.getCanvas());
    }
}