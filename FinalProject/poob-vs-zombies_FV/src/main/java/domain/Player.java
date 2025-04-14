package domain;

/**
 * The Player class represents an abstract player in a game.
 * It contains basic attributes and methods that are common to all players.
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public abstract class Player {

    /**
     * The name of the player.
     */
    protected String name;

    /**
     * The score of the player.
     */
    protected int score;

    /**
     * The team to which the player belongs.
     */
    protected Team team;

    /**
     * Constructs a new Player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the score of the player.
     *
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player.
     * This method must be implemented by subclasses.
     */
    public abstract void setScore();

    /**
     * Returns the team of the player.
     *
     * @return the team of the player
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Adds the specified number of points to the player's score.
     *
     * @param points the number of points to add
     */
    public void addToScore(int points) {
        this.score += points;
    }

    /**
     * Sets the score of the player to the specified value.
     *
     * @param score the new score of the player
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the team of the player.
     *
     * @param team the team of the player
     */
    public void setName(String name) {
        this.name = name;
    }
}