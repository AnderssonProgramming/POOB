package domain;
import java.util.*;
import java.lang.Class;
import java.lang.reflect.Constructor;

/**
 * Class representing a manufacturing lattice where different types of Things can be placed.
 * The lattice consists of a grid where various cells and other entities can be initialized and manipulated.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class AManufacturing {
    private static int SIZE = 50;
    private Thing[][] lattice;
    
    /**
     * Constructor for AManufacturing class.
     * Initializes the lattice with null values and sets up the initial pattern of Things.
     */
    public AManufacturing() {
        lattice = new Thing[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                lattice[r][c] = null;
            }
        }
        //someThings();
        initializePattern();
    }

    /**
     * Returns the size of the lattice.
     * @return the size of the lattice.
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Retrieves a Thing from a specific position in the lattice.
     * @param r the row index.
     * @param c the column index.
     * @return the Thing at the specified position, or null if none exists.
     */
    public Thing getThing(int r, int c) {
        return lattice[r][c];
    }

    /**
     * Places a Thing in a specific position in the lattice.
     * @param r the row index.
     * @param c the column index.
     * @param e the Thing to be placed at the specified position.
     */
    public void setThing(int r, int c, Thing e) {
        lattice[r][c] = e;
    }

    /**
     * Initializes some specific Things in the lattice.
     * This includes cells, tourist cells, poison, and reflective cells.
     */
    public void someThings() {  
        // CELLS
        Cell yersinia = new Cell(this, 10, 10, true);
        Cell listeria = new Cell(this, 15, 15, true);
        setThing(10, 10, yersinia);
        setThing(15, 15, listeria);
        
        // TOURIST CELLS
        TouristCell move = new TouristCell(this, 5, 5, true);
        TouristCell walk = new TouristCell(this, 20, 20, true);
        setThing(5, 5, move);
        setThing(20, 20, walk);
        
        // POISON
        Poison mercury = new Poison();
        Poison arsenic = new Poison();
        setThing(0, 0, mercury);
        setThing(0, getSize() - 1, arsenic);
        
        // Reflective Cells
        ReflectiveCell alice = new ReflectiveCell(this, 25, 25, true);
        ReflectiveCell bob = new ReflectiveCell(this, 30, 30, true);
        setThing(25, 25, alice);
        setThing(30, 30, bob);
        
        // Reflective Cells - unit test compilation in program
        ReflectiveCell Pedraza = new ReflectiveCell(this, 5, 5, true);
        ReflectiveCell Sanchez = new ReflectiveCell(this, 5, 6, true);
        setThing(5, 5, Pedraza);
        setThing(5, 6, Sanchez);
        
        // STICKY WALL
        // Define rows for each Wall
        int pedrazaRow = 4; // Example row for the Pedraza Wall
        int sanchezRow = 19; // Example row for the Sanchez Wall
    
        // Create the Walls with your names
        StickyWall pedrazaWall = new StickyWall(this, pedrazaRow);
        StickyWall sanchezWall = new StickyWall(this, sanchezRow);
    }
    
    /**
     * Initializes the lattice pattern with inactive original cells.
     * Sets up a 10x10 sub-grid in the bottom-left corner with active cells.
     */
    public void initializePattern() {
        // Initialize the entire lattice with inactive original cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                OriginalCell cell = new OriginalCell(this, r, c, false);
                setThing(r, c, cell);
            }
        }

        // Place active cells in the 10x10 sub-grid in the bottom-left corner
        ((OriginalCell) getThing(44, 5)).changeState(true); // Activate cell at (44,5)
        ((OriginalCell) getThing(45, 4)).changeState(true); // Activate cell at (45,4)
        ((OriginalCell) getThing(45, 5)).changeState(true); // Activate cell at (45,5)
    }

    /**
     * Counts the number of active neighboring Things around a specified position.
     * @param r the row index of the target position.
     * @param c the column index of the target position.
     * @return the number of active neighboring Things.
     */
    public int neighborsActive(int r, int c) {
        int num = 0;
        for (int dr = -1; dr < 2; dr++) {
            for (int dc = -1; dc < 2; dc++) {
                if ((dr != 0 || dc != 0) && inLatice(r + dr, c + dc) && 
                    (lattice[r + dr][c + dc] != null) && (lattice[r + dr][c + dc].isActive())) num++;
            }
        }
        return (inLatice(r, c) ? num : 0);
    }
   
    /**
     * Checks if a specific position in the lattice is empty.
     * @param r the row index.
     * @param c the column index.
     * @return {@code true} if the position is empty, {@code false} otherwise.
     */
    public boolean isEmpty(int r, int c) {
        return (inLatice(r, c) && lattice[r][c] == null);
    }    
        
    /**
     * Checks if the specified position is within the bounds of the lattice.
     * @param r the row index.
     * @param c the column index.
     * @return {@code true} if the position is within bounds, {@code false} otherwise.
     */
    protected boolean inLatice(int r, int c) {
        return ((0 <= r) && (r < SIZE) && (0 <= c) && (c < SIZE));
    }
   
    /**
     * Performs a tic-tac cycle on all Things in the lattice.
     * In each cycle, the Things decide their next state and then change their state accordingly.
     */
    public void ticTac() {
        // Determine the next state of cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Thing thing = lattice[r][c];
                if (thing != null) {
                    thing.decide();
                }
            }
        }
        // Update the state of cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Thing thing = lattice[r][c];
                if (thing != null) {
                    thing.change();
                }
            }
        }
    }
}