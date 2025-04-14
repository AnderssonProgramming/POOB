package presentation;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The PlayerVSMachine class represents a graphical user interface (GUI) for the Player vs Machine mode in the POOBvsZombies game.
 * It extends JFrame and provides various GUI components and actions for configuring and starting a game.
 * The GUI includes text fields for player name, match time, and horde number, as well as plant selection panels.
 */
public class PlayerVSMachine extends JFrame {

    // Domain Layer
    private POOBvsZombies poobvsZombies;

    // GUI Components
    private JTextField playerNameField, matchTimeField, hordeNumberField;
    private JButton startButton;
    private List<PlantPanel> plantPanelsList;

    /**
     * Constructor for the PlayerVSMachine class.
     * Initializes the game by preparing the necessary elements and actions.
     */
    public PlayerVSMachine() {
        prepareElements();
        prepareActions();
    }

    /**
     * Prepares and initializes the elements of the Player vs Machine window.
     * This method sets up the window configuration, background panel, text fields,
     * buttons, labels, and plant panels. It also adds the components to the background panel.
     */
    private void prepareElements() {
        // Window Configuration
        setTitle("Player vs Machine");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Initialize Components
        plantPanelsList = new ArrayList<>();

        // Text Fields
        playerNameField = createTextField("Player Name", 225, 235, 150, 30);
        matchTimeField = createTextField("Time", 230, 370, 60, 14);
        hordeNumberField = createTextField("Number", 305, 370, 60, 14);

        // Buttons
        startButton = new JButton("¡START!");
        startButton.setBounds(485, 355, 160, 27);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE);
        startButton.setEnabled(false);

        // Add Components to Panel
        backgroundPanel.add(playerNameField);
        backgroundPanel.add(matchTimeField);
        backgroundPanel.add(hordeNumberField);
        backgroundPanel.add(startButton);

        // Labels
        addLabels(backgroundPanel);

        // Plant Panels
        addPlantPanels(backgroundPanel);

