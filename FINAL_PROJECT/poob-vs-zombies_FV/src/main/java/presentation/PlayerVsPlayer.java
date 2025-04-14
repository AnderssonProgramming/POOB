package presentation;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The PlayerVsPlayer class represents a graphical user interface (GUI) for a player versus player mode in the POOBvsZombies game.
 * It extends JFrame and provides various GUI components for players to input their names, match time, and resources,
 * as well as select plants and zombies for the game.
 * 
 * The class includes methods to prepare the GUI elements, handle user actions, and validate input fields.
 * It also contains nested classes for custom panels and selectable items.
 * 
 */
/**
 * The PlayerVsPlayer class represents the player vs player game mode in the POOBvsZombies game.
 * It extends JFrame and provides a graphical user interface for setting up and starting a player vs player match.
 * 
 * The class includes the following main components:
 * - Domain Layer: An instance of POOBvsZombies to manage the game logic.
 * - GUI Components: Text fields for player names, match time, suns, and brains; buttons for starting the game; and panels for selecting plants and zombies.
 * 
 * The class provides methods to:
 * - Initialize and prepare the GUI elements.
 * - Set up actions for the GUI components.
 * - Start the game by collecting and validating input values, and initializing the game instance.
 * - Add custom components such as background panel, labels, plant and zombie panels, and top-right buttons.
 * - Check if all required fields are filled and enable the start button accordingly.
 * - Retrieve selected items from the plant and zombie panels.
 * 
 * The class also includes custom inner classes for:
 * - BackgroundPanel: A JPanel that displays a background image.
 * - SelectablePanel: An abstract class for panels that can be selected or deselected by clicking.
 * - PlantPanel: A subclass of SelectablePanel for displaying plant images.
 * - ZombiePanel: A subclass of SelectablePanel for displaying zombie images.
 * 
 * The main method launches the PlayerVsPlayer window.
 */
public class PlayerVsPlayer extends JFrame {

    // Domain Layer
    private POOBvsZombies poobvsZombies;

    // GUI Components
    private JTextField playerOneName, playerTwoName, matchTime, setSunsField, setBrainsField;
    private JButton startButton;
    private List<PlantPanel> plantPanelsList;
    private List<ZombiePanel> zombiePanelsList;


    /**
     * Constructor for the PlayerVsPlayer class.
     * Initializes the player vs player game mode by preparing the necessary elements and actions.
     */
    public PlayerVsPlayer() {
        prepareElements();
        prepareActions();
    }

    /**
     * Prepares and initializes the elements for the Player vs Player game window.
     * This method sets up the window configuration, background panel, text fields,
     * buttons, labels, and panels for plants and zombies. It also adds the necessary
     * components to the background panel.
     */
    private void prepareElements() {
        // Window Configuration
        setTitle("Player vs Player");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Initialize Components
        plantPanelsList = new ArrayList<>();
        zombiePanelsList = new ArrayList<>();

        // Text Fields
        playerOneName = createTextField("Name player one", 270, 380, 150, 30);
        playerTwoName = createTextField("Name player two", 465, 380, 150, 30);
        matchTime = createTextField("Time", 350, 455, 40, 20);
        setSunsField = createTextField("Amount of suns", 82, 613, 90, 20);
        setBrainsField = createTextField("Amount of brains", 730, 613, 100, 20);

        // Buttons
        startButton = new JButton("¡START!");
        startButton.setBounds(462, 449, 160, 30);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE);
        startButton.setEnabled(false);

        // Add Components to Panel
        backgroundPanel.add(playerOneName);
        backgroundPanel.add(playerTwoName);
        backgroundPanel.add(matchTime);
        backgroundPanel.add(setSunsField);
        backgroundPanel.add(setBrainsField);
        backgroundPanel.add(startButton);

        // Labels
        addLabels(backgroundPanel);

        // Plant and Zombie Panels
        addPlantPanels(backgroundPanel);
        addZombiePanels(backgroundPanel);

