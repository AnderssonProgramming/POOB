package domain;

import java.util.ArrayList;

/**
 * The Plants class represents a team of plants in the game.
 * It extends the Team class and provides specific functionality for the Plants team.
 */
public class Plants extends Team {
    
    /**
     * The constant amount by which the resource counter is increased.
     */
    protected static final int SUN_AMOUNT_INCREASE = 25;
    
    /**
     * The name of the team.
     */
    public static final String NAME = "Plants";
    
    /**
     * The time required for planting.
     */
    public static final int PLANTING_TIME = 120;

    /**
     * Constructs a new Plants team with the specified resource counter and characters.
     *
     * @param resourceCounter the initial resource counter value
     * @param characters the list of characters in the team
     */
    public Plants(int resourceCounter, ArrayList<String> characters) {
        super(resourceCounter, characters);
    }

    /**
     * Increases the resource counter by the predefined sun amount increase.
     */
    @Override
    public void increaseResourceAmount() {
        resourceCounter += SUN_AMOUNT_INCREASE;
    }

    /**
     * Increases the resource counter by the specified amount.
     *
     * @param amount the amount to increase the resource counter by
     */
    public void increaseResourceAmount(int amount) {
        resourceCounter += amount;
    }

    /**
     * Returns the name of the team.
     *
     * @return the name of the team
     */
    @Override
    public String getTeamName() {
        return NAME;
    }
}
