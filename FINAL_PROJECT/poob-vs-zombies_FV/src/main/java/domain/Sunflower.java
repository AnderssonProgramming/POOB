package domain;

/**
 * The Sunflower class represents a type of plant that generates resources in the form of sunlight.
 * It extends the Plant class and implements the ResourceGenerator interface.
 * Sunflowers are essential for generating the resources needed to plant other types of plants.
 */
public class Sunflower extends Plant implements ResourceGenerator {
    /**
     * The cost of planting a Sunflower.
     */
    private static final int COST = 50;

    /**
     * The health of the Sunflower.
     */
    private static final int HEALTH = 300;

    /**
     * Constructs a new Sunflower with predefined health and cost values.
     */
    public Sunflower() {
        super(HEALTH, COST);
    }

    /**
     * Generates a resource in the form of sunlight.
     * 
     * @param lane The lane in which the Sunflower is located.
     * @return A Resource object representing the generated sunlight.
     */
    @Override
    public Resource generateResource(int lane) {
        // Genera un sol de 25 en la posición actual
        return new Resource(Resource.SOL, 25);
    }
}
