import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Class that reads images and translates them into data and pixels.
 * Will use pixels attribute from this class to allow for importing of
 * image into the pixel editor. 
 */
public class Converter {
    private int width;
    private int height;
    private int[][] data; //raw pixel colour data from image
    private int[][][] pixels; //rgb values from each pixel

    public Converter(String filePath){
        try {
            File imageFile = new File(filePath);
            BufferedImage image = ImageIO.read(imageFile);
            setData(image);            
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setData(BufferedImage image){ 
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.data = new int[height][width];
        this.pixels = new int[height][width][3];

        for (int row = 0; row < height; row++){
            for (int column = 0; column < width; column++){
                int rgb = image.getRGB(column, row);
                this.pixels[row][column][0] = (rgb >> 16) & 0xFF;
                this.pixels[row][column][1] = (rgb >> 8 ) & 0xFF;
                this.pixels[row][column][2] = rgb & 0xFF;
                this.data[row][column] = rgb;
            }
        }
    }

    public int[][] getData(){return this.data;}

    public int[][][] getPixels(){return this.pixels;}

    public int getWidth(){return this.width;}

    public int getHeight(){return this.height;}
}
