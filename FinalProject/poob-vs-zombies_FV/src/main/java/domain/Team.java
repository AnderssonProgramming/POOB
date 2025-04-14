package domain;

import java.util.ArrayList;

import javax.swing.Timer;

/**
 * The Team class represents an abstract team in the game with a resource counter and a list of characters.
 * It provides methods to manage resources and characters.
 */


public abstract class Team {

    /**
     * Counter for the team's resources.
     */
    protected int resourceCounter;

    /**
     * List of characters in the team.
     */
    protected ArrayList<String> characters;

    /**
     * Timer to generate resources every 10 seconds.
     */
    public static final Timer RESOURCE_TIME_GENERATOR = new Timer(10000, null);

    /**
     * Constructor to initialize the team with a list of characters.
     *
     * @param characters List of characters in the team.
     */
    public Team(ArrayList<String> characters) {
        this.characters = characters;
    }

    /**
     * Constructor to initialize the team with a resource counter and a list of characters.
     *
     * @param resourceCounter Initial resource counter value.
     * @param characters      List of characters in the team.
     */
    public Team(int resourceCounter, ArrayList<String> characters) {
        this.resourceCounter = resourceCounter;
        this.characters = characters;
    }

    /**
     * Abstract method to increase the resource amount.
     * This method should be implemented by subclasses.
     */
    public abstract void increaseResourceAmount();

    /**
     * Gets the current amount of resources.
     *
     * @return The current resource counter amount.
     */
    public int getResourceCounterAmount() {
        return resourceCounter;
    }

    /**
     * Gets the list of characters in the team.
     *
     * @return The list of characters.
     */
    public ArrayList<String> getCharacters() {
        return characters;
    }

    /**
     * Sets the list of characters in the team.
     *
     * @param characters The new list of characters.
     */
    public void setCharacters(ArrayList<String> characters) {
        this.characters = characters;
    }

    /**
     * Sets the resource counter to a new value.
     *
     * @param resourceCounter The new resource counter value.
     */
    public void setResourceCounter(int resourceCounter) {
        this.resourceCounter = resourceCounter;
    }

    /**
     * Abstract method to get the team name.
     * This method should be implemented by subclasses.
     *
     * @return The name of the team.
     */
    public abstract String getTeamName();

    /**
     * Adds resources to the team's resource counter.
     *
     * @param resource The resource to be added.
     */
    public void addResource(Resource resource) {
        this.resourceCounter += resource.getValue();
    }

    /**
     * Deducts a specified amount of resources from the team's resource counter.
     *
     * @param amount The amount of resources to deduct.
     * @throws IllegalArgumentException if the amount to deduct is greater than the current resource counter.
     */
    public void deductResource(int amount) {
        if (amount > resourceCounter) {
            throw new IllegalArgumentException("No hay suficientes recursos.");
        }
        resourceCounter -= amount;
    }
}
