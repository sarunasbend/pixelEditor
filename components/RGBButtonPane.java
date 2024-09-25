package components;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;

public class RGBButtonPane {
    private int width;
    private int height;
    
    private JButton addButton;
    private JButton removeButton;

    private String imagePath;
    private ImageIcon imageIcon;

    private JLayeredPane pane;
    private JPanel panel;
    private JLabel foreGround;

    private ColourPreview colourPreview;

    public RGBButtonPane(int width, int height, String imagePath, ColourPreview colourPreview){
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        this.colourPreview = colourPreview;
        createButtons();
        createForeground();
        createPanel();
        createPane();
    }

    //WILL NOT ACTUALLY BE JBUTTONS, BUT RATHER JLABELS FOR IMAGEICONS//
    private void createButtons(){
        addButton = new JButton("ADD");
        addButton.setBounds(25,25,75,25);
        removeButton = new JButton("REM");
        removeButton.setBounds(100,25,75,25);
    }

    private void createForeground(){
        imageIcon = new ImageIcon(imagePath);
        foreGround = new JLabel(imageIcon);
        foreGround.setBounds(0, 0, width, height);
    }

    private void createPanel(){
        panel = new JPanel(null);
        panel.setBounds(0, 0, width, height);
        panel.setBackground(Color.BLACK);
        panel.add(addButton);
        panel.add(removeButton);
    }

    private void createPane(){
        pane = new JLayeredPane();
        pane.add(panel, JLayeredPane.DEFAULT_LAYER);
        pane.add(foreGround, JLayeredPane.MODAL_LAYER);
    }

    public JLayeredPane getPane(){return this.pane;}

    public JButton getAddButton(){
        return this.addButton;
    }

    public JButton getRemoveButton(){
        return this.removeButton;
    }
}
