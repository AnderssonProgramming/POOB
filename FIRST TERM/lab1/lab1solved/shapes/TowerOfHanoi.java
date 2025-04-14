import java.awt.Color;
import javax.swing.JOptionPane;

public class TowerOfHanoi {
    private Tower[] towers;
    private int numberOfDisks;
    private Color[] colors = {
        Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA,
        Color.GRAY, Color.LIGHT_GRAY, Color.BLUE, new Color(128, 0, 128), // Purple
        new Color(238, 130, 238), // Violet
        new Color(75, 0, 130), // Indigo
        new Color(255, 215, 0), // Gold
        new Color(192, 192, 192), // Silver
        new Color(64, 224, 208), // Turquoise
        new Color(255, 105, 180), // Hot Pink
        new Color(0, 255, 127), // Spring Green
        new Color(255, 69, 0), // Red-Orange
        new Color(255, 127, 80) // Coral
    };

    public TowerOfHanoi(int numberOfDisks) {
        if (numberOfDisks > 20) {
            throw new IllegalArgumentException("El número máximo de discos es 20.");
        }
        this.numberOfDisks = numberOfDisks;
        towers = new Tower[3];

        int baseX = 200; // Posición base en x
        int spacing = 300; // Espacio entre las torres

        // Crear las torres
        towers[0] = new Tower(numberOfDisks, colors, baseX, 400); // Ajusta la posición vertical si es necesario
        towers[1] = new Tower(0, colors, baseX + spacing, 400);
        towers[2] = new Tower(0, colors, baseX + 2 * spacing, 400);

        towers[0].makeVisible();
        towers[1].makeVisible();
        towers[2].makeVisible();
    }
    
        // Método para obtener una torre específica ++
    public Tower getTower(int index) {
        if (index < 0 || index >= towers.length) {
            throw new IllegalArgumentException("Índice de torre inválido.");
        }
        return towers[index];
    }

    public void moveDisk(int fromTower, int toTower) {
        if (fromTower < 1 || fromTower > 3 || toTower < 1 || toTower > 3) {
            JOptionPane.showMessageDialog(null, "Número de torre inválido.");
            return;
        }
        Tower source = towers[fromTower - 1];
        Tower destination = towers[toTower - 1];

        if (source.getDisks() == 0) {
            JOptionPane.showMessageDialog(null, "No hay discos en la torre de origen.");
            return;
        }

        if (destination.getDisks() > 0 && source.getTopDiskSize() > destination.getTopDiskSize()) {
            JOptionPane.showMessageDialog(null, "No se puede colocar un disco más grande sobre un disco más pequeño.");
            return;
        }

        Color topDiskColor = source.getTopDiskColor();
        int topDiskSize = source.getTopDiskSize();
        source.pop();
        destination.push(topDiskSize, topDiskColor);
    }
}
