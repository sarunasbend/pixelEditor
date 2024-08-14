import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

public class Driver {
    public static void main(String[] args) {
        //GUI t = new GUI();
        UndoStack temp = new UndoStack("");
        temp.pushDrawn(23, 1);
        temp.pushEraser(11, 2);
        temp.popUndoStack();
    }
}