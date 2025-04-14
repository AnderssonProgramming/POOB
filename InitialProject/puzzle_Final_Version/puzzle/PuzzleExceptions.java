package puzzle;


/**
 * This class contains all the exceptions that we'll do in Puzzle Class and PuzzleContest Class.
 * 
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez

 * @version 2024
 */

public class PuzzleExceptions extends Exception
{
    public static class ConstructorsExceptions extends Exception{
        public static final String NO_BOARDS_HW_NEGATIVE= "You cannot create the two boards with negative or zero h,w";
        public static final String NO_STARTING_ENDING_NULL = "You cannot create starting and ending if these list of lists are null";
        public static final String NO_ENDING_NULL = "You cannot create ending if this list of lists is null";
        
        public ConstructorsExceptions (String message){
            super(message);
        }   

    }
    
    
    public static class ExceedPuzzleSpaceException extends Exception{
        public static final String EXCEED_PUZZLE_SPACE= "You have exceeded the puzzle space.";
        
        public ExceedPuzzleSpaceException  (String message){
            super(message);
        }
    }
    
    public static class addDeleteTileExceptions extends Exception{
        public static final String NOT_VALID_LABEL= "Invalid label. Accepted labels are: r, g, b, y.";
        public static final String NOT_NEGATIVE_POSITION_TILE= "You're searching for a non-existent tile with negative position.";
        public static final String NOT_ADD_HOLE_TILE = "You cannot add a tile on a hole.";
        public static final String TILE_OCCUPIED = "There is already a tile here.";
        public static final String NOT_DELETE_HOLE_TILE = "You cannot delete a tile that is a hole.";
        public static final String NOT_DELETE_TILE_GLUE = "You cannot delete a tile that has glue or is stuck.";
        public static final String NOT_DELETE_NON_EXISTENT_TILE= "You're trying to delete a non-existent tile.";
        public static final String NOT_EXISTENT_TYPE_TILE = "Invalid tile type. Accepted types are: fr, fl, ro, fi.";
        public static final String NOT_DELETE_FIXED_TILE = "You cannot delete a Fixed tile.";
        public static final String NOT_DELETE_WILD_TILE = "You cannot delete a Wild tile.";
        
        public addDeleteTileExceptions (String message){
            super(message);
        }
    }
    
    public static class relocateTileExceptions extends Exception{
        public static final String INVALID_COORDINATES= "Invalid coordinates.";
        public static final String NOT_MOVE_HOLE_TILE= "You cannot move a hole tile.";
        public static final String NOT_RELOCATE_TILE_HOLE = "You cannot relocate a tile to a position that has a hole.";
        public static final String NOT_MOVE_NON_EXISTENT_TILE= "You cannot move a non-existent tile.";
        public static final String NOT_RELOCATE_TILE_OCCUPIED = "There is already a tile in the destination position.";
        public static final String NOT_MOVE_TILE_GLUE = "You cannot move a tile that has glue or is stuck";
        public static final String NOT_RELOCATE_FIXED_TILE = "You cannot relocate a Fixed tile.";
    
             
        public relocateTileExceptions  (String message){
            super(message);
        }
    }
    
    public static class addDeleteGlueExceptions extends Exception{
        public static final String INVALID_POSITION= "Invalid position.";
        public static final String NOT_ADD_GLUE_HOLE= "You cannot add glue on a hole tile";
        public static final String NOT_ADD_GLUE_EMPTY_TILE = "Cannot apply glue to an empty tile.";
        public static final String NOT_GLUE_EXISTING_TILE= "This tile already has glue applied.";
        public static final String NOT_DELETE_GLUE_HOLE= "You cannot delete glue on a hole tile.";
        public static final String NOT_DELETE_GLUE_EMPTY_TILE = "Cannot delete glue from an empty tile.";
        public static final String NOT_GLUE_TO_REMOVE= "There is no glue to remove on this tile."; 
        public static final String NOT_GLUE_FREELANCE_TILE = "You cannot apply glue to a Freelance tile.";
        public static final String NOT_VALID_TYPES_GLUE = "Invalid glue type. Must be 'fragil' or 'super'.";
        
        public addDeleteGlueExceptions  (String message){
            super(message);
        }
    }
    
    public static class tiltException extends Exception{
        public static final String INVALID_DIRECTION= "Invalid direction.";
        
        
        public tiltException (String message){
            super(message);
        }
    }
    
    public static class makeHoleExceptions extends Exception{
        public static final String NOT_NEGATIVE_POSITION_HOLE= "You cannot make a hole in a non-existent tile with negative position.";
        public static final String TILE_OCCUPIED_HOLE = "This tile already has a hole.";
        public static final String NOT_HOLE_TILE_OCCUPIED = "You can only make a hole in an empty tile.";
        
        public makeHoleExceptions (String message){
            super(message);
        }
    }
    
    public static class makeVisibleInvisibleExceptions extends Exception{
        public static final String NO_STARTING_BOARD_NULL = "You cannot convert starting board visible because the characteristic is null";
        public static final String NO_ENDING_BOARD_NULL = "You cannot convert ending board visible because the characteristic is null";
        
        public makeVisibleInvisibleExceptions (String message){
            super(message);
        }
    }
    
    
}
