package presentation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.POOBvsZombies;
import domain.POOBvsZombiesException;

/**
 * POOBvsZombiesGUI is the main graphical user interface for the POOBvsZombies game.
 * It extends JFrame and sets up the main menu with various options for the user to choose from.
 * The GUI includes buttons for different game modes and top-right buttons for importing and opening game states.
 * It also supports background music playback.
 */
public class POOBvsZombiesGUI extends JFrame {

    private static Clip clip;

    /**
     * POOBvsZombiesGUI is the main graphical user interface for the POOB vs Zombies game.
     * It sets up the main menu with options for different game modes and handles the
     * transitions to the respective game mode menus.
     */
    public POOBvsZombiesGUI() {
        setTitle("POOBvsZombies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window on the screen

        // Custom panel with background image
        JPanel panel = new JPanel() {
        Image backgroundImage = new ImageIcon(getClass().getResource("/images/menu/modalityMenu.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Absolute positioning

        // Create buttons with text on them
        JButton PlayerVsPlayerButton = createMenuButton("Player vs Player", "SURVIVAL", 242);
        JButton playerVsMachineButton = createMenuButton("Player vs Machine", "ORIGINAL", 362);
        JButton machineVsMachineButton = createMenuButton("Machine vs Machine", "SURVIVAL", 472);

        // Add buttons to panel
        panel.add(PlayerVsPlayerButton);
        panel.add(playerVsMachineButton);
        panel.add(machineVsMachineButton);

        // Add action listeners for each button
        PlayerVsPlayerButton.addActionListener(e -> {
            // Open Player vs Player Menu
            PlayerVsPlayer PlayerVsPlayerMenu = new PlayerVsPlayer();
            PlayerVsPlayerMenu.setVisible(true);
            dispose();
        });

        playerVsMachineButton.addActionListener(e -> {
            // Open Player vs Machine Menu
            PlayerVSMachine playerVsMachineMenu = new PlayerVSMachine();
            playerVsMachineMenu.setVisible(true);
            dispose(); // Close the main menu
        });

        machineVsMachineButton.addActionListener(e -> {
            // Open Machine vs Machine menu
            MachineVSMachine machineVSMachineMenu = new MachineVSMachine();
            machineVSMachineMenu.setVisible(true);
            dispose();
        });

        // Add panel to frame
        add(panel);
        addTopRightButtons(panel);

        // Play background music if it is not already playing
        playMusic("/sound/pvzSound.wav");
    }

    // Helper method to create buttons with consistent styling
    /**
     * Creates a menu button with specified main text, subtext, and vertical position.
     *
     * @param mainText The main text to be displayed on the button.
     * @param subText The subtext to be displayed below the main text on the button.
     * @param yPosition The vertical position of the button.
     * @return A JButton configured with the specified text, position, and styles.
     */
    private static JButton createMenuButton(String mainText, String subText, int yPosition) {
        JButton button = new JButton();

        // Use HTML to create multiline text
        button.setText("<html><center>" + mainText + "<br><font color='#FFFF00' size='4'>" + subText + "</font></center></html>");

        button.setBounds(343, yPosition, 250, 90);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        button.setBackground(new Color(110, 52, 48)); // Button background
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(120, 60, 50)); // Slightly lighter tan
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(110, 52, 48)); // Original tan
            }
        });

        return button;
    }

    /**
     * Adds buttons to the top-right corner of the specified panel.
     * The buttons are created using images specified in the buttonImagePaths array.
     * Each button is assigned an action listener that displays a message dialog when clicked.
     *
     * @param panel the JPanel to which the buttons will be added
     */
    private void addTopRightButtons(JPanel panel) {
        String[] buttonImagePaths = {
                "/images/buttons/import-icon.png", // Import
                "/images/buttons/open-icon.png" // Open
        };

        int x = 40;
        int y = 5;
        int buttonSize = 40;

        for (String imagePath : buttonImagePaths) {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            JButton button = new JButton(
                    new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
            button.setBounds(x, y, buttonSize, buttonSize);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            // Agregar eventos de acción a los botones
            if (imagePath.contains("import-icon")) {
                button.addActionListener(e -> {
                    // Implementar funcionalidad para exportar el estado del juego
                    JOptionPane.showMessageDialog(this, "Funcionalidad de importar aún no implementada.", "Importar", JOptionPane.INFORMATION_MESSAGE);
                });
            }

            if (imagePath.contains("open-icon")) {
            button.addActionListener(e -> {
                javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        POOBvsZombies loadedGame = POOBvsZombies.loadGame(file);
                        GardenMenu loadedMenu = new GardenMenu(loadedGame);
                        loadedMenu.reloadEntitiesUI();
                        loadedMenu.restoreGameState(loadedGame.getRestoredIndex(), loadedGame.getRestoredRemaining(), loadedGame.getRestoredPaused());
                        this.dispose();
                        loadedMenu.setVisible(true);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error al cargar la partida: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (POOBvsZombiesException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

            

            panel.add(button);
            x += 60; // Adjust X position for the next button
        }
    }

    // Method to play background music
    /**
     * Plays the music from the specified file path. If the music is already playing, it will not start again.
     * The music will loop continuously.
     *
     * @param filePath the path to the music file to be played
     */
    private void playMusic(String filePath) {
        if (clip == null) {
            try {
                InputStream audioSrc = getClass().getResourceAsStream(filePath);
                if (audioSrc != null) {
                    InputStream bufferedIn = new BufferedInputStream(audioSrc);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
                } else {
                    System.out.println("The specified music file was not found: " + filePath);
                }
            } catch (UnsupportedAudioFileException | LineUnavailableException | java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The main method that serves as the entry point for the application.
     * It schedules a job for the event-dispatching thread to create and show
     * the GUI for the POOBvsZombies application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POOBvsZombiesGUI POOBvsZombiesGUI = new POOBvsZombiesGUI();
            POOBvsZombiesGUI.setVisible(true);
        });
    }
}
