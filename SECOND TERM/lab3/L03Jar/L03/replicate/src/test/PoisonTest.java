package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.*;
import java.awt.Color;
import java.util.List;
import java.util.Arrays;

/**
 * Unit tests for the Poison class.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class PoisonTest {
    private Poison poison;
    private static final List<Color> RAINBOW_COLORS = Arrays.asList(
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, 
        Color.CYAN, Color.BLUE, Color.MAGENTA
    );

    /**
     * Sets up the Poison instance before each test.
     */
    @BeforeEach
    public void setUp() {
        poison = new Poison();
    }

    /**
     * Tests the initial color of the Poison object.
     * Verifies that the initial color is one of the rainbow colors.
     */
    @Test
    public void testInitialColor() {
        assertTrue(RAINBOW_COLORS.contains(poison.getColor()), "Poison's initial color should be from the rainbow colors.");
    }

    /**
     * Tests the color changes of the Poison object in the rainbow sequence.
     * Verifies that the color changes correctly through the rainbow sequence.
     */
    @Test
    public void testColorChangesInSequence() {
        Color initialColor = poison.getColor();
        int initialIndex = RAINBOW_COLORS.indexOf(initialColor);

        for (int i = 0; i < RAINBOW_COLORS.size(); i++) {
            poison.change();
            Color expectedColor = RAINBOW_COLORS.get((initialIndex + i + 1) % RAINBOW_COLORS.size());
            assertEquals(expectedColor, poison.getColor(), "Poison's color should change in the rainbow sequence.");
        }
    }

    /**
     * Tests the shape of the Poison object.
     * Confirms that the poison object is always square-shaped.
     */
    @Test
    public void testShapeIsSquare() {
        assertEquals(Thing.SQUARE, poison.shape(), "Poison should be square-shaped.");
    }

    /**
     * Tests that the Poison object is always active.
     * Verifies that Poison remains active at all times.
     */
    @Test
    public void testIsAlwaysActive() {
        assertTrue(poison.isActive(), "Poison should always be active.");
    }
}