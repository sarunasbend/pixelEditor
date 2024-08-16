import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class GUI extends JFrame {
    public GUI(){
        setLayout(null);
        setSize(800,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas blankCanvas = new Canvas(0,0,20,20,20);
        // Converter converter = new Converter("Blue Elephant.bmp");
        // ImageCanvas imageCanvas = new ImageCanvas(0, 0, 20, converter.getWidth(), converter.getHeight(), converter.getPixels());
        // Location mouse = new Location(0, 0, 100, 50, 20);
        // Create a new Thread with the tracker instance
        // Thread trackerThread = new Thread(mouse);

        // Start the thread
        // trackerThread.start();
        // JLabel text = mouse.getText();
        // text.setLocation(0,500);
        // add(text);

        UndoStack undo = new UndoStack("");
        undo.pushDrawn(0, 0);

        add(blankCanvas.getCanvas());

        JButton undoButton = new JButton("Undo");
        undoButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event){
                blankCanvas.undoAction(undo.popUndoStack());
            }
        });

        undoButton.setBounds(0,500,100,50);

        add(undoButton);
        JLabel loc = blankCanvas.getMouseLabel();
        loc.setBounds(200,500,100,50);
        add(loc);

    }
}