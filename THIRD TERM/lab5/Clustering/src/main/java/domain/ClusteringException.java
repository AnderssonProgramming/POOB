package domain;

/**
 * Clase de excepciones para mostrar los posibles errores del juego "Clustering".
 * @author Andersson David Sanchez Mendez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class ClusteringException extends Exception {
    public static final String INVALID_VALUES = "Invalid dimensions or percentage";
    public static final String INVALID_DIRECTION = "Invalid direction";
    public static final String NOT_GOOD_DIMENSIONS = "Dimensiones del tablero no coinciden con el tamaño actual";

    /**
     * Constructor que adquiere de su clase padre el mensaje que se va a enviar como error.
     * @param message Mensaje a enviar dependiendo del error.
     */
    public ClusteringException(String message) {
        super(message);
    }
}
