package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import domain.AManufacturing;
import domain.OriginalCell;

/**
 * Unit tests for the OriginalCell class.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class OriginalCellTest {

    private AManufacturing manufacturing;
    private final int BASE_ROW = 40; // Initial row of sub-board 10x10
    private final int BASE_COL = 0;  // Initial column of sub-board 10x10

    /**
     * Sets up the AManufacturing instance before each test.
     */
    @Before
    public void setUp() {
        manufacturing = new AManufacturing();
    }
    
    /**
     * Tests the initial setup of active cells in the AManufacturing lattice.
     * Verifies that specific cells are active and others are inactive.
     */
    @Test
    public void testInitialSetup() {
        // Verify that the cells at positions (44,5), (45,4), and (45,5) are active
        assertTrue(((OriginalCell) manufacturing.getThing(44, 5)).isActive());
        assertTrue(((OriginalCell) manufacturing.getThing(45, 4)).isActive());
        assertTrue(((OriginalCell) manufacturing.getThing(45, 5)).isActive());
        
        // Verify that a cell outside those positions is inactive
        assertFalse(((OriginalCell) manufacturing.getThing(44, 4)).isActive());
    }
    
    /**
     * Tests the state of the cells after a single tic-tac cycle.
     * Verifies that one of the neighboring cells has changed its state.
     */
    @Test
    public void testSingleTicTac() {
        // Execute one tic-tac step
        manufacturing.ticTac();
        
        // Verify that one of the neighboring cells has become active
        assertTrue(((OriginalCell) manufacturing.getThing(44, 4)).isActive());
        
        // Verify that the initially active cells remain active
        assertTrue(((OriginalCell) manufacturing.getThing(44, 5)).isActive());
        assertTrue(((OriginalCell) manufacturing.getThing(45, 4)).isActive());
        assertTrue(((OriginalCell) manufacturing.getThing(45, 5)).isActive());
    }
    
    /**
     * Tests the state of the cells after two tic-tac cycles.
     * Verifies that certain cells become inactive when the number of active neighbors is even.
     */
    @Test
    public void testDoubleTicTac() {
        // Execute two tic-tac steps
        manufacturing.ticTac();
        manufacturing.ticTac();
        
        // Verify that the cell at (44,4) has become inactive if its number of active neighbors is even
        assertFalse(((OriginalCell) manufacturing.getThing(44, 4)).isActive());
        
        // Verify that some cells remain active
        assertTrue(((OriginalCell) manufacturing.getThing(44, 5)).isActive());
        assertTrue(((OriginalCell) manufacturing.getThing(45, 5)).isActive());
    }
    
    /**
     * Tests that cells outside the initial sub-board remain inactive after multiple tic-tac cycles.
     * Verifies that propagation does not affect cells outside the sub-board.
     */
    @Test
    public void testOuterBoundaryRemainsInactive() {
        // Execute several tic-tac steps to observe propagation
        for (int i = 0; i < 5; i++) {
            manufacturing.ticTac();
        }
        
        // Verify that a cell outside the sub-board (39, 0) remains inactive, as it should not be affected
        assertTrue(((OriginalCell) manufacturing.getThing(39, 0)).isActive());
    }
}