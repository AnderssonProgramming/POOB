
package domain;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import presentation.GardenMenu;

/**
 * The ProjectTileThreadManager class represents the resource that shoot Peashooter and ECIZombie.
 * It serves as a controller for the projectile logic in the game.
 */
public class ProjectTileThreadManager {
    private POOBvsZombies game;
    @SuppressWarnings("unused")
    private GardenMenu garden;
    private ZombieThreadManager zombieThreadManager;
    @SuppressWarnings("unused")
    private JLabel projectTileLabel;

    private volatile boolean isPaused = false; // Variable para controlar la pausa
    private final Object pauseLock = new Object(); // Objeto para sincronización

    /**
     * Manages the threads for projectiles in the POOB vs Zombies game.
     *
     * @param game The instance of the POOBvsZombies game.
     * @param garden The GardenMenu instance where the game is played.
     * @param zombieThreadManager The manager for zombie threads.
     */
    public ProjectTileThreadManager(POOBvsZombies game, GardenMenu garden, ZombieThreadManager zombieThreadManager) {
        this.game = game;
        this.garden = garden;
        this.zombieThreadManager = zombieThreadManager;
    }

    /**
     * Registers a projectile and starts a new thread to handle its logic.
     *
     * @param row The row position where the projectile is registered.
     * @param yPos The y-coordinate position of the projectile.
     * @param projectTile The projectile object to be registered.
     * @param graphicXPosition The x-coordinate position for the graphical representation of the projectile.
     * @param graficYPosition The y-coordinate position for the graphical representation of the projectile.
     */
    public void registerProjectTile(int row, int yPos, ProjectTile projectTile, int graphicXPosition, int graficYPosition) {
        Thread t = new Thread(() -> projectTileLogic(row, yPos, projectTile, graphicXPosition, graficYPosition));
        t.start();
    }

    /**
     * Manages the logic for a projectile in the game, targeting zombies in a specific row.
     *
     * @param row The row in which the projectile is being fired.
     * @param yPos The y-position of the projectile.
     * @param projectTile The projectile object.
     * @param graphicXPosition The x-position of the projectile in the graphical interface.
     * @param graficYPosition The y-position of the projectile in the graphical interface.
     */
    private void projectTileLogic(int row, int yPos, ProjectTile projectTile, int graphicXPosition, int graficYPosition) {
        while (true) {
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
            ArrayList<Object> firstZombie = zombieThreadManager.getFirstZombieInRow(row);

            if (firstZombie.isEmpty()) {
                // No hay zombies, esperar un segundo y volver a chequear
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                continue;
            }

            // Si sigue vivo, esperar 3 segundos y volver a disparar
            if (game.getEntity(row, yPos) == null) {
                Thread.currentThread().interrupt();
                return;
            }
            Thread targetZombieThread = (Thread) firstZombie.get(0);
            JLabel targetZombieLabel = (JLabel) firstZombie.get(1);
            Zombie targetZombie = (Zombie) firstZombie.get(2);

            

            // Mientras el zombie esté vivo, disparar
            while (targetZombie.health > 0) {
                // Crear un nuevo proyectil cada vez que vamos a disparar
                JLabel projectTileLabel = this.projectTileLabel = createAndRegisterProjectTile(graphicXPosition,graficYPosition, targetZombieLabel);

                int originXpos = graphicXPosition;
                int targetXpos = targetZombieLabel.getX();

                moveProjectTile(originXpos + 95, targetXpos, projectTileLabel, targetZombie, targetZombieThread);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

        }
    }

    /**
     * Creates and registers a projectile tile (Peashooter projectile) at the specified position.
     *
     * @param row The x-coordinate for the initial position of the projectile.
     * @param yPos The y-coordinate for the initial position of the projectile.
     * @param zombieLabel The JLabel representing the zombie, used to get the parent container.
     * @return The JLabel representing the projectile tile.
     */
    private JLabel createAndRegisterProjectTile(int row, int yPos, JLabel zombieLabel) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/pea.png"));
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Failed to load image: /resources/images/pea.png");
        }
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
    
        JLabel projectTileLabel = new JLabel(scaledIcon);
    
        projectTileLabel.setBounds(row+95, yPos+95, 30, 30);
    
        SwingUtilities.invokeLater(() -> {
            Container parent = zombieLabel.getParent();
            if (parent != null) {
                parent.setLayout(null); // Ensure absolute positioning
                parent.add(projectTileLabel);
                parent.setComponentZOrder(projectTileLabel, 0); // Bring to front if necessary
                parent.revalidate();
                parent.repaint();
            }
        });
        return projectTileLabel;
    }

    /**
     * Moves a projectile from its origin position to the target position, updating its location
     * on the GUI and handling the impact with the target zombie.
     *
     * @param originXpos The starting X position of the projectile.
     * @param targetXpos The target X position where the projectile should stop.
     * @param projectTileLabel The JLabel representing the projectile in the GUI.
     * @param targetZombie The zombie that the projectile is targeting.
     * @param targetZombieThread The thread managing the target zombie.
     */
    private void moveProjectTile(int originXpos, int targetXpos, JLabel projectTileLabel, Zombie targetZombie, Thread targetZombieThread) {
        int currentX = originXpos;
    
        while (currentX < targetXpos - 30) { // Subtract projectile width to stop before overlapping
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
            currentX += 5; // Move speed
            int finalX = currentX;
    
            SwingUtilities.invokeLater(() -> {
                projectTileLabel.setLocation(finalX, projectTileLabel.getY());
            });
    
            try {
                Thread.sleep(30); // Adjust for movement smoothness
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }   
        }
    
        // Upon impact
        targetZombie.takeDamage(Peashooter.DAMAGE);
    
        // Remove projectile from GUI
        SwingUtilities.invokeLater(() -> {
            Container parent = projectTileLabel.getParent();
            if (parent != null) {
                parent.remove(projectTileLabel);
                parent.revalidate();
                parent.repaint();
            }
        });
    
        // Check if zombie is dead
        if (targetZombie.getHealth() <= 0) {
            zombieThreadManager.terminateZombie(targetZombieThread);
        }
    }

    /**
     * Pausa todos los hilos de proyectiles.
     */
    public void pauseProjectiles() {
        synchronized (pauseLock) {
            isPaused = true;
            System.out.println("ProjectTileThreadManager: Juego en pausa.");
        }
    }

    /**
     * Reanuda todos los hilos de proyectiles.
     */
    public void resumeProjectiles() {
        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
            System.out.println("ProjectTileThreadManager: Juego reanudado.");
        }
    }
}