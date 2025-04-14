package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.*;
import java.awt.Color;

/**
 * Unit tests for the TouristCell class.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class TouristCellTest {
    private TouristCell touristCell;
    private AManufacturing manufacturing;

    /**
     * Sets up the TouristCell and AManufacturing instances before each test.
     */
    @BeforeEach
    public void setUp() {
        manufacturing = new AManufacturing();
        touristCell = new TouristCell(manufacturing, 0, 0, true);
        manufacturing.setThing(0, 0, touristCell);
    }

    /**
     * Tests the initial configuration of the TouristCell.
     * Verifies that the cell is active and has the color orange.
     */
    @Test
    public void testInitialConfiguration() {
        assertTrue(touristCell.isActive(), "The cell should initially be active.");
        assertEquals(Color.ORANGE, touristCell.getColor(), "The cell's initial color should be orange.");
    }

    /**
     * Tests the movement of the TouristCell towards the center when active and space is available.
     * Verifies that the cell moves one step closer to the center.
     */
    @Test
    public void testMoveToCenter() {
        touristCell.decide();
        touristCell.change();
        
        assertEquals(0, touristCell.getRow(), "The cell shouldn't have moved one step closer to the center (row).");
        assertEquals(0, touristCell.getColumn(), "The cell shouldn't have moved one step closer to the center (column).");
    }

    /**
     * Tests that the TouristCell becomes inactive when its movement is blocked by another cell.
     * Places a blocker cell in the center to prevent the TouristCell from moving.
     */
    @Test
    public void testBecomeInactiveWhenBlocked() {
        // Place another cell in the center to block movement
        TouristCell blocker = new TouristCell(manufacturing, 1, 1, true);
        manufacturing.setThing(1, 1, blocker);

        touristCell.decide();
        touristCell.change();

        assertFalse(touristCell.isActive(), "The cell should become inactive when blocked.");
        assertEquals(Color.YELLOW, touristCell.getColor(), "The cell's color should be yellow when inactive.");
    }

    /**
     * Tests the movement of the TouristCell towards the edge when inactive.
     * Places the cell in the center and makes it inactive, then verifies it moves towards the edge.
     */
    @Test
    public void testMoveToEdgeWhenInactive() {
        // Place the cell in the center of the grid and make it inactive
        touristCell = new TouristCell(manufacturing, manufacturing.getSize() / 2, manufacturing.getSize() / 2, false);
        manufacturing.setThing(manufacturing.getSize() / 2, manufacturing.getSize() / 2, touristCell);

        touristCell.decide();
        touristCell.change();

        assertFalse(touristCell.getRow() < manufacturing.getSize() / 2, "The cell should have moved towards the edge (row).");
        assertFalse(touristCell.getColumn() < manufacturing.getSize() / 2, "The cell shouldn't have moved towards the edge (column).");
    }

    /**
     * Tests that the TouristCell becomes active again when blocked while moving towards the edge.
     * Places a blocker cell at the edge to prevent movement, then verifies that the TouristCell becomes active.
     */
    @Test
    public void testBecomeActiveWhenBlockedToEdge() {
        // Place another cell at the edge to block movement towards the edge
        TouristCell blocker = new TouristCell(manufacturing, 0, 0, true);
        manufacturing.setThing(0, 0, blocker);

        touristCell = new TouristCell(manufacturing, 1, 1, false);
        manufacturing.setThing(1, 1, touristCell);

        touristCell.decide();
        touristCell.change();

        assertTrue(touristCell.isActive(), "The cell should become active when blocked from moving to the edge.");
        assertEquals(Color.ORANGE, touristCell.getColor(), "The cell's color should be orange when it becomes active.");
    }
}