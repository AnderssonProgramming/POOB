// WallNut
package domain;
/**
 * The WallNut class represents a type of plant in the game that has high health and is used as a defensive barrier.
 * It extends the Plant class and initializes with specific health and cost values.
 */
public class WallNut extends Plant {
    /**
     * The cost of planting a WallNut.
     */
    private static final int COST = 50;

    /**
     * The health value of the WallNut, indicating how much damage it can take before being destroyed.
     */
    private static final int HEALTH = 4000;

    /**
     * Constructs a new WallNut with predefined health and cost values.
     */
    public WallNut() {
        super(HEALTH, COST);
    }
}
