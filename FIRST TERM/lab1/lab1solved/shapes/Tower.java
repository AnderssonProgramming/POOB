import java.awt.Color;
import java.util.ArrayList;

public class Tower {
    private int disks;
    private int xPosition;
    private int yPosition;
    private boolean isVisible;
    private ArrayList<Rectangle> disksRectangles;
    private ArrayList<Integer> diskSizes;
    private Color[] colors;

    private Rectangle baseRectangle;

    public Tower(int disks, Color[] colors, int xPosition, int yPosition) {
        this.disks = disks;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isVisible = false;
        this.disksRectangles = new ArrayList<>();
        this.diskSizes = new ArrayList<>();
        this.colors = colors;

        // Inicializa baseRectangle siempre
        int maxDiskWidth = 80;
        int diskHeight = 20;
        int baseHeight = (maxDiskWidth + 300); // Ajusta la altura de la base si es necesario
        baseRectangle = new Rectangle(baseHeight, 15, xPosition + 63, yPosition - 350, Color.BLACK);
        baseRectangle.makeInvisible(); // La base no se muestra inicialmente

        if (disks > 0) {
            // Inicializa los discos si es necesario
            for (int i = 0; i < disks; i++) {
                Rectangle disk = new Rectangle();
                Color color = colors[i % colors.length]; // Asigna color único para cada disco
                disk.changeColor(color);
                int diskWidth = maxDiskWidth - i * (maxDiskWidth / disks); // Ajusta el tamaño proporcionalmente
                disk.changeSize(diskHeight, diskWidth);

                disk.moveVertical(yPosition - i * diskHeight); // Posición vertical de los discos
                disk.moveHorizontal(xPosition - diskWidth / 2); // Centra el disco sobre la vara

                disksRectangles.add(disk);
                diskSizes.add(diskWidth);
            }
        }
    }

    public int getDisks() {
        return diskSizes.size();
    }

    public int getTopDiskSize() {
        if (diskSizes.size() == 0) return -1;
        return diskSizes.get(diskSizes.size() - 1);
    }

    public Color getTopDiskColor() {
        if (disksRectangles.size() == 0) return null; // Indicador de error
        return disksRectangles.get(disksRectangles.size() - 1).getColor(); // Obtener el color del disco en la cima
    }

    public void makeVisible() {
        if (!isVisible) {
            isVisible = true;
            baseRectangle.makeVisible(); // Asegúrate de que la base sea visible
            for (Rectangle disk : disksRectangles) {
                disk.makeVisible();
            }
        }
    }

    public void makeInvisible() {
        if (isVisible) {
            for (Rectangle disk : disksRectangles) {
                disk.makeInvisible();
            }
            baseRectangle.makeInvisible(); // Asegúrate de que la base sea invisible
            isVisible = false;
        }
    }

    public void moveTo(int x, int y) {
        int deltaX = x - xPosition;
        int deltaY = y - yPosition;

        for (Rectangle disk : disksRectangles) {
            disk.moveHorizontal(deltaX);
            disk.moveVertical(deltaY);
        }

        xPosition = x;
        yPosition = y;
        baseRectangle.moveHorizontal(deltaX);
        baseRectangle.moveVertical(deltaY);
    }

    public void changeColors(Color newColor) {
        for (Rectangle disk : disksRectangles) {
            disk.changeColor(newColor);
        }
    }

    public void push(int diskSize, Color color) {
        if (disksRectangles.size() > 0) {
            Rectangle topDisk = disksRectangles.get(disksRectangles.size() - 1);
            int topDiskWidth = topDisk.getWidth();
            if (diskSize >= topDiskWidth) {
                System.out.println("Error: El nuevo disco debe ser más pequeño que el disco en la cima.");
                return;
            }
        }

        // Calcular la posición vertical del nuevo disco
        int diskHeight = 20; // Altura de cada disco
        int newYPosition = (yPosition - (disks * diskHeight) + 14); // Calcula la nueva posición vertical

        // Calcular la posición horizontal para centrar el disco en la torre
        int newXPosition = (xPosition + (80 - diskSize) / 2) + 30; // Aquí 80 es el ancho máximo del disco

        Rectangle disk = new Rectangle(diskHeight, diskSize, newXPosition, newYPosition, color);
        disksRectangles.add(disk);
        diskSizes.add(diskSize);
        disks++;
        if (isVisible) {
            disk.makeVisible();
        }
    }

    public void pop() {
        if (disksRectangles.size() > 0) {
            Rectangle topDisk = disksRectangles.remove(disksRectangles.size() - 1);
            topDisk.makeInvisible();
            diskSizes.remove(diskSizes.size() - 1);
            disks--;
        }
    }
}
