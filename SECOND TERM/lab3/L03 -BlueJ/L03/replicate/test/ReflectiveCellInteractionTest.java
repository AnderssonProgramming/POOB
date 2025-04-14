package test;
import domain.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.*;
import java.awt.Color;

/**
 * Unit tests for the interaction between ReflectiveCell instances.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class ReflectiveCellInteractionTest {
    private AManufacturing manufacturing;
    private ReflectiveCell pedrazaCell;
    private ReflectiveCell sanchezCell;

    /**
     * Sets up the AManufacturing instance and creates the ReflectiveCell instances before each test.
     */
    @BeforeEach
    public void setUp() {
        manufacturing = new AManufacturing();

        // Create the "Pedraza" cell at position (5, 5)
        pedrazaCell = new ReflectiveCell(manufacturing, 5, 5, true);
        manufacturing.setThing(5, 5, pedrazaCell);

        // Create the "Sanchez" cell at position (5, 6)
        sanchezCell = new ReflectiveCell(manufacturing, 5, 6, true);
        manufacturing.setThing(5, 6, sanchezCell);
    }

    /**
     * Tests the interaction between Pedraza and Sanchez cells after several tic-tac cycles.
     * Verifies that both cells remain within bounds, do not occupy the same position, and remain active.
     */
    @Test
    public void testPedrazaAndSanchezInteraction() {
        // Execute several tic-tac cycles
        for (int i = 0; i < 5; i++) {
            // Execute decide and change for all cells in the grid
            for (int r = 0; r < manufacturing.getSize(); r++) {
                for (int c = 0; c < manufacturing.getSize(); c++) {
                    Thing thing = manufacturing.getThing(r, c);
                    if (thing != null) {
                        thing.decide();
                    }
                }
            }
            for (int r = 0; r < manufacturing.getSize(); r++) {
                for (int c = 0; c < manufacturing.getSize(); c++) {
                    Thing thing = manufacturing.getThing(r, c);
                    if (thing != null) {
                        thing.change();
                    }
                }
            }
        }

        // Verify that the cells are within bounds
        assertTrue(isWithinBounds(pedrazaCell.getRow(), pedrazaCell.getColumn()), "Pedraza cell is out of bounds.");
        assertTrue(isWithinBounds(sanchezCell.getRow(), sanchezCell.getColumn()), "Sanchez cell is out of bounds.");

        // Verify that the cells are not in the same position
        assertFalse(
            pedrazaCell.getRow() == sanchezCell.getRow() && pedrazaCell.getColumn() == sanchezCell.getColumn(),
            "Pedraza and Sanchez cells should not occupy the same position."
        );

        // Verify that the cells remain active
        assertTrue(pedrazaCell.isActive(), "Pedraza cell should remain active.");
        assertTrue(sanchezCell.isActive(), "Sanchez cell should remain active.");
    }

    /**
     * Checks if the specified position is within the bounds of the manufacturing grid.
     * @param row The row index to check.
     * @param col The column index to check.
     * @return {@code true} if the position is within bounds, {@code false} otherwise.
     */
    private boolean isWithinBounds(int row, int col) {
        int size = manufacturing.getSize();
        return (0 <= row && row < size) && (0 <= col && col < size);
    }
}