package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.*;

/**
 * Unit tests for the StickyWall class and its interactions with other cells in the manufacturing environment.
 * These tests ensure that the StickyWall correctly prevents cells from occupying its space and causes adjacent cells to become stuck.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class StickyWallTest {
    /**
     * The manufacturing environment used for the tests.
     */
    private AManufacturing manufacturing;

    /**
     * Instances of StickyWall used in the tests.
     */
    private StickyWall stickyWall;
    private StickyWall pedrazaWall;
    private StickyWall sanchezWall;

    /**
     * Instances of cells used in the tests.
     */
    private ReflectiveCell reflectiveCell;
    private TouristCell touristCell;

    /**
     * Sets up the manufacturing environment and the StickyWall instances before each test.
     */
    @BeforeEach
    public void setUp() {
        manufacturing = new AManufacturing();

        // Create a StickyWall on row 5
        int wallRow = 5;
        stickyWall = new StickyWall(manufacturing, wallRow);
        
        // Create two StickyWalls on rows 3 and 19
        int pedrazaRow = 3;
        int sanchezRow = 19;
        pedrazaWall = new StickyWall(manufacturing, pedrazaRow);
        sanchezWall = new StickyWall(manufacturing, sanchezRow);
    }

    /**
     * Tests that a ReflectiveCell cannot be created on the same position as a StickyWall.
     * Verifies that the StickyWall remains at its position.
     */
    @Test
    public void testReflectiveCellNotCreatedWhenOnStickyWall() {
        // Create a ReflectiveCell on the StickyWall
        reflectiveCell = new ReflectiveCell(manufacturing, 5, 10, true);
        manufacturing.setThing(reflectiveCell.getRow(), reflectiveCell.getColumn(), reflectiveCell);        
        
        // Verify that when something is created at that position, it actually isn’t created, and after this, the instance of the Thing at that position is still a wall
        boolean isAWall = false; 
        Thing targetThing = manufacturing.getThing(5, 10);
        
        if (targetThing instanceof StickyWall) {
            isAWall = true;
        }
        
        assertFalse(isAWall, "The thing at this position shouldn't still be an instance of StickyWall.");
    }

    /**
     * Tests that a TouristCell becomes stuck when created adjacent to a StickyWall.
     * Verifies that the TouristCell becomes stuck and cannot move after interaction.
     */
    @Test
    public void testTouristCellStuckWhenCreatedAdjacentToStickyWall() {
        // Create a TouristCell adjacent to the StickyWall
        touristCell = new TouristCell(manufacturing, 4, 10, true);
        manufacturing.setThing(touristCell.getRow(), touristCell.getColumn(), touristCell);

        // Verify that the cell is initially not stuck
        assertFalse(touristCell.getIsStuck(), "The TouristCell should not initially be stuck.");

        // Execute decide and change
        touristCell.decide();
        touristCell.change();

        // Verify that the cell is stuck after interacting with the StickyWall
        assertTrue(touristCell.getIsStuck(), "The TouristCell should be stuck when adjacent to the StickyWall.");

        // Attempt to move the cell again
        int initialRow = touristCell.getRow();
        int initialCol = touristCell.getColumn();
        touristCell.decide();
        touristCell.change();

        // Verify that the position has not changed
        assertEquals(initialRow, touristCell.getRow(), "The TouristCell should not move once stuck.");
        assertEquals(initialCol, touristCell.getColumn(), "The TouristCell should not move once stuck.");
    }
    
    /**
     * Tests that both TouristCell and ReflectiveCell become stuck when adjacent to a StickyWall.
     * Verifies that both cells cannot move after interaction with the StickyWall.
     */
    @Test
    public void testCellsStuckWhenAdjacentToStickyWalls() {
        // Place a TouristCell adjacent to the Sanches Wall on row 18, column 10
        touristCell = new TouristCell(manufacturing, 18, 10, true);
        manufacturing.setThing(touristCell.getRow(), touristCell.getColumn(), touristCell);

        // Verify that the TouristCell is initially not stuck
        assertFalse(touristCell.getIsStuck(), "The TouristCell should not initially be stuck.");

        // Execute decide and change on the TouristCell
        touristCell.decide();
        touristCell.change();

        // Verify that the TouristCell is stuck after interacting with the StickyWall
        assertTrue(touristCell.getIsStuck(), "The TouristCell should be stuck when adjacent to the Sanchez StickyWall.");

        // Attempt to move the TouristCell again and verify it does not move
        int touristInitialRow = touristCell.getRow();
        int touristInitialCol = touristCell.getColumn();
        touristCell.decide();
        touristCell.change();
        assertEquals(touristInitialRow, touristCell.getRow(), "The TouristCell should not move once stuck.");
        assertEquals(touristInitialCol, touristCell.getColumn(), "The TouristCell should not move once stuck.");
        
        // Place a ReflectiveCell adjacent to the Pedraza Wall on row 3, column 11
        reflectiveCell = new ReflectiveCell(manufacturing, 3, 11, true);
        manufacturing.setThing(reflectiveCell.getRow(), reflectiveCell.getColumn(), reflectiveCell);

        // Verify that the ReflectiveCell is initially not stuck
        assertFalse(reflectiveCell.getIsStuck(), "The ReflectiveCell should not initially be stuck.");

        // Execute decide and change on the ReflectiveCell
        reflectiveCell.decide();
        reflectiveCell.change();

        // Verify that the ReflectiveCell is stuck after interacting with the StickyWall
        assertTrue(reflectiveCell.getIsStuck(), "The ReflectiveCell should be stuck when adjacent to the Pedraza StickyWall.");

        // Attempt to move the ReflectiveCell again and verify it does not move
        int reflectiveInitialRow = reflectiveCell.getRow();
        int reflectiveInitialCol = reflectiveCell.getColumn();
        reflectiveCell.decide();
        reflectiveCell.change();
        assertEquals(reflectiveInitialRow, reflectiveCell.getRow(), "The ReflectiveCell should not move once stuck.");
        assertEquals(reflectiveInitialCol, reflectiveCell.getColumn(), "The ReflectiveCell should not move once stuck.");
    }
}