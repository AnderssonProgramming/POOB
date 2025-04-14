/**
 * The Conehead class represents a type of Zombie with predefined cost, health, and damage values.
 * It extends the Zombie class.
 * This type of zombie has a cone on its head, providing it with more health compared to a Basic Zombie.
 * 
 * @version 2024
 */
package domain;

public class Conehead extends Zombie {
    /**
     * The cost of creating a Conehead Zombie.
     */
    private static final int COST = 150;

    /**
     * The health points of a Conehead Zombie.
     */
    private static final int HEALTH = 380;

    /**
     * The damage points a Conehead Zombie can inflict.
     */
    private static final int DAMAGE = 100;

    /**
     * Constructs a Conehead Zombie with predefined health, cost, and damage values.
     */
    public Conehead() {
        super(HEALTH, COST, DAMAGE);
    }
}