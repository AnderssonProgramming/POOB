    package puzzle;
    
    import java.awt.Color;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Comparator;
    import java.util.HashSet;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Queue;
    import java.util.Set;
    import javax.swing.JOptionPane;


    import shapes.Circle;
    import shapes.Rectangle;


    /**
     * This class represents a puzzle simulator with tiles, including initial and final boards,
     * movable tiles, holes, and glue. It allows operations such as adding, removing, moving tiles,
     * and applying or removing glue.
     *
     * @author Andersson David Sánchez Méndez
     * @author Cristian Santiago Pedraza Rodríguez   

    * @version 2024
    */


    public class Puzzle {   

        // Height and width of the board
        private int h;
        private int w;
        
        // Visual representations of the initial and final boards
        private Rectangle startingBoard;
        private Rectangle endingBoard;
        
        // Board color
        private Color color;
        
        // Character matrices representing the initial and target state of the board
        private char[][] starting;
        private char[][] ending;
        
        // Lists of lists of tiles representing the current board state and reference state
        private List<List<BaseTile>> tiles; // List of lists of tiles
        private List<List<BaseTile>> referingTiles; // List of lists of reference tiles
        
        // Determines if the simulator is visible
        private boolean visible = true;
        
        // Tracks if the last action was successful
        private boolean ok = true;
        
        // Auxiliary circle to represent holes
        private Circle circle;
        
        // Default board color
        public static Color lightBrown = new Color(207, 126, 60);
        
        // Matrix to track holes and list to store hole circles
        private boolean[][] holes;
        private List<Circle> holeCircles;
        
        // Queue that will be useful to store tiles with fragileGlue (reason because of tilt), after tilt, all tiles must remove glue 
        private ArrayList<BaseTile> fragileGlueTilesQueue;
        private ArrayList<BaseTile> wildTiles;
        
        /**
         * Constructor to initialize the boards without initial and final matrices.
         * 
         * @param h Height of the board.
         * @param w Width of the board.
         * @throws PuzzleExceptions.ExceedPuzzleSpaceException if the specified height or width exceeds 500.
         * @throws PuzzleExceptions.ConstructorsExceptions if the specified height or width is less than 1.
         */
        public Puzzle(int h, int w) throws puzzle.PuzzleExceptions.ExceedPuzzleSpaceException,puzzle.PuzzleExceptions.ConstructorsExceptions {
            try {
                if (h < 1 || w < 1) throw new PuzzleExceptions.ConstructorsExceptions(PuzzleExceptions.ConstructorsExceptions.NO_BOARDS_HW_NEGATIVE);
                if (h > 500 || w > 500) throw new PuzzleExceptions.ExceedPuzzleSpaceException (PuzzleExceptions.ExceedPuzzleSpaceException.EXCEED_PUZZLE_SPACE);
                this.h = h; 
                this.w = w;
                this.color = lightBrown;
                this.tiles = new ArrayList<>();
                this.referingTiles = new ArrayList<>();
                
                // Initialize the hole matrix and the list of circles 
                holeCircles = new ArrayList<>();
                holes = new boolean[h][w];
                
                
                // Initialize Fragile glue tiles matrix
                fragileGlueTilesQueue = new ArrayList<>();
                
                // Initialize WildTiles
                wildTiles = new ArrayList<>();
                
                // Create the initial and final boards
                startingBoard = new Rectangle(h,w,100,50,"starting");
                endingBoard = new Rectangle(h,w,350,50,"ending"); 
                
                // Create empty char list to be used as board argument to create both boards empty
                char[][] emptyCharList = createCharEmptyList();
                createTiles(emptyCharList, tiles, true);
                createTiles(emptyCharList, referingTiles, false);
                this.ok = true;
            } catch (PuzzleExceptions.ConstructorsExceptions e){                
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e;
            }    
            catch (PuzzleExceptions.ExceedPuzzleSpaceException e){                
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e;
            }       
        }
        

        /**
         * Constructor to initialize the boards with initial and final character matrices.
         * 
         * @param starting Initial state of the board.
         * @param ending Final state of the board.
         */
        public Puzzle(char[][] starting, char[][] ending) throws puzzle.PuzzleExceptions.ConstructorsExceptions {
            try {
                if(starting == null || ending == null) throw new PuzzleExceptions.ConstructorsExceptions(PuzzleExceptions.ConstructorsExceptions.NO_STARTING_ENDING_NULL); 
                this.h = starting.length;
                this.w = starting[0].length;
                this.starting = starting;
                this.ending = ending;
                this.tiles = new ArrayList<>();
                this.referingTiles = new ArrayList<>();
                this.color = lightBrown;
            
                // Initialize the hole matrix and the list of circles
                holeCircles = new ArrayList<>();
                holes = new boolean[h][w];
                
                // Initialize Fragile glue tiles matrix
                fragileGlueTilesQueue = new ArrayList<>();
                
                // Initialize WildTiles
                wildTiles = new ArrayList<>();
            
                // Create the boards
                startingBoard = new Rectangle(h, w, 100, 50, "starting");
                endingBoard = new Rectangle(h, w, 350, 50, "ending");
                
                // Create tiles based on the initial and final matrices
                createTiles(starting, tiles, true); // Initial position of the initial tiles
                createTiles(ending, referingTiles, false); // Initial position of the reference board
                
                this.ok = true;
            
            }catch(PuzzleExceptions.ConstructorsExceptions e){
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e;
            }
        }
        
        
        /**
         * Constructor to initialize an empty initial board and a final board with tiles.
         * 
         * @param ending Final state of the board.
         */
        public Puzzle(char[][] ending) throws puzzle.PuzzleExceptions.ConstructorsExceptions {
            try {
                if(ending == null) throw new PuzzleExceptions.ConstructorsExceptions(PuzzleExceptions.ConstructorsExceptions.NO_ENDING_NULL);
                this.h = ending.length;
                this.w = ending[0].length;
                this.ending = ending;
                this.tiles = new ArrayList<>();
                this.referingTiles = new ArrayList<>();
                this.color = lightBrown;
               
                
                // Initialize the hole matrix and the list of circles
                holeCircles = new ArrayList<>();
                holes = new boolean[h][w];
                
                // Initialize Fragile glue tiles matrix
                fragileGlueTilesQueue = new ArrayList<>();
                
                // Initialize WildTiles
                wildTiles = new ArrayList<>();
                
                // Create the boards
                startingBoard = new Rectangle(h,w,100,50,"starting");
                endingBoard = new Rectangle(h, w, 350, 50, "ending");
                
                // Create empty char list, it is going to be used as emptyCharList
                char[][] emptyCharList = createCharEmptyList();
                // Create empty tiles for starting board
                createTiles(emptyCharList, tiles, true);
                // Create tiles for ending board
                createTiles(ending, referingTiles, false);
                
                this.ok = true;
            
            
            }catch(PuzzleExceptions.ConstructorsExceptions e){
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e;
            }
        }
        

        /**
         *Method to create a empty char list of lists representation
        * @return a char[][] filled with '*'.
        */
        private char[][] createCharEmptyList(){
            char[][] emptyCharList = new char[h][w];
            for(int row = 0;row < h;row++){
                for (int col = 0; col < w; col++){
                    emptyCharList[row][col] = '*';
                }
            }
            return emptyCharList;
        }
        
        /**
         * Method to create tiles in a list of lists, given the reference board.
         * 
         * @param board Character matrix representing the board.
         * @param tileList List of lists to store the created tiles.
         * @param startingBoard boolean value to determinate if it is the starting or ending board
         */
        private void createTiles(char[][] board ,List<List<BaseTile>> tileList, boolean startingBoard) {
            int xOffset = 105;
            int yOffset = 55;
            // if it's not starting board, it is going to have a ending coordinates for x
            if (startingBoard == false){
                xOffset = w * (Tile.SIZE + Tile.MARGIN) + 355;              
            }
            for (int row = 0; row < h; row++) {
                List<BaseTile> rowList = new ArrayList<>();
                for (int col = 0; col < w; col++) {
                    char label = board[row][col];
                    int xPosition = xOffset + (col * (Tile.SIZE + Tile.MARGIN));
                    int yPosition = yOffset + (row * (Tile.SIZE + Tile.MARGIN));
                    Tile tile = new Tile(label, xPosition, yPosition, row, col);
                    rowList.add(tile);
                }
                tileList.add(rowList);
            }
        }

        /**
         * Gets the height of the board.
         * 
         * @return Height of the board.
         */
        public int getHeight() {
            return this.h;
        }

        
        /**
         * Gets the width of the board.
         * 
         * @return Width of the board.
         */
        public int getWidth() {
            return this.w;
        }

        
        /**
         * This method takes a string formatted as "[type label]", where the first character(s)
         * represent the type of tile and the second part is the label of the tile.
         *
         * @param tileData A string containing the type and label of the tile, formatted as "[type label]".
         * @param xPosition The x-coordinate position of the tile.
         * @param yPosition The y-coordinate position of the tile.
         * @param row The row index of the tile in the puzzle grid.
         * @param column The column index of the tile in the puzzle grid.
         * @return An instance of a subclass of BaseTile based on the type specified in tileData.
         *         If the type is unrecognized, a default Tile instance is returned.
         *
         */
        private BaseTile createTile(String tileData, int xPosition, int yPosition, int row, int column) {
            String[] tileInformation = tileData.split(" ");
            String kindOfTile = tileInformation[0];
            char label = tileInformation[1].charAt(0);

            switch (kindOfTile) {
                case "fi":  // Example label for FixedTile
                    return new FixedTile(label, xPosition, yPosition, row, column);
                case "ro":  // Example label for RoughTile
                    return new RoughTile(label, xPosition, yPosition, row, column);
                case "fr":  // Example label for FreelanceTile
                    return new FreelanceTile(label, xPosition, yPosition, row, column);
                case "fl":  // Example label for FlyingTile
                    return new FlyingTile(label, xPosition, yPosition, row, column);
                case "wi":  // Example label for WildTile
                    return new WildTile(label,xPosition, yPosition, row, column);
                
                default:
                    return new Tile(label, xPosition, yPosition, row, column); // Instance of a normal Tile
            }
        }



        /**
         * Adds a tile to the board at the specified position.
         * 
         * @param row Row index of the tile.
         * @param column Column index of the tile.
         * @param label Label of the tile.
         * @throws PuzzleExceptions.addDeleteTileExceptions if the tile data is invalid or if the position is invalid.
         * @throws PuzzleExceptions.ExceedPuzzleSpaceException if the specified position exceeds the puzzle dimensions.  
         */
        public void addTile(int row, int column, char label) throws PuzzleExceptions.addDeleteTileExceptions, PuzzleExceptions.ExceedPuzzleSpaceException  {
            try {
                // List of valid labels for the tiles
                char[] validLabels = {'r', 'g', 'b', 'y'};
                boolean isValidLabel = false;
                
                //Validate if the label is valid
                for (char validLabel : validLabels) {
                    if (label == validLabel) {
                        isValidLabel = true;
                        break;
                    }
                }
                 // If the label is invalid, show an error message
                if (!isValidLabel) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_VALID_LABEL);
                }
                
                 // Other validations for the position
                if (row >= h || column >= w) {
                    throw new PuzzleExceptions.ExceedPuzzleSpaceException(PuzzleExceptions.ExceedPuzzleSpaceException.EXCEED_PUZZLE_SPACE);
                    
                } else if (row < 0 || column < 0) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_NEGATIVE_POSITION_TILE);
                }
        
                BaseTile previousTile = tiles.get(row).get(column);
                
                if (previousTile.getLabel() == 'h') {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_ADD_HOLE_TILE);
                    
                } else if (!isTileEmpty(previousTile)) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.TILE_OCCUPIED);
                }
        
                previousTile.setTileColor(label); //Change the color of the tile
                previousTile.setLabel(label);
                previousTile.makeVisible();
                this.ok = true;
        
            } catch (PuzzleExceptions.addDeleteTileExceptions | PuzzleExceptions.ExceedPuzzleSpaceException e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false;
                throw e;
                    
            }
        }
        
        
        /**
         * Adds a tile to the board at the specified position using tile data in string format.
         * This method is overloaded to allow the creation of different types of tiles.
         *
         * @param row Row index of the tile.
         * @param column Column index of the tile.
         * @param tileData A string containing the type and label of the tile, formatted as "[type label]".
         * @throws PuzzleExceptions.addDeleteTileExceptions if the tile data is invalid or if the position is invalid.
         * @throws PuzzleExceptions.ExceedPuzzleSpaceException if the specified position exceeds the puzzle dimensions.
         */
        public void addTile(int row, int column, String tileData) throws PuzzleExceptions.addDeleteTileExceptions, PuzzleExceptions.ExceedPuzzleSpaceException {
            try {
                // Validate row and column boundaries
                if (row >= h || column >= w) {
                    throw new PuzzleExceptions.ExceedPuzzleSpaceException(PuzzleExceptions.ExceedPuzzleSpaceException.EXCEED_PUZZLE_SPACE);
                } else if (row < 0 || column < 0) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_NEGATIVE_POSITION_TILE);
                }
                
                // Validate tileData for valid tile types
                String tileType = tileData.substring(0, 2);
                if (!tileType.equals("fr") && !tileType.equals("fl") && !tileType.equals("ro") && !tileType.equals("fi") && !tileType.equals("wi")) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_EXISTENT_TYPE_TILE);
                }
                String tileLabel = tileData.substring(3,4);
                if(!tileLabel.equals("y") && !tileLabel.equals("r") && !tileLabel.equals("b") && !tileLabel.equals("g")){
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_VALID_LABEL);
                }
                
                BaseTile currentTile = tiles.get(row).get(column);

                if (currentTile.getLabel() == 'h') {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_ADD_HOLE_TILE);
                } else if (!isTileEmpty(currentTile)) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.TILE_OCCUPIED);
                }
                
                currentTile.setTileColor('*'); //Change the color of the tile
                currentTile.setLabel('*');
                currentTile.makeInvisible();
                
                int xPosition = 105 + (column * (Tile.SIZE + Tile.MARGIN));
                int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));

                // Create the appropriate tile type using the factory method
                BaseTile newTile = createTile(tileData, xPosition, yPosition, row, column);
                
                // Replace the current tile in the tiles list
                tiles.get(row).set(column, newTile);
                
                // Set the label based on the tile type
                if (tileType.equals("fr")) newTile.setLabel('l');
                if (tileType.equals("fl")) newTile.setLabel('f');
                if (tileType.equals("ro")) newTile.setLabel('o');
                if (tileType.equals("fi")) newTile.setLabel('x');
                if(tileData.substring(0, 2).equals("wi")) {
                    newTile.setLabel('w');
                    // Add the wildTile we added
                    wildTiles.add(newTile);
                }

                this.ok = true;

            } catch (PuzzleExceptions.addDeleteTileExceptions | PuzzleExceptions.ExceedPuzzleSpaceException e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false;
                throw e;
            }
        }

        

        /**
         * Removes a tile from the board at the specified position.
         * 
         * @param row Row index of the tile.
         * @param column Column index of the tile.
         * @throws PuzzleExceptions.addDeleteTileExceptions if the tile cannot be deleted due to specific conditions.
         * @throws PuzzleExceptions.ExceedPuzzleSpaceException if the specified position exceeds the puzzle dimensions.
         */
        public void deleteTile(int row, int column) throws PuzzleExceptions.addDeleteTileExceptions, PuzzleExceptions.ExceedPuzzleSpaceException {
            try {
                // Validate row and column boundaries
                if (row >= h || column >= w) {
                    throw new PuzzleExceptions.ExceedPuzzleSpaceException(PuzzleExceptions.ExceedPuzzleSpaceException.EXCEED_PUZZLE_SPACE);
                } else if (row < 0 || column < 0) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_NEGATIVE_POSITION_TILE);
                }

                BaseTile previousTile = tiles.get(row).get(column);

                // Check if the tile has a hole
                if (previousTile.getLabel() == 'h') {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_DELETE_HOLE_TILE);
                } else if (previousTile.hasGlue() || previousTile.isStuck()) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_DELETE_TILE_GLUE);
                } else if (previousTile instanceof FixedTile) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_DELETE_FIXED_TILE);
                } else if (previousTile instanceof WildTile ){
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_DELETE_WILD_TILE);
                }else if (isTileEmpty(previousTile)) {
                    throw new PuzzleExceptions.addDeleteTileExceptions(PuzzleExceptions.addDeleteTileExceptions.NOT_DELETE_NON_EXISTENT_TILE);
                }

                // If all checks pass, delete the tile
                previousTile.setTileColor('n');
                previousTile.setLabel('*');
                previousTile.makeInvisible();
                this.ok = true; // Successful action

            } catch (PuzzleExceptions.addDeleteTileExceptions | PuzzleExceptions.ExceedPuzzleSpaceException e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e; // Re-throw the exception for further handling
            }
        }


        /**
         * Relocates a tile from the given source position to the destination position.
         * 
         * @param from the coordinates of the source position as an integer array [row, col].
         * @param to   the coordinates of the destination position as an integer array [row, col].
         * @throws PuzzleExceptions.relocateTileExceptions if the relocation cannot be performed due to specific conditions.
         */
        public void relocateTile(int[] from, int[] to) throws PuzzleExceptions.relocateTileExceptions {
            try {
                // Validate input coordinates
                if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.INVALID_COORDINATES);
                }

                BaseTile fromTile = tiles.get(from[0]).get(from[1]);
                BaseTile toTile = tiles.get(to[0]).get(to[1]);

                // Validate existence of the source tile and availability of the destination tile
                if (fromTile.getLabel() == 'h') {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.NOT_MOVE_HOLE_TILE);
                } else if (toTile.getLabel() == 'h') {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.NOT_RELOCATE_TILE_HOLE);
                } else if (isTileEmpty(fromTile)) {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.NOT_MOVE_NON_EXISTENT_TILE);
                } else if (!isTileEmpty(toTile)) {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.NOT_RELOCATE_TILE_OCCUPIED);
                } else if (fromTile.hasGlue() || fromTile.isStuck()) {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.NOT_MOVE_TILE_GLUE);
                } else if (fromTile instanceof FixedTile) {
                    throw new PuzzleExceptions.relocateTileExceptions(PuzzleExceptions.relocateTileExceptions.NOT_RELOCATE_FIXED_TILE);
                }
                // Look for validation to wild Tile
                
                // Perform the movement
                relocateTileMovement(fromTile, toTile, from, to);
                this.ok = true; // Successful action

            } catch (PuzzleExceptions.relocateTileExceptions e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e; // Re-throw the exception for further handling
            }
        }


        /**
         * Performs the visual and logical movement of a tile from one position to another.
         * 
         * @param fromTile the tile to be moved from the source position.
         * @param toTile   the tile at the destination position (empty).
         * @param from     the source coordinates as an integer array [row, col].
         * @param to       the destination coordinates as an integer array [row, col].
         */
        private void relocateTileMovement(BaseTile fromTile, BaseTile toTile, int[] from, int[] to) {
            // Visually move the tile by updating its position
            fromTile.moveHorizontal((to[1] - from[1]) * (Tile.SIZE + Tile.MARGIN));
            fromTile.moveVertical((to[0] - from[0]) * (Tile.SIZE + Tile.MARGIN));

            // Update the tile list: move the tile to the new position
            tiles.get(to[0]).set(to[1], fromTile);

            // Update the tile's internal position
            fromTile.setRow(to[0]);
            fromTile.setCol(to[1]);

            // Create a new empty tile at the original position
            Tile emptyTile = createEmptyTile(from[0], from[1]);
            tiles.get(from[0]).set(from[1], emptyTile);
        }
        
        /**
         * Validates whether the given coordinates are within the bounds of the board.
         * 
         * @param coords the coordinates to validate as an integer array [row, col].
         * @return {@code true} if the coordinates are valid; {@code false} otherwise.
         */
        private boolean areValidCoordinates(int[] coords) {
            return coords.length == 2 && coords[0] >= 0 && coords[0] < h && coords[1] >= 0 && coords[1] < w;
        }  


        /**
         * Applies glue to a tile at the specified position.
         *
         * @param row the row of the tile.
         * @param column the column of the tile.
         * @throws PuzzleExceptions.addDeleteGlueExceptions if the glue cannot be applied due to specific conditions.
         */
        public void addGlue(int row, int column) throws PuzzleExceptions.addDeleteGlueExceptions {
            try {
                BaseTile tile = getTileAtPosition(row, column);
                
                if (tile == null) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.INVALID_POSITION);
                    
                } else if (tile.getLabel() == 'h') {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_ADD_GLUE_HOLE);
                    
                } else if (isTileEmpty(tile)) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_ADD_GLUE_EMPTY_TILE);
                    
                } else if (tile.hasGlue()) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_GLUE_EXISTING_TILE);
                    
                } else if (tile instanceof FreelanceTile) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_GLUE_FREELANCE_TILE);
                }

                // Proceed to apply glue
                tile.setHasGlue(true);

                // Change the tile color to a paler version
                Color evenPalerColor = getPaleColor(tile.getOriginalColor(), 150);
                tile.setTileColor(evenPalerColor);

                // Update adjacent tiles
                updateAdjacentTiles(tile);

                // Collect the group of stuck tiles
                List<BaseTile> group = new ArrayList<>();
                collectStuckGroup(tile, group);

                // Reset visited flags
                resetVisitedFlags();
                tile.setLabel('p');
                this.ok = true; // Successful action

            } catch (PuzzleExceptions.addDeleteGlueExceptions e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e; // Re-throw the exception for further handling
            }
        }

        
        /**
         * Applies glue to a tile at the specified position.
         *
         * @param row      the row of the tile.
         * @param column   the column of the tile.
         * @param glueType the type of glue ("normal" or "super").
         * @throws PuzzleExceptions.addDeleteGlueExceptions if the glue cannot be applied due to specific conditions.
         */
        public void addGlue(int row, int column, String glueType) throws PuzzleExceptions.addDeleteGlueExceptions {
            try {
                BaseTile tile = getTileAtPosition(row, column);
                
                // Validate glue type
                if (!glueType.equalsIgnoreCase("fragile") && !glueType.equalsIgnoreCase("super")) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_VALID_TYPES_GLUE);
                }

                if (tile == null) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.INVALID_POSITION);
                    
                } else if (tile.getLabel() == 'h') {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_ADD_GLUE_HOLE);
                    
                } else if (isTileEmpty(tile)) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_ADD_GLUE_EMPTY_TILE);
                    
                } else if (tile.hasGlue() || tile.hasSuperGlue() || tile.hasFragileGlue() ){
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_GLUE_EXISTING_TILE);
                    
                } else if (tile instanceof FreelanceTile) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_GLUE_FREELANCE_TILE);
                }
                
                tile.setHasGlue(true);
                // Change tile color if glue is normal                
                Color evenPalerColor = getPaleColor(tile.getOriginalColor(), 150);
                tile.setTileColor(evenPalerColor);
                tile.setLabel('p');
                   
                //Determine glue type
                   
                if (glueType.equalsIgnoreCase("super")) {                                          
                   tile.setSuperGlue(true);                                              
                }
                
                
                if (glueType.equalsIgnoreCase("fragile")) {
                       tile.setFragileGlue(true);
                       // Add tiles if glue is fragile
                       fragileGlueTilesQueue.add(tile);
                }                        
                
                this.ok = true; 

            } catch (PuzzleExceptions.addDeleteGlueExceptions e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e; // Re-throw the exception for further handling
            }
        }



        /**
         * Auxiliary method to verify if the tile has superGlue.
         *
         * @param tile Tile to verify.
         * @return true if the tile has superGlue, false otherwise.
         */
        private boolean hasSuperGlue (BaseTile tile) {
            if (tile instanceof BaseTile) {
                return ((BaseTile) tile).hasSuperGlue();
            }
            return false;
        }
        

        /**
         * Removes glue from a tile at the specified position.
         * 
         * @param row the row of the tile.
         * @param column the column of the tile.
         * @throws PuzzleExceptions.addDeleteGlueExceptions if the glue cannot be removed due to specific conditions.
         */
        public void deleteGlue(int row, int column) throws PuzzleExceptions.addDeleteGlueExceptions {
            try {
                BaseTile tile = getTileAtPosition(row, column);
                
                if (tile == null) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.INVALID_POSITION);
                    
                } else if (tile.getLabel() == 'h') {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_DELETE_GLUE_HOLE);
                    
                } else if (isTileEmpty(tile)) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_DELETE_GLUE_EMPTY_TILE);
                    
                } else if (!tile.hasGlue()) {
                    throw new PuzzleExceptions.addDeleteGlueExceptions(PuzzleExceptions.addDeleteGlueExceptions.NOT_GLUE_TO_REMOVE);
                }

                // Proceed to remove glue
                tile.setHasGlue(false);
                
                if(tile.hasSuperGlue()) tile.setSuperGlue(false);
                if(tile.hasFragileGlue()) tile.setFragileGlue(false);

                // If the tile is no longer stuck to any other, adjust its color
                if (!tile.isStuck()) {
                    Color slightlyPalerColor = getPaleColor(tile.getOriginalColor(), 50);
                    tile.setTileColor(slightlyPalerColor);
                }

                // Update adjacent tiles
                updateAdjacentTilesAfterGlueRemoval(tile);

                // Collect the group of stuck tiles to update states
                List<BaseTile> group = new ArrayList<>();
                collectStuckGroup(tile, group);

                // Reset visited flags
                resetVisitedFlags();
                
                this.ok = true; // Successful action

            } catch (PuzzleExceptions.addDeleteGlueExceptions e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e; // Re-throw the exception for further handling
            }
        }


        /**
         * Updates the adjacent tiles after applying glue to a tile.
         * 
         * @param tile the tile to which glue has been applied.
         */
        private void updateAdjacentTiles(BaseTile tile) {
            int row = tile.getRow();
            int column = tile.getCol();
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
            for (int[] dir : directions) {
                int adjRow = row + dir[0];
                int adjCol = column + dir[1];
                BaseTile adjacentTile = getTileAtPosition(adjRow, adjCol);

                if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.isStuck()
                        && !adjacentTile.getIsHole()) {
                    adjacentTile.setIsStuck(true);
                    // Change the color to a paler version
                    Color paleColor = getPaleColor(adjacentTile.getOriginalColor(), 100);
                    adjacentTile.setTileColor(paleColor);
                }
            }
        }


        /**
         * Updates the adjacent tiles after removing glue from a tile.
         * 
         * @param tile the tile from which glue has been removed.
         */
        private void updateAdjacentTilesAfterGlueRemoval(BaseTile tile) {
            int row = tile.getRow();
            int column = tile.getCol();
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
            for (int[] dir : directions) {
                int adjRow = row + dir[0];
                int adjCol = column + dir[1];
                BaseTile adjacentTile = getTileAtPosition(adjRow, adjCol);

                if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.getIsHole()) {
                    if (isAdjacentToGlue(adjacentTile)) {
                        // If still adjacent to another tile with glue, remain stuck
                        continue;
                    } else {
                        adjacentTile.setIsStuck(false);
                        if (!adjacentTile.hasGlue()) {
                            // Change the color to a slightly paler version
                            Color slightlyPalerColor = getPaleColor(adjacentTile.getOriginalColor(), 50);
                            adjacentTile.setTileColor(slightlyPalerColor);
                        }
                    }
                }
            }
        }

        /**
         * Checks if a tile is adjacent to any tile with glue applied.
         * 
         * @param tile the tile to check.
         * @return {@code true} if adjacent to a tile with glue; {@code false} otherwise.
         */
        private boolean isAdjacentToGlue(BaseTile tile) {
            int row = tile.getRow();
            int column = tile.getCol();
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

            for (int[] dir : directions) {
                int adjRow = row + dir[0];
                int adjCol = column + dir[1];
                BaseTile adjacentTile = getTileAtPosition(adjRow, adjCol);
                if (adjacentTile != null && adjacentTile.hasGlue()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Generates a paler version of the given color based on the paleness factor.
         * 
         * @param color the original color.
         * @param palenessFactor the amount to increase each RGB component.
         * @return a new {@code Color} object with adjusted RGB values.
         */
        private Color getPaleColor(Color color, int palenessFactor) {
            int r = Math.min(255, color.getRed() + palenessFactor);
            int g = Math.min(255, color.getGreen() + palenessFactor);
            int b = Math.min(255, color.getBlue() + palenessFactor);
            return new Color(r, g, b);
        }

        /**
         * Tilts the board in the specified direction.
         * 
         * @param direction the direction to tilt ('d' for down, 'u' for up, 'r' for right, 'l' for left).
         * @throws PuzzleExceptions.addDeleteGlueExceptions  Validate fragile glue when tilt
         */
        public void tilt(char direction) throws PuzzleExceptions.addDeleteGlueExceptions  {
            try {
                switch (direction) {
                    case 'd':
                        for (int col = 0; col < w; col++) {
                            tiltDownWithGlue(col);
                        }
                        this.ok = true;
                        break;
                        
                    case 'u':
                        for (int col = 0; col < w; col++) {
                            tiltUpWithGlue(col);
                        }
                        this.ok = true;
                        break;
                        
                    case 'r':
                        for (int row = 0; row < h; row++) {
                            tiltRightWithGlue(row);
                        }
                        this.ok = true;
                        break;
                        
                    case 'l':
                        for (int row = 0; row < h; row++) {
                            tiltLeftWithGlue(row);
                        }
                        this.ok = true;
                        break;
                        
                    default:
                        throw new PuzzleExceptions.tiltException(PuzzleExceptions.tiltException.INVALID_DIRECTION);
                }
                resetVisitedFlags(); // Reset visited flags after tilting
                
                if (!fragileGlueTilesQueue.isEmpty()) {
                    for (BaseTile tile : fragileGlueTilesQueue) {
                    this.deleteGlue(tile.getRow(), tile.getCol());
                    }
                    fragileGlueTilesQueue.clear();    
                }
                
                if (!wildTiles.isEmpty()) {                                    
                    for (BaseTile tile : wildTiles) {
                    if (tile instanceof WildTile) {
                       // Process specified elements WildTile
                       ((WildTile) tile).setRandowColor();
                    }                        
                    }                
                }
                
        
            } catch (PuzzleExceptions.tiltException e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false;
                
            }
        }
        

        /**
         * Performs a single tilt in the specified direction.
         * 
         * @param direction the direction to tilt ('d' for down, 'u' for up, 'r' for right, 'l' for left).
         * @return {@code true} if there were changes; {@code false} otherwise.
         * @throws PuzzleExceptions.addDeleteGlueExceptions tilt controlled once
         */
        public boolean tiltOnce(char direction) throws PuzzleExceptions.addDeleteGlueExceptions {
            // Implement a version of tilt that returns true if there were changes
            // and false if there were no changes.
            // For simplicity, it currently always returns true.
            tilt(direction);
            return true; 
        }


        /**
         * Tilts the specified column upwards, handling glue and stuck tiles.
         * 
         * @param col the column to tilt upwards.
         */
        private void tiltUpWithGlue(int col) {
            List<List<BaseTile>> groups = new ArrayList<>();
            List<BaseTile> flyingTiles = new ArrayList<>();

            for (int row = 0; row < h; row++) {
                BaseTile tile = getTileAtPosition(row, col);
                if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole() && !(tile instanceof RoughTile)) {
                    if (tile instanceof FlyingTile) {
                        tile.setVisited(true);
                        flyingTiles.add(tile);
                    } else {
                        List<BaseTile> group = new ArrayList<>();
                        boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                        if (isGluedOrStuck) {
                            collectStuckGroup(tile, group);
                        } else {
                            tile.setVisited(true);
                            group.add(tile);
                        }
                        groups.add(group);
                    }
                }
            }

            // Reset visited flags
            resetVisitedFlags();

            // First, process the flying tiles
            for (BaseTile tile : flyingTiles) {
                int maxMove = calculateMaxMoveUpFlyingTile(tile);
                moveTileUp(tile, maxMove);
            }

            // Sort groups by the minimum row (upper tiles first)
            groups.sort(Comparator.comparingInt(g -> g.stream().mapToInt(BaseTile::getRow).min().orElse(h)));

            // Move the groups
            for (List<BaseTile> group : groups) {
                boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
                int maxMove = calculateMaxMoveUpGroup(group, isGluedOrStuck);
                if (maxMove == -1) {
                    if (!isGluedOrStuck) {
                        // Remove free tiles that fall into a hole
                        for (BaseTile tile : group) {
                            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                            tile.makeInvisible();
                            tile.setLabel('*');
                        }
                    }
                    // Glued or stuck tiles do not move
                } else {
                    moveGroupUp(group, maxMove);
                }
            }
        }


        /**
         * Tilts the specified column downwards, handling glue and stuck tiles.
         * 
         * @param col the column to tilt downwards.
         */
        private void tiltDownWithGlue(int col) {
            List<List<BaseTile>> groups = new ArrayList<>();
            List<BaseTile> flyingTiles = new ArrayList<>();

            for (int row = h - 1; row >= 0; row--) {
                BaseTile tile = getTileAtPosition(row, col);
                if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole() && !(tile instanceof RoughTile)) {
                    if (tile instanceof FlyingTile) {
                        tile.setVisited(true);
                        flyingTiles.add(tile);
                    } else {
                        List<BaseTile> group = new ArrayList<>();
                        boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                        if (isGluedOrStuck) {
                            collectStuckGroup(tile, group);
                        } else {
                            tile.setVisited(true);
                            group.add(tile);
                        }
                        groups.add(group);
                    }
                }
            }

            // Reset visited flags
            resetVisitedFlags();

            // First, process the flying tiles
            for (BaseTile tile : flyingTiles) {
                int maxMove = calculateMaxMoveDownFlyingTile(tile);
                moveTileDown(tile, maxMove);
            }

            // Sort groups by the maximum row (lower tiles first)
            groups.sort((g1, g2) -> {
                int maxRow1 = g1.stream().mapToInt(BaseTile::getRow).max().orElse(-1);
                int maxRow2 = g2.stream().mapToInt(BaseTile::getRow).max().orElse(-1);
                return Integer.compare(maxRow2, maxRow1);
            });

            // Move the groups
            for (List<BaseTile> group : groups) {
                boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
                int maxMove = calculateMaxMoveDownGroup(group, isGluedOrStuck);
                if (maxMove == -1) {
                    if (!isGluedOrStuck) {
                        // Remove free tiles that fall into a hole
                        for (BaseTile tile : group) {
                            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                            tile.makeInvisible();
                            tile.setLabel('*');
                        }
                    }
                    // Glued or stuck tiles do not move
                } else {
                    moveGroupDown(group, maxMove);
                }
            }
        }

        /**
         * Tilts the specified row to the left, handling glue and stuck tiles.
         * 
         * @param row the row to tilt to the left.
         */
        private void tiltLeftWithGlue(int row) {
            List<List<BaseTile>> groups = new ArrayList<>();
            List<BaseTile> flyingTiles = new ArrayList<>();

            for (int col = 0; col < w; col++) {
                BaseTile tile = getTileAtPosition(row, col);
                if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole() && !(tile instanceof RoughTile)) {
                    if (tile instanceof FlyingTile) {
                        tile.setVisited(true);
                        flyingTiles.add(tile);
                    } else {
                        List<BaseTile> group = new ArrayList<>();
                        boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                        if (isGluedOrStuck) {
                            collectStuckGroup(tile, group);
                        } else {
                            tile.setVisited(true);
                            group.add(tile);
                        }
                        groups.add(group);
                    }
                }
            }

            // Reset visited flags
            resetVisitedFlags();

            // First, process the flying tiles
            for (BaseTile tile : flyingTiles) {
                int maxMove = calculateMaxMoveLeftFlyingTile(tile);
                moveTileLeft(tile, maxMove);
            }

            // Sort groups by the minimum column (leftmost tiles first)
            groups.sort(Comparator.comparingInt(g -> g.stream().mapToInt(BaseTile::getCol).min().orElse(w)));

            // Move the groups
            for (List<BaseTile> group : groups) {
                boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
                int maxMove = calculateMaxMoveLeftGroup(group, isGluedOrStuck);
                if (maxMove == -1) {
                    if (!isGluedOrStuck) {
                        // Remove free tiles that fall into a hole
                        for (BaseTile tile : group) {
                            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                            tile.makeInvisible();
                            tile.setLabel('*');
                        }
                    }
                    // Glued or stuck tiles do not move
                } else {
                    moveGroupLeft(group, maxMove);
                }
            }
        }



        /**
         * Tilts the specified row to the right, considering glue and stuck tiles.
         * 
         * @param row the row index to tilt to the right.
         */
        private void tiltRightWithGlue(int row) {
            List<List<BaseTile>> groups = new ArrayList<>();
            List<BaseTile> flyingTiles = new ArrayList<>();

            for (int col = w - 1; col >= 0; col--) {
                BaseTile tile = getTileAtPosition(row, col);
                if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole() && !(tile instanceof RoughTile)) {
                    if (tile instanceof FlyingTile) {
                        tile.setVisited(true);
                        flyingTiles.add(tile);
                    } else {
                        List<BaseTile> group = new ArrayList<>();
                        boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                        if (isGluedOrStuck) {
                            collectStuckGroup(tile, group);
                        } else {
                            tile.setVisited(true);
                            group.add(tile);
                        }
                        groups.add(group);
                    }
                }
            }

            // Reset visited flags
            resetVisitedFlags();

            // First, process the flying tiles
            for (BaseTile tile : flyingTiles) {
                int maxMove = calculateMaxMoveRightFlyingTile(tile);
                moveTileRight(tile, maxMove);
            }

            // Sort groups by the maximum column (rightmost tiles first)
            groups.sort((g1, g2) -> {
                int maxCol1 = g1.stream().mapToInt(BaseTile::getCol).max().orElse(-1);
                int maxCol2 = g2.stream().mapToInt(BaseTile::getCol).max().orElse(-1);
                return Integer.compare(maxCol2, maxCol1);
            });

            // Move the groups
            for (List<BaseTile> group : groups) {
                boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
                int maxMove = calculateMaxMoveRightGroup(group, isGluedOrStuck);
                if (maxMove == -1) {
                    if (!isGluedOrStuck) {
                        // Remove free tiles that fall into a hole
                        for (BaseTile tile : group) {
                            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                            tile.makeInvisible();
                            tile.setLabel('*');
                        }
                    }
                    // Glued or stuck tiles do not move
                } else {
                    moveGroupRight(group, maxMove);
                }
            }
        }

        private int calculateMaxMoveUpFlyingTile(BaseTile tile) {
            int row = tile.getRow();
            int col = tile.getCol();
            int maxMove = 0;
            for (int i = row - 1; i >= 0; i--) {
                BaseTile nextTile = getTileAtPosition(i, col);
                if (isTileEmpty(nextTile) || nextTile.getIsHole()) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        private void moveTileUp(BaseTile tile, int steps) {
            if (steps == 0) return;
            // Remove tile from current position
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            // Move tile
            int newRow = tile.getRow() - steps;
            tile.moveVertical(-steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }

        private int calculateMaxMoveDownFlyingTile(BaseTile tile) {
            int row = tile.getRow();
            int col = tile.getCol();
            int maxMove = 0;
            for (int i = row + 1; i < h; i++) {
                BaseTile nextTile = getTileAtPosition(i, col);
                if (isTileEmpty(nextTile) || nextTile.getIsHole()) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        private void moveTileDown(BaseTile tile, int steps) {
            if (steps == 0) return;
            // Remove tile from current position
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            // Move tile
            int newRow = tile.getRow() + steps;
            tile.moveVertical(steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }

        private int calculateMaxMoveLeftFlyingTile(BaseTile tile) {
            int row = tile.getRow();
            int col = tile.getCol();
            int maxMove = 0;
            for (int i = col - 1; i >= 0; i--) {
                BaseTile nextTile = getTileAtPosition(row, i);
                if (isTileEmpty(nextTile) || nextTile.getIsHole()) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        private void moveTileLeft(BaseTile tile, int steps) {
            if (steps == 0) return;
            // Remove tile from current position
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            // Move tile
            int newCol = tile.getCol() - steps;
            tile.moveHorizontal(-steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }

        private int calculateMaxMoveRightFlyingTile(BaseTile tile) {
            int row = tile.getRow();
            int col = tile.getCol();
            int maxMove = 0;
            for (int i = col + 1; i < w; i++) {
                BaseTile nextTile = getTileAtPosition(row, i);
                if (isTileEmpty(nextTile) || nextTile.getIsHole()) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        private void moveTileRight(BaseTile tile, int steps) {
            if (steps == 0) return;
            // Remove tile from current position
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            // Move tile
            int newCol = tile.getCol() + steps;
            tile.moveHorizontal(steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }


        // Adjusted maximum movement calculation methods to handle tiles with holes
        
        // Movement upwards
        /**
         * Calculates the maximum possible upward movement for a tile.
         *
         * @param row The current row of the tile.
         * @param column The current column of the tile.
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tile is glued or stuck.
         * @return The maximum number of steps the tile can move up, or -1 if it would fall into a hole.
         */
        private int calculateMaxMoveUp(int row,int column, List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = 0;
            for (int i = row - 1; i >= 0; i--) {
                BaseTile nextTile = getTileAtPosition(i, column);
                if (nextTile.getIsHole()) {
                    if (isGluedOrStuck) {
                        // Stuck tile: it cannot move to the hole, stop before
                        break;
                    } else {
                        // Free tile: it would fall in the hole 
                        return -1; // Indicates that the free tile will fall in the hole 
                    }
                } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        /**
         * Calculates the maximum possible upward movement for a group of tiles.
         *
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
         * @return The maximum number of steps the group can move up, or -1 if any tile would fall into a hole.
         */
        private int calculateMaxMoveUpGroup(List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = h;
            for (BaseTile tile : group) {
                int tileMaxMove = calculateMaxMoveUp(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
                if (tileMaxMove == -1) {
                    // One of the free tile of the group will fall in a hole
                    return -1;
                }
                maxMove = Math.min(maxMove, tileMaxMove);
            }
            return maxMove;
        }


        // Movement downwards
        /**
         * Calculates the maximum possible downward movement for a tile.
         *
         * @param row The current row of the tile.
         * @param column The current column of the tile.
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tile is glued or stuck.
         * @return The maximum number of steps the tile can move down, or -1 if it would fall into a hole.
         */
        private int calculateMaxMoveDown(int row,int column, List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = 0;
            for (int i = row + 1; i < h; i++) {
                BaseTile nextTile = getTileAtPosition(i, column);
                if (nextTile.getIsHole()) {
                    if (isGluedOrStuck) {
                        break;
                    } else {
                        return -1;
                    }
                } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        /**
         * Calculates the maximum possible downward movement for a group of tiles.
         *
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
         * @return The maximum number of steps the group can move down, or -1 if any tile would fall into a hole.
         */
        private int calculateMaxMoveDownGroup(List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = h;
            for (BaseTile tile : group) {
                int tileMaxMove = calculateMaxMoveDown(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
                if (tileMaxMove == -1) {
                    return -1;
                }
                maxMove = Math.min(maxMove, tileMaxMove);
            }
            return maxMove;
        }

        // Movement leftwards
        /**
         * Calculates the maximum possible leftward movement for a tile.
         *
         * @param row The current row of the tile.
         * @param column The current column of the tile.
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tile is glued or stuck.
         * @return The maximum number of steps the tile can move left, or -1 if it would fall into a hole.
         */
        private int calculateMaxMoveLeft(int row,int column, List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = 0;
            for (int i = column - 1; i >= 0; i--) {
                BaseTile nextTile = getTileAtPosition(row, i);
                if (nextTile.getIsHole()) {
                    if (isGluedOrStuck) {
                        break;
                    } else {
                        return -1;
                    }
                } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }

        /**
         * Calculates the maximum possible leftward movement for a group of tiles.
         *
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
         * @return The maximum number of steps the group can move left, or -1 if any tile would fall into a hole.
         */
        private int calculateMaxMoveLeftGroup(List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = w;
            for (BaseTile tile : group) {
                int tileMaxMove = calculateMaxMoveLeft(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
                if (tileMaxMove == -1) {
                    return -1;
                }
                maxMove = Math.min(maxMove, tileMaxMove);
            }
            return maxMove;
        }

        // Movement rightwards
        /**
         * Calculates the maximum possible rightward movement for a tile.
         *
         * @param row The current row of the tile.
         * @param column The current column of the tile.
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tile is glued or stuck.
         * @return The maximum number of steps the tile can move right, or -1 if it would fall into a hole.
         */
        private int calculateMaxMoveRight(int row,int column, List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = 0;
            for (int i = column + 1; i < w; i++) {
                BaseTile nextTile = getTileAtPosition(row, i);
                if (nextTile.getIsHole()) {
                    if (isGluedOrStuck) {
                        break;
                    } else {
                        return -1;
                    }
                } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                    maxMove++;
                } else {
                    break;
                }
            }
            return maxMove;
        }
        
        /**
         * Calculates the maximum possible rightward movement for a group of tiles.
         *
         * @param group The group of tiles that are considered to be moving together.
         * @param isGluedOrStuck Indicates if the tiles are glued or stuck.
         * @return The maximum number of steps the group can move right, or -1 if any tile would fall into a hole.
         */
        private int calculateMaxMoveRightGroup(List<BaseTile> group, boolean isGluedOrStuck) {
            int maxMove = w;
            for (BaseTile tile : group) {
                int tileMaxMove = calculateMaxMoveRight(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
                if (tileMaxMove == -1) {
                    return -1;
                }
                maxMove = Math.min(maxMove, tileMaxMove);
            }
            return maxMove;
        }

    // Methods for moving groups

        /**
         * Moves a group of tiles upward by a specified number of steps.
         * The tiles are sorted such that the uppermost tiles move first.
         *
         * @param group List of tiles to be moved
         * @param steps Number of steps to move upward
         */
        private void moveGroupUp(List<BaseTile> group, int steps) {
            if (steps == 0) return;
            // Order the group because the higher tiles move firstly
            group.sort(Comparator.comparingInt(BaseTile::getRow));
            // Delete tiles of last positions 
            for (BaseTile tile : group) {
                tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            }
            // Move tiles to the new positions
            for (BaseTile tile : group) {
                int newRow = tile.getRow() - steps;
                tile.moveVertical(-steps * (Tile.SIZE + Tile.MARGIN));
                tiles.get(newRow).set(tile.getCol(), tile);
                tile.setRow(newRow);
                
                // Yposition on tile will be equal to Y in Rectangle 
                tile.setYPos(tile.getYPosition());
            }
        }

        /**
         * Moves a group of tiles downward by a specified number of steps.
         * The tiles are sorted such that the bottom most tiles move first.
         *
         * @param group List of tiles to be moved
         * @param steps Number of steps to move downward
         */
        private void moveGroupDown(List<BaseTile> group, int steps) {
            if (steps == 0) return;
            // Order the group because the lower tiles move firstly
            group.sort((t1, t2) -> Integer.compare(t2.getRow(), t1.getRow()));
            // Delete tiles of last positions 
            for (BaseTile tile : group) {
                tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            }
            // Move tiles to the new positions
            for (BaseTile tile : group) {
                int newRow = tile.getRow() + steps;
                tile.moveVertical(steps * (Tile.SIZE + Tile.MARGIN));
                tiles.get(newRow).set(tile.getCol(), tile);
                tile.setRow(newRow);
                
                // Yposition on tile will be equal to Y in Rectangle 
                tile.setYPos(tile.getYPosition());
            }
        }

        /**
         * Moves a group of tiles to the left by a specified number of steps.
         * The tiles are sorted such that the leftmost tiles move first.
         *
         * @param group List of tiles to be moved
         * @param steps Number of steps to move to the left
         */
        private void moveGroupLeft(List<BaseTile> group, int steps) {
            if (steps == 0) return;
            // Order the group because the tiles with lower columns move firstly
            group.sort(Comparator.comparingInt(BaseTile::getCol));
            // Delete tiles of last positions
            for (BaseTile tile : group) {
                tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            }
            // Move tiles to the new positions
            for (BaseTile tile : group) {
                int newCol = tile.getCol() - steps;
                tile.moveHorizontal(-steps * (Tile.SIZE + Tile.MARGIN));
                tiles.get(tile.getRow()).set(newCol, tile);
                tile.setCol(newCol);
                
                // Xposition on tile will be equal to X in Rectangle 
                tile.setXPos(tile.getXPosition());
            }
        }

        /**
         * Moves a group of tiles to the right by a specified number of steps.
         * The tiles are sorted such that the rightmost tiles move first.
         *
         * @param group List of tiles to be moved
         * @param steps Number of steps to move to the right
         */
        private void moveGroupRight(List<BaseTile> group, int steps) {
            if (steps == 0) return;
            // Order the group because the tiles with higher columns move firstly
            group.sort((t1, t2) -> Integer.compare(t2.getCol(), t1.getCol()));
            // Delete tiles of last positions
            for (BaseTile tile : group) {
                tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
            }
            // Move tiles to the new positions
            for (BaseTile tile : group) {
                int newCol = tile.getCol() + steps;
                tile.moveHorizontal(steps * (Tile.SIZE + Tile.MARGIN));
                tiles.get(tile.getRow()).set(newCol, tile);
                tile.setCol(newCol);
                
                // Xposition on tile will be equal to X in Rectangle 
                tile.setXPos(tile.getXPosition());
            }
        }

        /**
         * Collects all tiles that are stuck together as a group, starting from the given tile.
         * Uses depth-first search to recursively find connected tiles.
         *
         * @param tile The starting tile
         * @param group List to store the collected tiles
         */
        private void collectStuckGroup(BaseTile tile, List<BaseTile> group) {
            if (tile.isVisited()) return;
            tile.setVisited(true);
            group.add(tile);
            int row = tile.getRow();
            int column = tile.getCol();
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
            for (int[] dir : directions) {
                int adjRow = row + dir[0];
                int adjCol = column + dir[1];
                BaseTile adjacentTile = getTileAtPosition(adjRow, adjCol);
                if (adjacentTile != null && !adjacentTile.isVisited() && !isTileEmpty(adjacentTile) &&
                        !(adjacentTile instanceof FlyingTile) &&
                        (adjacentTile.isStuck() || adjacentTile.hasGlue()) && !adjacentTile.getIsHole()) {
                    collectStuckGroup(adjacentTile, group);
                }
            }
        }

        /**
         * Retrieves the tile at a specific position.
         *
         * @param row Row index
         * @param column Column index
         * @return The tile at the specified position, or null if out of bounds
         */
        public BaseTile getTileAtPosition(int row,int column) {
            if (row >= 0 && row < h && column >= 0 && column < w) {
                return tiles.get(row).get(column);
            }
            return null;
        }

        /**
         * Checks if a given tile is empty.
         *
         * @param tile The tile to check
         * @return True if the tile is empty, false otherwise
         */
        private boolean isTileEmpty(BaseTile tile) {
            if (tile == null) return false;
            return !tile.getIsHole() && tile.getLabel() == '*';
        }

        /**
         * Resets the "visited" flags for all tiles after a tilt operation.
         */
        private void resetVisitedFlags() {
            for (List<BaseTile> rowList : tiles) {
                for (BaseTile tile : rowList) {
                    tile.setVisited(false);
                }
            }
        }

        /**
         * Creates an empty tile at the given position.
         *
         * @param row Row index
         * @param column Column index
         * @return A new empty tile
         */
        private Tile createEmptyTile(int row,int column) {
            int xPosition = 105 + (column * (Tile.SIZE + Tile.MARGIN));
            int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
            Tile emptyTile;
            if (holes[row][column]) {
                emptyTile = new Tile('h', xPosition, yPosition, row, column);
                emptyTile.setIsHole(true);
                createHoleCircle(emptyTile);
            } else {
                emptyTile = new Tile('*', xPosition, yPosition, row, column);
            }
            return emptyTile;
        }
        
        /**
         * Displays an error message if the simulator is visible.
         * @param message The message to display.
         * @param title The title of the message.
         */
        private void showMessage(String message, String title) {
            if (this.visible) {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
                this.ok = false;
            }
        }
        
        /**
         * Checks if the puzzle has reached the goal state.
         * Compares the current board (tiles) with the reference board (ending).
         *
         * @return True if the puzzle is in the goal state, false otherwise
         */
        public boolean isGoal() {
            // Move in in the actual board  (tiles) and compare it with the reference board(ending)
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    BaseTile currentTile = tiles.get(row).get(col);
                    char currentLabel = currentTile.getLabel();
                    
                    BaseTile targetTile = referingTiles.get(row).get(col);
                    char targetLabel = targetTile.getLabel();
                    
                    if (currentLabel == 'h' || targetLabel == 'h' || currentLabel == 'l' || targetLabel == 'l' || currentLabel == 'f' || targetLabel == 'f' || currentLabel == 'x' || targetLabel == 'x' || currentLabel == 'o' || targetLabel == 'o' || currentLabel == 'p' || targetLabel == 'p' || currentLabel == 'w' || targetLabel == 'w' ){
                        continue;
                    }
                    // Compare the actual tile with the tile in the objective state
                    if (currentLabel != targetLabel) {
                        this.ok = false;
                        return false;  // If that's not true, the final state hasn't been reached 
                    }
                    
                }
            }
            
            // If all tiles assimilates with the reference tiles, so we reached the final state
            this.ok = true;
            return true;
        }


        /**
         * Makes the simulator visible.
         * @throws PuzzleExceptions.makeVisibleInvisibleExceptions exceptions for starting and ending board
         */
        
        public void makeVisible() throws PuzzleExceptions.makeVisibleInvisibleExceptions{
            try{
                if(startingBoard == null) throw new PuzzleExceptions.makeVisibleInvisibleExceptions(PuzzleExceptions.makeVisibleInvisibleExceptions.NO_STARTING_BOARD_NULL);
                if(endingBoard == null) throw new PuzzleExceptions.makeVisibleInvisibleExceptions(PuzzleExceptions.makeVisibleInvisibleExceptions.NO_ENDING_BOARD_NULL);
                
                this.visible = true;
            
                
                startingBoard.makeVisible();  // Make visible the initial board
                endingBoard.makeVisible();    // Make visible the final board
                
                for (List<BaseTile> row : tiles) {
                    for (BaseTile tile : row) {
                        tile.makeVisible();
                    }
                }
            
                for (List<BaseTile> row : referingTiles) {
                    for (BaseTile tile : row) {
                        tile.makeVisible();
                    }
                }
            
                this.ok = true;  // Successful action
            
            }catch (PuzzleExceptions.makeVisibleInvisibleExceptions e){
                showMessage(e.getMessage(),"Error");
                this.ok = false;
                throw e;
            }
            
        }
        
        
        /**
         * Makes the simulator invisible.
         * @throws PuzzleExceptions.makeVisibleInvisibleExceptions exceptions for starting and ending board
         */
        
        public void makeInvisible() throws PuzzleExceptions.makeVisibleInvisibleExceptions {
            try {
                if (startingBoard == null) throw new PuzzleExceptions.makeVisibleInvisibleExceptions(PuzzleExceptions.makeVisibleInvisibleExceptions.NO_STARTING_BOARD_NULL);
                if (endingBoard == null) throw new PuzzleExceptions.makeVisibleInvisibleExceptions(PuzzleExceptions.makeVisibleInvisibleExceptions.NO_ENDING_BOARD_NULL);
        
                this.visible = false;
        
                for (List<BaseTile> row : tiles) {
                    for (BaseTile tile : row) {
                        tile.makeInvisible();
                    }
                }
        
                for (List<BaseTile> row : referingTiles) {
                    for (BaseTile tile : row) {
                        tile.makeInvisible();
                    }
                }
        
                startingBoard.makeInvisible();  // Make starting board invisible
                endingBoard.makeInvisible();    // Make ending board invisible
        
                this.ok = true;  // Successful action
        
            } catch (PuzzleExceptions.makeVisibleInvisibleExceptions e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false;
                throw e;
            }
        }
        
        

        /**
         * Ends the simulator and exits the program.
         */
        public void finish() {
            System.out.println("The simulator has been finished.");
            System.exit(0);
            this.ok = true;
        }
        

        /**
         * Returns a copy of the current puzzle board (starting), representing the current state.
         *
         * @return A copy of the starting matrix
         */
        
        public char[][] actualArrangement() {
            // Create a copy of starting matrix
            char[][] currentArrangement = new char[h][w];
            
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    BaseTile tile = getTileAtPosition(row, col);
                    char label = tile.getLabel();
                    System.out.print(label + " ");
                }
                System.out.println();
            }
            
            System.out.println();
            
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    currentArrangement[row][col] = starting[row][col]; // Copy the actual value
        
                    // Simulation of painting or showing the tiles
                    BaseTile tile = tiles.get(row).get(col);
                    System.out.println("Baldosa en (" + row + ", " + col + "): " + tile.getLabel());
                }
            }
            
            this.ok = true;
            
            return currentArrangement; // Return the matrix copy
            
            
        }
        
        /**
         * Returns whether the last action was successful.
         *
         * @return True if the last action was successful, false otherwise
         */
        public boolean ok() {
            return this.ok;
        }
        

        /**
         * Exchanges the current puzzle board with the reference board.
         * Swaps the starting and ending matrices and updates the tiles visually.
         */
        
        public void exchange() {
            // Exchange character matrixes
            char[][] temp = starting;
            starting = ending;
            ending = temp;
        
            // Exchange tiles lists
            List<List<BaseTile>> tempTiles = tiles;
            tiles = referingTiles;
            referingTiles = tempTiles;
            
            // Exchange tiles positions visually 
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    BaseTile startingTile = tiles.get(row).get(col);
                    int xPositionStartingTile = startingTile.getXPos();
                    int yPositionStartingTile = startingTile.getYPos();
        
                    BaseTile endingTile = referingTiles.get(row).get(col);
                    int xPositionEndingTile = endingTile.getXPos();
                    int yPositionEndingTile = endingTile.getYPos();
        
                    // Calculate the difference in positions
                    int deltaXStarting = xPositionEndingTile - xPositionStartingTile;
                    int deltaYStarting = yPositionEndingTile - yPositionStartingTile;
        
                    int deltaXEnding = xPositionStartingTile - xPositionEndingTile;
                    int deltaYEnding = yPositionStartingTile - yPositionEndingTile;
        
                    // Move the tiles to the new positions
                    startingTile.moveHorizontal(deltaXStarting);
                    startingTile.moveVertical(deltaYStarting);
                    startingTile.setXPos(xPositionEndingTile);
                    startingTile.setYPos(yPositionEndingTile);
        
                    endingTile.moveHorizontal(deltaXEnding);
                    endingTile.moveVertical(deltaYEnding);
                    endingTile.setXPos(xPositionStartingTile);
                    endingTile.setYPos(yPositionStartingTile);
                }
            }
            
            this.ok = true;
            System.out.println("Boards have been exchanged. Now, you're editing the board that was the reference board before.");
        }
        
        
        /**
         * Creates a hole in a specified tile position.
         *
         * @param row Row index
         * @param column Column index
         * @throws PuzzleExceptions.makeHoleExceptions if a hole cannot be created due to specific conditions.
         * @throws PuzzleExceptions.ExceedPuzzleSpaceException if the specified position exceeds the puzzle boundaries.
         */
        public void makeHole(int row, int column) throws PuzzleExceptions.makeHoleExceptions, PuzzleExceptions.ExceedPuzzleSpaceException {
            try {
                // Validate the coordinates
                if (row >= h || column >= w) {
                    throw new PuzzleExceptions.ExceedPuzzleSpaceException(PuzzleExceptions.ExceedPuzzleSpaceException.EXCEED_PUZZLE_SPACE);
                    
                } else if (row < 0 || column < 0) {
                    throw new PuzzleExceptions.makeHoleExceptions(PuzzleExceptions.makeHoleExceptions.NOT_NEGATIVE_POSITION_HOLE);
                }

                BaseTile targetTile = tiles.get(row).get(column);

                if (targetTile.getLabel() == 'h') {
                    throw new PuzzleExceptions.makeHoleExceptions(PuzzleExceptions.makeHoleExceptions.TILE_OCCUPIED_HOLE);
                    
                } else if (isTileEmpty(targetTile) && !targetTile.getIsHole()) {
                    // Mark the tile as a hole
                    targetTile.setLabel('h');
                    targetTile.setIsHole(true);
                    holes[row][column] = true;
                    createHoleCircle(targetTile);
                    this.ok = true; // Successful action

                } else {
                    throw new PuzzleExceptions.makeHoleExceptions(PuzzleExceptions.makeHoleExceptions.NOT_HOLE_TILE_OCCUPIED);
                }

            } catch (PuzzleExceptions.makeHoleExceptions | PuzzleExceptions.ExceedPuzzleSpaceException e) {
                showMessage(e.getMessage(), "Error");
                this.ok = false; // Unsuccessful action
                throw e; // Re-throw the exception for further handling
            }
        }

        
        
        /**
         * Smart tilt method that performs an intelligent tilt to bring the puzzle closer to the solution.
         * @throws PuzzleExceptions.addDeleteGlueExceptions tilt in an intelligent way needs to be handled
         */
        public void tilt() throws PuzzleExceptions.addDeleteGlueExceptions {
            
            // Get the current board configuration
            char[][] currentArrangement = getCurrentArrangement();
        
            // Use BFS to find the sequence of moves from the current configuration to the solution
            List<Character> moves = bfsSolve(currentArrangement, ending);
        
            // If a sequence of moves is found, apply the first move
            if (moves != null && !moves.isEmpty()) {
                char firstMove = moves.get(0);
                tilt(firstMove);
                System.out.println("An intelligent tilt has been applied towards " + directionToString(firstMove));
                this.ok = true;
            } else {
                System.out.println("No possible moves to bring the puzzle closer to the solution.");
                this.ok = false;
            }
        }
        
        
        /**
         * Private method to get the current board configuration.
         */
        private char[][] getCurrentArrangement() {
            char[][] currentArrangement = new char[h][w];
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    BaseTile tile = getTileAtPosition(row, col);
                    currentArrangement[row][col] = tile.getLabel();
                }
            }
            return currentArrangement;
        }
        
        /**
         * BFS implementation to find the sequence of tilts from the current configuration to the solution.
         */
        private List<Character> bfsSolve(char[][] startConfig, char[][] goalConfig) {
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
                    return Arrays.deepToString(board);
                }
            }
        
            // Possible tilt directions
            char[] directions = {'u', 'd', 'l', 'r'};
        
            // Initialize the BFS queue and the set of visited states
            Queue<State> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
        
            // Add initial state to the queue
            State initialState = new State(copyBoard(startConfig), new ArrayList<>());
            queue.add(initialState);
            visited.add(initialState.getKey());
        
            while (!queue.isEmpty()) {
                State currentState = queue.poll();
        
                // Check if the current state matches the goal configuration
                if (boardsEqual(currentState.board, goalConfig)) {
                    return currentState.moves;
                }
        
                // Generate next possible states
                for (char direction : directions) {
                    char[][] newBoard = tiltBoard(currentState.board, direction);
                    String key = Arrays.deepToString(newBoard);
        
                    if (!visited.contains(key)) {
                        visited.add(key);
                        List<Character> newMoves = new ArrayList<>(currentState.moves);
                        newMoves.add(direction);
                        queue.add(new State(newBoard, newMoves));
                    }
                }
            }
        
            // Return null if no solution is found
            return null;
        }
        
        // Helper methods for BFS and board manipulation
        
        /**
         * Copy a board.
         */
        private char[][] copyBoard(char[][] board) {
            return Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
        }
        
        /**
         * Check if two boards are equal.
         */
        private boolean boardsEqual(char[][] board1, char[][] board2) {
            return Arrays.deepEquals(board1, board2);
        }
        
        /**
         * Apply a tilt to a board and return the resulting new board.
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
                                newBoard[insertPos++][col] = newBoard[row][col];
                                if (insertPos - 1 != row) {
                                    newBoard[row][col] = '*';
                                }
                            }
                        }
                    }
                    break;
                case 'd':
                    for (int col = 0; col < w; col++) {
                        int insertPos = h - 1;
                        for (int row = h - 1; row >= 0; row--) {
                            if (newBoard[row][col] != '*') {
                                newBoard[insertPos--][col] = newBoard[row][col];
                                if (insertPos + 1 != row) {
                                    newBoard[row][col] = '*';
                                }
                            }
                        }
                    }
                    break;
                case 'l':
                    for (int row = 0; row < h; row++) {
                        int insertPos = 0;
                        for (int col = 0; col < w; col++) {
                            if (newBoard[row][col] != '*') {
                                newBoard[row][insertPos++] = newBoard[row][col];
                                if (insertPos - 1 != col) {
                                    newBoard[row][col] = '*';
                                }
                            }
                        }
                    }
                    break;
                case 'r':
                    for (int row = 0; row < h; row++) {
                        int insertPos = w - 1;
                        for (int col = w - 1; col >= 0; col--) {
                            if (newBoard[row][col] != '*') {
                                newBoard[row][insertPos--] = newBoard[row][col];
                                if (insertPos + 1 != col) {
                                    newBoard[row][col] = '*';
                                }
                            }
                        }
                    }
                    break;
            }
        
            return newBoard;
        }
        
        /**
         * Convert the direction to a readable string.
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
         * Creates a visual representation of a hole at the specified tile position.
         *
         * @param tile The tile to create the hole in
         */
        private void createHoleCircle(BaseTile tile) {
            int xPos = tile.getXPos();
            int yPos = tile.getYPos();
            int diameter = Tile.SIZE;

            // Calculate the center position of the circle
            int circleX = xPos;
            int circleY = yPos;

            // Create and make visible the circle (hole)
            Circle hole = new Circle(diameter, circleX, circleY, Color.WHITE);
            hole.makeVisible();
            holeCircles.add(hole);
        }

    
        
        /**
         * Identifies and returns a matrix indicating the fixed tiles that cannot move.
         * 
         * @return A matrix of fixed tiles, where 0 indicates a fixed tile and 1 indicates a movable tile.
         */
        public int[][] fixedTiles() {
            // Validates each row to check if there is an empty tile or a hole. 
            // If an empty tile or a hole is found, mark the entire row as not fixed.
            for (int i = 0; i < h; i++) {
                boolean flag = findEmptyTileOrHoleSegmentRow(i);
                if (flag) {
                    for (int j = 0; j < w; j++) {
                        BaseTile tile = getTileAtPosition(i, j);
                        if (tile.getFixedStatus()) {
                            tile.setIsNotFixed();
                        }
                    }
                }
            }
        
            // Validates each column to check if there is an empty tile or a hole. 
            // If an empty tile or a hole is found, mark the entire column as not fixed.
            for (int j = 0; j < w; j++) {
                boolean flag = findEmptyTileOrHoleSegmentColumn(j);
                if (flag) {
                    for (int i = 0; i < h; i++) {
                        BaseTile tile = getTileAtPosition(i, j);
                        if (tile.getFixedStatus()) {
                            tile.setIsNotFixed();
                        }
                    }
                }
            }
        
            // Create a matrix representing the fixed tiles, with 1's and 0's.
            // 0 represents a fixed tile, while 1 represents a movable tile.
            int[][] fixedTilesMatrix = new int[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    BaseTile targetTile = getTileAtPosition(i, j);
                    if (targetTile.getFixedStatus()) {
                        fixedTilesMatrix[i][j] = 0;
                        if (visible) {                        
                                if (visible) {                        
                                    targetTile.blink();
                                }
                            }
                    } else {
                        fixedTilesMatrix[i][j] = 1;
                    }
                }
            }
        
            // Print the fixed tiles matrix to the console
            System.out.println("Fixed Tiles Matrix:");
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    System.out.print(fixedTilesMatrix[row][col] + " ");
                }
                System.out.println();
            }
            System.out.println();
            this.ok = true;
        
            return fixedTilesMatrix;
        }
        
        /**
         * Finds an empty tile or a hole in the specified row.
         * 
         * @param row The row index to check.
         * @return true if an empty tile or a hole is found in the row, false otherwise.
         */
        private boolean findEmptyTileOrHoleSegmentRow(int row) {
            for (int col = 0; col < w; col++) {
                BaseTile currentTile = getTileAtPosition(row, col);
                if (currentTile.getLabel() == 'h' || currentTile.getLabel() == '*') {
                    return true;
                }
            }
            return false;
        }
        
        
        /**
         * Finds an empty tile or a hole in the specified column.
         * 
         * @param col The column index to check.
         * @return true if an empty tile or a hole is found in the column, false otherwise.
         */
        private boolean findEmptyTileOrHoleSegmentColumn(int col) {
            for (int row = 0; row < h; row++) {
                BaseTile currentTile = getTileAtPosition(row, col);
                if (currentTile.getLabel() == 'h' || currentTile.getLabel() == '*') {
                    return true;
                }
            }
            return false;
        }
        
        
        
        /**
         * Counts the number of misplaced tiles compared to the reference board.
         *
         * @return The number of misplaced tiles
         */
        public int misplacedTiles(){
            
            int cont = 0;
            
            for (int row = 0; row < h;row++){
                for (int col = 0; col < w;col++){
                    BaseTile currentTile = tiles.get(row).get(col);
                    char currentLabel = currentTile.getLabel();
        
                    BaseTile referingTile = referingTiles.get(row).get(col);
                    char referenceLabel = referingTile.getLabel();
                    
                    if(currentLabel == 'h' || referenceLabel == 'h'){
                        continue;
                    }
                    
                    if (currentLabel != referenceLabel && currentLabel != '*'){
                        cont++;
                    }
                }
            }
            
            this.ok = true;
            return cont;
        }

        
        public static void main(String[] args)  {
            
            
            //SECOND TEST
            char[][] starting1 = {
                {'y', 'r', 'g', '*', 'y', 'b', 'g', 'r', 'y', '*'},
                {'*', 'b', 'g', 'b', 'r', 'g', 'b', 'y', '*', 'g'},
                {'b', '*', 'y', 'r', 'y', '*', '*', 'r', 'y', 'b'},
                {'r', 'g', 'b', '*', 'r', 'g', 'b', 'y', 'r', '*'},
                {'*', 'b', '*', 'r', '*', 'b', 'g', '*', 'y', 'b'},
                {'g', 'r', '*', 'b', 'g', 'r', '*', '*', '*', 'r'},
                {'r', 'g', 'b', 'y', '*', '*', 'b', '*', 'r', 'b'},
                {'*', 'r', 'g', 'b', 'y', 'r', 'g', '*', 'y', 'r'},
                {'g', '*', '*', 'r', 'g', 'b', '*', 'y', 'g', 'b'},
                {'r', 'g', '*', 'y', 'r', '*', 'b', '*', '*', '*'}
            };
            
            char[][] ending1 = {
            {'y', 'r', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
            {'g', 'b', 'g', 'b', 'r', 'g', 'b', 'y', 'r', 'g'},
            {'b', 'g', 'y', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
            {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'},
            {'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
            {'g', 'r', 'y', 'b', 'g', 'r', 'y', '*', 'g', 'r'},
            {'r', 'g', 'b', 'y', 'r', 'g', 'b', '*', 'r', 'b'},
            {'y', 'r', 'g', 'b', 'y', 'r', 'g', '*', 'y', 'r'},
            {'g', 'b', 'y', 'r', 'g', 'b', 'y', '*', 'g', 'b'},
            {'r', 'g', 'b', 'y', 'r', 'g', 'b', '*', 'r', 'g'}
        };
            
            //Puzzle pz3 = new Puzzle(10, 10); //  Board without matrixes
            try{
                Puzzle pz4 = new Puzzle(starting1, ending1);   
                pz4.fixedTiles();
            } // Board with matrixes
            catch(Exception e){
                
            }
            //Puzzle pz4 = new Puzzle(ending1);
            
            //pz4.addTile(9,0,"fl y");
            //pz4.addTile(9,7,"fl r");            
            //pz4.addGlue(0, 0);
            //pz4.makeHole(9,8);
            //pz4.deleteTile(9, 7);
            //pz4.addTile(9, 7, 'r');
            //int[] from1 = {9,7};
            //int[] to1   = {8,7};            
            //pz4.relocateTile(from1, to1);
            //pz4.addTile(8, 7, 'r');
            //pz4.addTile(7,0,"fr g");
            //pz4.addTile(6,0,"ro b");
            
            //pz4.addGlue(7, 6,"super");
            //pz4.addGlue(7, 6);
            
            //pz4.makeInvisible();
            //pz4.makeVisible();

            //pz4.addGlue(9,1);
            //pz4.tilt('r');
            //pz4.tilt('l');
            //pz4.tilt('d');
            // if (pz4.isGoal()) System.out.println("You go it");
            // pz4.tilt('r');
            
            // //pz4.addTile(5,1,'b');
            // //pz4.deleteTile(5,1);
            // //pz4.deleteTile(9,8);
            
            int[] from1 = {7,6};
            int[] to1   = {4,7};
            //pz4.relocateTile(from1,to1);
            //pz4.deleteGlue(7, 6);
            
            //pz4.exchange();
            
            // int[] from3 = {3,1};
            // int[] to3   = {9,9};
            // //pz4.relocateTile(from3,to3);
            
            // int[] from2 = {1,9};
            // int[] to2   = {3,2};
            // //pz4.relocateTile(from2,to2);
        
            // //pz4.tilt('l');
            // //pz4.tilt('g');
            // pz4.addTile(6,0,'r');
            
            // int[] from4 = {6,0};
            // int[] to4   = {3,1};
            // pz4.relocateTile(from4,to4);
        }
    }