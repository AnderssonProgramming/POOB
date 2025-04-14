package test;

import puzzle.*;
import puzzle.PuzzleExceptions.ExceedPuzzleSpaceException;
import puzzle.PuzzleExceptions.addDeleteGlueExceptions;
import puzzle.PuzzleExceptions.addDeleteTileExceptions;
import puzzle.PuzzleExceptions.makeHoleExceptions;
import puzzle.PuzzleExceptions.makeVisibleInvisibleExceptions;
import puzzle.PuzzleExceptions.relocateTileExceptions;
import puzzle.PuzzleExceptions.ConstructorsExceptions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class PuzzleC1Test.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class PuzzleC1Test
{   
    // Elementary attributes to make tests
    private Puzzle puzzle;
    //private Puzzle puzzleI;
    //private Puzzle puzzleF;
    private char[][] starting;
    private char[][] ending;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        starting = new char[][]{
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'}
        };
        
        ending = new char[][]{
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'}
        };

        
        //puzzleI = new Puzzle(9,6);
        try{
            puzzle = new Puzzle(starting,ending);    
        }
        catch (ConstructorsExceptions e){
            e.getMessage();
        }
        
        
    }

    /**
     * Test
     *
     * We define different tests to every method in Cycle 1 about should and shouldn't do
     */
    
    //TESTS ABOUT CONSTRUCTORS
    /**
    @Test
    public void accordingPSShouldntCreateBoardsWithHWNegative(){
        puzzleI = new Puzzle(-7,0);
        assertFalse(puzzleI.ok());
        puzzleI = null;
    }
    
    @Test
    public void accordingPSShouldCreateBoardsWithHWPositive(){
        puzzleI = new Puzzle(4,2);
        assertTrue(puzzleI.ok());
        puzzleI = null;
    }
    **/
    
    @Test 
    public void accordingPSshouldCreateStartingEnding(){
        assertTrue(puzzle.ok());
    }
    
    /**
    @Test
    public void accordingPSShouldCreateEnding(){
        assertTrue(puzzleF.ok());
    }
    **/
    
    //addTile method
    
    @Test
    public void accordingPSshouldAddValidTile(){
        try {
            puzzle.addTile(0,4,'y');
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should add a tile in the position because is empty and good label.");
        
    }
    

      
    @Test
    public void accordingPSshouldntAddInvalidTileLabel(){
        try {
            puzzle.addTile(4,0,'t');
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"It shouldn't add a tile with an invalid label.");
    }
    
    @Test
    public void accordingPSshouldntAddInvalidTileOutOfRange(){
        try {
            puzzle.addTile(10,4,'y');
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"It shouldn't add a tile because you have exceeded the puzzle space.");
    }
    
    @Test
    public void accordingPSshouldntAddInvalidTileNegativePositions(){
        try {
            puzzle.addTile(-3,3,'y');
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"It shouldn't add a tile cuz you're searching for a non-existent tile with negative position.");
    }
    
    @Test
    public void accordingPSshouldntAddInvalidTileHole(){
        try {
            puzzle.makeHole(1,3);
        } catch (makeHoleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.addTile(1,3,'y');
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot add a tile on a hole.");
    }
    
    //deleteTile method
    
    @Test
    public void accordingPSshouldDeleteValidTile(){
        try {
            puzzle.deleteTile(0,0);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should delete a tile in the position cuz the tile doesn't have glue or is stuck.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileInexistent(){
        try {
            puzzle.deleteTile(0,4);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You're trying to delete a non-existent tile.");
    }
    
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileOutOfRange(){
        try {
            puzzle.deleteTile(10,4);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"It shouldn't delete a tile because you have exceeded the puzzle space.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileNegativePositions(){
        try {
            puzzle.deleteTile(-3,3);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"It shouldn't delete a tile cuz you're searching for a non-existent tile with negative position.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidTileHole(){
        try {
            puzzle.makeHole(1,3);
        } catch (makeHoleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.deleteTile(1,3);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot delete a tile that is a hole.");
    }
    
    @Test
    public void accordingPSshouldntDeleteInvalidGlueStuck(){
        try {
            puzzle.addGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.deleteTile(0,0);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.deleteTile(1,0);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot delete a tile that has glue or is stuck.");
    }
    
    
    //relocateTile method
    
    @Test
    public void accordingPSshouldRelocateTotileFromTile(){
        int [] fromTile = {0,0};
        int [] toTile = {7,2};
        try {
            puzzle.relocateTile(fromTile,toTile);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"You can relocate from tile to tile, because it fulfills all the conditions.");
    }
    
    
}
