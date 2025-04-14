package domain;

/**
 * The Basic class represents a basic type of Zombie with predefined cost, health, and damage values.
 * It extends the Zombie class.
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class Basic extends Zombie {
    /**
     * The cost of creating a Basic Zombie.
     */
    private static final int COST = 100;

    /**
     * The health points of a Basic Zombie.
     */
    private static final int HEALTH = 100;

    /**
     * The damage points a Basic Zombie can inflict.
     */
    private static final int DAMAGE = 100;

    /**
     * Constructs a Basic Zombie with predefined health, cost, and damage values.
     */
    public Basic() {
        super(HEALTH, COST, DAMAGE);
    }
}