// ECIZombie
package domain;
/**
 * The ECIZombie class represents a specific type of zombie in the game.
 * This zombie has the ability to shoot and has specific attributes such as cost, health, and damage.
 * It extends the Zombie class and implements the Shooter interface.
 */
public class ECIZombie extends Zombie implements Shooter {
    /**
     * The cost of the ECIZombie in the game.
     */
    public static final int COST = 250;

    /**
     * The health points of the ECIZombie.
     */
    public static final int HEALTH = 200;

    /**
     * The damage dealt by the ECIZombie.
     */
    public static final int DAMAGE = 50;

    /**
     * Constructs an ECIZombie with predefined health, cost, and damage values.
     */
    public ECIZombie() {
        super(HEALTH, COST, 0);
    }

    /**
     * Shoots a projectile in the specified lane.
     * 
     * @param lane the lane in which the ECIZombie will shoot.
     */
    @Override
    public void shoot(int lane) {
        // Disparar POOmBa
    }
}
