package domain;

import java.awt.Color;

/**
 * The StickyWall class represents an immovable barrier within the manufacturing simulation environment.
 * It is a passive entity that prevents other cells from moving past it, effectively creating an obstacle in the lattice.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class StickyWall implements Thing {

    private AManufacturing aManufacturing; //Reference to the manufacturing environment where the wall resides
    private int wallRow; //The row where the StickyWall is located
    private int startColumn, endColumn; //The start and end columns defining the extent of the StickyWall
    private Color color; //Color of the wall

    /**
     * Constructs a StickyWall that spans the entire row of the manufacturing lattice.
     *
     * @param am  The manufacturing environment where the wall is created.
     * @param row The row where the StickyWall is placed.
     */
    public StickyWall(AManufacturing am, int row) {
        this.aManufacturing = am;
        this.wallRow = row;
        this.color = Color.BLACK;

        int size = am.getSize();
        this.startColumn = 0;      // Start at the first column
        this.endColumn = size - 1; // End at the last column

        // Place the StickyWall on the selected row
        for (int c = startColumn; c <= endColumn; c++) {
            aManufacturing.setThing(wallRow, c, this);
        }
    }

    /**
     * Gets the row where the StickyWall is located.
     *
     * @return The row of the StickyWall.
     */
    public int getWallRow() {
        return wallRow;
    }

    /**
     * The StickyWall does not perform any action in its decide step.
     */
    @Override
    public void decide() {
        // The StickyWall does not perform any action in decide
    }

    /**
     * The StickyWall does not change state.
     */
    @Override
    public void change() {
        // The StickyWall does not change state
    }

    /**
     * Gets the color of the StickyWall.
     *
     * @return The color of the StickyWall.
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Indicates that the StickyWall is always active.
     *
     * @return True, as the StickyWall is always active.
     */
    @Override
    public boolean isActive() {
        return true; // Always active
    }

    /**
     * Gets the shape representation of the StickyWall.
     *
     * @return The shape representation, which is a square.
     */
    @Override
    public int shape() {
        return Thing.SQUARE; // Represented as a square
    }
}