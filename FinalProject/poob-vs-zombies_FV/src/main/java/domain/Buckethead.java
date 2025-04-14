// Buckethead
package domain;
/**
 * The Buckethead class represents a type of Zombie with higher health and cost compared to a Basic Zombie.
 * It extends the Zombie class.
 * 
 * <p>This class is part of the domain package and is used to create a Buckethead Zombie with predefined
 * cost, health, and damage values.</p>
 * 
 * @version 2024
 */
public class Buckethead extends Zombie {
    /**
     * The cost of creating a Buckethead Zombie.
     */
    private static final int COST = 200;

    /**
     * The health points of a Buckethead Zombie.
     */
    private static final int HEALTH = 800;

    /**
     * The damage points a Buckethead Zombie can inflict.
     */
    private static final int DAMAGE = 100;

    /**
     * Constructs a Buckethead Zombie with predefined health, cost, and damage values.
     */
    public Buckethead() {
        super(HEALTH, COST, DAMAGE);
    }
}