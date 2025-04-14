package domain;

import java.awt.Color;
import java.util.Random;

/**
 * The ReflectiveCell class represents a specialized type of cell within a manufacturing simulation environment.
 * It extends the base Cell class and has the ability to move in a specific direction, changing direction when it
 * encounters obstacles or the boundaries of the lattice.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class ReflectiveCell extends Cell {
    private int directionRow;
    private int directionCol;
    private AManufacturing aManufacturing;
    private static final Random RANDOM = new Random();

    /**
     * Constructs a ReflectiveCell with the given parameters.
     *
     * @param am      The manufacturing environment in which this cell resides.
     * @param row     The initial row position of the cell.
     * @param column  The initial column position of the cell.
     * @param active  Whether the cell is initially active.
     */
    public ReflectiveCell(AManufacturing am, int row, int column, boolean active) {
        super(am, row, column, active);
        this.aManufacturing = am;
        // Initialize with a random direction (-1, 0, or 1)
        this.directionRow = RANDOM.nextInt(3) - 1;
        this.directionCol = RANDOM.nextInt(3) - 1;
        updateColor();
    }

    /**
     * Decides the next action for this cell based on its current direction and surroundings.
     * Attempts to move the cell in the current direction or changes direction if movement is blocked.
     */
    @Override
    public void decide() {
        int newRow = row + directionRow;
        int newCol = column + directionCol;

        // Check if the cell can move in the current direction
        if (aManufacturing.inLatice(newRow, newCol)) {
            Thing target = aManufacturing.getThing(newRow, newCol);
            if (target == null) {
                // Move to the empty position
                aManufacturing.setThing(row, column, null);
                row = newRow;
                column = newCol;
                aManufacturing.setThing(row, column, this);
            } else if (target instanceof Poison) {
                // If the target is Poison, deactivate the cell
                nextState = INACTIVE;
            } else if (target instanceof StickyWall) {
                // Logic for StickyWall is not yet implemented
            } else {
                // Bounce back by reversing the direction
                reverseDirection();
            }
        } else {
            // If out of bounds, reverse the direction
            reverseDirection();
        }
    }

    /**
     * Changes the state of the cell and updates its color accordingly.
     */
    @Override
    public void change() {
        super.change();
        updateColor();
    }

    /**
     * Updates the color of the cell based on its current state.
     * Active cells are colored green, while inactive cells are colored gray.
     */
    private void updateColor() {
        color = isActive() ? Color.GREEN : Color.GRAY;
    }

    /**
     * Reverses the direction of movement for the cell.
     */
    private void reverseDirection() {
        directionRow = -directionRow;
        directionCol = -directionCol;
    }
}