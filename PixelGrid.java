import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;

public class PixelGrid {
    private JPanel pixelGrid; //the size of the grid component

    private int pixelSize; //size of individual pixel
    private int gridWidth; //rows
    private int gridHeight; //columns
    
    private int mouseX = -1;
    private int mouseY = -1;
    private boolean isMouseHeldDown = false; //enables drag paint

    private final RenderingHints renderingHints  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti-aliasing

    private ArrayList<Rectangle> tiles = new ArrayList<>(); //maintains state of the pixels
    private Color tileColor = new Color(0,0,0);

    public PixelGrid(int pixelSize, int gridWidth, int gridHeight){
        this.pixelSize = pixelSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        createPixelGrid();
    }

    private void createPixelGrid(){
        this.pixelGrid = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){ //enables painting of tiles
                super.paintComponent(g);
                
                Graphics2D g2d = (Graphics2D) g; 
                g2d.setRenderingHints(renderingHints); //enabling anti-aliasing
                drawGrid(g2d);

                if (mouseX >= 0 && mouseY >= 0){ //if mouse has been pressed
                    g2d.setColor(tileColor);
                    for (Rectangle tile : tiles){ //fills the page with all the shaded tiles
                        g2d.fillRect(tile.x, tile.y, tile.width, tile.height);
                    }
                }
            }
        };
        this.pixelGrid.setPreferredSize(new Dimension(this.gridWidth, this.gridHeight));
        this.pixelGrid.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent event){ //listens for mouse press
                isMouseHeldDown = true;
                mouseX = (event.getX() / pixelSize) * pixelSize;
                mouseY = (event.getY() / pixelSize) * pixelSize;
                tiles.add(new Rectangle(mouseX, mouseY, pixelSize, pixelSize)); 
                pixelGrid.repaint(); //updates display
            }

            public void mouseReleased(MouseEvent event){ //listens for mouse release
                isMouseHeldDown = false;
            }
        });

        this.pixelGrid.addMouseMotionListener(new MouseAdapter() { //enables mouse drag painting
            public void mouseDragged(MouseEvent event) { //only valid if pressed
                if (isMouseHeldDown){
                    mouseX = (event.getX() / pixelSize) * pixelSize;
                    mouseY = (event.getY() / pixelSize) * pixelSize;
                    tiles.add(new Rectangle(mouseX, mouseY, pixelSize, pixelSize));
                    pixelGrid.repaint();
                }
            }
        });
    }

    private void drawGrid(Graphics2D g2d){ //initial tile grid
        for (int i = 0; i < this.gridWidth; i+=20){
            g2d.drawLine(i, 0, i, this.gridHeight);
        }

        for (int j = 0; j < this.gridHeight; j+= 20){
            g2d.drawLine(0,j, this.gridHeight, j);
        }
    }

    public JPanel getPanel(){
        return this.pixelGrid;
    }

    public void setTileColor(Color tileColor){
        this.tileColor = tileColor;
    }
}
