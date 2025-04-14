package domain;

/**
 * The Plant class represents an abstract plant character in the game.
 * It extends the Character class and provides the basic attributes and
 * functionalities for all plant characters.
 */
public abstract class Plant extends Character {
    
    /**
     * Constructs a new Plant with the specified health and cost.
     *
     * @param health the health points of the plant
     * @param cost the cost to place the plant in the game
     */
    protected Plant(int health, int cost) {
        super(health, cost);
    }
}
