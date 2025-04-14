package puzzle;


import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 * Here is the class that solves Cycle 3 where in this cycle exists two methods simulate and solve simulator problem.
 *
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class PuzzleContest {

    /**
     * Method to determine if the puzzle can be solved from the starting configuration to the ending configuration.
     * Uses BFS to find if there is a sequence of tilts that transforms the starting board into the ending board.
     *
     * @param starting The starting configuration of the puzzle as a 2D char array.
     * @param ending The ending configuration of the puzzle as a 2D char array.
     * @return True if the puzzle can be solved, false otherwise.
     */
    public boolean solve(char[][] starting, char[][] ending) throws PuzzleExceptions.ConstructorsExceptions {
        // Check if both boards have the same count of tiles for each color
        if (!tileCountsMatch(starting, ending)) {
            JOptionPane.showMessageDialog(null,"No");
            return false;
        }
        
        JOptionPane.showMessageDialog(null,"Yes", "Icono de Octopocto", 0, new ImageIcon("img/checkMark.png"));
        // Implement BFS to find if there exists a sequence of tilts that transforms starting into ending
        return bfsSolve(starting, ending) != null;
    }

    /**
     * Method to simulate the solution by showing each step from the starting configuration to the ending configuration.
     * It first checks if a solution exists, then applies the sequence of tilts step by step, displaying the intermediate configurations.
     *
     * @param starting The starting configuration of the puzzle as a 2D char array.
     * @param ending The ending configuration of the puzzle as a 2D char array.
     * @throws PuzzleExceptions.makeVisibleInvisibleExceptions here it encloses exception messages for makeVisible and makeInvisible methods
     * @throws PuzzleExceptions.addDeleteGlueExceptions  exception to validate tilt correctly
     */    
    public void simulate(char[][] starting, char[][] ending) throws PuzzleExceptions.ConstructorsExceptions ,PuzzleExceptions.makeVisibleInvisibleExceptions, PuzzleExceptions.addDeleteGlueExceptions {
        // First, check if a solution exists
        List<Character> moves = bfsSolve(starting, ending);
        if (moves == null) {
            JOptionPane.showMessageDialog(null,"There's not possible solution");
            return;
        }

        // Create an instance of Puzzle with the initial configuration
        Puzzle puzzle = new Puzzle(starting, ending);
        puzzle.makeVisible();

        // Display the initial configuration
        System.out.println("Initial configuration:");
        printBoard(puzzle);

        // Apply the moves and display each step
        int step = 1;
        for (char move : moves) {
            puzzle.tilt(move);
            System.out.println("Step " + step + ": Tilt to " + directionToString(move));
            printBoard(puzzle);
            step++;
        }
        System.out.println("Finished");

        // Check if the goal configuration has been reached
        if (puzzle.isGoal()) {
            JOptionPane.showMessageDialog(null,"It reached the final configuration\n starting = ending", "Icono de Octopocto", 0, new ImageIcon("img/matrixEqual.png"));
        } else {
            JOptionPane.showMessageDialog(null,"It didn't reach the final configuration with the realized tilts.");
        }
    }

     /**
     * Breadth-First Search (BFS) implementation to find the sequence of tilts that transforms the starting board into the ending board.
     *
     * @param starting The starting configuration of the puzzle as a 2D char array.
     * @param ending The ending configuration of the puzzle as a 2D char array.
     * @return A list of characters representing the sequence of tilts, or null if no solution is found.
     */
    private List<Character> bfsSolve(char[][] starting, char[][] ending) {
        int h = starting.length;
        int w = starting[0].length;

        // Inner class to represent the state of the board
        class State {
            char[][] board;
            List<Character> moves;

            State(char[][] board, List<Character> moves) {
                this.board = board;
                this.moves = moves;
            }

            // Generate a unique key for the board state
            String getKey() {
                StringBuilder sb = new StringBuilder();
                for (char[] row : board) {
                    sb.append(row);
                }
                return sb.toString();
            }
        }

        // Possible tilt directions
        char[] directions = { 'u', 'd', 'l', 'r' };

        // Initialize the queue for BFS and the set of visited states
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Initial state
        State initialState = new State(copyBoard(starting), new ArrayList<>());
        queue.add(initialState);
        visited.add(initialState.getKey());

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            // Check if we have reached the final state
            if (boardsEqual(currentState.board, ending)) {
                return currentState.moves;
            }

            // Generate neighboring states
            for (char dir : directions) {
                char[][] newBoard = tiltBoard(currentState.board, dir);
                State newState = new State(newBoard, new ArrayList<>(currentState.moves));
                newState.moves.add(dir);

                String key = newState.getKey();
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.add(newState);
                }
            }
        }

        // No solution found
        return null;
    }

    // Auxiliary methods

    /**
     * Check if two boards are equal.
     *
     * @param board1 The first board.
     * @param board2 The second board.
     * @return True if both boards are equal, false otherwise.
     */
    private boolean boardsEqual(char[][] board1, char[][] board2) {
        int h = board1.length;
        for (int i = 0; i < h; i++) {
            if (!Arrays.equals(board1[i], board2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copy a board.
     *
     * @param board The board to copy.
     * @return A copy of the given board.
     */
    private char[][] copyBoard(char[][] board) {
        int h = board.length;
        char[][] newBoard = new char[h][];
        for (int i = 0; i < h; i++) {
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }

        /**
     * Check if the tile counts match between two boards.
     *
     * @param starting The starting board.
     * @param ending The ending board.
     * @return True if the tile counts match, false otherwise.
     */
    private boolean tileCountsMatch(char[][] starting, char[][] ending) {
        Map<Character, Integer> startingCounts = countTiles(starting);
        Map<Character, Integer> endingCounts = countTiles(ending);
        return startingCounts.equals(endingCounts);
    }

    /**
     * Count the tiles of each color on a board.
     *
     * @param board The board to count tiles on.
     * @return A map of tile colors and their respective counts.
     */
    private Map<Character, Integer> countTiles(char[][] board) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != '*') {
                    counts.put(cell, counts.getOrDefault(cell, 0) + 1);
                }
            }
        }
        return counts;
    }

    /**
     * Apply a tilt to a board and return the new board.
     *
     * @param board The current board.
     * @param direction The direction of the tilt ('u', 'd', 'l', 'r').
     * @return The new board after applying the tilt.
     */
    private char[][] tiltBoard(char[][] board, char direction) {
        int h = board.length;
        int w = board[0].length;
        char[][] newBoard = copyBoard(board);

        switch (direction) {
            case 'u':
                for (int col = 0; col < w; col++) {
                    int insertPos = 0;
                    for (int row = 0; row < h; row++) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[insertPos][col] = temp;
                            insertPos++;
                        }
                    }
                }
                break;
            case 'd':
                for (int col = 0; col < w; col++) {
                    int insertPos = h - 1;
                    for (int row = h - 1; row >= 0; row--) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[insertPos][col] = temp;
                            insertPos--;
                        }
                    }
                }
                break;
            case 'l':
                for (int row = 0; row < h; row++) {
                    int insertPos = 0;
                    for (int col = 0; col < w; col++) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[row][insertPos] = temp;
                            insertPos++;
                        }
                    }
                }
                break;
            case 'r':
                for (int row = 0; row < h; row++) {
                    int insertPos = w - 1;
                    for (int col = w - 1; col >= 0; col--) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[row][insertPos] = temp;
                            insertPos--;
                        }
                    }
                }
                break;
        }

        return newBoard;
    }

     /**
     * Convert the direction character to a readable string.
     *
     * @param direction The direction character ('u', 'd', 'l', 'r').
     * @return The string representation of the direction.
     */
    private String directionToString(char direction) {
        switch (direction) {
            case 'u':
                return "up";
            case 'd':
                return "down";
            case 'l':
                return "left";
            case 'r':
                return "right";
            default:
                return "";
        }
    }

    /**
     * Print the current board state of the puzzle.
     *
     * @param puzzle The puzzle instance to print.
     */
    private void printBoard(Puzzle puzzle) {
        int h = puzzle.getHeight();
        int w = puzzle.getWidth();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                BaseTile tile = puzzle.getTileAtPosition(row, col);
                System.out.print(tile.getLabel());
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Main method for testing the PuzzleContest class.
     * 
     * @param args Command-line arguments (not used).
     * @throws PuzzleExceptions.makeVisibleInvisibleExceptions here it encloses exception messages for makeVisible and makeInvisible methods.
     * @throws PuzzleExceptions.addDeleteGlueExceptions exception to validate tilt perfectly in simulate method
     */
    public static void main(String[] args) throws puzzle.PuzzleExceptions.ConstructorsExceptions,PuzzleExceptions.makeVisibleInvisibleExceptions, PuzzleExceptions.addDeleteGlueExceptions {
        PuzzleContest contest = new PuzzleContest();

        // Sample Input 1
        char[][] starting1 = {
            { '*', 'r', '*', '*' },
            { 'r', 'g', 'y', 'b' },
            { '*', 'b', '*', '*' },
            { '*', 'y', 'r', '*' }
        };

        char[][] ending1 = {
            { 'y', 'r', 'b', 'r' },
            { '*', '*', 'y', 'r' },
            { '*', '*', '*', 'g' },
            { '*', '*', '*', 'b' }
        };

        // Test solve method
        boolean canSolve = contest.solve(starting1, ending1);
        System.out.println("¿Is it possible to solve this puzzle? " + (canSolve ? "Yes" : "No"));

        // Test simulate method
        contest.simulate(starting1, ending1);
    }
}