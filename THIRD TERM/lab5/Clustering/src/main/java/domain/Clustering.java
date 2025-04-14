package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase Clustering que maneja la lógica del juego de agrupamiento.
 * Contiene métodos para inicializar el tablero, realizar movimientos (tilts), y calcular el puntaje.
 * 
 * @author Andersson David Sanchez Mendez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 * 
 */
public class Clustering {
    private char[][] board;
    private int height;
    private int width;
    private int tiltings;
    private int score;
    private int initialPercentage;

    /**
     * Constructor de la clase Clustering.
     * Inicializa el tablero con las dimensiones dadas y un porcentaje de celdas coloreadas.
     *
     * @param height     Altura del tablero.
     * @param width      Anchura del tablero.
     * @param percentage Porcentaje de celdas coloreadas.
     * @throws ClusteringException Si los valores son inválidos.
     */
    public Clustering(int height, int width, int percentage) throws ClusteringException {
        if (height <= 0 || width <= 0 || percentage < 0 || percentage > 100) {
            throw new ClusteringException(ClusteringException.INVALID_VALUES);
        }

        this.height = height;
        this.width = width;
        this.tiltings = 0;
        this.score = 0;
        this.initialPercentage = percentage; // Guardar el porcentaje inicial
        this.board = new char[height][width];
        initializeBoard(percentage);
    }

    /**
     * Inicializa el tablero con un porcentaje específico de celdas coloreadas.
     *
     * @param percentage Porcentaje de celdas a colorear.
     */
    private void initializeBoard(int percentage) {
        // Inicializa todas las celdas vacías
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = ' '; // Vacío por defecto
            }
        }

        // Número total de celdas y cantidad de celdas coloreadas
        int totalCells = height * width;
        int coloredCells = (int) (totalCells * (percentage / 100.0));

        // Lista para almacenar los índices de las celdas y mezclarlos aleatoriamente
        List<Integer> cellIndices = new ArrayList<>();
        for (int i = 0; i < totalCells; i++) {
            cellIndices.add(i);
        }
        Collections.shuffle(cellIndices);

        // Colorear las celdas aleatoriamente
        Random random = new Random();
        for (int i = 0; i < coloredCells; i++) {
            int index = cellIndices.get(i);
            int row = index / width;
            int col = index % width;

            // Colores representados como caracteres (r, g, b, y)
            char color = switch (random.nextInt(4)) {
                case 0 -> 'r'; // rojo
                case 1 -> 'g'; // verde
                case 2 -> 'b'; // azul
                case 3 -> 'y'; // amarillo
                default -> ' ';
            };
            board[row][col] = color;
        }
    }

    /**
     * Realiza un movimiento (tilt) en una dirección específica.
     *
     * @param side Dirección del movimiento ('U' = arriba, 'D' = abajo, 'L' = izquierda, 'R' = derecha).
     * @throws ClusteringException Si la dirección es inválida.
     */
    public void tilt(char side) throws ClusteringException {
        switch (side) {
            case 'U': // Tilt hacia arriba: las baldosas se mueven hacia abajo
                for (int i = 1; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if (board[i][j] != ' ' && board[i - 1][j] == ' ') {
                            board[i - 1][j] = board[i][j];
                            board[i][j] = ' ';
                        }
                    }
                }
                break;
            case 'D': // Tilt hacia abajo: las baldosas se mueven hacia arriba
                for (int i = height - 2; i >= 0; i--) {
                    for (int j = 0; j < width; j++) {
                        if (board[i][j] != ' ' && board[i + 1][j] == ' ') {
                            board[i + 1][j] = board[i][j];
                            board[i][j] = ' ';
                        }
                    }
                }
                break;
            case 'L': // Tilt hacia la izquierda: las baldosas se mueven hacia la derecha
                for (int j = 1; j < width; j++) {
                    for (int i = 0; i < height; i++) {
                        if (board[i][j] != ' ' && board[i][j - 1] == ' ') {
                            board[i][j - 1] = board[i][j];
                            board[i][j] = ' ';
                        }
                    }
                }
                break;
            case 'R': // Tilt hacia la derecha: las baldosas se mueven hacia la izquierda
                for (int j = width - 2; j >= 0; j--) {
                    for (int i = 0; i < height; i++) {
                        if (board[i][j] != ' ' && board[i][j + 1] == ' ') {
                            board[i][j + 1] = board[i][j];
                            board[i][j] = ' ';
                        }
                    }
                }
                break;
            default:
                throw new ClusteringException(ClusteringException.INVALID_DIRECTION);
        }
        tiltings++;
    }

    /**
     * Calcula el puntaje basado en el agrupamiento de las fichas.
     * Solo se consideran las fichas de colores válidos (r, g, b, y, c).
     *
     * @return El puntaje calculado.
     */
    public int score() {
        score = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char current = board[i][j];
                if (current != ' ') {
                    // Verificar si el carácter es de un color válido
                    if (current == 'r' || current == 'g' || current == 'b' || current == 'y' || current == 'c') {
                        // Verificar adyacentes
                        if (i + 1 < height && board[i + 1][j] == current) score++;
                        if (j + 1 < width && board[i][j + 1] == current) score++;
                    }
                }
            }
        }
        return score;
    }

    /**
     * Retorna el número de inclinaciones realizadas.
     *
     * @return Número de inclinaciones realizadas.
     */
    public int tiltings() {
        return tiltings;
    }

    /**
     * Retorna el estado actual del tablero.
     *
     * @return El estado actual del tablero en forma de matriz de caracteres.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Establece el estado del tablero.
     * Permite restaurar un estado previo del tablero.
     *
     * @param newBoard Nuevo estado del tablero.
     * @throws ClusteringException Si las dimensiones del nuevo tablero no coinciden con el actual.
     */
    public void setBoard(char[][] newBoard) throws ClusteringException {
        if (newBoard.length != height || newBoard[0].length != width) {
            throw new ClusteringException(ClusteringException.NOT_GOOD_DIMENSIONS);
        }
        for (int i = 0; i < height; i++) {
            System.arraycopy(newBoard[i], 0, board[i], 0, width);
        }
    }

    /**
     * Retorna la altura del tablero.
     *
     * @return La altura del tablero.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retorna la anchura del tablero.
     *
     * @return La anchura del tablero.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retorna el porcentaje inicial de celdas coloreadas.
     *
     * @return El porcentaje inicial de celdas coloreadas.
     */
    public int getInitialPercentage() {
        return initialPercentage;
    }
}
