package domain;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Timer; // Importa javax.swing.Timer

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;
import presentation.POOBvsZombiesGUI;

/**
 * Manages zombie threads and their interactions within the game.
 * <p>
 * This class handles the creation, management, and termination of zombie threads,
 * as well as their interactions with plants and other game elements. It also manages
 * the graphical representation of zombies and their movements within the user interface.
 * </p>
 */
public class ZombieThreadManager {

    private POOBvsZombies game;
    private GardenMenu garden;

    // Almacenamos los hilos segun su columna
    private Map<Integer, List<Thread>> zombieThreadsByRow = new HashMap<>();

    // Almacenamos los JLabels de los zombies para poder moverlos o quitarlos
    private Map<Thread, JLabel> threadToLabelMap = new HashMap<>();

    // Almacenamos los zombies para poder acceder a ellos despues y a sus atributos
    private Map<Thread, Zombie> threadToZombieMap = new HashMap<>();

    // Nueva estructura para rastrear los hilos de proyectiles por ECIZombie
    private Map<Thread, Thread> zombieToProjectileThreadMap = new HashMap<>();

    // Map para almacenar el Timer de activación de PotatoMine por posición
    private Map<String, Timer> potatoMineTimers = new HashMap<>();

    private volatile boolean isPaused = false; // Variable para controlar la pausa
    private final Object pauseLock = new Object(); // Objeto para sincronización

    /**
     * Terminates all zombie threads in the specified row, stops their movement,
     * removes their graphical representation from the user interface, and interrupts
     * any associated projectile threads if the zombies are instances of {@code ECIZombie}.
     *
     * @param row the row number whose zombie threads are to be terminated
     */
    public void terminateZombiesInRow(int row) {
        List<Thread> threads;
        synchronized (zombieThreadsByRow) {
            threads = zombieThreadsByRow.remove(row);
        }
        if (threads != null) {
            for (Thread thread : threads) {
                Zombie zombie = threadToZombieMap.get(thread);
                // decimos que el zombie fue asesinado por la cortadora de cesped
                zombie.setKilledByLoawnmower();
                thread.interrupt();
                JLabel zombieLabel = threadToLabelMap.remove(thread);
                // Eliminar el zombieLabel de la interfaz gráfica
                if (zombieLabel != null) {
                    SwingUtilities.invokeLater(() -> {
                        Container parent = zombieLabel.getParent();
                        if (parent != null) {
                            parent.remove(zombieLabel);
                            parent.revalidate();
                            parent.repaint();
                        }
                    });
                }
                if (zombie instanceof ECIZombie) {
                    Thread projectileThread = zombieToProjectileThreadMap.remove(thread);
                    if (projectileThread != null) {
                        projectileThread.interrupt();
                    }
                }                
            }
        }
    }

    /**
     * Constructs a new ZombieThreadManager associated with the given game and garden menu.
     *
     * @param game   the POOBvsZombies game instance
     * @param garden the GardenMenu associated with the game
     */
    public ZombieThreadManager(POOBvsZombies game, GardenMenu garden) {
        this.game = game;
        this.garden = garden;
    }

    /**
     * Registers a zombie to be managed by the thread manager.
     * <p>
     * This method creates a new thread to handle the zombie's movement and logic.
     * It stores the thread, zombie instance, and associated JLabel for future reference.
     * If the zombie is an instance of {@code ECIZombie}, it also creates and starts a separate
     * thread to manage its projectile attacks.
     *
     * @param row         the row where the zombie is located
     * @param zombie      the zombie instance to register
     * @param zombieLabel the JLabel representing the zombie in the GUI
     */
    public void registerZombie(int row, Zombie zombie, JLabel zombieLabel) {
        Thread zombieThread = new Thread(() -> zombieLogic(row, zombie, zombieLabel));

        // almacenamos el hilo recien creado.
        synchronized (zombieThreadsByRow) {
        zombieThreadsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(zombieThread);
        }

        // almacenamos el JLabel del zombie para poder moverlo o quitarlo
        synchronized (threadToLabelMap) {
            threadToLabelMap.put(zombieThread, zombieLabel);
        }

        // almacenamos el zombie para poder acceder a sus atributos en su respectivo
        // hilo
        synchronized (threadToZombieMap) {
            threadToZombieMap.put(zombieThread, zombie);
        }

        synchronized (potatoMineTimers) {
            String key = row + "," + 0; // La PotatoMine siempre está en la col 0
            Timer activationTimer = potatoMineTimers.get(key);
            if (activationTimer != null) {
                activationTimer.stop();
                potatoMineTimers.remove(key);
                System.out.println("Timer de activación detenido para PotatoMine en (" + row + ", 0).");
            }
        }
        zombieThread.start();

        // Si el zombie es un ECIZombie, iniciamos su hilo de proyectil único
        if (zombie instanceof ECIZombie) {
            Thread projectileThread = new Thread(() -> attackPlantWithProjectile(row, (ECIZombie) zombie, zombieLabel));
            synchronized (zombieToProjectileThreadMap) {
                zombieToProjectileThreadMap.put(zombieThread, projectileThread);
            }
            projectileThread.start();
        }
    }

