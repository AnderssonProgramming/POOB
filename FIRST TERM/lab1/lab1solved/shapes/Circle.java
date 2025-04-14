import java.awt.*;
import java.awt.geom.*;
import java.lang.*;
import java.util.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000) 
 */

public class Circle{

    public static final double PI=3.1416;
    
    private double perimeter;
    private int diameter;
    private int xPosition;
    private int yPosition;
    private Color color;
    private boolean isVisible;
    

    public Circle(){
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = Color.BLUE;
        isVisible = false;
    }
    
    public Circle(int perimeter, int diameter, int xPosition, int yPosition, Color color, boolean isVisible) {
        if (diameter < 0) {
            throw new IllegalArgumentException("Diameter must be non-negative");
        }
        if (perimeter < 0) {
            throw new IllegalArgumentException("Perimeter must be non-negative");
        }
        this.perimeter = perimeter;
        this.diameter = diameter;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
        this.isVisible = isVisible;
        
        if (isVisible){
            draw();
        }
    }
    
    public int getX(){
        return xPosition;
    }

    public int getY(){
        return yPosition;
    }   
    
    public int getDiameter(){
        return diameter;
    }
    
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    

    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition, yPosition, 
                diameter, diameter));
            canvas.wait(10);
        }
    }

    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    /**
     * Move the circle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the circle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the circle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the circle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the circle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the circle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the circle horizontally.
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
     * Slowly move the circle vertically
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        }else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter){
        if (newDiameter < 0) {
            throw new IllegalArgumentException("Diameter must be non-negative");
        }
        erase();
        diameter = newDiameter;
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

    public void calculatePerimeter(){
        perimeter = PI * diameter;
        System.out.println("The perimeter of your circle is: " + perimeter);
    }
    
    
    /**
     * @param operator The operation that the program will do with the current area (+,-,*,/)
     * @param value The number that the program will use with the operator. It musn't be 0 in operator /
     */
    public void change(char operator, int value){
        int exponent = 2;
        double radio = diameter / 2;
        double currentArea = PI* Math.pow(radio,exponent);
        
        switch (operator){
            case '+':
                double newArea = currentArea + value;
                if (newArea < 0){
                    throw new IllegalArgumentException("The new area must be non-negative");  
                }
                else{
                    System.out.println("The current area is: " + currentArea + " and the new area is: " +  newArea);
                }
                
                break;
                
            case '-':
                double newArea1 = currentArea - value;
                if (newArea1 < 0){
                    throw new IllegalArgumentException("The new area must be non-negative"); 
                }
                else{
                    System.out.println("The current area is: " + currentArea + " and the new area is: " +  newArea1);
                }
                break;
            
            case '*':
                double newArea2 = currentArea * value;
                if (newArea2 < 0){
                    throw new IllegalArgumentException("The new area must be non-negative");     
                }
                else{
                    System.out.println("The current area is: " + currentArea + " and the new area is: " +  newArea2);
                }
                break;
                
            case '/':
                if (value == 0){
                    throw new ArithmeticException("You cannot divide by zero.");
                }
                else{
                    double newArea3 = currentArea / value;
                
                    if (newArea3 < 0){
                        throw new IllegalArgumentException("The new area must be non-negative"); 
                    }
                    else{
                        System.out.println("The current area is: " + currentArea + " and the new area is: " +  newArea3);
                    }
                }
                break;
                
            default:
                System.out.println("Invalid operator");
                break;
                
            
        }
    }
    
    
    /**
     * @param times Number of times that the circle will do(movements). It must be > 0
     * @param maxDistance Maximum distance when the program choose a random number in distance. distance <= maxDistance
     */
    public void roll(int times, int maxDistance){
        Random random = new Random();
        while (times > 0 && maxDistance >0){
        
            for (int i= 0; i < times; i++){
                int angle = random.nextInt(91);
                int distance = random.nextInt(maxDistance + 1);
            
                double angleToRadians = Math.toRadians(angle);
            
                int horizontalMove = (int)(distance * Math.cos(angleToRadians));
                int verticalMove = (int)(distance * Math.sin(angleToRadians));
            
                slowMoveHorizontal(horizontalMove);
                slowMoveVertical(verticalMove);
            
                draw();
                makeVisible();
                changeColor(Color.BLACK);
                
            }
            break;
        }
    }
    
    /**
     * @param quantity Number that will be useful to create the square with side quantity
     */
    public void circleSquare(int quantity) {
        
        //Initialize variables
        
        int newXPosition = xPosition;
        int newYPosition = yPosition;
        int spacing = diameter;
        
        //Move the circles(complete)
        
        for (int i=0;i < quantity; i++){
            for (int j = 0; j < quantity; j++){
                Circle circle = new Circle();
                circle.moveHorizontal(xPosition + j * spacing);
                circle.moveVertical(yPosition + i * spacing);
                circle.makeVisible();
            }
        }
        
        //Reinitialize variables
        
        xPosition = newXPosition;
        yPosition = newYPosition;
    } 
    
    /**
     * @param quantity Number that will be useful to create the square with side quantity
     */
    
    public void circleSquare1(int quantity) {
        
        //Initialize variables
        
        int newXPosition = xPosition;
        int newYPosition = yPosition;
        int spacing = diameter;
        
        //Move the circles(only the limits)
        
        for (int i = 0; i < quantity; i++){
            for (int j = 0; j < quantity; j++){
                if (i==0 || i == quantity - 1 || j==0 || j == quantity - 1){
                    Circle circle = new Circle();
                    circle.moveHorizontal(xPosition + j * spacing);
                    circle.moveVertical(yPosition + i * spacing);
                    circle.makeVisible();
                }
            }
        }
    }


    public static void main(String [] args){
        Circle customCircle = new Circle(0, 50,100,100, Color.RED, true);
    }
    

}
