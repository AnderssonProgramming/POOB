package domain;

import java.awt.Color;

/**
 * Represents a tourist cell in the manufacturing lattice.
 * The tourist cell moves towards the center of the lattice or the origin, depending on its state.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class TouristCell extends Cell {
    private int centerRow, centerCol;
    private AManufacturing aManufacturing;
    private int directionRow = 0, directionCol = 0;
    private int nextRow, nextCol;
    
    /**
     * Creates a new tourist cell at the specified row and column in the given manufacturing lattice.
     * Sets the initial color of the cell based on its active state.
     * @param am The manufacturing lattice to which the cell belongs.
     * @param row The row index of the cell.
     * @param column The column index of the cell.
     * @param active The initial state of the cell, active or inactive.
     */
    public TouristCell(AManufacturing am, int row, int column, boolean active) {
        super(am, row, column, active);
        this.aManufacturing = am;
        this.centerRow = am.getSize() / 2;
        this.centerCol = am.getSize() / 2;
        updateColor();
    }

    /**
     * Decides the next state and position of the tourist cell based on its current state and neighbors.
     * If the cell is active, it attempts to move towards the center of the lattice.
     * If the cell is inactive, it attempts to move towards the origin.
     * The cell becomes inactive if it cannot move towards its target or if it encounters poison.
     */
    @Override
    public void decide() {
        
        if (isStuck) {
            // Do nothing, the cell is stuck
            nextRow = row;
            nextCol = column;
            return;
        }
        
        if (isActive()) {
            int dr = Integer.compare(centerRow, getRow());
            int dc = Integer.compare(centerCol, getColumn());

            if (neighborIsEmpty(dr, dc) && !(aManufacturing.getThing(row + dr, column + dc) instanceof Poison)) {
                nextRow = row + dr;
                nextCol = column + dc;
            } else if (aManufacturing.getThing(row + dr, column + dc) instanceof Poison) {
                // If there is Poison in the intended direction, become inactive
                nextState = INACTIVE;
                nextRow = row;
                nextCol = column;
            } else {
                // Unable to move, stay in place and become inactive
                nextState = INACTIVE;
                nextRow = row;
                nextCol = column;
            }
        } else {
            int dr = Integer.compare(0, getRow());
            int dc = Integer.compare(0, getColumn());

            if (neighborIsEmpty(dr, dc) && !(aManufacturing.getThing(row + dr, column + dc) instanceof Poison)) {
                nextRow = row + dr;
                nextCol = column + dc;
            } else if (aManufacturing.getThing(row + dr, column + dc) instanceof Poison) {
                // If there is Poison in the intended direction, become active
                nextState = ACTIVE;
                nextRow = row;
                nextCol = column;
            } else {
                // Unable to move, stay in place and become active
                nextState = ACTIVE;
                nextRow = row;
                nextCol = column;
            }
        }
    }


    /**
     * Changes the current state of the tourist cell and updates its color accordingly.
     */
    @Override
    public void change() {
        super.change();
        updateColor();
        
        if (isStuck) {
            // Do nothing, the cell remains in its position
            return;
        }

        // Move to next position
        aManufacturing.setThing(row, column, null);
        row = nextRow;
        column = nextCol;
        aManufacturing.setThing(row, column, this);
    }

    /**
     * Updates the color of the tourist cell based on its current state.
     * Active cells are colored orange, while inactive cells are colored yellow.
     */
    private void updateColor() {
        color = (isActive() ? Color.ORANGE : Color.YELLOW);
    }
}