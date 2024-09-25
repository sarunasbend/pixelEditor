package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Creates a new bufferedImage, that is then painted and exported
 */
public class ExportTool {
    public ExportTool(int pixels[][][], int canvasWidth, int canvasHeight, String fileFormat){
        BufferedImage bi = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        for (int  i = 0 ; i < canvasHeight; i++){
            for (int j = 0; j < canvasWidth; j++){
                Color pixelColour = new Color(pixels[i][j][0], pixels[i][j][1], pixels[i][j][2]);
                g.setColor(pixelColour);
                g.drawRect(j, i, 1, 1);
                g.fillRect(j, i, 1, 1);
            }
        }

        try {
            ImageIO.write(bi, fileFormat, new File("export" + "." + fileFormat));
            System.out.println("Image Exported");
        } catch (IOException exception){
            exception.printStackTrace();
            System.out.println("Image Export Failed");
        }

    }   
}
