package domain;
import java.awt.Color;

/**
 * Represents an original type of cell in the manufacturing lattice.
 * This cell has specific behavior for deciding its next state based on the number of active neighbors.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class OriginalCell extends Cell {

    /**
     * Creates a new original cell at the specified row and column in the given manufacturing lattice.
     * Sets the color of the cell to gray.
     * @param am The manufacturing lattice to which the cell belongs.
     * @param row The row index of the cell.
     * @param column The column index of the cell.
     * @param active The initial state of the cell, active or inactive.
     */
    public OriginalCell(AManufacturing am, int row, int column, boolean active) {
        super(am, row, column, active);
        this.color = Color.GRAY; // Set color to gray
    }

    /**
     * Decides the next state of the cell based on the number of active neighbors.
     * The cell becomes active if the number of active neighbors (including itself) is odd.
     */
    @Override
    public void decide() {
        int activeNeighbors = totalActiveNeighbors();
        if (activeNeighbors % 2 == 1) {
            nextState = Artefact.ACTIVE;
        } else {
            nextState = Artefact.INACTIVE;
        }
    }

    /**
     * Counts the total number of active neighboring cells, including the current cell.
     * @return The total number of active neighboring cells.
     */
    private int totalActiveNeighbors() {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = column + dc;
                if (aManufacturing.inLatice(newRow, newCol)) {
                    Thing neighbor = aManufacturing.getThing(newRow, newCol);
                    if (neighbor != null && neighbor.isActive()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Returns the color of the original cell, which is gray.
     * @return The color of the cell.
     */
    @Override
    public Color getColor() {
        return Color.GRAY;
    }

    /**
     * Changes the state of the cell to the specified state (active or inactive).
     * @param active The new state of the cell.
     */
    public void changeState(boolean active) {
        this.state = (active ? Artefact.ACTIVE : Artefact.INACTIVE);
        this.nextState = this.state;
    }
}