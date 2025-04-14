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
public class PuzzleC12Test
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
    
    //tilt method
    
    @Test
    public void accordingPSshouldTilt(){
        try {
            puzzle.tilt('r');
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.tilt('u');
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.tilt('d');
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        try {
            puzzle.tilt('l');
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"You can tilt the actual board cuz it fulfills all the conditions.");
    }
    
    @Test
    public void accordingPSshouldntTiltLabel(){
        try {
            puzzle.tilt('s');
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertFalse(puzzle.ok(),"You cannot tilt the actual board cuz invalid direction(inexistent).");
    }
    
    // isGoal method
    
    @Test
    public void accordingPSshouldIsGoalBoolean(){
        puzzle.isGoal();
        assertTrue(puzzle.ok(),"Returns true cuz starting = ending.");
    }
    
    @Test
    public void accordingPSshouldntIsGoalBoolean(){
        try {
            puzzle.addTile(0,4,'b');
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        puzzle.isGoal();
        assertFalse(puzzle.ok(),"Returns false cuz starting != ending.");
    }
    
    // makeVisible method
    @Test
    public void accordingPSshouldMakeVisible(){
        try {
            puzzle.makeVisible();
        } catch (makeVisibleInvisibleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"Simulator makes visible.");
    }
    
    
    // makeInvisible method
    @Test
    public void accordingPSshouldMakeInvisible(){
        try {
            puzzle.makeInvisible();
        } catch (makeVisibleInvisibleExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"Simulator makes invisible.");
    }
    
    /**
    // finish method
    @Test
    public void accordingPSshouldfinish(){
        puzzle.finish();
        assertTrue(puzzle.ok(),"Finishes without mistakes.");
    }
    **/
    
    // actualArrangement method
    @Test
    public void accordingPSshouldActualArrangement(){
        puzzle.actualArrangement();
        assertTrue(puzzle.ok(),"It printed the actual arrangement correctly.");
    }
    
    
    // ok method
    @Test
    public void accordingPSshouldOk(){
        puzzle.ok();
        assertTrue(puzzle.ok(),"Ok.");
    }
    
    
}
