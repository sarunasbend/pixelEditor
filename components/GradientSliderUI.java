package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class GradientSliderUI extends BasicSliderUI{
    private GradientSlider slider;

    public GradientSliderUI(GradientSlider slider){
        super(slider);
        this.slider = slider;
    }

    @Override
    protected Dimension getThumbSize(){
        return new Dimension(25,25);
    }

    /**
     * Customising the thumb of the slider 
     */
    @Override
    public void paintThumb(Graphics g){
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-alias
        AffineTransform transform = g2d.getTransform();

        g2d.translate(thumbRect.x, thumbRect.y); 
        g2d.setPaint(Color.WHITE); //colour of thumb - outer
        g2d.fill(new Ellipse2D.Double(2, 2,thumbRect.width - 4, thumbRect.height - 4));
        // g2d.setPaint(new GradientPaint(0,0, slider.getStartColour(), thumbRect.width, thumbRect.height, slider.getEndColour())); //colour of thumb - inner
        // g2d.fill(new Ellipse2D.Double(1,1,thumbRect.width - 2, thumbRect.height - 2));
        g2d.setTransform(transform); //transform method changes the innner colour of the thumb to match the colour of the gradient as it moves
        g2d.setPaint(new GradientPaint(trackRect.x, trackRect.y, slider.getStartColour(), trackRect.width, trackRect.height, slider.getEndColour()));
        g2d.fill(new Ellipse2D.Double(thumbRect.x + 5,thumbRect.y + 5,thumbRect.width - 10, thumbRect.height - 10));
        g2d.dispose();
    }

    /**
     * Customising the track of the slider
     */
    @Override
    public void paintTrack(Graphics g){
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-alias
        g2d.setPaint(new GradientPaint(trackRect.x, trackRect.y, slider.getStartColour(), trackRect.width, trackRect.height, slider.getEndColour()));
        int size = slider.getTrackSize();
        if (slider.getOrientation() == JSlider.HORIZONTAL){ //changes paint depending if the slider is horizontal or vertical
            int x = 0;
            int y = (trackRect.height - size) / 2;
            g2d.fill(new RoundRectangle2D.Double(trackRect.x + x, trackRect.y + y, trackRect.width, size, size, size));
        } else {
            int x = (trackRect.width - size) / 2;
            int y = 0;
            g2d.fill(new RoundRectangle2D.Double(trackRect.x + x, trackRect.y + y, size, trackRect.height, size, size));
        }
        g2d.dispose();
    }

    /**
     * When the user selects the slider, a border appears to showcase that the slider has been selected
     * This overrides it to match the background (which is black and will always be black).
     */
    @Override
    public void paintFocus(Graphics  g){
        new Color(0,0,0);
    }
}
