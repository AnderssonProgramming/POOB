import java.awt.Color;

public class Rectangle {
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private Color color;
    private boolean isVisible;

    public Rectangle() {
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = Color.MAGENTA;
        isVisible = false;
    }

    public Rectangle(int height, int width, int xPosition, int yPosition, Color color) {
        this.height = height;
        this.width = width;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
        this.isVisible = false;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    public void moveRight() {
        moveHorizontal(20);
    }

    public void moveLeft() {
        moveHorizontal(-20);
    }

    public void moveUp() {
        moveVertical(-20);
    }

    public void moveDown() {
        moveVertical(20);
    }

    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    public void changeColor(Color newColor) {
        color = newColor;
        draw();
    }

    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }

    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