    /**
     * Retrieves the first zombie in the specified row.
     *
     * This method returns an {@code ArrayList<Object>} containing the following elements:
     * <ul>
     *   <li>The {@code Thread} associated with the first zombie.</li>
     *   <li>The {@code JLabel} representing the zombie's visual component.</li>
     *   <li>The {@code Zombie} object representing the zombie's data.</li>
     * </ul>
     *
     * @param row the row number to search for the first zombie.
     * @return an {@code ArrayList<Object>} containing the first zombie's thread, label, and object;
     *         or an empty list if there are no zombies in the specified row.
     */
    public ArrayList<Object> getFirstZombieInRow(int row) {
        ArrayList<Object> zombieTarget = new ArrayList<>();
        List<Thread> threadsInRow;
        synchronized (zombieThreadsByRow) {
            threadsInRow = zombieThreadsByRow.get(row);
        }
        if (threadsInRow != null && !threadsInRow.isEmpty()) {
            Thread firstThread = threadsInRow.get(0);
            JLabel zombieLabel;
            synchronized (threadToLabelMap) {
                zombieLabel = threadToLabelMap.get(firstThread);
            }
            Zombie zombie;
            synchronized (threadToZombieMap) {
                zombie = threadToZombieMap.get(firstThread);
            }
            zombieTarget.add(firstThread);
            zombieTarget.add(zombieLabel);
            zombieTarget.add(zombie);

         
        }
        return zombieTarget;
    }

