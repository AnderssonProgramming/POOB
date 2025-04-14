
package puzzle;
import shapes.*;

/**
 * The FlyingTile class represents a type tile in puzzle.  
 *
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class FlyingTile extends BaseTile{

    private Circle innerCircle; //Inside light brown circle

    public FlyingTile(char label, int xPosition, int yPosition,int row, int column) {
        super(label, xPosition, yPosition, row, column);
        int innerSize = Tile.SIZE - 10; // To reduce 5 pxs for each side to leave the edge
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;
        this.makeVisible();       
        innerCircle = new Circle(innerSize, innerXPosition, innerYPosition,Puzzle.lightBrown);
    }

    @Override
    public void makeVisible(){
        super.makeVisible();
        if (innerCircle != null) innerCircle.makeVisible();
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        if (innerCircle != null) innerCircle.makeInvisible();
    }

    @Override
    public void moveVertical(int distance){
        super.moveVertical(distance);
        if (innerCircle != null) innerCircle.moveVertical(distance);
    }

    @Override
    public void moveHorizontal(int distance){
        super.moveHorizontal(distance);
        if (innerCircle != null) innerCircle.moveHorizontal(distance);
    }
}
