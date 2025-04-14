package puzzle;
import java.awt.Color;
import shapes.*;

/**
 * The FixedTile class represents a type tile in puzzle.  
 *
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class FixedTile extends BaseTile {
    private Rectangle innerRectangle; // Inside white rectangle  

    public FixedTile(char label, int xPosition, int yPosition, int row, int column) {
        super(label, xPosition, yPosition, row, column);
        int innerSize = Tile.SIZE - 10; // To reduce 5 pxs for each side to leave the edge
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;
        innerRectangle = new Rectangle(innerSize, innerSize, Color.WHITE, innerXPosition, innerYPosition);
        this.makeVisible();
        innerRectangle.makeVisible(); // Make sure that the drawing is after the red rectangle
    }

    @Override
    public void makeVisible(){
        super.makeVisible();
        if (innerRectangle != null) innerRectangle.makeVisible();
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        if (innerRectangle != null) innerRectangle.makeInvisible();
    }

    @Override
    public void moveVertical(int distance){
        super.moveVertical(distance);
        if (innerRectangle != null) innerRectangle.moveVertical(distance);
    }

    @Override
    public void moveHorizontal(int distance){
        super.moveHorizontal(distance);
        if (innerRectangle != null) innerRectangle.moveHorizontal(distance);
    }
}