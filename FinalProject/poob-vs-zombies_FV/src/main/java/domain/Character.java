package domain;
/**
 * The Character class is an abstract representation of a character in the game.
 * It extends the Entity class and provides common attributes and methods for all characters.
 * This class cannot be instantiated directly and must be subclassed.
 * 
 * @version 2024
 */
public abstract class Character extends Entity {
    /**
     * The health points of the character.
     */
    protected int health;

    /**
     * The cost of creating the character.
     */
    protected int cost;

    /**
     * Constructs a Character with the specified health and cost values.
     * 
     * @param health the health points of the character
     * @param cost the cost of creating the character
     */
    protected Character(int health, int cost) {
        this.health = health;
        this.cost = cost;
    }

    /**
     * Returns the health points of the character.
     * 
     * @return the health points of the character
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the cost of creating the character.
     * 
     * @return the cost of creating the character
     */
    public int getCost() {
        return cost;
    }

    /**
     * Makes the character shoot in the specified line.
     * 
     * @param line the line in which the character will shoot
     */
    public void shoot(int line) {
        // Disparar
    }

    /**
     * Reduces the health points of the character by the specified damage value.
     * 
     * @param damage the amount of damage to inflict on the character
     */
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    /**
     * Checks if the character is dead.
     * 
     * @return true if the character's health is less than or equal to 0, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }
}