package puzzle;
import java.awt.Color;

/**
 * The Tile class represents a tile in a puzzle.  
 *
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class Tile extends BaseTile{
    // Constants
    public static final int SIZE = 50;
    public static final int MARGIN = 10;
    public static final int PADDING = 10; // Intern Padding 
    public static Color FIXED_TILE_COLOR = new Color(139, 0, 0);

     
    /**
     * Constructor to create a new Tile object with specified label, position, row, and column.
     *
     * @param label The label character for the tile.
     * @param xPosition The x-coordinate of the tile.
     * @param yPosition The y-coordinate of the tile.
     * @param row The row index of the tile in the grid.
     * @param column The column index of the tile in the grid.
     */
    public Tile(char label, int xPosition, int yPosition,int row, int column) {
        super(label, xPosition, yPosition, row, column);
        this.makeVisible();                              
    }
}
