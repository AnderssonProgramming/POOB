package domain;

import java.awt.Color;

/**
 * Information about a cell in the manufacturing lattice.
 * <br>
 * <b>(aManufacturing, row, column, age, state, nextState, color)</b>
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class Cell extends Artefact implements Thing {
    protected char nextState; //The next state of the cell, either ACTIVE or INACTIVE
    protected Color color; //The color representing the cell
    protected AManufacturing aManufacturing; //Reference to the manufacturing environment that the cell resides in
    protected int row, column; //The position of the cell within the lattice (row and column)
    protected boolean isStuck = false;  //Indicates whether the cell is stuck, which occurs when it is adjacent to a StickyWall
    
    /**
     * Creates a new cell at the specified row and column in the given manufacturing lattice.
     * @param am The manufacturing lattice to which the cell belongs.
     * @param row The row index of the cell.
     * @param column The column index of the cell.
     * @param active The initial state of the cell, active or inactive.
     */
    public Cell(AManufacturing am, int row, int column, boolean active) {
        this.aManufacturing = am;
        this.row = row;
        this.column = column;
        state = (active ? Artefact.ACTIVE : Artefact.INACTIVE);
        nextState = (active ? Artefact.ACTIVE : Artefact.INACTIVE);
        aManufacturing.setThing(row, column, (Thing) this);
        color = Color.black;
    }

    /**
     * Returns the row index of the cell.
     * @return The row index.
     */
    public final int getRow() {
        return row;
    }

    /**
     * Returns the column index of the cell.
     * @return The column index.
     */
    public final int getColumn() {
        return column;
    }

    /**
     * Returns the color of the cell.
     * @return The color of the cell.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Decides the next state of the cell based on a given rule.
     */
    public void decide() {
        nextState = (getSteps() % 2 == 0 ? Artefact.ACTIVE : Artefact.INACTIVE);
    }

    /**
     * Changes the current state of the cell to the next state.
     */
    public void change() {
        step();
        state = nextState;
        
        // Check if the cell is adjacent to a StickyWall or is on top of one
        if (!isStuck && isAdjacentToStickyWall()) {
            isStuck = true;
        }

        // If the cell is stuck, it stays in place
        if (isStuck) {
            step();
        } else {
            // Additional behavior for unstuck cells can be added here
        }
    }
    
     /**
     * Checks if the cell is adjacent to or on top of a StickyWall.
     *
     * @return True if the cell is adjacent to a StickyWall, false otherwise.
     */
    private boolean isAdjacentToStickyWall() {
        // Check the current position
        Thing currentThing = aManufacturing.getThing(row, column);
        if (currentThing instanceof StickyWall) {
            return true;
        }

        // Check adjacent positions
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int newRow = row + dr;
                int newCol = column + dc;
                if (aManufacturing.inLatice(newRow, newCol)) {
                    Thing adjacentThing = aManufacturing.getThing(newRow, newCol);
                    if (adjacentThing instanceof StickyWall) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Counts the number of active neighboring cells around this cell.
     * @return The number of active neighboring cells.
     */
    public int neighborsActive() {
        return aManufacturing.neighborsActive(row, column);
    }

    /**
     * Checks if a neighboring position relative to this cell is empty.
     * @param dr The row offset from the current cell.
     * @param dc The column offset from the current cell.
     * @return {@code true} if the neighboring position is empty, {@code false} otherwise.
     */
    public boolean neighborIsEmpty(int dr, int dc) {
        return aManufacturing.isEmpty(row + dr, column + dc);
    }
    
    /**
     * Gets whether the cell is currently stuck.
     *
     * @return True if the cell is stuck, false otherwise.
     */
    public boolean getIsStuck() {
        return isStuck;
    }
}