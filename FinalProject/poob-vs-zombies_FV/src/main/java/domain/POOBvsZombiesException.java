package domain;


/**
 * POOBvsZombiesException is a custom exception class for handling specific errors in the POOB vs Zombies game.
 * This class extends the Exception class and provides predefined error messages for various scenarios.
 */
public class POOBvsZombiesException extends Exception {

    /**
     * Error message for invalid coordinates.
     * This message is used when the row or column entered is not valid.
     */
    public static final String INVALID_COORDINATES = "Invalid row or column. Please enter valid numbers.";

    /**
     * Error message for invalid lawnmower removal.
     * This message is used when a lawnmower cannot be removed.
     */
    public static final String INVALID_LAWNMOWER = "Cannot remove lawnmower.";

    /**
     * Error message for no plant to remove.
     * This message is used when there is no plant to remove in the selected cell.
     */
    public static final String NO_PLANT_TO_REMOVE = "No plant to remove in the selected cell.";

    /**
     * Error message for invalid inputs.
     * This message is used when the input provided is not a valid integer.
     */
    public static final String INVALID_INPUTS = "Invalid input, please enter valid integers.";

    /**
     * Error message for invalid time limit.
     * This message is used when the total time is negative or there is no horde.
     */
    public static final String TIME_LIMIT = "El tiempo total no debe ser negativo y por lo menos debe haber una horda.";    

    /**
     * Constructor that receives the exception message.
     * When the exception is created, it is automatically recorded in the log.
     *
     * @param message The message describing the exception.
     */
    public POOBvsZombiesException(String message) {
        super(message);
        // Al crear la excepción, automáticamente se registra en el log
        Log.record(this);
    }
}

