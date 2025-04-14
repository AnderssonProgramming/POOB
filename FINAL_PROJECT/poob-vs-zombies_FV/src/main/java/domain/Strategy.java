package domain;

/**
 * The Strategy interface defines the contract for implementing different strategies
 * in the game. Classes that implement this interface will provide specific 
 * implementations for executing strategic turns and managing strategy states.
 */
public interface Strategy {

    /**
     * Executes a strategic turn based on the given state.
     *
     * @param state the current state of the game which will influence the strategy execution
     */
    public abstract void executeStrategicTurn(int state);

    /**
     * Retrieves the strategy based on the current game state.
     *
     * @param currentGameState the current state of the game
     * @return an integer representing the strategy to be used
     */
    public abstract int getStrategy(int currentGameState);

    /**
     * Sets the strategy based on the given strategic game state.
     *
     * @param strategicGameState the state of the game that determines the strategy to be set
     */
    public abstract void setStrategy(int strategicGameState);

}