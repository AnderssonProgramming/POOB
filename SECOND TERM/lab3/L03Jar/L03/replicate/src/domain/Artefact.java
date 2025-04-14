package domain;
import java.awt.Color;

/**
 * Represents an abstract artefact in the manufacturing lattice.
 * Artefacts have a state (UNKNOWN, ACTIVE, or INACTIVE) and can keep track of steps taken.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public abstract class Artefact {
    public final static char UNKNOWN = 'u', ACTIVE = 'a', INACTIVE = 'd';
    protected char state;
    private int steps;

    /**
     * Creates a new Artefact with an initial state of UNKNOWN and step count set to zero.
     */
    public Artefact() {
        state = UNKNOWN;
        steps = 0;
    }

    /**
     * Increments the step count of the Artefact by one.
     */
    protected void step() {
        steps++;
    }
    
    /**
     * Returns the number of steps taken by the Artefact.
     * @return The number of steps.
     */   
    public final int getSteps() {
        return steps;
    }

    /**
     * Checks if the Artefact is in an active state.
     * @return {@code true} if the Artefact is ACTIVE, {@code false} otherwise.
     */
    public boolean isActive() {
        return (state == Artefact.ACTIVE);
    }
}