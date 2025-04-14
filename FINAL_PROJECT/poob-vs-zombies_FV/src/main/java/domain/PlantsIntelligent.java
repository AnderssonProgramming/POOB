package domain;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * The PlantsIntelligent class represents an intelligent machine player that 
 * automatically plants different types of plants in a predefined order.
 * It extends the MachinePlayer class and manages the planting strategy 
 * and resources for the plants team.
 * 
 * The class includes methods to start automatic planting, determine target 
 * columns for different plant types, and find empty rows in specific columns.
 * 
 */
public class PlantsIntelligent extends MachinePlayer {

    private ArrayList<String> plantingOrder; // Orden de tipos de plantas a colocar
    private int currentPlantIndex; // Índice actual en el orden de plantación

    /**
     * Constructs a PlantsIntelligent object with the specified number of suns.
     *
     * @param suns the number of suns available for planting
     */
    public PlantsIntelligent(int suns) {
        super("StPlant");
        Team plantsTeam = new Plants(suns, MACHINE_PLANTS);
        this.team = plantsTeam;
        this.plantingOrder = new ArrayList<>(Arrays.asList("Sunflower", "Peashooter", "WallNut"));
        this.currentPlantIndex = 0;
    }

    /**
     * Inicia la plantación automática siguiendo la estrategia definida.
     * Este método se ejecuta en un hilo separado para no bloquear el hilo principal del juego.
     */
    /**
     * Starts the automatic planting process for the game.
     * This method runs in a separate thread and continuously attempts to plant
     * based on the current planting strategy.
     *
     * @param game The instance of the POOBvsZombies game.
     *
     * The method performs the following steps:
     * 1. Retrieves the current plant type from the planting strategy.
     * 2. Creates an instance of the plant.
     * 3. Checks if there are enough resources to plant.
     * 4. Determines the target column based on the plant type.
     * 5. Finds the first empty row in the target column.
     * 6. Places the plant in the game domain and updates the UI.
     * 7. Deducts the cost of the plant from the team's resources.
     * 8. Updates the score labels in the UI.
     * 9. Moves to the next plant in the planting strategy.
     * 10. Waits for a specified time before attempting to plant the next plant.
     *
     * The method handles invalid plant types, insufficient resources, and
     * synchronization of resource access. It also ensures the thread can be
     * interrupted gracefully.
     */
    public void startAutomaticPlanting(POOBvsZombies game) {
        new Thread(() -> {
            while (true) { // Intentar plantar continuamente
                // Obtener el tipo de planta actual en la estrategia
                String plantType = plantingOrder.get(currentPlantIndex);
                Plant plant = game.createPlantInstance(plantType);
                if (plant == null) {
                    System.err.println("Tipo de planta inválido: " + plantType);
                    continue;
                }

                // Verificar si hay suficientes recursos para plantar
                synchronized (team) { // Sincronizar acceso a los recursos
                    if (team.getResourceCounterAmount() >= plant.getCost()) {
                        // Determinar la columna objetivo basada en el tipo de planta
                        int targetCol = getTargetColumn(plantType);
                        if (targetCol == -1) {
                            System.err.println("No se ha definido una columna objetivo para: " + plantType);
                            continue;
                        }

                        // Encontrar la primera fila vacía en la columna objetivo
                        int targetRow = findEmptyRowInColumn(targetCol,game);
                        if (targetRow != -1) {
                            // Colocar la planta en el dominio (lógica del juego)
                            game.addEntity(targetRow, targetCol, plant);
                            // Visualizar la planta en la interfaz gráfica
                            game.spawnPlantUI(targetRow, targetCol, plant);
                            // Deducir el costo de la planta
                            team.deductResource(plant.getCost());
                            // Actualizar las etiquetas de puntaje en la interfaz
                            if (game.getGardenMenu() != null) {
                                game.getGardenMenu().updateScoreLabels();
                            }
                            System.out.println("Máquina colocó un " + plantType + " en (" + targetRow + ", " + targetCol + ")");
                        } else {
                            System.out.println("No hay celdas vacías en la columna " + targetCol + " para " + plantType);
                        }
                    } else {
                        // No hay suficientes recursos, esperar hasta que se acumulen más
                        System.out.println("No hay suficientes recursos para plantar " + plantType + ". Esperando...");
                    }
                }

                // Pasar a la siguiente planta en la estrategia
                currentPlantIndex = (currentPlantIndex + 1) % plantingOrder.size();

                // Esperar antes de intentar plantar la siguiente planta
                try {
                    Thread.sleep(2000); // Esperar 3 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    /**
     * Determines the target column for a given plant type.
     *
     * @param plantType the type of the plant (e.g., "Sunflower", "Peashooter", "WallNut")
     * @return the column number where the plant should be placed:
     *         - 1 for "Sunflower" (first column after the lawn mower)
     *         - 2 for "Peashooter" (second column)
     *         - 3 for "WallNut" (third column)
     *         - -1 for any other plant type
     */
    private int getTargetColumn(String plantType) {
        switch (plantType) {
            case "Sunflower":
                return 1; // Primera columna después de la cortadora de césped
            case "Peashooter":
                return 2; // Segunda columna
            case "WallNut":
                return 3; // Tercera columna
            default:
                return -1;
        }
    }

    
    /**
     * Finds the first empty row in the specified column.
     *
     * @param col the column index to search for an empty row.
     * @param game the game instance to check for entities.
     * @return the index of the first empty row in the specified column, or -1 if no empty row is found.
     */
    private int findEmptyRowInColumn(int col,POOBvsZombies game) {
        for (int row = 0; row < 5; row++) {
            String entity = game.getEntity(row, col);
            if (entity == null) {
                return row;
            }
        }
        return -1; // No hay celdas vacías
    }

    @Override
    public void setScore() {
        throw new UnsupportedOperationException("Unimplemented method 'setScore'");
    }
    
}
