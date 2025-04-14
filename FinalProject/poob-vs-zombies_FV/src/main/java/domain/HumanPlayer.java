package domain;

/**
 * The HumanPlayer class represents a human player in the game.
 * It is an abstract class that extends the Player class.
 * This class provides a constructor to initialize the player's name
 * and declares an abstract method for playing a turn.
 */
public abstract class HumanPlayer extends Player {

    /**
     * Constructs a HumanPlayer with the specified name.
     *
     * @param name the name of the human player
     */
    public HumanPlayer(String name) {
        super(name);
    }
    
    /**
     * Sets the score for the human player.
     * This method is currently not implemented and will throw an UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException if the method is called
     */
    @Override
    public void setScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }

    /**
     * Abstract method to be implemented by subclasses to define the actions
     * taken by the human player during their turn.
     */
    public abstract void playTurn();
    
}
