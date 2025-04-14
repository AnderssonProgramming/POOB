import domain.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la funcionalidad del juego "Clustering".
 * @author Andersson David Sanchez Mendez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class ClusteringTest {

    private Clustering clustering;

    /**
     * Configura el entorno antes de cada prueba, inicializando un objeto de Clustering.
     * @throws ClusteringException si los parámetros para crear el tablero son inválidos.
     */
    @BeforeEach
    public void setUp() throws ClusteringException {
        // Inicializamos con un tablero de 4x4 y 50% de las celdas coloreadas
        clustering = new Clustering(4, 4, 50);
    }

    /**
     * Prueba que el constructor de Clustering no lance una excepción cuando se proporcionan valores válidos.
     */
    @Test
    public void testConstructorValidValues() {
        assertDoesNotThrow(() -> new Clustering(5, 5, 30));
    }

    /**
     * Prueba que el constructor lance ClusteringException cuando se proporcionan valores inválidos.
     */
    @Test
    public void testConstructorInvalidValues() {
        assertThrows(ClusteringException.class, () -> new Clustering(-1, 4, 50));
        assertThrows(ClusteringException.class, () -> new Clustering(4, -2, 50));
        assertThrows(ClusteringException.class, () -> new Clustering(4, 4, 110));
    }

    /**
     * Prueba la lógica del cálculo de puntaje.
     * @throws ClusteringException si ocurre un error al crear el tablero.
     */
    @Test
    public void testScore() throws ClusteringException {
        // Inicializamos un tablero con un caso específico para calcular el puntaje
        clustering = new Clustering(4, 4, 0); // Tablero vacío
        char[][] board = clustering.getBoard();
        // Configuramos las baldosas de tal manera que se puedan contar agrupamientos
        board[0][0] = 'r';
        board[0][1] = 'r';
        board[1][1] = 'r';
        board[2][2] = 'g';
        board[3][2] = 'g';

        int score = clustering.score();

        // En este caso, hay dos grupos de "r" y uno de "g", lo cual debería dar un puntaje de 3
        assertEquals(3, score);
    }

    /**
     * Prueba que el número de movimientos realizados (tilts) se cuente correctamente.
     * @throws ClusteringException si ocurre un error al realizar un tilt.
     */
    @Test
    public void testTiltings() throws ClusteringException {
        // Verificamos que inicialmente los movimientos sean cero
        assertEquals(0, clustering.tiltings());

        // Hacemos algunos tilts y verificamos el contador
        clustering.tilt('U');
        clustering.tilt('L');
        clustering.tilt('D');
        assertEquals(3, clustering.tiltings());
    }

    /**
     * Prueba la inicialización del tablero para verificar si el porcentaje de celdas coloreadas es correcto.
     * @throws ClusteringException si ocurre un error al crear el tablero.
     */
    @Test
    public void testBoardInitialization() throws ClusteringException {
        clustering = new Clustering(4, 4, 50);
        char[][] board = clustering.getBoard();

        // Contamos el número de celdas coloreadas para verificar el porcentaje
        int coloredCells = 0;
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != ' ') {
                    coloredCells++;
                }
            }
        }

        int expectedColoredCells = (int) (4 * 4 * 0.5);
        assertEquals(expectedColoredCells, coloredCells);
    }

    /**
     * Prueba la funcionalidad de un tilt hacia arriba y verifica que las baldosas se muevan correctamente.
     * @throws ClusteringException si ocurre un error al realizar el tilt.
     */
    @Test
    public void testTiltUp() throws ClusteringException {
        // Realizamos un tilt hacia arriba (que moverá las baldosas hacia abajo)
        char[][] beforeTilt = copyBoard(clustering.getBoard());
        clustering.tilt('U');
        char[][] afterTilt = clustering.getBoard();

        // Verificamos que todas las baldosas se hayan movido una posición hacia abajo si es posible
        for (int i = beforeTilt.length - 2; i >= 0; i--) {
            for (int j = 0; j < beforeTilt[0].length; j++) {
                if (beforeTilt[i][j] != ' ' && beforeTilt[i + 1][j] == ' ') {
                    assertNotEquals(beforeTilt[i][j], afterTilt[i + 1][j]);
                }
            }
        }
    }

    /**
     * Método auxiliar para copiar el estado del tablero.
     * @param originalBoard el tablero original a copiar.
     * @return una copia del tablero.
     */
    private char[][] copyBoard(char[][] originalBoard) {
        char[][] copy = new char[originalBoard.length][originalBoard[0].length];
        for (int i = 0; i < originalBoard.length; i++) {
            System.arraycopy(originalBoard[i], 0, copy[i], 0, originalBoard[i].length);
        }
        return copy;
    }

}
