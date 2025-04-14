import java.util.Stack;

public class Autoplayer {
    private TowerOfHanoi towerOfHanoi;
    private Stack<int[]> moveHistory; // Pila para almacenar el historial de movimientos
    private int moveCounter;

    public Autoplayer(TowerOfHanoi towerOfHanoi) {
        this.towerOfHanoi = towerOfHanoi;
        this.moveHistory = new Stack<>();
        this.moveCounter = 0;
    }

    // Método para mover automáticamente un disco en pos de resolver el juego
    public void autoMoveDisk() {
        int fromTower = -1;
        int toTower = -1;

        // Estrategia clásica para resolver las Torres de Hanoi
        if (moveCounter % 3 == 0) {
            // Mover el disco más pequeño en sentido horario
            fromTower = findSmallestDiskTower();
            toTower = (fromTower + 1) % 3;
        } else {
            // Realizar el movimiento permitido restante
            int firstOptionFrom = (moveCounter % 3 == 1) ? 0 : 1;
            int secondOptionFrom = (moveCounter % 3 == 1) ? 1 : 2;

            if (canMove(firstOptionFrom, secondOptionFrom)) {
                fromTower = firstOptionFrom;
                toTower = secondOptionFrom;
            } else {
                fromTower = secondOptionFrom;
                toTower = firstOptionFrom;
            }
        }

        // Realiza el movimiento y guarda en el historial
        towerOfHanoi.moveDisk(fromTower + 1, toTower + 1);
        moveHistory.push(new int[]{fromTower, toTower});
        moveCounter++;
    }

    // Método para deshacer el último movimiento
    public void undoLastMove() {
        if (moveHistory.isEmpty()) {
            System.out.println("No hay movimientos para deshacer.");
            return;
        }

        int[] lastMove = moveHistory.pop();
        towerOfHanoi.moveDisk(lastMove[1] + 1, lastMove[0] + 1); // Deshacer el movimiento
        moveCounter--;
    }

    // Verifica si se puede mover un disco desde la torre 'from' a la torre 'to'
    private boolean canMove(int from, int to) {
        Tower fromTower = towerOfHanoi.getTower(from);
        Tower toTower = towerOfHanoi.getTower(to);

        if (fromTower.getDisks() == 0) {
            return false;
        }
        if (toTower.getDisks() == 0 || fromTower.getTopDiskSize() < toTower.getTopDiskSize()) {
            return true;
        }
        return false;
    }

    // Encuentra la torre que contiene el disco más pequeño (siempre 1 en este caso)
    private int findSmallestDiskTower() {
        for (int i = 0; i < 3; i++) {
            if (towerOfHanoi.getTower(i).getDisks() > 0 && towerOfHanoi.getTower(i).getTopDiskSize() == 1) {
                return i;
            }
        }
        return -1;
    }
}
