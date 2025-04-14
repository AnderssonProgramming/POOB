package presentation;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The MachineVSMachine class represents a graphical user interface (GUI) for the "Machine vs Machine" mode
 * in the POOBvsZombies game. This mode allows both sides (plants and zombies) to be controlled by machines
 * with intelligent strategies.
 * 
 * The class extends JFrame and includes various GUI components such as text fields, buttons, and panels
 * for selecting plants and zombies. It also handles user interactions and game initialization.
 * 
 */
public class MachineVSMachine extends JFrame {

    // Domain Layer
    private POOBvsZombies poobvsZombies;

    // GUI Components
    private JTextField timeField, quantityField, setSunsField, setBrainsField;
    //private JLabel sunsValueLabel, brainsValueLabel;
    private JButton startButton;
    private List<PlantPanel> plantPanelsList;
    private List<ZombiePanel> zombiePanelsList;

    /**
     * Constructor for the MachineVSMachine class.
     * Initializes the elements and actions required for the machine vs machine functionality.
     */
    public MachineVSMachine() {
        prepareElements();
        prepareActions();
    }

    /**
     * Prepares and initializes the elements of the Machine vs Machine window.
     * This includes setting up the window properties, creating and adding
     * various components such as text fields, buttons, and panels for plants
     * and zombies. It also configures the layout and appearance of these components.
     */
    private void prepareElements() {
        // Configuración de la ventana
        setTitle("Machine vs Machine");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel de fondo personalizado
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Posicionamiento absoluto
        setContentPane(backgroundPanel);

        // Inicializar listas de paneles
        plantPanelsList = new ArrayList<>();
        zombiePanelsList = new ArrayList<>();

        // Campos de texto para tiempo y cantidad de hordas
        timeField = createTextField("Time", 385, 415, 40, 15);
        quantityField = createTextField("Number", 455, 415, 55, 15);
        setSunsField = createTextField("Amount of suns", 150, 487, 100, 20);
        setBrainsField = createTextField("Amount of brains", 654, 484, 107, 20);
        

        // Botón START
        startButton = new JButton("¡START!");
        startButton.setBounds(395, 460, 90, 38);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.WHITE); // Color del texto
        startButton.setEnabled(false); // Inicialmente deshabilitado
		
        backgroundPanel.add(startButton);
        backgroundPanel.add(timeField);
        backgroundPanel.add(quantityField);
        backgroundPanel.add(setSunsField);
        backgroundPanel.add(setBrainsField);
        

        // Etiquetas
        addLabels(backgroundPanel);

        // Paneles de plantas y zombies
        addPlantPanels(backgroundPanel);
        addZombiePanels(backgroundPanel);

