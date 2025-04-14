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
public class PuzzleC11Test
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
    
    @Test
    public void accordingPSshouldntRelocateTileInvalidCoords(){
        int [] fromTile = {-4,0};
        int [] toTile = {7,2};
        
        int [] fromTile1 = {4,0};
        int [] toTile1 = {7,-2};
        
        try {
            puzzle.relocateTile(fromTile,toTile);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.relocateTile(fromTile1,toTile1);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot relocate a tile cuz there are invalid coordinates(negative or out of range).");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileHole(){
        try {
            puzzle.makeHole(7,2);
        } catch (makeHoleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.makeHole(0,4);
        } catch (makeHoleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        int [] fromTile = {0,0};
        int [] toTile = {7,2};
        
        int [] fromTile1 = {0,4};
        int [] toTile1 = {8,1};
        
        try {
            puzzle.relocateTile(fromTile,toTile);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.relocateTile(fromTile1,toTile1);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        assertFalse(puzzle.ok(),"You cannot move a hole tile or you cannot relocate a tile to a position that has a hole.");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileGlueStuck(){
        try {
            puzzle.addGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        
        int [] fromTile = {0,0};
        int [] toTile = {0,4};
        
        int [] fromTile1 = {1,0};
        int [] toTile1 = {8,1};
        
        try {
            puzzle.relocateTile(fromTile,toTile);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.relocateTile(fromTile1,toTile1);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        assertFalse(puzzle.ok(),"You cannot move a tile that has glue or is stuck.");
    }
    
    @Test
    public void accordingPSshouldntRelocateTileEmptyFull(){
        
        int [] fromTile = {0,0};
        int [] toTile = {0,5};
        
        int [] fromTile1 = {8,1};
        int [] toTile1 = {7,2};
        
        try {
            puzzle.relocateTile(fromTile,toTile);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.relocateTile(fromTile1,toTile1);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        assertFalse(puzzle.ok(),"You cannot move a non-existent tile or there is already a tile in the destination position. ");
    }
    
    
    // addGlue method
    
    @Test
    public void accordingPSshouldAddGlue(){
        try {
            puzzle.addGlue(1,5);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"You can add glue because that tile fulfills with all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueInvalidCoords(){
        try {
            puzzle.addGlue(1,-5);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot add a glue cuz there are invalid coordinates(negative or out of range).");
    }
    
    @Test
    public void accordingPSshouldntAddGlueHole(){
        try {
            puzzle.makeHole(8,1);
        } catch (makeHoleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.addGlue(8,1);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot add glue on a hole tile.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueEmpty(){
        try {
            puzzle.addGlue(8,1);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot apply glue to an empty tile.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueExistent(){
        try {
            puzzle.addGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.addGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"This tile already has glue applied.");
    }
    
    // deleteGlue method
    
    @Test
    public void accordingPSshouldDeleteGlue(){
        try {
            puzzle.addGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.deleteGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"You can delete glue because that tile fulfills with all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntDeleteGlueInvalidCoords(){
        try {
            puzzle.deleteGlue(1,-5);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot delete glue cuz there are invalid coordinates(negative or out of range).");
    }
    
    @Test
    public void accordingPSshouldntDeleteGlueHole(){
        try {
            puzzle.makeHole(8,1);
        } catch (makeHoleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.deleteGlue(8,1);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot delete glue on a hole tile.");
    }
    
    @Test
    public void accordingPSshouldntDeleteGlueEmpty(){
        try {
            puzzle.deleteGlue(8,1);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot delete glue to an empty tile.");
    }
    
    @Test
    public void accordingPSshouldntAddGlueInexistent(){
        try {
            puzzle.deleteGlue(0,0);
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"There is no glue to remove on this tile.");
    }
    
    
}
