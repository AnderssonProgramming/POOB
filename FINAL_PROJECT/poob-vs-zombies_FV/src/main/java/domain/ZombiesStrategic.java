package domain;

import java.util.ArrayList;

/**
 * The ZombiesStrategic class represents a human player with a strategic approach
 * in a game involving zombies. This class extends the HumanPlayer class and 
 * initializes the player's team as a Zombies team.
 * 
**/
public class ZombiesStrategic extends HumanPlayer {
        
    /**
     * Constructs a ZombiesStrategic player with the specified name, brains, and list of zombies.
     *
     * @param name the name of the player
     * @param brains the amount of brains (resources) for the player
     * @param zombies the list of zombies in the player's team
     */
    public ZombiesStrategic(String name, int brains, ArrayList<String> zombies) {
        // brains are the resources for the zombies in zombies team
        super(name);
        Team zombiesTeam = new Zombies(zombies,brains);
        this.team = zombiesTeam;
    }


    @Override
    public void playTurn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playTurn'");
    }
}
