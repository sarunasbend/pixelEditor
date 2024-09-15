package components;

import javax.swing.JSlider;
import java.awt.Color;

public class GradientSlider extends JSlider {
    private Color startColour;
    private Color endColour;

    private int trackSize = 5;

    public GradientSlider(Color startColour, Color endColour){
        setUI(new GradientSliderUI(this));
        this.startColour = startColour;
        this.endColour = endColour;
        setSlider();
    }

    private void setSlider(){
        setOrientation(JSlider.VERTICAL);
        setMinimum(0);
        setMaximum(255);
        setValue(126);
    }

    public Color getStartColour(){return this.startColour;}
    public Color getEndColour(){return this.endColour;}
    public int getTrackSize(){return this.trackSize;}
    
    public void setStartColour(Color newColour){this.startColour = newColour;}
    public void setEndColour(Color newColour){this.endColour = newColour;}
    public void setTrackSize(int newSize){this.trackSize = newSize;}


}

