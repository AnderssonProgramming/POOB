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
 * The test class PuzzleC4Test.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class PuzzleC4Test
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
            {'y', '*', '*', 'g', '*', 'y'},
            {'*', 'r', '*', 'b', 'y', '*'},
            {'r', 'g', '*', 'y', '*', '*'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', '*'},
            {'*', '*', 'r', 'g', '*', '*'}
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
    public void accordingPSshouldCreateStartingEnding(){
        assertTrue(puzzle.ok());
    }
    
    
    //addTile method for every type tile
    
    //Fixed Tile
    @Test
    public void accordingPSshouldAddValidFixedTile(){
        try {
            puzzle.addTile(0,4,"fi y");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should add a fixed tile in the position because is empty and good label.");
        
    }
    
    @Test
    public void accordingPSshouldntDeleteFixedTile(){
        try {
            puzzle.addTile(0,4,"fi y");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        try {
            puzzle.deleteTile(0,4);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        assertFalse(puzzle.ok(),"It shouldnt delete a fixed tile in the position because it's a requirement.");
        
    }
    
    @Test
    public void accordingPSshouldntRelocateFixedTile() throws PuzzleExceptions.relocateTileExceptions{
        try {
            puzzle.addTile(0,4,"fi y");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        try {
            int[] from1 = {0,4};
                        int[] to1   = {4,0};            
                        puzzle.relocateTile(from1, to1);
        } catch (relocateTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } 
        
        assertFalse(puzzle.ok(),"It shouldnt relocate a fixed tile to another position because it's a requirement.");
        
    }
    
    
    //Rough Tile
    @Test
    public void accordingPSshouldAddValidRoughTile(){
        try {
            puzzle.addTile(8,1,"ro b");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should add a rough tile in the position because is empty and good label.");
        
    }
    
    @Test
    public void accordingPSshouldntTiltRoughTile() throws PuzzleExceptions.tiltException{
        try {
            puzzle.addTile(8,1,"ro b");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        
    try
    {
        puzzle.tilt('r');
    }
    catch (addDeleteGlueExceptions dge)
    {
        dge.getMessage();
    }
        
        
        assertTrue(puzzle.ok(),"It shouldnt tilt a rough tile because it's a requirement.");
        
    }
    
    
    //Freelance Tile
    @Test
    public void accordingPSshouldAddValidFreelanceTile(){
        try {
            puzzle.addTile(3,1,"fr r");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should add a freelance tile in the position because is empty and good label.");
        
    }
    
    @Test
    public void accordingPSshouldntAddGlueFreelanceTile()  {
        try {
            puzzle.addTile(3,1,"fr r");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        try {
            puzzle.addGlue(3,1,"super");
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } 
    
    
        assertFalse(puzzle.ok(),"It shouldnt add glue a freelance tile because it's a requirement.");
        
    }
    
    
    //Flying Tile
    
    @Test
    public void accordingPSshouldAddValidFlyingTile(){
        try {
            puzzle.addTile(8,5,"fl g");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should add a flying tile in the position because is empty and good label.");
        
    }
    
    @Test
    public void accordingPSshouldntFallOffHoleFlyingTile()  {
        try {
            puzzle.addTile(8,5,"fl g");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        try {
            puzzle.makeHole(8,4);
        } catch (makeHoleExceptions  e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        try
    {
        puzzle.tilt('l');
    }
    catch (addDeleteGlueExceptions dge)
    {
        dge.getMessage();
    }
    
    
        assertTrue(puzzle.ok(),"It shouldnt fall into the hole the flying tile because it's a requirement.");
        
    } 
    
    //Wild Tile
    
    @Test
    public void accordingPSshouldAddValidWildTile(){
        try {
            puzzle.addTile(6,3,"wi y");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"It should add a wild tile in the position because is empty and good label.");
        
    }
    
    @Test
    public void accordingPSshouldntDeleteFlyingTile()  {
        try {
            puzzle.addTile(6,3,"wi y");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        try {
            puzzle.deleteTile(6,3);
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        assertFalse(puzzle.ok(),"It shouldnt delete the wild tile because it's a requirement.");
        
    } 
    
    @Test
    public void accordingPSshouldChangeColorTiltWildTile()  {
        try {
            puzzle.addTile(6,3,"wi y");
        } catch (addDeleteTileExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        } catch (ExceedPuzzleSpaceException e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
    
        try
    {
        puzzle.tilt('u');
    }
    catch (addDeleteGlueExceptions dge)
    {
        dge.getMessage();
    }
        
        assertTrue(puzzle.ok(),"It should change the color of the T wild tile when tilt because it's a requirement.");
        
    } 
    
    
    
    // addGlue method for the two types of glue
    
    //Super Glue
    @Test
    public void accordingPSshouldAddSuperGlue(){
        try {
            puzzle.addGlue(0,0,"super");
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"You can add super glue because that tile fulfills with all the conditions.");
    }
    
    @Test
    public void accordingPSshouldTiltNormallyWithoutGroupSuperGlue(){
        try {
            puzzle.addGlue(0,0,"super");
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        try
    {
        puzzle.tilt('d');
    }
    catch (addDeleteGlueExceptions dge)
    {
        dge.getMessage();
    }
    
        assertTrue(puzzle.ok(),"You can look for that the tile with superGlue moves correctly");
    }
    
    //Fragile Glue
    @Test
    public void accordingPSshouldAddFragileGlue(){
        try {
            puzzle.addGlue(0,5,"fragile");
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        assertTrue(puzzle.ok(),"You can add fragile glue because that tile fulfills with all the conditions.");
    }
    
    @Test
    public void accordingPSshouldChangeStateNormallyTileOnly1TiltFragileGlue(){
        try {
            puzzle.addGlue(0,5,"fragile");
        } catch (addDeleteGlueExceptions e) {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        
        try
    {
        puzzle.tilt('l');
    }
    catch (addDeleteGlueExceptions dge)
    {
        dge.getMessage();
    }
    
        assertTrue(puzzle.ok(),"You can look for that the tile with fragile Glue changes to a normal tile cuz you can only make 1 tilt with this type of glue");
    }
    

}