        // Botones en la esquina superior derecha
        addTopRightButtons(backgroundPanel);
    }

    /**
     * Prepares the actions for the window and various components.
     * 
     * This method sets up the following actions:
     * - Closes the window and exits the application when the window is closed.
     * - Starts the action when the start button is pressed.
     * - Adds focus listeners to the time, quantity, setSuns, and setBrains fields.
     */
    private void prepareActions() {
        // Acción al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                setVisible(false);
                System.exit(0);
            }
        });

        // Acción del botón de inicio
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                actionStart();
            }
        });
		

		// Añadir FocusListeners a los campos de tiempo y cantidad
        addTextFieldListeners(timeField);
        addTextFieldListeners(quantityField);
        addTextFieldListeners(setSunsField);
        addTextFieldListeners(setBrainsField);
    }

    // Método para manejar el inicio del juego
    /**
     * Initiates the start of the game by retrieving user inputs, validating them,
     * and setting up the game configuration. It handles exceptions for invalid
     * inputs and displays appropriate error messages.
     * 
     * @throws POOBvsZombiesException if the match time or horde number is less than 1,
     *                                or if the sun amount or brain amount is negative.
     * @throws NumberFormatException  if the input values are not valid integers.
     */
    private void actionStart() {
        try {
            // Obtener valores ingresados
            int matchTime = Integer.parseInt(timeField.getText());
            int hordeNumber = Integer.parseInt(quantityField.getText());
            int sunAmount = Integer.parseInt(setSunsField.getText());
            int brainAmount = Integer.parseInt(setBrainsField.getText());

            if(matchTime < 1 || hordeNumber < 1){
                throw new POOBvsZombiesException(POOBvsZombiesException.TIME_LIMIT);
            }
            if(sunAmount < 0 || brainAmount < 0){
                throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
            }
            // Plantas y Zombies seleccionados (preseleccionados)
            ArrayList<String> selectedPlants = getSelectedPlantNames();
            ArrayList<String> selectedZombies = getSelectedZombieNames();

            // Crear instancia de POOBvsZombies para MachineVsMachine
            poobvsZombies = new POOBvsZombies(
                    matchTime,
                    hordeNumber,sunAmount,brainAmount
            );

            // Agregar plantas y zombies seleccionados al POOBvsZombies
            poobvsZombies.getPlayerOne().getTeam().setCharacters(selectedPlants);
            poobvsZombies.getPlayerTwo().getTeam().setCharacters(selectedZombies);

            // // Setear suns y brains fijos
            // poobvsZombies.getPlayerOne().getTeam().setResourceCounter(100); // Suns
            // poobvsZombies.getPlayerTwo().getTeam().setResourceCounter(200); // Brains

            // Abrir el GardenMenu con la configuración
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

    // Métodos auxiliares
    /**
     * Creates a customized JTextField with specified placeholder text and dimensions.
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
     * Additionally, it calls the checkFields() method when the text field loses focus.
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
     * Adds labels to the specified JPanel. This includes an informational label
     * describing the mode, a label for selecting plants, and a label for selecting
     * zombies. The labels are styled with specific fonts, colors, and positions.
     *
     * @param panel the JPanel to which the labels will be added
     */
    private void addLabels(JPanel panel) {
        // Etiqueta de información
        JLabel infoLabel = new JLabel("<html>"
                + "In this mode, both sides will be controlled <br>by machines.The machines configure intelligent <br>movements to respond to the opponent <br>with strategy.");
        infoLabel.setBounds(320, 270, 380, 100); // Posición y tamaño ajustados
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(infoLabel);

        // Etiqueta "SELECT PLANTS"
        JLabel selectPlantLabel = new JLabel("PLANTS");
        selectPlantLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectPlantLabel.setForeground(Color.WHITE);
        selectPlantLabel.setBounds(165, 295, 150, 30);
        panel.add(selectPlantLabel);

        // Etiqueta "SELECT ZOMBIES"
        JLabel selectZombieLabel = new JLabel("ZOMBIES");
        selectZombieLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectZombieLabel.setForeground(Color.WHITE);
        selectZombieLabel.setBounds(665, 290, 170, 30);
        panel.add(selectZombieLabel);
    }

    /**
     * Adds plant selection panels to the specified parent panel.
     *
     * @param panel the parent JPanel to which the plant selection panels will be added
     *
     * This method creates a plant selection panel with a grid layout and adds individual
     * plant panels to it. Each plant panel displays an image and name of a plant.
     * The plant selection panel is then added to the specified parent panel.
     *
     * The plant images and names are defined in the arrays `plantImages` and `plantNames`.
     * The plant selection panel is positioned and sized using setBounds, and it is made
     * transparent to allow the background to be visible.
     */
    private void addPlantPanels(JPanel panel) {
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(130, 340, 120, 120); // Posición y tamaño ajustados
        plantSelectionPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 3 filas, 1 columna, 10px de separación
        plantSelectionPanel.setOpaque(false); // Transparente para ver el fondo

        String[] plantImages = {
            "/images/plants/Sunflower/Sunflower.jpg",
            "/images/plants/Peashooter/Peashooter.jpg",
            "/images/plants/WallNut/Wall-nutGrass.jpg"
        };

        String[] plantNames = {
            "Sunflower",
            "Peashooter",
            "WallNut"
        };

        for (int i = 0; i < plantImages.length; i++) {
            PlantPanel plantPanel = new PlantPanel(plantImages[i], plantNames[i]);
            plantPanelsList.add(plantPanel);
            plantSelectionPanel.add(plantPanel);
        }

        panel.add(plantSelectionPanel);
    }

    /**
     * Adds a panel with zombie selection options to the specified parent panel.
     *
     * @param panel The parent JPanel to which the zombie selection panel will be added.
     *
     * This method creates a new JPanel for zombie selection, sets its layout and bounds,
     * and populates it with ZombiePanel instances representing different types of zombies.
     * The zombie selection panel is then added to the specified parent panel.
     */
    private void addZombiePanels(JPanel panel) {
        JPanel zombieSelectionPanel = new JPanel();
        zombieSelectionPanel.setBounds(640, 340, 120, 120); // Posición y tamaño ajustados
        zombieSelectionPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 3 filas, 1 columna, 10px de separación
        zombieSelectionPanel.setOpaque(false); // Transparente para ver el fondo

        String[] zombieImages = {
            "/images/zombies/Basic/Basic.jpg",
            "/images/zombies/Conehead/Conehead.jpg",
            "/images/zombies/BucketHead/Buckethead.jpg"
        };

        String[] zombieNames = {
            "Basic",
            "Conehead",
            "BucketHead"
        };

        for (int i = 0; i < zombieImages.length; i++) {
            ZombiePanel zombiePanel = new ZombiePanel(zombieImages[i], zombieNames[i]);
            zombiePanelsList.add(zombiePanel);
            zombieSelectionPanel.add(zombiePanel);
        }

        panel.add(zombieSelectionPanel);
    }

    /**
     * Adds a button to the top-right corner of the specified panel.
     * The button uses an image icon and is configured to return to the main menu
     * when clicked.
     *
     * @param panel the JPanel to which the button will be added
     */
    private void addTopRightButtons(JPanel panel) {
        String buttonImagePath = "/images/buttons/return-icon.png"; // Botón Return

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
                // Volver al menú principal
                dispose(); // Cerrar la ventana actual
                new POOBvsZombiesGUI().setVisible(true);
            }
        });

        panel.add(button);
        
    }

    // Método para habilitar el botón START si todos los campos tienen valores
    /**
     * Checks if all required fields are filled and enables the start button if they are.
     * 
     * The method verifies the following conditions:
     * - The time field is not empty and does not contain the default text "Time".
     * - The quantity field is not empty and does not contain the default text "Number".
     * - The suns field is not empty and does not contain the default text "Amount of suns".
     * - The brains field is not empty and does not contain the default text "Amount of brains".
     * - At least one plant is selected.
     * - At least one zombie is selected.
     * 
     * If all conditions are met, the start button is enabled.
     */
    private void checkFields() {
        boolean timeFilled = !timeField.getText().isEmpty() && !timeField.getText().equals("Time");
        boolean quantityFilled = !quantityField.getText().isEmpty() && !quantityField.getText().equals("Number");
        boolean sunsFilled = !setSunsField.getText().isEmpty() && !setSunsField.getText().equals("Amount of suns");
        boolean brainsFilled = !setBrainsField.getText().isEmpty() && !setBrainsField.getText().equals("Amount of brains");

        // En este modo, las plantas y zombies están preseleccionados
        boolean atLeastOnePlantSelected = plantPanelsList.size() >= 1;
        boolean atLeastOneZombieSelected = zombiePanelsList.size() >= 1;

        startButton.setEnabled(timeFilled && quantityFilled && sunsFilled && brainsFilled &&
                               atLeastOnePlantSelected && atLeastOneZombieSelected);
    }

    /**
     * Retrieves the names of the selected plants from the list of plant panels.
     *
     * @return an ArrayList containing the names of the selected plants.
     */
    private ArrayList<String> getSelectedPlantNames() {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (PlantPanel panel : plantPanelsList) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getPlantName());
            }
        }
        return selectedItems;
    }

    /**
     * Retrieves the names of the selected zombies from the list of zombie panels.
     *
     * @return An ArrayList of Strings containing the names of the selected zombies.
     */
    private ArrayList<String> getSelectedZombieNames() {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (ZombiePanel panel : zombiePanelsList) {
            if (panel.isSelected()) {
                selectedItems.add(panel.getZombieName());
            }
        }
        return selectedItems;
    }

    // Clases personalizadas
    /**
     * BackgroundPanel is a custom JPanel that displays a background image.
     * The image is loaded from a specified file path and is drawn to fill the entire panel.
     */
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        /**
         * Constructs a BackgroundPanel object.
         * This constructor initializes the background image by loading it from the specified path
         * and sets the layout of the panel to null.
         */
        public BackgroundPanel() {
            // Cargar la imagen desde la ruta especificada
            // Usar el cargador de clases para obtener el recurso
            backgroundImage = new ImageIcon(getClass().getResource("/images/menu/MachineVSMachineMenu.png")).getImage();
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen en el fondo del panel
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // PlantPanel que representa cada panel de planta preseleccionada
    /**
     * The PlantPanel class represents a custom JPanel that displays a plant image
     * and a green transparent overlay. The panel is always marked as selected.
     */
    class PlantPanel extends JPanel {
        private boolean selected;
        private Image plantImage;
        private String plantPath;
        private String plantName;

        /**
         * Constructs a PlantPanel with the specified image path and plant name.
         *
         * @param imagePath the path to the image of the plant
         * @param plantName the name of the plant
         */
        public PlantPanel(String imagePath, String plantName) {
            this.plantPath = imagePath;
            this.plantName = plantName;
            this.selected = true; // Siempre seleccionado
            this.plantImage = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);
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
            // Dibujar la imagen de la planta
            if (plantImage != null) {
                g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
            }
            // Dibujar el recuadro verde transparente
            g.setColor(new Color(0, 255, 0, 128)); // Verde con transparencia
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ZombiePanel que representa cada panel de zombie preseleccionado
    /**
     * The ZombiePanel class represents a custom JPanel that displays a zombie image
     * and a green transparent overlay. The panel is always selected.
     */
    class ZombiePanel extends JPanel {
        private boolean selected;
        private Image zombieImage;
        private String zombiePath;
        private String zombieName;

        /**
         * Constructs a ZombiePanel with the specified image path and zombie name.
         *
         * @param imagePath the path to the image of the zombie
         * @param zombieName the name of the zombie
         */
        public ZombiePanel(String imagePath, String zombieName) {
            this.zombiePath = imagePath;
            this.zombieName = zombieName;
            this.selected = true; // Siempre seleccionado
            this.zombieImage = new ImageIcon(getClass().getResource(imagePath)).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(50, 50));
            setOpaque(false);
        }

        public boolean isSelected() {
            return selected;
        }

        public String getZombieName() {
            return zombieName;
        }

        public String getZombiePath() {
            return zombiePath;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujar la imagen del zombie
            if (zombieImage != null) {
                g.drawImage(zombieImage, 0, 0, getWidth(), getHeight(), this);
            }
            // Dibujar el recuadro verde transparente
            g.setColor(new Color(0, 255, 0, 128)); // Verde con transparencia
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * The main method that serves as the entry point of the application.
     * It creates and displays the main window of the application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        // Crear y mostrar la ventana
        SwingUtilities.invokeLater(() -> {
            MachineVSMachine frame = new MachineVSMachine();
            frame.setVisible(true);
        });
    }
}