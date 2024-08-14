import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

public class Driver {
    public static void main(String[] args) {
        //GUI t = new GUI();
        UndoStack temp = new UndoStack("");
        temp.pushDrawn(1, 1);
        temp.pushDrawn(2, 2);
        temp.pushDrawn(3, 3);
        temp.pushEraser(4, 4);
        System.out.println(temp.popUndoStack());
        System.out.println(temp.popUndoStack());
        System.out.println(temp.popUndoStack());
        System.out.println(temp.popUndoStack());
        System.out.println(temp.popUndoStack());
        temp.deleteUndoStack();
    }
}