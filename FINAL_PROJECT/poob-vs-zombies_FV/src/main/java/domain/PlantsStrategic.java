package domain;

import java.util.ArrayList;

/**
 * The PlantsStrategic class represents a human player controlling the plants team
 * in the game. It extends the HumanPlayer class and initializes the plants team
 * with the given resources and plants.
 */
public class PlantsStrategic extends HumanPlayer {

    /**
     * Constructs a new PlantsStrategic player with the specified name, sunlight resources,
     * and list of plants.
     *
     * @param name     the name of the player
     * @param sunlight the amount of sunlight resources available for the plants team
     * @param plants   the list of plants available for the plants team
     */
    public PlantsStrategic(String name, int sunlight, ArrayList<String> plants) {
        // sunlight are the resources for the plants in plants team
        super(name);
        Team plantsTeam = new Plants(sunlight, plants);
        this.team = plantsTeam;
    }

    /**
     * Plays the turn for the plants team. This method is currently not implemented.
     *
     * @throws UnsupportedOperationException if the method is not implemented
     */
    @Override
    public void playTurn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playTurn'");
    }

}
