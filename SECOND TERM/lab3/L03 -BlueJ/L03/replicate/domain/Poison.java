package domain;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents a Poison artefact in the manufacturing lattice.
 * Poison changes color in a rainbow sequence and is always considered active.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class Poison extends Artefact implements Thing {
    
    private static final List<Color> RAINBOW_COLORS = Arrays.asList(
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, 
        Color.CYAN, Color.BLUE, Color.MAGENTA
    );
    private int colorIndex = 0;
    private Color color;

    /**
     * Creates a new Poison artefact, starting with a random color from the rainbow colors.
     */
    public Poison() {
        colorIndex = new Random().nextInt(RAINBOW_COLORS.size());
        color = RAINBOW_COLORS.get(colorIndex);
    }

    /**
     * Decides the next state of the Poison artefact.
     * Since Poison does not have a next state, this method is left empty.
     */
    @Override
    public void decide() {
        // Poison doesn't have a next state in the traditional sense, so this can be left empty
    }

    /**
     * Changes the color of the Poison artefact to the next one in the rainbow sequence.
     */
    @Override
    public void change() {
        colorIndex = (colorIndex + 1) % RAINBOW_COLORS.size();
        color = RAINBOW_COLORS.get(colorIndex);
    }

    /**
     * Returns the shape of the Poison artefact, which is square.
     * @return The shape of the artefact, represented as {@code SQUARE}.
     */
    @Override
    public int shape() {
        return SQUARE; // Poison is square-shaped
    }

    /**
     * Returns the current color of the Poison artefact.
     * @return The color of the Poison artefact.
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Checks if the Poison artefact is active.
     * Poison is always active.
     * @return {@code true} because Poison is always active.
     */
    @Override
    public boolean isActive() {
        return true; // Poison is always "active" in the sense that it exists
    }
}