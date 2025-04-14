package domain;

import java.util.ArrayList;

/**
 * The Zombies class represents a team of zombies in the game.
 * It extends the Team class and provides specific functionality for the Zombies team.
 * This class includes methods to increase the resource amount and get the team name.
 */
public class Zombies extends Team {
    
    /**
     * The name of the team.
     */
    public static final String NAME = "Zombies";
    
    /**
     * The amount by which the resource counter is increased for Zombies.
     */
    protected static final int BRAIN_AMOUNT_INCREASE = 50;

    /**
     * Default constructor that initializes the Zombies team with the original settings.
     */
    public Zombies(){
        super(MachinePlayer.ORIGINAL_ZOMBIES);
    }

    /**
     * Constructor that initializes the Zombies team with a specified list of characters and brain count.
     * 
     * @param characters The list of characters in the Zombies team.
     * @param brains The initial amount of brains (resources) for the Zombies team.
     */
    public Zombies(ArrayList<String> characters, int brains){
        super(brains, characters);
    }

    /**
     * Increases the resource amount for the Zombies team by the predefined amount.
     */
    @Override
    public void increaseResourceAmount(){
        resourceCounter += BRAIN_AMOUNT_INCREASE;
    }

    /**
     * Returns the name of the team.
     * 
     * @return The name of the team.
     */
    @Override
    public String getTeamName(){
        return NAME;
    }           
}
