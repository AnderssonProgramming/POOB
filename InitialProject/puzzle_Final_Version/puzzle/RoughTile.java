package puzzle;
import java.awt.Color;
import shapes.*;

/**
 * The RoughTile class represents a type tile in puzzle.  
 *
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class RoughTile extends BaseTile{
    private Rectangle outerBorder; // Rectangle that will be useful for edge

    public RoughTile(char label, int xPosition, int yPosition,int row, int column) {
        
        super(label, xPosition, yPosition, row, column);
        this.changeColor(Color.GRAY);

        int innerSize = Tile.SIZE - 10; // To reduce 5 pxs for each side to leave the edge
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;

        Color RectangleColor = ColorTochangeInsideRectangle(label);
        this.makeVisible();
        outerBorder = new Rectangle(innerSize, innerSize, RectangleColor, innerXPosition, innerYPosition);
        outerBorder.makeVisible();                              
    }

    private Color ColorTochangeInsideRectangle(char label){
        Color color = Color.GRAY;
        switch (label) {
            case 'r':
                color = Color.RED;
                break;
            case 'b':
                color = Color.BLUE;
                break;
            case 'y':
                color = Color.YELLOW;
                break;
            case 'g':
                color = Color.GREEN;
                break;
            case 'h':
                color = Color.WHITE;
                break;
        }

        return color;
    }

    @Override
    public void makeVisible(){
        super.makeVisible();
        if (outerBorder != null) outerBorder.makeVisible();    
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        if (outerBorder != null) outerBorder.makeInvisible();
    }
}