    /**
     * Lógica principal del zombie.
     * Solo maneja el movimiento y ataques directos para zombies que no son ECIZombie.
     */
    /**
     * Executes the main logic for a zombie's behavior within the game.
     * <p>
     * This method handles the movement and actions of a zombie based on its type.
     * Regular zombies move towards the nearest plant in their row, attack it when adjacent,
     * and handle interactions with lawnmowers. Special zombies like {@code Brainstein} and
     * {@code ECIZombie} have unique behaviors: {@code Brainstein} generates resources,
     * while {@code ECIZombie} attacks using projectiles managed in separate threads.
     * </p>
     *
     * @param row         the row number where the zombie is located
     * @param zombie      the zombie instance performing actions
     * @param zombieLabel the JLabel representing the zombie in the user interface
     */
    private void zombieLogic(int row, Zombie zombie, JLabel zombieLabel) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Verificar si el juego está pausado
                synchronized (pauseLock) {
                    while (isPaused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
                if (zombie instanceof Brainstein) {
                    // Solo genera recursos
                    ((Brainstein) zombie).generateResource(row);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    continue;
                }
    
                // Recalcular siempre la planta más cercana
                int plantCol = game.getFirstPlantInRow(row);
    
                if (plantCol == -1) {
                    // No hay plantas
                    // El objetivo es llegar a la columna 0, pero si aparece una planta durante el camino,
                    // debemos abortar y perseguir la planta.
                    boolean reached = moveZombie(row, 0, zombieLabel, true);
                    if (!reached) {
                        // Significa que durante el movimiento aparecieron plantas
                        // Volver al inicio del while para recalcular
                        continue;
                    }
    
                    // Llegamos a la col 0
                    boolean hadLawnmower = game.getLawnmowerInRow(row);
                    if (hadLawnmower) {
                        game.removeZombiesInRow(row);
                        terminateZombiesInRow(row);
                        garden.deleteLawnmover(row);
                        return;
                    } else {
                        // Sin podadora
                        if (game.getWinner().equals("") && !zombie.getKilledByLawnmower()) {
                            game.setWinner("Zombies");
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(garden.getMainPanel(), "¡Los zombies han ganado!");
                                garden.dispose(); 
                                POOBvsZombiesGUI menu = new POOBvsZombiesGUI();
                                menu.setVisible(true);
                            });
                        }
                        break; 
                    }
                } else {
                    // Hay planta
                    // Movernos hacia plantCol+1, con verificación constante.
                    int targetCol = plantCol + 1;
                    boolean reached = moveZombie(row, targetCol, zombieLabel, true);
                    if (!reached) {
                        // Apareció otra planta más a la derecha o cambió la situación,
                        // volver a recalcular planta en la siguiente iteración
                        continue;
                    }
    
                    // Si llegamos aquí, logramos llegar a targetCol
                    // Recalcular planta por si cambió justo al llegar
                    int newPlantCol = game.getFirstPlantInRow(row);
                    if (newPlantCol == plantCol) {
                        // La planta sigue siendo la misma, si no es ECIZombie, atacamos
                        if (!(zombie instanceof ECIZombie)) {
                            attackPlant(row, plantCol, zombie, zombieLabel);
                        }
                    } else {
                        // Cambió la planta, volver al inicio del while a recalcular
                        continue;
                    }
                }
            }
        } finally {
            cleanupZombieThread(row, zombieLabel);
        }
    }

    /**
     * Limpia los datos y hilos asociados a un zombie al terminar su ejecución.
     */
    /**
     * Cleans up resources associated with a zombie thread after it has finished its execution.
     * This includes removing the thread from tracking collections, interrupting any associated
     * projectile threads (for ECIZombie types), and removing the zombie's label from the GUI.
     *
     * @param row         the row index where the zombie was located
     * @param zombieLabel the JLabel representing the zombie in the GUI
     */
    private void cleanupZombieThread(int row, JLabel zombieLabel) {
        synchronized (zombieThreadsByRow) {
            List<Thread> threads = zombieThreadsByRow.get(row);
            if (threads != null) {
                threads.remove(Thread.currentThread());
                if (threads.isEmpty()) {
                    zombieThreadsByRow.remove(row);
                }
            }
        }
        synchronized (threadToLabelMap) {
            threadToLabelMap.remove(Thread.currentThread());
        }
        synchronized (threadToZombieMap) {
            threadToZombieMap.remove(Thread.currentThread());
        }
        // Si es un ECIZombie, también removemos su hilo de proyectil
        synchronized (zombieToProjectileThreadMap) {
            Thread projectileThread = zombieToProjectileThreadMap.remove(Thread.currentThread());
            if (projectileThread != null) {
                projectileThread.interrupt();
            }
        }
        SwingUtilities.invokeLater(() -> {
            Container parent = zombieLabel.getParent();
            if (parent != null) {
                parent.remove(zombieLabel);
                parent.revalidate();
                parent.repaint();
            }
        });
    }

    /**
     * Calcula la columna actual del zombie basándose en su posición X.
     */
    /**
     * Calculates the current column index based on the given x-coordinate position.
     *
     * @param xPosition the x-coordinate position
     * @return the current column index corresponding to the xPosition
     */
    @SuppressWarnings("unused")
    private int getCurrentColumn(int xPosition) {
        int cellWidth = 80;
        int gridStartX = 40;
        return Math.max(0, (xPosition - gridStartX) / cellWidth);
    }

    /**
     * Moves a zombie JLabel from its current position to a target column.
     *
     * @param row The row in which the zombie is located.
     * @param targetCol The target column to which the zombie should move.
     * @param zombieLabel The JLabel representing the zombie to be moved.
     */
    private boolean moveZombie(int row, int targetCol, JLabel zombieLabel,boolean checkForNewPlants) {
        int cellWidth = 80;
        int startX = zombieLabel.getX(); // Posición actual X
        int startY = zombieLabel.getY();
        int endX = 40 + targetCol * cellWidth;

        int currentX = startX;
        int currentY = startY;

        // Guardamos el plantCol inicial antes de mover
        int initialPlantCol = game.getFirstPlantInRow(row);

        while (currentX > endX) {
            // Verificar si el juego está pausado
            synchronized (pauseLock) {
                while (isPaused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
            currentX -= 5;
            int finalX = currentX;
            int finalY = currentY;
            SwingUtilities.invokeLater(() -> {
                zombieLabel.setLocation(finalX, finalY);
            });
            try {
                // Espera de 150ms para controlar la velocidad de movimiento
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            if (checkForNewPlants) {
                int currentPlantCol = game.getFirstPlantInRow(row);
                // Si la planta más cercana cambió y es distinta a la que usábamos como referencia
                // significa que apareció una planta más a la derecha (más grande currentPlantCol)
                // o simplemente cambió la situación, debemos redirigir el zombie
                if (currentPlantCol != initialPlantCol) {
                    // Abortar este movimiento para recalcular objetivo
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Attacks a plant at the specified position with the given zombie.
     * 
     * @param row The row position of the plant.
     * @param plantCol The column position of the plant.
     * @param zombie The zombie that will attack the plant.
     * @param zombieLabel The label representing the zombie in the UI.
     */
    
    
    private void attackPlant(int row, int plantCol, Zombie zombie, JLabel zombieLabel) {
        // Obtener la planta desde el dominio
        Plant plant = game.getPlantAt(row, plantCol);
        if (plant == null) {
            // Ya no hay planta (otro zombie la mató?), volver
            return;
        }

        System.out.println("Zombie intentando atacar planta en (" + row + ", " + plantCol + ").");

        // Si la planta es una PotatoMine y está activada
        if (plant instanceof PotatoMine) {
            PotatoMine pm = (PotatoMine) plant;
            synchronized (pm) { // Sincronizar para evitar múltiples explosiones
                if (pm.isActivated() && !pm.hasExploded()) {
                    pm.explode(); // Marca la PotatoMine como explotada
                    System.out.println("PotatoMine activada detectada. Explosión iniciada.");

                    // Cambiar la imagen a explosión
                    JLabel plantLabel = garden.getPlantLabelAt(row, plantCol);
                    if (plantLabel != null) {
                        List<String> plantInfo = GardenMenu.PLANTS_VIEW.get(3); // Índice 3 para PotatoMine
                        String explodedImagePath = plantInfo.get(5); // Sexta posición para explosión

                        ImageIcon explodedIcon = new ImageIcon(getClass().getResource(explodedImagePath));
                        if (explodedIcon.getIconWidth() == -1) {
                            System.err.println("No se pudo cargar la imagen de explosión: " + explodedImagePath);
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                plantLabel.setIcon(explodedIcon);
                                plantLabel.revalidate();
                                plantLabel.repaint();
                                System.out.println("Imagen de explosión asignada al JLabel.");
                            });
                        }

                        // Crear y mostrar "BOOM!!!" en la pantalla
                        JLabel boomLabel = new JLabel("<html><div style='text-align: center; color: red; font-size: 48pt; font-weight: bold;'>BOOM!!!</div></html>", SwingConstants.CENTER);
                        // Posicionar el "BOOM!!!" sobre la planta
                        boomLabel.setBounds(plantLabel.getX(), plantLabel.getY(), 200, 100); // Ajusta según tu interfaz
                        boomLabel.setOpaque(false);

                        SwingUtilities.invokeLater(() -> {
                            garden.getMainPanel().add(boomLabel);
                            garden.getMainPanel().setComponentZOrder(boomLabel, 0); // Traer al frente
                            garden.getMainPanel().revalidate();
                            garden.getMainPanel().repaint();
                            System.out.println("BOOM!!! mostrado en pantalla para PotatoMine en (" + row + ", " + plantCol + ").");
                        });

                        // Programar la eliminación de la imagen de explosión y "BOOM!!!" después de 2 segundos
                        Timer removalTimer = new Timer(2000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                SwingUtilities.invokeLater(() -> {
                                    // Eliminar el "BOOM!!!" de la interfaz
                                    garden.getMainPanel().remove(boomLabel);

                                    // Remover la imagen de explosión de la PotatoMine
                                    plantLabel.setIcon(null); // Opcional: Puedes establecer una imagen vacía o una imagen por defecto
                                    plantLabel.revalidate();
                                    plantLabel.repaint();

                                    // Remover la planta de la interfaz gráfica
                                    garden.removePlantAt(row, plantCol); // Método para eliminar el JLabel de la planta

                                    // Actualizar la interfaz
                                    garden.getMainPanel().revalidate();
                                    garden.getMainPanel().repaint();

                                    System.out.println("Explosión y BOOM!!! eliminados de la interfaz.");

                                    // Remover la PotatoMine del dominio después de la explosión
                                    game.removeEntity(row, plantCol);
                                    game.deleteEntity(row, plantCol);
                                    System.out.println("PotatoMine removida del dominio en (" + row + ", " + plantCol + ").");
                                });
                            }
                        });
                        removalTimer.setRepeats(false); // Ejecutar una sola vez
                        removalTimer.start();
                    }

                    // Terminar el hilo del zombie que atacó la PotatoMine
                    Thread zombieThread = findThreadByLabel(zombieLabel);
                    if (zombieThread != null) {
                        terminateZombie(zombieThread);
                        System.out.println("Zombie que atacó la PotatoMine ha sido terminado.");
                    }

                    return; // Salir del método ya que la PotatoMine explotó
                }
            }
        }


        
            // Si no es una PotatoMine activada, proceder con el ataque normal
            // Mientras la planta esté viva, atacarla cada 0.5s
            while (!plant.isDead() && zombie.getHealth() > 0 && !Thread.currentThread().isInterrupted()) {
                plant.takeDamage(zombie.getDamage());
                if (plant.isDead()) {
                    // Planta muerta, remover del dominio y de la interfaz
                    Player plantsPlayer = game.getPlayerOne();
                    plantsPlayer.setScore(plantsPlayer.getScore() - plant.getCost());
                    game.removeEntity(row, plantCol);
                    SwingUtilities.invokeLater(() -> {
                        garden.removePlantAt(row, plantCol);
                        System.out.println("Planta removida del dominio y de la interfaz.");
                    });
                    break;
                }
        
                // Esperar 0.5 segundos antes del próximo ataque
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    
    /**
     * Registra una PotatoMine y configura su activación después de 14 segundos.
     *
     * @param row Fila de la PotatoMine.
     * @param col Columna de la PotatoMine.
     */
    public void registerPotatoMine(int row, int col) {
        String key = row + "," + col;
        Timer activationTimer = new Timer(14000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activatePotatoMine(row, col);
            }
        });
        activationTimer.setRepeats(false);
        activationTimer.start();
        potatoMineTimers.put(key, activationTimer);
        System.out.println("Timer de activación iniciado para PotatoMine en (" + row + ", " + col + ").");
    }


    /**
     * Activa la PotatoMine después de 14 segundos.
     *
     * @param row Fila de la PotatoMine.
     * @param col Columna de la PotatoMine.
     */
    public void activatePotatoMine(int row, int col) {
        // Obtener el JLabel de la planta
        JLabel plantLabel = garden.getPlantLabelAt(row, col);
        if (plantLabel == null) {
            System.err.println("No se encontró la JLabel para la PotatoMine en (" + row + ", " + col + ").");
            return;
        }

        // Cambiar a la imagen activada
        List<String> plantInfo = GardenMenu.PLANTS_VIEW.get(3); // Índice 3 para PotatoMine
        String activatedGifPath = plantInfo.get(4); // Quinta posición para activación

        ImageIcon activatedIcon = new ImageIcon(getClass().getResource(activatedGifPath));
        if (activatedIcon.getIconWidth() == -1) {
            System.err.println("No se pudo cargar la imagen activada: " + activatedGifPath);
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            plantLabel.setIcon(activatedIcon);
            plantLabel.revalidate();
            plantLabel.repaint();
        });

        // Actualizar el estado de la planta en el dominio del juego
        Plant plant = game.getPlantAt(row, col);
        if (plant instanceof PotatoMine) {
            ((PotatoMine) plant).activate();
            System.out.println("PotatoMine activada en (" + row + ", " + col + ").");
        }
    }



     /**
     * Encuentra el Thread asociado a un JLabel de zombie.
     *
     * @param zombieLabel JLabel del zombie.
     * @return Thread correspondiente o null si no se encuentra.
     */
    private Thread findThreadByLabel(JLabel zombieLabel) {
        synchronized (threadToLabelMap) {
            for (Map.Entry<Thread, JLabel> entry : threadToLabelMap.entrySet()) {
                if (entry.getValue().equals(zombieLabel)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }



    // método para obtener la posición X de un zombie, dado su hilo.
    /**
     * Retrieves the X position of the zombie associated with the given thread.
     *
     * @param zombieThread the thread associated with the zombie whose X position is to be retrieved
     * @return the X position of the zombie
     * @throws IllegalArgumentException if the zombie thread is not found or has no associated JLabel
     */
    public int getZombieXPosition(Thread zombieThread) {
        JLabel zombieLabel;
        synchronized (threadToLabelMap) {
            zombieLabel = threadToLabelMap.get(zombieThread);
        }
        if (zombieLabel != null) {
            return zombieLabel.getX();
        } else {
            throw new IllegalArgumentException("Zombie thread not found or has no associated JLabel.");
        }
    }



    /**
     * Terminates the specified zombie thread by interrupting it.
     *
     * @param thread the thread representing the zombie to be terminated
     */
    public void terminateZombie(Thread thread) {
        thread.interrupt();
    }


    /**
     * Notifies the game that a zombie has reached the house in the specified row.
     * If there is a lawnmower in the row, it removes all zombies in that row and deletes the lawnmower.
     * Otherwise, it calculates the final scores and ends the game with a message indicating that the zombies have won.
     *
     * @param row the row in which a zombie has reached the house
     */
    @SuppressWarnings("unused")
    private void notifyZombieReachedHouse(int row) {
        if (game.getLawnmowerInRow(row)) {
            game.removeZombiesInRow(row);
            terminateZombiesInRow(row);
            garden.deleteLawnmover(row);
        } else {
            game.calculateScores(); // Calcula puntajes finales
            String winnerMessage = "¡Los zombies han ganado! Llegaron a la casa.";
            game.endGame(winnerMessage); // Finaliza el juego
        }
    }
    
     /**
     * Método que maneja el ataque con proyectiles de un ECIZombie.
     * Este método corre en un hilo separado y asegura que solo haya un proyectil
     * activo por ECIZombie.
     */
    /**
     * Attacks the first plant in the specified row with a projectile from the given zombie.
     * The method continuously checks for the presence of plants in the row and fires projectiles
     * at them until the zombie is interrupted or its health drops to zero.
     *
     * @param row The row in which to attack the plant.
     * @param zombie The zombie that is attacking the plant.
     * @param zombieLabel The JLabel representing the zombie in the graphical interface.
     */
    private void attackPlantWithProjectile(int row, ECIZombie zombie, JLabel zombieLabel) {
        try {
            JPanel mainPanel = garden.getMainPanel(); // Debes crear un getter en GardenMenu que retorne el panel principal
            while (!Thread.currentThread().isInterrupted() && zombie.getHealth() > 0) {
                // Verificar si el juego está pausado
                synchronized (pauseLock) {
                    while (isPaused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
                int plantCol = game.getFirstPlantInRow(row);
                if (plantCol == -1) {
                    Thread.sleep(1000);
                    continue;
                }

                Plant targetPlant = game.getPlantAt(row, plantCol);
                if (targetPlant == null || targetPlant.isDead()) {
                    Thread.sleep(1000);
                    continue;
                }

                // Obtener la etiqueta de la planta
                JLabel plantLabel = garden.getPlantLabelAt(row, plantCol);
                if (plantLabel == null) {
                    Thread.sleep(1000);
                    continue;
                }

                // Convertir posiciones al mismo sistema de coordenadas (el del panel principal)
                // Posición absoluta del zombie en panel:
                java.awt.Point zombiePos = SwingUtilities.convertPoint(zombieLabel.getParent(), 
                                                                    zombieLabel.getLocation(), 
                                                                    mainPanel);

                // Posición absoluta de la planta en panel:
                java.awt.Point plantPos = SwingUtilities.convertPoint(plantLabel.getParent(), 
                                                                    plantLabel.getLocation(), 
                                                                    mainPanel);

                int zombieCenterX = zombiePos.x + zombieLabel.getWidth()/2;
                int zombieCenterY = zombiePos.y + zombieLabel.getHeight()/2;
                int plantCenterX = plantPos.x + plantLabel.getWidth()/2;
                int plantCenterY = plantPos.y + plantLabel.getHeight()/2;

                // Crear y mostrar el proyectil en el mismo panel que zombie y planta
                JLabel projectileLabel = createProjectileLabel(mainPanel, zombieCenterX - 15, zombieCenterY - 15);

                // Mover el proyectil hacia la planta
                boolean hit = moveProjectile(projectileLabel, zombieCenterX, plantCenterX, zombieCenterY, plantCenterY);

                // Eliminar el proyectil de la interfaz
                SwingUtilities.invokeLater(() -> {
                    Container parent = projectileLabel.getParent();
                    if (parent != null) {
                        parent.remove(projectileLabel);
                        parent.revalidate();
                        parent.repaint();
                    }
                });

                if (hit) {
                    targetPlant.takeDamage(ECIZombie.DAMAGE);
                    if (targetPlant.isDead()) {
                        game.removeEntity(row, plantCol);
                        SwingUtilities.invokeLater(() -> {
                            garden.removePlantAt(row, plantCol);
                        });
                    }
                }

                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Crea un JLabel para el proyectil y lo agrega a la interfaz gráfica.
     */
    /**
     * Creates a JLabel representing a projectile with a specified starting position.
     * The projectile is represented by an image icon which is scaled to a specific size.
     * The JLabel is then added to the garden's content pane and its Z-order is set.
     *
     * @param startX the starting X coordinate of the projectile
     * @param startY the starting Y coordinate of the projectile
     * @return the JLabel representing the projectile
     */
    private JLabel createProjectileLabel(JPanel mainPanel, int startX, int startY) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/blackPea.png"));
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
    
        JLabel projectileLabel = new JLabel(scaledIcon);
        projectileLabel.setSize(30, 30);
        projectileLabel.setLocation(startX, startY);
    
        SwingUtilities.invokeLater(() -> {
            mainPanel.add(projectileLabel);
            mainPanel.setComponentZOrder(projectileLabel, 0);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    
        return projectileLabel;
    }

    /**
     * Mueve el proyectil desde la posición inicial hasta la posición objetivo.
     * Retorna true si el proyectil alcanzó la planta, false si fue interrumpido.
     */
    /**
     * Moves a projectile from a starting position to a target position.
     *
     * @param projectileLabel the JLabel representing the projectile
     * @param startX the starting X coordinate of the projectile
     * @param targetX the target X coordinate of the projectile
     * @param startY the starting Y coordinate of the projectile
     * @param targetY the target Y coordinate of the projectile
     * @return true if the projectile reached the target position, false if the thread was interrupted
     */
    private boolean moveProjectile(
        JLabel projectileLabel,
        int startX,
        int targetX,
        int startY,
        int targetY
    ) {
        int currentX = startX;
        int currentY = startY;

        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double steps = distance / 5;
        double stepX = deltaX / steps;
        double stepY = deltaY / steps;

        for (int i = 0; i < steps; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return false;
            }

            currentX += stepX;
            currentY += stepY;
            int finalX = (int) currentX;
            int finalY = (int) currentY;

            SwingUtilities.invokeLater(() -> {
                projectileLabel.setLocation(finalX, finalY);
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // Impacto con la planta
        return true;
    }
    
    /**
     * Pausa todos los hilos de zombies.
     */
    public void pauseZombies() {
        synchronized (pauseLock) {
            isPaused = true;
            System.out.println("ZombieThreadManager: Juego en pausa.");
        }
    }

    /**
     * Reanuda todos los hilos de zombies.
     */
    public void resumeZombies() {
        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
            System.out.println("ZombieThreadManager: Juego reanudado.");
        }
    }

    /**
     * Pausa un ECIZombie al detener su hilo de proyectiles.
     */
    @SuppressWarnings("unused")
    private void pauseECIZombie(Thread zombieThread) {
        Thread projectileThread = zombieToProjectileThreadMap.get(zombieThread);
        if (projectileThread != null) {
            projectileThread.interrupt();
        }
    }

}