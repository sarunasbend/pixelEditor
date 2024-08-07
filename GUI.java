import javax.swing.*;

public class GUI extends JFrame {
    public GUI(){
        setSize(800,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas blankCanvas = new Canvas(0,0,20,20,20);
        add(blankCanvas.getCanvas());
        

    }
}