        // Top Right Buttons
        addTopRightButtons(backgroundPanel);
    }

    /**
     * Prepares the actions for the player vs machine interface.
     * This method sets up the following actions:
     * - Window closing action: Hides the window and exits the application.
     * - Start button action: Initiates the start action when the start button is pressed.
     * - Text fields document listeners: Adds listeners to the player name, match time, and horde number text fields.
     */
    private void prepareActions() {
        // Window Closing Action
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                setVisible(false);
                System.exit(0);
            }
        });

        // Start Button Action
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                actionStart();
            }
        });

        // Text Fields Document Listeners
        addTextFieldListeners(playerNameField);
        addTextFieldListeners(matchTimeField);
        addTextFieldListeners(hordeNumberField);
    }

    // Action Methods
    /**
     * Initiates the start action for the Player vs Machine game mode.
     * 
     * This method collects input values from the user interface, validates them,
     * and creates an instance of the POOBvsZombies game with the specified parameters.
     * If the inputs are invalid or an error occurs, appropriate error messages are displayed.
     * 
     * @throws POOBvsZombiesException if the match time or horde number is less than 1,
     *                                or if the inputs are invalid.
     */
    private void actionStart() {
        try {
            // Collect Values
            String playerName = playerNameField.getText();
            int matchTime = Integer.parseInt(matchTimeField.getText());
            int hordeNumber = Integer.parseInt(hordeNumberField.getText());

            if (matchTime < 1 || hordeNumber < 1) {
                throw new POOBvsZombiesException(POOBvsZombiesException.TIME_LIMIT);
            }
            // Selected Plants
            ArrayList<String> selectedPlants = getSelectedItems(plantPanelsList);

            if (selectedPlants.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one plant.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create POOBvsZombies Instance
            poobvsZombies = new POOBvsZombies(
                    matchTime,
                    hordeNumber,
                    playerName,
                    selectedPlants);

            // Proceed to the next step (e.g., open GardenMenu)
            new GardenMenu(poobvsZombies).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            try{
                throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
            }
            catch (POOBvsZombiesException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper Methods
    /**
     * Creates a customized JTextField with specified placeholder text, position, and size.
     *
     * @param placeholder the placeholder text to be displayed in the text field
     * @param x the x-coordinate of the text field's position
     * @param y the y-coordinate of the text field's position
     * @param width the width of the text field
     * @param height the height of the text field
     * @return a customized JTextField with the specified properties
     */
    private JTextField createTextField(String placeholder, int x, int y, int width, int height) {
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Arial", Font.BOLD, 13));
        textField.setBounds(x, y, width, height);
        textField.setBorder(null);
        textField.setBackground(new Color(228, 206, 171));
        textField.setForeground(new Color(134, 119, 94));
        return textField;
    }

    /**
     * Adds focus listeners to a JTextField to handle placeholder text behavior.
     * When the text field gains focus, if the current text is the placeholder, it clears the text.
     * When the text field loses focus, if the current text is empty, it sets the text back to the placeholder.
     * Also calls the checkFields() method when the text field loses focus.
     *
     * @param textField the JTextField to which the focus listeners will be added
     */
    private void addTextFieldListeners(JTextField textField) {
        String placeholder = textField.getText();
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                }
                checkFields();
            }
        });
    }

    /**
     * Adds various labels to the specified JPanel.
     *
     * @param panel the JPanel to which the labels will be added
     *
     * The labels added are:
     * - Info Label: Provides information about the game behavior and configuration options.
     * - "SELECT PLANTS" Label: Indicates the section where players can select their plants.
     * - Player Name Label: Label for the player's name input field.
     * - Time Label: Label for the time configuration input field.
     * - Number Label: Label for the number configuration input field.
     */
    private void addLabels(JPanel panel) {
        // Info Label
        JLabel infoLabel = new JLabel("<html>"
                + "Machine will adopt its original<br>"
                + "behavior from PvsZ original game.<br>"
                + "You can configure your name, hordes <br>"
                + "duration and number. You can also <br>"
                + "select your plants to play. Have fun!"
                + "</html>");
        infoLabel.setBounds(485, 215, 500, 100);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(infoLabel);

        // "SELECT PLANTS" Label
        JLabel selectPlantLabel = new JLabel("<html>SELECT PLANTS</html>");
        selectPlantLabel.setBounds(385, 525, 500, 100);
        selectPlantLabel.setForeground(Color.WHITE);
        selectPlantLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        panel.add(selectPlantLabel);

        // Player Name Label
        JLabel playerNameLabel = new JLabel("PlayerName:");
        playerNameLabel.setBounds(260, 205, 150, 30);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(playerNameLabel);

        // Time Label
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(245, 345, 150, 30);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        panel.add(timeLabel);

        // Number Label
        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(320, 345, 150, 30);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 10));
        panel.add(numberLabel);
    }

    /**
     * Adds plant panels to the specified parent panel.
     *
     * This method creates a plant selection panel, sets its bounds, layout, and opacity,
     * and then iterates through the list of plants defined in GardenMenu.PLANTS_VIEW.
     * For each plant, it creates a PlantPanel with the plant's image path and name,
     * adds it to the plant selection panel, and also adds it to the plantPanelsList.
     * Finally, the plant selection panel is added to the specified parent panel.
     *
     * @param panel the parent panel to which the plant selection panel will be added
     */
    private void addPlantPanels(JPanel panel) {
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(115, 590, 600, 150);
        plantSelectionPanel.setLayout(new FlowLayout());
        plantSelectionPanel.setOpaque(false);

        for (List<String> plant : GardenMenu.PLANTS_VIEW) {
            String imagePath = plant.get(1);
            String plantName = plant.get(0);
            PlantPanel plantPanel = new PlantPanel(imagePath, plantName);
            plantPanelsList.add(plantPanel);
            plantSelectionPanel.add(plantPanel);
        }
        panel.add(plantSelectionPanel);
    }

    /**
     * Adds a button to the top-right corner of the specified JPanel.
     * The button uses an image icon and is configured to be transparent and borderless.
     * When clicked, the button will close the current window and open the main menu.
     *
     * @param panel the JPanel to which the button will be added
     */
    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "/images/buttons/return-icon.png";
        int x = 830;
        int y = 5;
        int buttonSize = 40;

        
        ImageIcon icon = new ImageIcon(getClass().getResource(buttonImagePath));
        JButton button = new JButton(
                new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH)));
            button.setBounds(x, y, buttonSize, buttonSize);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Back to main menu
                    dispose(); // Close the current window
                    new POOBvsZombiesGUI().setVisible(true);
                }
            });

            panel.add(button);
        } 


    /**
     * Checks if the necessary fields are filled and enables or disables the start button accordingly.
     * 
     * This method performs the following checks:
     * 1. At least one plant is selected from the list of plant panels.
     * 2. The player name field is not empty and does not contain the default text "Player Name".
     * 3. The match time field is not empty and does not contain the default text "Time".
     * 4. The horde number field is not empty and does not contain the default text "Number".
     * 
     * If all the above conditions are met, the start button is enabled. Otherwise, it is disabled.
     */
    private void checkFields() {
        boolean atLeastOnePlantSelected = false;
        for (PlantPanel plantPanel : plantPanelsList) {
            if (plantPanel.isSelected()) {
                atLeastOnePlantSelected = true;
                break;
            }
        }

        boolean playerNameFilled = !playerNameField.getText().isEmpty()
                && !playerNameField.getText().equals("Player Name");
        boolean matchTimeFilled = !matchTimeField.getText().isEmpty()
                && !matchTimeField.getText().equals("Time");
        boolean hordeNumberFilled = !hordeNumberField.getText().isEmpty()
                && !hordeNumberField.getText().equals("Number");

        startButton.setEnabled(atLeastOnePlantSelected && playerNameFilled && matchTimeFilled && hordeNumberFilled);
    }

    /**
     * Retrieves the names of the selected plants from a list of PlantPanel objects.
     *
     * @param panels the list of PlantPanel objects to check for selection
     * @return an ArrayList containing the names of the selected plants
     */
    private ArrayList<String> getSelectedItems(List<PlantPanel> panels) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (PlantPanel panel : panels) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getPlantName());
            }
        }
        return selectedItems;
    }

    // Custom Classes
    /**
     * BackgroundPanel is a custom JPanel that displays a background image.
     * The image is loaded from the specified file path and scaled to fit the panel.
     */
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Usar el cargador de clases para obtener el recurso
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/menu/PlayervsMachineMenu.png");
            if (inputStream != null) {
                backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/menu/PlayervsMachineMenu.png")).getImage();
            } else {
                System.out.println("Recurso no encontrado");
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    /**
     * The PlantPanel class represents a custom JPanel that displays a plant image
     * and allows the user to select or deselect the plant by clicking on it.
     * The panel changes its appearance based on the selection state.
     */
    class PlantPanel extends JPanel {
        private boolean selected = false;
        private Image plantImage;
        private String plantPath;
        private String plantName;
    
        /**
         * Constructs a PlantPanel with the specified image path and plant name.
         *
         * @param imagePath the path to the image file for the plant
         * @param plantName the name of the plant
         */
        public PlantPanel(String imagePath, String plantName) {
            this.plantPath = imagePath;
            this.plantName = plantName;
            this.plantImage = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);
    
            // Change selection state on click
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selected = !selected; // Toggle state
                    repaint();
                    checkFields();
                }
            });
        }
    
        public boolean isSelected() {
            return selected;
        }
    
        public String getPlantName() {
            return plantName;
        }
    
        public String getPlantPath() {
            return plantPath;
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (plantImage != null) {
                g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
            }
            if (selected) {
                g.setColor(new Color(0, 255, 0, 128)); // Semitransparente verde
            } else {
                g.setColor(new Color(255, 0, 0, 128)); // Semitransparente rojo
            }
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * The main method that serves as the entry point for the application.
     * It schedules a job for the event-dispatching thread to create and show
     * the PlayerVSMachine frame.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerVSMachine frame = new PlayerVSMachine();
            frame.setVisible(true);
        });
    }
}