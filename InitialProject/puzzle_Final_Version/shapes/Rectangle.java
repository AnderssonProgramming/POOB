package shapes;
import java.awt.*;
import puzzle.*;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)()
 */


 
public class Rectangle{

    public static int EDGES = 4;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private Color color;
    private boolean isVisible;

    /**
     * Default constructor for Rectangle.
     * Creates a rectangle at a default position with no specified height, width, or color.
     * The rectangle will need to be configured before it becomes useful.
     */
    
    public Rectangle(){
        // Empty constructor, attributes will be initialized later
    }
    
    /**
     * Constructor to create a rectangle with specified dimensions, color, and position.
     * 
     * @param height The height of the rectangle in pixels.
     * @param width The width of the rectangle in pixels.
     * @param color The color of the rectangle.
     * @param xPosition The x-coordinate of the rectangle's position on the canvas.
     * @param yPosition The y-coordinate of the rectangle's position on the canvas.
     */   
    public Rectangle(int height, int width, Color color, int xPosition, int yPosition){
        this.width = width;
        this.height = height;
        this.color = color;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.makeVisible();
    }
    
    /**
     * Constructor to create a rectangle with a specific board configuration, used in puzzles.
     * It automatically sets its size based on tiles, margin, and board name.
     * 
     * @param h The height in terms of the number of tiles.
     * @param w The width in terms of the number of tiles.
     * @param xPosition The x-coordinate of the rectangle's starting position.
     * @param yPosition The y-coordinate of the rectangle's starting position.
     * @param boardName The name of the board configuration (e.g., "starting" or other positions).
     */    
    public Rectangle(int h, int w, int xPosition, int yPosition, String boardName){
        int tileSize = Tile.SIZE;
        int tileMargin = Tile.MARGIN;
        color = Puzzle.lightBrown;
        this.width = w * (tileSize + tileMargin);
        this.height = h * (tileSize + tileMargin);            
        this.xPosition = xPosition;
        if (boardName.equals("starting")){
            
        } else{
            this.xPosition = xPosition + width;
        }
        this.yPosition = yPosition;            
        this.makeVisible();
    }
    
    /**
     * Make this rectangle visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
        
    }
    
    /**
     * Make this rectangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
        
    }
    
    /**
     * Move the rectangle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the rectangle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the rectangle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the rectangle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the rectangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the rectangle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the rectangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the rectangle vertically.
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidht the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
    
    /**
     * Change the color. 
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(Color newColor){
        color = newColor;
        draw();
    }

    /*
     * Draw the rectangle with current specifications on screen.
     */

    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, 
                                       width, height));
            canvas.wait(10);
        }
    }

    /*
     * Erase the rectangle on screen.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    public int getXPosition(){
        return xPosition;
    }
    
    public int getYPosition(){
        return yPosition;
    }
}