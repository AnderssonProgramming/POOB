package domain;

/**
 * The Brainstein class represents a type of Zombie that can generate resources.
 * It extends the Zombie class and implements the ResourceGenerator interface.
 * This zombie has a predefined cost and health value, and it can generate a brain resource.
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class Brainstein extends Zombie implements ResourceGenerator {
    /**
     * The cost of creating a Brainstein Zombie.
     */
    private static final int COST = 50;

    /**
     * The health points of a Brainstein Zombie.
     */
    private static final int HEALTH = 300;

    /**
     * Constructs a Brainstein Zombie with predefined health and cost values.
     */
    public Brainstein() {
        super(HEALTH, COST, 0);
    }

    /**
     * Generates a resource in the form of a brain with a value of 25 at the current position.
     *
     * @param lane The lane in which the resource is generated.
     * @return A new Resource object representing a brain with a value of 25.
     */
    @Override
    public Resource generateResource(int lane) {
        // Genera un cerebro de 25 en la posición actual
        return new Resource(Resource.BRAIN, 25);
    }
}