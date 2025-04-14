// PotatoMine.java
package domain;

/**
 * The PotatoMine class represents a plant that can explode and damage zombies in the game.
 * It extends the Plant class and has specific attributes and behaviors.
 */
public class PotatoMine extends Plant {
    /**
     * The cost of the PotatoMine in the game.
     */
    private static final int COST = 25;

    /**
     * The health of the PotatoMine.
     */
    private static final int HEALTH = 100;

    /**
     * The time it takes for the PotatoMine to activate, in seconds.
     */
    public static final int ACTIVATION_TIME = 14;

    private boolean hasExploded = false;
    /**
     * Indicates whether the PotatoMine is activated.
     */
    private boolean activated = false;

    /**
     * Constructs a new PotatoMine with default health and cost.
     */
    public PotatoMine() {
        super(HEALTH, COST);
    }

    /**
     * Activates the PotatoMine, making it ready to explode.
     */
    public void activate() {
        activated = true;
    }

    
    /**
     * Checks if the PotatoMine is activated.
     *
     * @return true if the PotatoMine is activated, false otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Causes the PotatoMine to explode if it is activated.
     * This method should implement the logic to damage adjacent zombies.
     */
    public void explode() {
        if (activated) {
            // Lógica de explosión
            System.out.println("PotatoMine ha explotado.");
            // Implementa la lógica de daño a los zombies adyacentes
        }
    }

    /**
     * Sets the activation status of the PotatoMine.
     *
     * @param activated a boolean indicating whether the PotatoMine is activated (true) or not (false)
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean hasExploded() {
        return hasExploded;
    }
}
