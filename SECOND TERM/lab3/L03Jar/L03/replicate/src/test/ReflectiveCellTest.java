package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.*;
import java.awt.Color;

/**
 * Unit tests for the ReflectiveCell class to ensure correct behavior, including movement, bouncing, and interaction with other objects.
 * These tests verify the ReflectiveCell's ability to move to neighboring cells, bounce back when blocked, and change state when encountering poison.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class ReflectiveCellTest {
    
    private AManufacturing manufacturing; //The manufacturing environment used for the tests
    private ReflectiveCell reflectiveCell; //The ReflectiveCell instance used in the tests

    /**
     * Sets up the manufacturing environment and creates a ReflectiveCell before each test.
     */
    @BeforeEach
    public void setUp() {
        manufacturing = new AManufacturing();
        reflectiveCell = new ReflectiveCell(manufacturing, 1, 1, true);
        manufacturing.setThing(1, 1, reflectiveCell);
    }

    /**
     * Tests that the ReflectiveCell moves to a neighboring cell or bounces back when it reaches a boundary.
     * Verifies that the movement is valid and the cell does not move outside the allowed range.
     */
    @Test
    public void testRandomMovementAndBounce() {
        // Save the initial position
        int initialRow = reflectiveCell.getRow();
        int initialCol = reflectiveCell.getColumn();

        // Execute decide and change
        reflectiveCell.decide();
        reflectiveCell.change();

        // Ensure that the cell moved to a valid position or bounced
        int newRow = reflectiveCell.getRow();
        int newCol = reflectiveCell.getColumn();

        assertTrue(
            // abs for the absolute value of difference
            (Math.abs(newRow - initialRow) <= 1 && Math.abs(newCol - initialCol) <= 1),
            "ReflectiveCell should move to a neighboring cell."
        );
    }

    /**
     * Tests that the ReflectiveCell becomes inactive after encountering poison in any adjacent cell.
     * Verifies that the ReflectiveCell changes its state and color appropriately when it encounters poison.
     */
    @Test
    public void testInteractionWithPoison() {
        // Positions adjacent to (1,1)
        int[][] adjacentPositions = {
            {0, 1}, // up
            {2, 1}, // down
            {1, 0}, // left
            {1, 2}, // right
            {0, 0}, // up left
            {0, 2}, // up right
            {2, 0}, // down left
            {2, 2}  // down right
        };          

        // Place Poison in all adjacent positions
        for (int[] pos : adjacentPositions) {
            Poison poison = new Poison();
            manufacturing.setThing(pos[0], pos[1], poison);
        }

        // Execute decide and change
        reflectiveCell.decide();
        reflectiveCell.change();

        // Verify that the cell is now inactive
        assertFalse(reflectiveCell.isActive(), "ReflectiveCell should become inactive after encountering poison.");
        assertEquals(Color.GRAY, reflectiveCell.getColor(), "ReflectiveCell should be gray when inactive.");
    }
}