        // Top Right Buttons
        addTopRightButtons(backgroundPanel);
    }

    /**
     * Prepares the actions for the PlayerVsPlayer window.
     * 
     * This method sets up the following actions:
     * - Window closing action: Closes the window and exits the application.
     * - Start button action: Initiates the start action when the start button is pressed.
     * - Text fields document listeners: Adds listeners to the text fields for player names, match time, suns, and brains.
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
        addTextFieldListeners(playerOneName);
        addTextFieldListeners(playerTwoName);
        addTextFieldListeners(matchTime);
        addTextFieldListeners(setSunsField);
        addTextFieldListeners(setBrainsField);
    }

    /**
     * Starts the game by collecting input values, validating them, and initializing the game instance.
     * 
     * This method performs the following steps:
     * 1. Collects values from input fields for player names, match timer, sun amount, and brain amount.
     * 2. Validates the collected values to ensure they are non-negative.
     * 3. Retrieves the selected plants and zombies.
     * 4. Creates an instance of POOBvsZombies with the collected and validated values.
     * 5. Opens the GardenMenu and disposes of the current window.
     * 
     * If any input values are invalid (e.g., non-numeric or negative), a custom POOBvsZombiesException is thrown and handled.
     * 
     * @throws POOBvsZombiesException if any input values are invalid.
     */
    private void actionStart() {
        try {
            // Recoger valores de los campos
            String namePlayerOne = playerOneName.getText();
            String namePlayerTwo = playerTwoName.getText();
            int matchTimer = Integer.parseInt(matchTime.getText());
            int sunAmount = Integer.parseInt(setSunsField.getText());
            int brainAmount = Integer.parseInt(setBrainsField.getText());

            if(matchTimer < 0 || sunAmount < 0 || brainAmount < 0){
                throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
            }
            // Plantas y zombis seleccionados
            ArrayList<String> selectedPlants = getSelectedItems(plantPanelsList);
            ArrayList<String> selectedZombies = getSelectedItems(zombiePanelsList);

            // Crear instancia de POOBvsZombies
            poobvsZombies = new POOBvsZombies(
                    matchTimer,
                    namePlayerOne,
                    selectedPlants,
                    sunAmount,
                    namePlayerTwo,
                    brainAmount,
                    selectedZombies);

            // Continuar con el siguiente paso (ejemplo: abrir GardenMenu)
            new GardenMenu(poobvsZombies).setVisible(true);
            dispose();

        } catch (NumberFormatException ex) {
            // Lanza una excepción personalizada en vez de NumberFormatException
            try {
                throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
            } catch (POOBvsZombiesException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Helper Methods
    /**
     * Creates a customized JTextField with the specified placeholder text, position, and size.
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
     * When the text field gains focus, the placeholder text is cleared if it matches
     * the current text. When the text field loses focus, the placeholder text is
     * restored if the field is empty. Additionally, it calls the checkFields method
     * when the text field loses focus.
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
     * Adds labels to the specified JPanel. This includes:
     * - A label prompting the user to select plants.
     * - A label prompting the user to select zombies.
     * - A label describing the game mode.
     *
     * @param panel the JPanel to which the labels will be added
     */
    private void addLabels(JPanel panel) {
        // Label "Select your plants"
        JLabel selectPlantsLabel = new JLabel("Select your plants");
        selectPlantsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantsLabel.setForeground(Color.WHITE);
        selectPlantsLabel.setBounds(46, 405, 150, 30);
        panel.add(selectPlantsLabel);

        // Label "Select your zombies"
        JLabel selectZombiesLabel = new JLabel("Select your zombies");
        selectZombiesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombiesLabel.setForeground(Color.WHITE);
        selectZombiesLabel.setBounds(690, 405, 170, 30);
        panel.add(selectZombiesLabel);

        // Game Mode Description
        JLabel gameModeLabel = new JLabel("<html><div style='width:385px;'>"
                + "In this mode, players control plants and zombies, defining strategies."
                + " <br> The plant team has 2 minutes to set up and must withstand zombie "
                + "<br>waves configured by the zombie team. Each player decides their "
                + "<br>team's starting resources."
                + "</div></html>");
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gameModeLabel.setForeground(Color.WHITE);
        gameModeLabel.setBounds(245, 520, 380, 100);
        panel.add(gameModeLabel);
    }

    /**
     * Adds plant panels to the specified parent panel.
     *
     * This method creates a new JPanel with a GridLayout to hold plant panels.
     * It iterates through the list of plants from GardenMenu.PLANTS_VIEW, creates
     * a PlantPanel for each plant using its image path, and adds the PlantPanel
     * to the plantsPanel. Finally, it adds the plantsPanel to the specified parent panel.
     *
     * @param panel the parent JPanel to which the plant panels will be added
     */
    private void addPlantPanels(JPanel panel) {
        JPanel plantsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        plantsPanel.setBounds(65, 440, 100, 160);
        plantsPanel.setOpaque(false);

        for (List<String> plant : GardenMenu.PLANTS_VIEW) {
            String imagePath = plant.get(1);
            PlantPanel plantPanel = new PlantPanel(imagePath);
            plantPanelsList.add(plantPanel);
            plantsPanel.add(plantPanel);
        }
        panel.add(plantsPanel);
    }

    /**
     * Adds zombie panels to the specified parent panel.
     *
     * This method creates a new JPanel with a GridLayout to hold zombie panels.
     * It iterates over the list of zombies from GardenMenu.ZOMBIES_VIEW, creates
     * a ZombiePanel for each zombie using its image path, and adds it to the
     * zombiesPanel. Finally, the zombiesPanel is added to the specified parent panel.
     *
     * @param panel the parent JPanel to which the zombiesPanel will be added
     */
    private void addZombiePanels(JPanel panel) {
        JPanel zombiesPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        zombiesPanel.setBounds(710, 440, 100, 160);
        zombiesPanel.setOpaque(false);
        for (List<String> zombies : GardenMenu.ZOMBIES_VIEW) {
            String imagePath = zombies.get(1);        
            ZombiePanel zombiePanel = new ZombiePanel(imagePath);
            zombiePanelsList.add(zombiePanel);
            zombiesPanel.add(zombiePanel);
        }
        panel.add(zombiesPanel);
    }
    

    /**
     * Adds a button to the top-right corner of the specified panel.
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
        button.setBounds(x, y, buttonSize, buttonSize);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Back to main menu
                dispose(); // Close the current window
                // Open the main menu (uncomment if POOBvsZombiesGUI exists)
                new POOBvsZombiesGUI().setVisible(true);
            }
        });

        panel.add(button);
    }

    /**
     * Checks if all required fields are filled and at least one plant and one zombie are selected.
     * Enables the start button if all conditions are met:
     * - At least one plant is selected.
     * - At least one zombie is selected.
     * - Player one's name is filled and not the default text.
     * - Player two's name is filled and not the default text.
     * - Match time is filled and not the default text.
     * - Suns field is filled and not the default text.
     * - Brains field is filled and not the default text.
     */
    private void checkFields() {
        boolean atLeastOnePlantSelected = false;
        for (PlantPanel plantPanel : plantPanelsList) {
            if (plantPanel.isSelected()) {
                atLeastOnePlantSelected = true;
                break;
            }
        }

        boolean atLeastOneZombieSelected = false;
        for (ZombiePanel zombiePanel : zombiePanelsList) {
            if (zombiePanel.isSelected()) {
                atLeastOneZombieSelected = true;
                break;
            }
        }

        boolean playerOneNameFilled = !playerOneName.getText().isEmpty()
                && !playerOneName.getText().equals("Name player one");
        boolean playerTwoNameFilled = !playerTwoName.getText().isEmpty()
                && !playerTwoName.getText().equals("Name player two");
        boolean matchTimeFilled = !matchTime.getText().isEmpty() && !matchTime.getText().equals("Time");
        boolean sunsFilled = !setSunsField.getText().isEmpty() && !setSunsField.getText().equals("Amount of suns");
        boolean brainsFilled = !setBrainsField.getText().isEmpty()
                && !setBrainsField.getText().equals("Amount of brains");

        startButton.setEnabled(atLeastOnePlantSelected && atLeastOneZombieSelected && playerOneNameFilled
                && playerTwoNameFilled && matchTimeFilled && sunsFilled && brainsFilled);
    }

    /**
     * Retrieves the selected items from a list of selectable panels.
     *
     * @param panels the list of selectable panels to check for selected items
     * @return an ArrayList of selected item types based on the selected panels
     */
    /**
     * Retrieves the selected items from a list of SelectablePanel objects.
     * It checks each panel to see if it is selected, and if so, it compares the item path
     * with predefined static lists of plant and zombie images. If a match is found,
     * the corresponding plant or zombie type is added to the list of selected items.
     *
     * @param panels the list of SelectablePanel objects to check for selected items
     * @return an ArrayList of selected item types (plants or zombies)
     */
    private ArrayList<String> getSelectedItems(List<? extends SelectablePanel> panels) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (SelectablePanel panel : panels) {
            if (panel.isSelected()) {
                String itemPath = panel.getItemPath();

                for (int i = 0; i < 5; i++) {
                    List<String>plant = GardenMenu.PLANTS_VIEW.get(i);
                    List<String>zombie = GardenMenu.ZOMBIES_VIEW.get(i);
                    if (itemPath.equals(plant.get(1))) {
                        selectedItems.add(plant.get(0));
                    } else if (itemPath.equals(zombie.get(1))) {
                        selectedItems.add(zombie.get(0));
                    // }   
                    // if (itemPath.equals(GardenMenu.PLANTS_VIEW.get(3))){
                    //     selectedItems.add(Plants.PLANT_TYPES[i]);
                    // } else if (itemPath.equals(GardenMenu.ZOMBIES_VIEW.get(3))) {
                    //     selectedItems.add(Zombies.ZOMBIE_TYPES[i]);
                    }
                }           
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

        public BackgroundPanel(){
            backgroundImage = new ImageIcon(getClass().getResource("/images/menu/PlayerVSPlayerMenu.png")).getImage();
            
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    /**
     * The SelectablePanel class is an abstract class that extends JPanel and provides
     * a panel with an image that can be selected or deselected by clicking on it.
     * The panel visually indicates its selection state by overlaying a semi-transparent
     * color (green for selected, red for not selected).
     */
    abstract class SelectablePanel extends JPanel {
        private boolean selected = false;
        private Image itemImage;
        private String itemPath;

        /**
         * Constructs a SelectablePanel with the specified image path.
         * The panel displays an image scaled to 50x50 pixels and toggles its selection state when clicked.
         *
         * @param imagePath the path to the image to be displayed in the panel
         */
        public SelectablePanel(String imagePath) {
            this.itemPath = imagePath;
            this.itemImage = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selected = !selected;
                    repaint();
                    checkFields();
                }
            });
        }

        public boolean isSelected() {
            return selected;
        }

        /**
         * Sets the selected state of this component and repaints it.
         *
         * @param selected true to select this component, false to deselect it
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        public String getItemPath() {
            return itemPath;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (itemImage != null) {
                g.drawImage(itemImage, 0, 0, getWidth(), getHeight(), this);
            }
            if (selected) {
                g.setColor(new Color(0, 255, 0, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g.setColor(new Color(255, 0, 0, 128));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    class PlantPanel extends SelectablePanel {
        public PlantPanel(String imagePath) {
            super(imagePath);
        }
    }

    class ZombiePanel extends SelectablePanel {
        public ZombiePanel(String imagePath) {
            super(imagePath);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerVsPlayer frame = new PlayerVsPlayer();
            frame.setVisible(true);
        });
    }
}
