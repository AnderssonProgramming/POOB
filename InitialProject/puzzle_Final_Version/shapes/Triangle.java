package shapes;


import java.awt.*;

import puzzle.Tile;

/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0  (15 July 2000)
 */

public class Triangle{
    
    public static int VERTICES=3;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private Color color;
    private boolean isVisible;
    
    
   
    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle(){
        height = 10;
        width = 10;
        xPosition = 0;
        yPosition = 0;
        color = Color.BLACK;
        isVisible = false;
    }
    
    /**
     * Make this triangle visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        if(!isVisible) {
            isVisible = true;
            draw();
        }
    }
    
    /**
     * Make this triangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        if(isVisible) {
            erase();
            isVisible = false;
        }
    }
    
    /**
     * Move the triangle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the triangle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the triangle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the triangle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the triangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        if(isVisible) {
            erase();
            xPosition += distance;
            draw();
        }
    }

    /**
     * Move the triangle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        if(isVisible) {
            erase();
            yPosition += distance;
            draw();
        }
    }

    /**
     * Slowly move the triangle horizontally.
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
            moveHorizontal(delta);
        }
    }

    /**
     * Slowly move the triangle vertically.
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
            moveVertical(delta);
        }
    }

    /**
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidth the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
    
    /**
     * Change the color. 
     * @param newColor the new color of the triangle.
     */
    public void changeColor(Color newColor){
        color = newColor;
        draw();
    }

    /**
     * Set the absolute position of the triangle.
     * @param x The x-coordinate on the canvas.
     * @param y The y-coordinate on the canvas.
     */
    public void setPosition(int x, int y){
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /*
     * Draw the triangle with current specifications on screen.
     */
    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width / 2), xPosition - (width / 2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            Polygon polygon = new Polygon(xpoints, ypoints, VERTICES);
            canvas.draw(this, color, polygon);
            canvas.wait(10);
        }
    }

    /*
     * Erase the triangle on screen.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }

    public int getXPos() {
        return this.xPosition;
    }
    
    public int getYPos() {
        return this.yPosition;
    }
    
}
