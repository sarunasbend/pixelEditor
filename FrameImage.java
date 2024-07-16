import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

//class that will allow for setting images as the background of JFrame
class FrameImage extends JPanel {
    private BufferedImage backgroundImage;
    private int width;
    private int height;
    //specify the image dir as parameter
    public FrameImage(String imagePath, int width, int height) {
        try {
            this.width = width;
            this.height = height;
            backgroundImage = ImageIO.read(new File(imagePath)); //sets attribute as image specified
        } catch (IOException event){
            //Include a way to handle the exception
        }
    }

    //method overriding for java swing Super class' method paintComponent (from JComponent class) to allow for custom images being set as the background of JPanels
    public void paintComponent(Graphics frame){
        super.paintComponent(frame); //super declaration, passing image as parameter
        if (backgroundImage != null){ //image will be null if not appropriate image file type
            frame.drawImage(backgroundImage, 0, 0, width, height, this); //sets background to image matching height and width of JFrame
        }
    }
}