// Peashooter
package domain;
/**
 * The Peashooter class represents a type of plant that shoots projectiles at zombies.
 * It extends the Plant class and implements the Shooter interface.
 * Peashooters have a fixed cost, health, and damage per projectile.
 */
public class Peashooter extends Plant implements Shooter {
    /**
     * The cost of planting a Peashooter.
     */
    public static final int COST = 100;

    /**
     * The health of the Peashooter.
     */
    public static final int HEALTH = 300;

    /**
     * The damage dealt by each projectile shot by the Peashooter.
     */
    public static final int DAMAGE = 20; // Daño por proyectil

    /**
     * Constructs a new Peashooter with predefined health and cost.
     */
    public Peashooter() {
        super(HEALTH, COST);
    }

    /**
     * Shoots a projectile in the specified lane.
     *
     * @param lane the lane in which to shoot the projectile
     */
    @Override
    public void shoot(int lane) {
        // Lógica para disparar
    }

    /**
     * Returns the damage dealt by each projectile shot by the Peashooter.
     *
     * @return the damage per projectile
     */
    public int getDamage() {
        return DAMAGE;
    }
}
