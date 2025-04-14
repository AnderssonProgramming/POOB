package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import domain.*;

/**
 * Unit tests for the AManufacturing class.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

class AManufacturingTest {

    private AManufacturing aManufacturing;

    /**
     * Sets up the AManufacturing instance before each test.
     */
    @BeforeEach
    public void setUp() {
        aManufacturing = new AManufacturing();
    }

    /**
     * Tests the getSize method to verify the size of the board.
     * The expected size is 50.
     */
    @Test
    @DisplayName("Verify the board size")
    public void testGetSize() {
        assertEquals(50, aManufacturing.getSize(), "The initial size must be 50");
    }

    /**
     * Tests the isEmpty method to verify that it correctly identifies non-empty cells.
     * The cell at position (0, 0) should not be empty at the beginning.
     */
    @Test
    @DisplayName("Verify that isEmpty method works perfectly")
    public void testIsEmpty() {
        assertFalse(aManufacturing.isEmpty(0, 0), "The cell (0,0) mustn't be empty at the beginning");
    }

    /**
     * Tests the neighborsActive method to verify the count of active neighboring cells.
     * Verifies that the active cell at position (1, 1) has three active neighbors.
     */
    @Test
    @DisplayName("Verify neighborsActive functionality")
    public void testNeighborsActive() {
        // Create an active cell at position (1, 1)
        Thing cell = new Cell(aManufacturing, 1, 1, true);
        aManufacturing.setThing(1, 1, cell);

        // Create active cells around cell (1, 1)
        aManufacturing.setThing(0, 1, new Cell(aManufacturing, 0, 1, true));
        aManufacturing.setThing(1, 0, new Cell(aManufacturing, 1, 0, true));
        aManufacturing.setThing(2, 1, new Cell(aManufacturing, 2, 1, true));
        
        int activeNeighbors = aManufacturing.neighborsActive(1, 1);
        assertEquals(3, activeNeighbors, "The cell (1,1) must have 3 active neighbors");
    }

    /**
     * Tests the state change of cells after executing a tic-tac cycle.
     * Verifies that the active cell at position (2, 2) remains active if it has no active neighbors.
     */
    @Test
    @DisplayName("Verify the state change of cells with ticTac")
    public void testTicTac() {
        // Configure an active initial cell at position (2, 2)
        Thing cell = new Cell(aManufacturing, 2, 2, true);
        aManufacturing.setThing(2, 2, cell);
        
        aManufacturing.ticTac();
        assertTrue(aManufacturing.getThing(2, 2).isActive(), "The cell(2,2) must be active while ticTac maintains without active neighbors");
    }
}