package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container; // Add this import
import java.awt.Cursor;
import java.awt.Dimension; // Add this import
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.Box; // Add this import
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import domain.Basic;
import domain.Brainstein;
import domain.Buckethead;
import domain.Conehead;
import domain.ECIPlant;
import domain.ECIZombie;
import domain.Entity;
import domain.POOBvsZombies;
import domain.POOBvsZombiesException;
import domain.Peashooter;
import domain.Plant;
import domain.Plants;
import domain.Player;
import domain.PotatoMine;
import domain.ProjectTile;
import domain.ProjectTileThreadManager;
import domain.Resource;
import domain.ResourceGenerator;
import domain.Sunflower;
import domain.WallNut;
import domain.Zombie;
import domain.ZombieThreadManager;

public class GardenMenu extends JFrame {
    private ArrayList<String> selectedPlants;
    private ArrayList<String> selectedZombies;
    private String modality; // "PlayerVsPlayer" or "PlayerVsMachine"
    private JLabel shovelLabel;
    @SuppressWarnings("unused")
    private Point originalShovelPosition;
    private JPanel[][] gridCells = new JPanel[5][10];
    private boolean shovelSelected = false;
    private List<JLabel> movingZombies = new ArrayList<>();
    private POOBvsZombies poobvszombies;
    private JLabel playerOneNameLabel, playerTwoNameLabel;
    private JLabel playerOneSunsLabel, playerTwoBrainsLabel;
    private JLabel playerOneScoreLabel, playerTwoScoreLabel;
    @SuppressWarnings("unused")
    private Timer resourceGenerationTimer; // Timer cada 20 segundos
    // Mapa para gestionar timers individuales por entidad
    private Map<Entity, Timer> entityTimers = new HashMap<>();

    // UPDATE
    private ZombieThreadManager zombieThreadManager;
    private ProjectTileThreadManager projectTileThreadManager;


    public static final List<List<String>> ZOMBIES_VIEW = Arrays.asList(
            Arrays.asList(
                    "Basic",
                    "/images/zombies/Basic/Basic.jpg",
                    "/images/cards/Zombies/card_basic_zombie.png",
                    "/images/zombies/Basic/BasicDinamic.gif"),
            Arrays.asList(
                    "Brainstein",
                    "/images/zombies/Brainstein/brainsteinGarden.jpeg",
                    "/images/cards/Zombies/card_brainstein.png",
                    "/images/zombies/Brainstein/brainsteinAnimated.gif"),
            Arrays.asList(
                    "BucketHead",
                    "/images/zombies/BucketHead/Buckethead.jpg",
                    "/images/cards/Zombies/card_buckethead_zombie.png",
                    "/images/zombies/BucketHead/BucketheadAnimated.gif"),
            Arrays.asList(
                    "Conehead",
                    "/images/zombies/Conehead/Conehead.jpg",
                    "/images/cards/Zombies/card_conehead_zombie.png",
                    "/images/zombies/Conehead/ConeheadAnimated.gif"),
            Arrays.asList(
                    "ECIZombie",
                    "/images/zombies/ECIZombie/ECIZombie.png",
                    "/images/cards/Zombies/card_ECIZombie.png",
                    "/images/zombies/ECIZombie/ECIZombieAnimated.gif"));

    public static final List<List<String>> PLANTS_VIEW = Arrays.asList(
            Arrays.asList(
                    "Sunflower",
                    "/images/plants/Sunflower/Sunflower.jpg",
                    "/images/cards/Plants/card_sunflower.png",
                    "/images/plants/Sunflower/sunflowerAnimated.gif"),
            Arrays.asList(
                    "Peashooter",
                    "/images/plants/Peashooter/Peashooter.jpg",
                    "/images/cards/Plants/card_peashooter.png",
                    "/images/plants/Peashooter/peashooterAnimated.gif"),
            Arrays.asList(
                    "WallNut",
                    "/images/plants/WallNut/Wall-nutGrass.jpg",
                    "/images/cards/Plants/card_wallnut.png",
                    "/images/plants/WallNut/wall-nutAnimated.gif"),
            Arrays.asList(
                    "PotatoMine",
                    "/images/plants/PotatoMine/Potato_MineGrass.jpg",
                    "/images/cards/Plants/card_potatomine.png",
                    "/images/plants/PotatoMine/before-potato-mineAnimated.gif",
                    "/images/plants/PotatoMine/potato-mineAnimated.gif",   // Nueva ruta para activación
                    "/images/plants/PotatoMine/PotatoMineExplode_0.png"),    // Nueva ruta para explosión),
            Arrays.asList(
                    "ECIPlant",
                    "/images/plants/ECIPlant/ECIPlant.png",
                    "/images/cards/Plants/card_ECIPlant.png",
                    "/images/plants/ECIPlant/ECIPlantAnimated.gif"));
    private List<TimerTask> timerTasks = new ArrayList<>();
    
    // Arreglo estatico de lawnmovers para poder acceder a ellos desde cualquier parte
    private JLabel[] lawnmowersArrayJLabels = new JLabel[5];

    // Etiquetas para mostrar el mensaje y el tiempo
    private JLabel messageLabel;
    private JLabel timeLabel;
    // Añade este mapa en la declaración de variables de la clase GardenMenu
    private Map<Entity, Timer> imageChangeTimers = new HashMap<>();
    private boolean isPaused = false; // Estado de pausa
    private JLabel pauseLabel; // Etiqueta para mostrar el mensaje de pausa

    // Timers individuales
    private Timer resourceTimerForPlayerVsPlayer; // Ejemplo: el timer que genera recursos cada 10s en PlayerVsPlayer
    private Timer resourceTimerForPvMOrMvM; // Otro timer para PlayerVsMachine o MachineVsMachine
    private Timer phaseTimer; // Timer que maneja la cuenta regresiva de cada fase (startSequentialTimers)
    private Timer resourceTimerMvM; // Timer para MachineVsMachine
    private Timer collectTimer; 

    // Agregar un método para almacenar el estado restaurado
    private int currentTimerTaskIndex = 0;
    private int currentTimerTaskRemainingTime = 0;
    


    public GardenMenu(POOBvsZombies poobvszombies) {
        setTitle("Garden Menu");
        setSize(900, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Inicializar ZombieThreadManager
        this.zombieThreadManager = new ZombieThreadManager(poobvszombies, this);

        // Inicializar ProjectTileThreadManager
        this.projectTileThreadManager = new ProjectTileThreadManager(poobvszombies, this, zombieThreadManager);
        
        this.poobvszombies = poobvszombies;
        this.modality = poobvszombies.getModality();
        Player playerOne = poobvszombies.getPlayerOne();
        Player playerTwo = poobvszombies.getPlayerTwo();
        this.selectedPlants = playerOne.getTeam().getCharacters();
        this.selectedZombies = playerTwo.getTeam().getCharacters();

        // Panel with custom background
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon(getClass().getResource("/images/gardenPvsP.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Display plant cards based on selection and make them draggable
        addPlantsCards(panel);

        // Add the shovel at the top right corner
        addShovel(panel);

        // Add the 5x10 GridLayout for cells
        addGridLayout(panel);

        // Add the "Use Shovel" button below the shovel
        addUseShovelButton(panel);

        // Add icons for returning to menus, saving, and exporting
        addTopRightButtons(panel);

        // Add zombie components only if in "PlayerVsPlayer" mode

        if ("PlayerVsPlayer".equals(modality)) {
            addPlayerInfo(panel);
            addScoreLabels(panel);
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
        } else if ("PlayerVsMachine".equals(modality)) {
            addPlayerInfo(panel); // Solo mostrar información del jugador
            addScoreLabels(panel);
        } else if ("MachineVsMachine".equals(modality)) {
            addPlayerInfo(panel);
            addScoreLabels(panel);
            addBrainIcon(panel);
            addZombieCards(panel);
            addZombieTable(panel);
            
        }

        initializeTimers(poobvszombies);
        setupTimerLabels(panel);
        startSequentialTimers(0);
        

        addPauseLabel(panel);

        add(panel);
        
        poobvszombies.setGardenMenu(this);
    }

    private void addPauseLabel(JPanel panel) {
        pauseLabel = new JLabel("PAUSE GAME", SwingConstants.CENTER);
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 48));
        pauseLabel.setForeground(Color.RED);
        pauseLabel.setBounds(0, 0, getWidth(), getHeight());
        pauseLabel.setOpaque(false);
        pauseLabel.setVisible(false);
        panel.add(pauseLabel);
    }

    private void addPlantsCards(JPanel panel) {

        int x = 75; // Initial X position
        int y = -25; // Initial Y position
        for (String plantName : selectedPlants) {
            if (plantName != null) { // Only if it's a valid plant

                List<String> plant = PLANTS_VIEW.get(0);

                // Buscar la planta en la lista de plantas y asignarla a la variable plant para
                // tener todas sus posibles representaciones graficas
                for (List<String> plants : PLANTS_VIEW) {
                    if (plants.get(0).equals(plantName)) {
                        plant = plants;
                        break;
                    }
                }

                if (plant == null)
                    continue;

                ImageIcon icon = new ImageIcon(getClass().getResource(plant.get(2)));                
                JLabel cardLabel = new JLabel(
                        new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                cardLabel.setBounds(x, y, 100, 150);
                panel.add(cardLabel);

                // Add drag functionality
                String dragImagePath = plant.get(3);

                cardLabel.setTransferHandler(new TransferHandler("icon") {
                    @Override
                    protected Transferable createTransferable(JComponent c) {
                        ImageIcon dragIcon = new ImageIcon(getClass().getResource(dragImagePath));  
                        EntityData entityData = new EntityData("plant", plantName, dragIcon.getImage());
                        return new EntityTransferable(entityData);
                    }

                    @Override
                    public int getSourceActions(JComponent c) {
                        return COPY;
                    }
                });

                cardLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        JComponent comp = (JComponent) e.getSource();
                        TransferHandler handler = comp.getTransferHandler();
                        if (handler != null) {
                            handler.exportAsDrag(comp, e, TransferHandler.COPY);
                        }
                    }
                });

                x += 70; // Move X position for the next card
            }
        }
    }

    private void addShovel(JPanel panel) {
        // Path of the shovel
        String shovelImagePath = "/images/shovel.png";

        // Load and scale the shovel
        ImageIcon shovelIcon = new ImageIcon(getClass().getResource(shovelImagePath));
        Image scaledShovelImage = shovelIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);

        // Create a JLabel to display the shovel
        shovelLabel = new JLabel(new ImageIcon(scaledShovelImage));
        shovelLabel.setBounds(520, 28, 50, 50); // Place at the top right corner
        originalShovelPosition = shovelLabel.getLocation();

        shovelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shovelSelected = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambiar el cursor
            }
        });

        panel.add(shovelLabel);
    }

    // Method to delete a lawnmower from the interface and the domain, it is gonna be used in ZombieThreadManager
    public void deleteLawnmover(int row) {
        if (row >= 0 && row < lawnmowersArrayJLabels.length) {
            JLabel lawnmowerLabel = lawnmowersArrayJLabels[row];
            if (lawnmowerLabel != null) {
                JPanel parentPanel = (JPanel) lawnmowerLabel.getParent();
                if (parentPanel != null) {
                    parentPanel.remove(lawnmowerLabel);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                }
                poobvszombies.deleteEntity(row, 0);
                lawnmowersArrayJLabels[row] = null;
            }
        }
    }

    private void addGridLayout(JPanel panel) {
        // Create a panel with a 5x10 GridLayout
        JPanel gridPanel = new JPanel(new GridLayout(5, 10, 5, 5)); // 5 rows, 10 columns, 5px spacing
        gridPanel.setBounds(40, 80, 800, 500); // Adjust position and size
        gridPanel.setOpaque(false); // Transparent to make the background visible

        // Path of the lawn mower
        String lawnMowerImagePath = "/images/Lawnmower.jpg";

        // Add cells to the grid with specific restrictions for plants and zombies
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                final int finalRow = row;
                final int finalCol = col;

                JPanel cellPanel = new JPanel(new BorderLayout());
                cellPanel.setPreferredSize(new Dimension(50, 50));
                cellPanel.setOpaque(false);
                cellPanel.setBorder(new LineBorder(new Color(0, 0, 0, 0), 1)); // Add transparent border to each cell

                if (col == 0) {
                    // Add the lawn mower to the first column of each row
                    ImageIcon mowerIcon = new ImageIcon(getClass().getResource(lawnMowerImagePath));
                    Image scaledMowerImage = mowerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    JLabel mowerLabel = new JLabel(new ImageIcon(scaledMowerImage));
                    cellPanel.add(mowerLabel, BorderLayout.CENTER);

                    // Add the lawnmower to the array
                    lawnmowersArrayJLabels[row] = mowerLabel;

                    poobvszombies.addEntity(finalRow, finalCol, "LownMover");
                } else if (col >= 1 && col <= 8) {
                    // Allow dragging and dropping plants only in columns 1 to 8
                    cellPanel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        public boolean canImport(TransferSupport support) {
                            if (isPaused) return false;
                            if (!support.isDrop()) {
                                return false;
                            }
                            if (!support.isDataFlavorSupported(EntityTransferable.ENTITY_FLAVOR)) {
                                return false;
                            }

                            // Validate if the cell already has a plant
                            JPanel targetPanel = (JPanel) support.getComponent();
                            if (targetPanel.getComponentCount() > 0) {
                                return false; // There's already a plant in this cell
                            }

                            try {

                                // El transfaerable es un objeto de tipo EntityTransferable en donde esta
                                // guardado entre otras cosas, imagen, hicimos que tambien tenga el string del
                                // nombre de la entidad
                                // EntityTransferable transferable = (EntityTransferable)
                                // support.getTransferable()

                                // vamos a hacer que al añadir una imagen, se mande a dominio como addEntity
                                EntityData entityData = (EntityData) support.getTransferable()
                                        .getTransferData(EntityTransferable.ENTITY_FLAVOR);

                                // Aquí identificamos el tipo de planta a colocar
                                // Supongamos que tienes un método para obtener el nombre de la carta
                                // arrastrada.
                                // Este podría ser pasado en el Transferable o tener un mapeo global.
                                String plantName = entityData.getName(); // Necesitas implementar este método

                                // Crear instancia de la planta correspondiente
                                Plant plant = createPlantInstance(plantName);
                                if (plant == null)
                                    return false;

                                // Verificar recursos
                                Player playerOne = poobvszombies.getPlayerOne();
                                if (playerOne.getTeam().getResourceCounterAmount() < plant.getCost()) {
                                    return false;
                                }

                                return "plant".equals(entityData.getType()); // Only accept plants

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }

                        @Override
                        public boolean importData(TransferSupport support) {
                            if (!canImport(support)) {
                                return false;
                            }

                            try {
                                EntityData entityData = (EntityData) support.getTransferable()
                                        .getTransferData(EntityTransferable.ENTITY_FLAVOR);
                            
                                 // Añadir a dominio
                                 poobvszombies.addEntity(finalRow, finalCol, entityData.getName());
                                 updateScoreLabels();
                                 
                                Image image = entityData.getImage();

                                JLabel label = new JLabel(new ImageIcon(image));
                                label.setHorizontalAlignment(JLabel.CENTER);
                                JPanel targetPanel = (JPanel) support.getComponent();
                                targetPanel.add(label);
                                targetPanel.revalidate();
                                targetPanel.repaint();


                                if (entityData.getName().equals("Peashooter")){
                                    ProjectTile uselessProjectTile = new ProjectTile();
                                    projectTileThreadManager.registerProjectTile(finalRow, finalCol, uselessProjectTile, targetPanel.getX(), targetPanel.getY());
                                }

                                // NUEVO: Deducir recursos
                                String plantName = entityData.getName();
                                Plant plant = createPlantInstance(plantName);
                                if (plant != null) {
                                    Player playerOne = poobvszombies.getPlayerOne();
                                    // Deducir el costo de la planta
                                    playerOne.getTeam().deductResource(plant.getCost());
                                    updateScoreLabels();
                                    // Actualizar la etiqueta de soles
                                    playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                                }

                                // Iniciar Timer si la entidad es un generador de recursos
                                if (plant instanceof ResourceGenerator) {
                                    Timer plantTimer = createEntityTimer((ResourceGenerator) plant, finalRow, finalCol);
                                    entityTimers.put(plant, plantTimer);
                                    plantTimer.start();
                                }

                                // **NUEVO: Registrar PotatoMine en ZombieThreadManager**
                                if (plantName.equals("PotatoMine")) {
                                    zombieThreadManager.registerPotatoMine(finalRow, finalCol);
                                    System.out.println("PotatoMine registrada en ZombieThreadManager para la celda (" + finalRow + ", " + finalCol + ").");
                                }
                                
                                return true;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }
                    });

                   // Dentro del MouseListener para usar la pala en la clase GardenMenu
                    cellPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (isPaused) return;
                            if (shovelSelected) {
                                if (cellPanel.getComponentCount() > 0) {
                                    // Obtener la entidad de dominio
                                    Entity entity = (Entity) poobvszombies.getEntitiesMatrix().get(finalRow).get(finalCol);  
                                    if (entity != null) {
                                        // Detener y remover el Timer asociado (generación de recursos)
                                        Timer resourceTimer = entityTimers.get(entity);
                                        if (resourceTimer != null) {
                                            resourceTimer.stop();
                                            entityTimers.remove(entity);
                                            removePlantAt(finalRow, finalCol);
                                            
                                            System.out.println("Timer de recursos detenido y removido para la entidad: " + entity.getName());
                                        }

                                        // Detener y remover el Timer de cambio de imagen si es PotatoMine
                                        if (entity instanceof PotatoMine) {
                                            Timer imageChangeTimer = imageChangeTimers.get(entity);
                                            if (imageChangeTimer != null) {
                                                imageChangeTimer.stop();
                                                imageChangeTimers.remove(entity);
                                                System.out.println("Timer de cambio de imagen detenido y removido para PotatoMine.");
                                            }
                                        }
                                    }

                                    // Remover la planta visualmente
                                    cellPanel.removeAll();
                                    cellPanel.revalidate();
                                    cellPanel.repaint();
                                    poobvszombies.deleteEntity(finalRow, finalCol); // Implementa este método en POOBvsZombies
                                    poobvszombies.removeEntity(finalRow, finalCol);
                                    
                                    shovelSelected = false;
                                    setCursor(Cursor.getDefaultCursor()); // Restablecer el cursor
                                    System.out.println("Planta eliminada en la celda (" + finalRow + ", " + finalCol + ").");
                                } else {
                                    JOptionPane.showMessageDialog(null, "No hay planta en esta celda.");
                                }
                            }
                        }
                    });


                } else if (col == 9) {
                    if ("PlayerVsPlayer".equals(modality)) {
                        // Allow dragging and dropping zombies only in the last column
                        cellPanel.setTransferHandler(new TransferHandler("icon") {
                            @Override
                            public boolean canImport(TransferSupport support) {
                                if (isPaused) return false;
                                if (!support.isDrop()) {
                                    return false;
                                }
                                if (!support.isDataFlavorSupported(EntityTransferable.ENTITY_FLAVOR)) {
                                    return false;
                                }

                                // Validate if the cell already has a zombie
                                JPanel targetPanel = (JPanel) support.getComponent();
                                if (targetPanel.getComponentCount() > 0) {
                                    return false; // There's already a zombie in this cell
                                }

                                try {
                                    EntityData entityData = (EntityData) support.getTransferable()
                                            .getTransferData(EntityTransferable.ENTITY_FLAVOR);

                                    String zombieName = entityData.getName();
                                    Zombie zombie = createZombieInstance(zombieName);
                                    if (zombie == null)
                                        return false;

                                    Player playerTwo = poobvszombies.getPlayerTwo();
                                    if (playerTwo.getTeam().getResourceCounterAmount() < zombie.getCost()) {
                                        return false;
                                    }

                                    return "zombie".equals(entityData.getType());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            @Override
                            public boolean importData(TransferSupport support) {
                                if (!canImport(support)) {
                                    return false;
                                }

                                try {
                                    EntityData entityData = (EntityData) support.getTransferable()
                                            .getTransferData(EntityTransferable.ENTITY_FLAVOR);
                                    String zombieName = entityData.getName();
                                    Zombie zombie = createZombieInstance(zombieName);
                                    // Añadir a dominio
                                    poobvszombies.addEntity(finalRow, finalCol, entityData.getName());
                                    updateScoreLabels();

                                    // Crear el JLabel del zombi
                                    Image image = entityData.getImage();
                                    JLabel zombieLabel = new JLabel(new ImageIcon(image));
                                    zombieLabel.setHorizontalAlignment(JLabel.CENTER);

                                    JPanel targetCell = (JPanel) support.getComponent();
                                    Point cellLocation = targetCell.getLocation();
                                    int x = gridPanel.getX() + cellLocation.x;
                                    int y = gridPanel.getY() + cellLocation.y;
                                    zombieLabel.setBounds(x, y, targetCell.getWidth(), targetCell.getHeight());

                                    panel.add(zombieLabel);
                                    panel.setComponentZOrder(zombieLabel, 0);
                                    panel.revalidate();
                                    panel.repaint();

                                    // Registrar el zombi en el ZombieThreadManager para moverlo
                                    zombieThreadManager.registerZombie(finalRow, zombie, zombieLabel);

                                    if (zombie != null) {
                                        Player playerTwo = poobvszombies.getPlayerTwo();
                                        playerTwo.getTeam().deductResource(zombie.getCost());
                                        playerTwoBrainsLabel
                                                .setText("" + playerTwo.getTeam().getResourceCounterAmount());
                                    }
                                    // Add the zombie to the movingZombies list
                                    movingZombies.add(zombieLabel);
                                    // Iniciar Timer si la entidad es un generador de recursos
                                    if (zombie instanceof ResourceGenerator) {
                                        Timer zombieTimer = createEntityTimer((ResourceGenerator) zombie, finalRow, finalCol);
                                        entityTimers.put(zombie, zombieTimer);
                                        zombieTimer.start();
                                    }
                                    return true;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                return false;
                            }
                        });
                    }
                }

                gridCells[row][col] = cellPanel; // Save the cell reference
                gridPanel.add(cellPanel);
            }
        }

        panel.add(gridPanel);
    }

    private void addUseShovelButton(JPanel panel) {
        JButton useShovelButton = new JButton("Use Shovel");
        useShovelButton.setBounds(580, 55, 100, 30);
        useShovelButton.addActionListener(e -> {
            // Show JOptionPane to enter row and column
            JTextField rowField = new JTextField(2);
            JTextField colField = new JTextField(2);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Row (0-4):"));
            inputPanel.add(rowField);
            inputPanel.add(Box.createHorizontalStrut(15)); // Space between fields
            inputPanel.add(new JLabel("Column (1-9):"));
            inputPanel.add(colField);
    
            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Enter Row and Column to Remove Plant",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int row = Integer.parseInt(rowField.getText());
                    int col = Integer.parseInt(colField.getText());
    
                    // Validate limits and conditions
                    if (row < 0 || row > 4 || col < 1 || col > 9) {
                        throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_COORDINATES);
                    } else if (col == 0) {
                        throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_LAWNMOWER);
                    } else {
                        JPanel targetPanel = gridCells[row][col];
                        if (targetPanel.getComponentCount() == 0) {
                            throw new POOBvsZombiesException(POOBvsZombiesException.NO_PLANT_TO_REMOVE);
                        } else {
                            // Remove the plant
                            // Obtener la entidad de dominio
                            Entity entity = (Entity) poobvszombies.getEntitiesMatrix().get(row).get(col);    
                            if (entity != null) {
                                // Detener y remover el Timer asociado
                                Timer timer = entityTimers.get(entity);
                                if (timer != null) {
                                    timer.stop();
                                    entityTimers.remove(entity);
                                }
                            }
    
                            // Remover la planta visualmente
                            targetPanel.removeAll();
                            targetPanel.revalidate();
                            targetPanel.repaint();
                            poobvszombies.deleteEntity(row, col); // Implementa este método en POOBvsZombies
                        }
                    }
                } catch (NumberFormatException ex) {
                    try {
                        throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
                    } catch (POOBvsZombiesException exc) {
                        JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (POOBvsZombiesException exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        panel.add(useShovelButton);
    }
    

    private void addTopRightButtons(JPanel panel) {
        String[] buttonImagePaths = {
                "/images/buttons/pause-icon.png", // Pause
                "/images/buttons/export-icon.png", // Export
                "/images/buttons/save-icon.png", // Save
                "/images/buttons/return-icon.png", // Return
                "/images/buttons/home-icon.png" // Back to main menu
        };

        int x = 600;
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
            if (imagePath.contains("pause-icon")) {
                button.addActionListener(e -> {
                    togglePause();
                });
            }

            // Agregar eventos de acción a los botones
            if (imagePath.contains("export-icon")) {
                button.addActionListener(e -> {
                    // Implementar funcionalidad para exportar el estado del juego
                    JOptionPane.showMessageDialog(this, "Funcionalidad de exportar aún no implementada.", "Exportar",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            }

            // En addTopRightButtons:
            if (imagePath.contains("save-icon")) {
                button.addActionListener(e -> {
                    pauseGame();
                    javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
                    int returnVal = fc.showSaveDialog(this);
                    if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        try {
                            // currentTimerTaskIndex y currentTimerTaskRemainingTime deben ser guardados
                            // Cuando incrementas la fase o decrece el tiempo, actualiza estas variables.
                            // Aquí asumimos que currentTimerTaskIndex y currentTimerTaskRemainingTime
                            // se mantienen al día en startSequentialTimers.
                            int currentIndex = currentTimerTaskIndex;
                            int currentRemaining = currentTimerTaskRemainingTime;
                            poobvszombies.saveGame(file, currentIndex, currentRemaining, isPaused);
                            JOptionPane.showMessageDialog(this, "Partida guardada con éxito.", "Guardar",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this, "Error al guardar la partida: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }


            // Add action events to buttons
            if (imagePath.contains("return-icon")) {
                button.addActionListener(e -> {
                    dispose(); // Close the current window
                    if ("PlayerVsMachine".equals(modality)) {
                        PlayerVSMachine pvmMenu = new PlayerVSMachine();
                        pvmMenu.setVisible(true);
                    } else if ("PlayerVsPlayer".equals(modality)) {
                        PlayerVsPlayer pvpMenu = new PlayerVsPlayer();
                        pvpMenu.setVisible(true);
                    } else {
                        MachineVSMachine mvsmMenu = new MachineVSMachine();
                        mvsmMenu.setVisible(true);
                    }
                });
            }

            if (imagePath.contains("home-icon")) {
                button.addActionListener(e -> {
                    // Back to main menu
                    dispose(); // Close the current window
                    POOBvsZombiesGUI POOBvsZombiesGUI = new POOBvsZombiesGUI(); // Open the main menu
                    POOBvsZombiesGUI.setVisible(true);
                });
            }

            panel.add(button);
            x += 60; // Adjust X position for the next button
        }
    }

    // Método para recargar entidades en la UI después de cargar partida
    public void reloadEntitiesUI() {
        ArrayList<ArrayList<Object>> matrix = poobvszombies.getEntitiesMatrix();
        for (int i=0; i<5; i++){
            for (int j=0; j<10; j++){
                Object obj = matrix.get(i).get(j);
                if (j<9) {
                    if (obj instanceof Plant) {
                        spawnPlantAutomatically(i,j,(Plant)obj);
                    } else if (obj instanceof Zombie) {
                        spawnZombieAutomatically(i,j,(Zombie)obj);
                    }
                } else {
                    @SuppressWarnings("unchecked")
                    Queue<Zombie> q = (Queue<Zombie>)obj;
                    if (!q.isEmpty()) {
                        Zombie z = q.peek();
                        spawnZombieAutomatically(i,j,z);
                    }
                }
            }
        }
        updateScoreLabels();
    }

    // Método para restaurar estado tras cargar partida
    public void restoreGameState(int currentIndex, int currentRemaining, boolean wasPaused) {
        // Limpiar timers actuales
        if (phaseTimer != null && phaseTimer.isRunning()) phaseTimer.stop();
        if (resourceTimerForPlayerVsPlayer != null) resourceTimerForPlayerVsPlayer.stop();
        if (resourceTimerForPvMOrMvM != null) resourceTimerForPvMOrMvM.stop();
        if (resourceTimerMvM != null) resourceTimerMvM.stop();

        // Re-crear timerTasks según modalidad y matchTime/hordas
        timerTasks.clear();
        if ("PlayerVsPlayer".equals(modality)) {
            timerTasks.add(new TimerTask("Planting time 1", Plants.PLANTING_TIME));
            timerTasks.add(new TimerTask("Round time", (int) poobvszombies.getRoundTime()));
            timerTasks.add(new TimerTask("Planting time 2", Plants.PLANTING_TIME));
            timerTasks.add(new TimerTask("Last round", (int) poobvszombies.getRoundTime()));

            resourceTimerForPlayerVsPlayer = new Timer(10000, e->{
                if(isPaused)return;
                String currentPhase=messageLabel.getText();
                if (currentPhase.contains("Planting time")) {
                    spawnResource(new Resource(Resource.SOL));
                } else if(currentPhase.contains("Round")||currentPhase.contains("Last round")){
                    spawnResource(new Resource(Resource.BRAIN));
                }
            });
            resourceTimerForPlayerVsPlayer.start();

        } else if ("PlayerVsMachine".equals(modality)) {
            timerTasks.add(new TimerTask("Planting time",20));
            int hordersNumber=poobvszombies.getHordersNumber();
            int totalTime=(int)poobvszombies.getMatchTime();
            int remainingTime=totalTime;
            remainingTime-=20;
            int hordeDuration=remainingTime/hordersNumber;
            int extra=remainingTime%hordersNumber;
            for(int i=1;i<=hordersNumber;i++){
                int dur=hordeDuration;
                if(i<=extra)dur+=1;
                timerTasks.add(new TimerTask("Horda "+i,dur));
            }

            resourceTimerForPvMOrMvM = new Timer(10000,e->{
                if(isPaused)return;
                spawnResource(new Resource(Resource.SOL));
            });
            resourceTimerForPvMOrMvM.start();

        } else if ("MachineVsMachine".equals(modality)) {
            timerTasks.add(new TimerTask("Planting time",20));
            int hordersNumber=poobvszombies.getHordersNumber();
            int totalTime=(int)poobvszombies.getMatchTime();
            int rem=totalTime-20;
            int hordeDuration=rem/hordersNumber;
            int extra=rem%hordersNumber;
            for(int i=1;i<=hordersNumber;i++){
                int dur=hordeDuration;
                if(i<=extra)dur+=1;
                timerTasks.add(new TimerTask("Horda "+i,dur));
            }

            resourceTimerMvM = new Timer(10000,e->{
                if(isPaused)return;
                String currentPhase=messageLabel.getText();
                if(currentPhase.contains("Planting time")){
                    spawnResource(new Resource(Resource.SOL));
                } else if (currentPhase.contains("Horda")){
                    spawnResource(new Resource(Resource.SOL));
                    spawnResource(new Resource(Resource.BRAIN));
                }
            });
            resourceTimerMvM.start();
        }

        // Actualizar índices
        this.currentTimerTaskIndex = currentIndex;
        this.currentTimerTaskRemainingTime = currentRemaining;

        // Iniciar desde ese punto
        startSequentialTimers(currentIndex, currentRemaining);
        
        reloadCards(); // Recargar cartas de plantas y zombies
        if (wasPaused) {
            pauseGame();
        }
    }

    public void reloadCards() {
        // Actualizar las listas selectedPlants y selectedZombies a partir de lo que tengan los jugadores.
        Player playerOne = poobvszombies.getPlayerOne();
        Player playerTwo = poobvszombies.getPlayerTwo();

        this.selectedPlants = playerOne.getTeam().getCharacters();
        // Dependiendo de la modalidad, puede que PlayerTwo tenga equipo de zombies
        // En PlayerVsPlayer y MachineVsMachine el PlayerTwo es zombies, en PlayerVsMachine el PlayerTwo es zombies también.
        // En PlayerVsMachine, PlayerTwo (la máquina) pone zombies automáticamente, igual su team tiene su lista
        this.selectedZombies = playerTwo.getTeam().getCharacters();

        // Antes de volver a añadir las cartas, podemos asegurarnos de no duplicar.
        // Si las cartas ya estaban, al cargar estado quizá queremos removerlas primero.
        // Sin embargo, en el código original las cartas se añadían en el constructor.
        // Podríamos, por seguridad, remover las cartas antiguas si las hubo.
        // Asumiremos que las cartas estaban en la parte superior del panel (en posiciones fijas).

        // Opcional: Limpiar el área donde estaban las cartas
        // Suponiendo que las cartas se añadieron en posiciones fijas (x,y),
        // podemos no hacer nada y volver a mostrarlas superpuestas, o mejor, reabrir la ventana.
        // Por simplicidad, asumimos que las cartas no se duplican. Si es necesario, crear un método para limpiar.

        // Volver a añadir las cartas de plantas y zombies
        Container parentPanel = (Container) getContentPane().getComponent(0);
        if (parentPanel instanceof JPanel) {
            JPanel panel = (JPanel) parentPanel;
            addPlantsCards(panel);
            if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
                addPlantsCards(panel);
                addZombieCards(panel);
                panel.revalidate();
                panel.repaint();
                
            } else if ("PlayerVsMachine".equals(modality)) {
                // En PlayerVsMachine la lista de zombies del playerTwo se usa internamente
                // Dependiendo de si se necesita mostrar sus cartas (si se permitía arrastrar zombies o no)
                // Originalmente, PlayerVsMachine no mostraba zombies para arrastrar el jugador humano, 
                // ya que el segundo jugador es la máquina. Así que en PlayerVsMachine no mostramos zombies.
                // Sólo en PlayerVsPlayer o MachineVsMachine mostramos las cartas de zombies.

                addPlantsCards(panel);
            }
            panel.revalidate();
            panel.repaint();
        }
    }
    // Sobrecarga startSequentialTimers para aceptar remainingTime
    
    public void startSequentialTimers(int index, int remainingTime) {
        if (index >= timerTasks.size()) {
            poobvszombies.calculateScores();
            String winnerMessage=poobvszombies.determineWinner();
            poobvszombies.endGame(winnerMessage);
            return;
        }

        TimerTask currentTask = timerTasks.get(index);
        int duration = currentTask.getDurationInSeconds();
        int timeToUse = (remainingTime>0 && remainingTime<duration)?remainingTime:duration;
        messageLabel.setText(currentTask.getMessage());
        timeLabel.setText(formatTime(timeToUse));
        messageLabel.setVisible(true);
        timeLabel.setVisible(true);

        if (phaseTimer!=null && phaseTimer.isRunning()) phaseTimer.stop();
        phaseTimer = new Timer(1000,null);
        phaseTimer.addActionListener(new ActionListener(){
            int remain=timeToUse;
            @Override
            public void actionPerformed(ActionEvent e){
                if(isPaused)return;
                if(remain>0){
                    remain--;
                    currentTimerTaskRemainingTime=remain;
                    timeLabel.setText(formatTime(remain));
                } else {
                    phaseTimer.stop();
                    messageLabel.setVisible(false);
                    timeLabel.setVisible(false);
                    currentTimerTaskIndex=index+1;
                    currentTimerTaskRemainingTime=0;
                    startSequentialTimers(index+1,0);
                }
            }
        });
        phaseTimer.setInitialDelay(0);
        phaseTimer.start();
    }


    private void togglePause() {
        if (!isPaused) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    // Para pausar el juego:
    private void pauseGame() {
        isPaused = true;

        // Detener cada Timer individualmente
        if (resourceTimerForPlayerVsPlayer != null && resourceTimerForPlayerVsPlayer.isRunning()) {
            resourceTimerForPlayerVsPlayer.stop();
        }

        if (resourceTimerForPvMOrMvM != null && resourceTimerForPvMOrMvM.isRunning()) {
            resourceTimerForPvMOrMvM.stop();
        }

        if (phaseTimer != null && phaseTimer.isRunning()) {
            phaseTimer.stop();
        }

        if(collectTimer != null && collectTimer.isRunning()){
            collectTimer.stop();
        }

        if(resourceTimerMvM != null && resourceTimerMvM.isRunning()){
            resourceTimerMvM.stop();
        }
        // Pausar ZombieThreadManager y ProjectTileThreadManager
        if (zombieThreadManager != null) zombieThreadManager.pauseZombies();
        if (projectTileThreadManager != null) projectTileThreadManager.pauseProjectiles();

        // Ocultar interacción del usuario:
        setCursor(Cursor.getDefaultCursor());

        pauseLabel.setVisible(true);
        pauseLabel.repaint();
    }

    private void resumeGame() {
        isPaused = false;

        // Reiniciar los timers individuales si estaban activos antes
        if (resourceTimerForPlayerVsPlayer != null && !resourceTimerForPlayerVsPlayer.isRunning()) {
            resourceTimerForPlayerVsPlayer.start();
        }

        if (resourceTimerForPvMOrMvM != null && !resourceTimerForPvMOrMvM.isRunning()) {
            resourceTimerForPvMOrMvM.start();
        }

        if (phaseTimer != null && !phaseTimer.isRunning()) {
            phaseTimer.start();
        }

        if(collectTimer != null && !collectTimer.isRunning()){
            collectTimer.start();
        }
        if(resourceTimerMvM != null && !resourceTimerMvM.isRunning()){
            resourceTimerMvM.start();
        }
        // Reanudar ZombieThreadManager y ProjectTileThreadManager
        if (zombieThreadManager != null) zombieThreadManager.resumeZombies();
        if (projectTileThreadManager != null) projectTileThreadManager.resumeProjectiles();

        pauseLabel.setVisible(false);
        pauseLabel.repaint();
    }

    private void addPlayerInfo(JPanel panel) {
        if (("PlayerVsPlayer".equals(modality) || "PlayerVsMachine".equals(modality))
                || "MachineVsMachine".equals(modality)) {
            Player playerOne = poobvszombies.getPlayerOne();

            // Etiqueta para el nombre del Jugador 1
            playerOneNameLabel = new JLabel("" + playerOne.getName());
            playerOneNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneNameLabel.setForeground(Color.YELLOW);
            playerOneNameLabel.setBounds(480, 595, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneNameLabel);

            // Etiqueta para los soles iniciales del Jugador 1
            playerOneSunsLabel = new JLabel("" + playerOne.getTeam().getResourceCounterAmount());
            playerOneSunsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneSunsLabel.setForeground(Color.ORANGE);
            playerOneSunsLabel.setBounds(30, 60, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneSunsLabel);
    
            if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
                Player playerTwo = poobvszombies.getPlayerTwo();

                // Etiqueta para el nombre del Jugador 2
                playerTwoNameLabel = new JLabel("" + playerTwo.getName());
                playerTwoNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoNameLabel.setForeground(Color.RED);
                playerTwoNameLabel.setBounds(480, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoNameLabel);

                // Etiqueta para los cerebros iniciales del Jugador 2
                playerTwoBrainsLabel = new JLabel("" + playerTwo.getTeam().getResourceCounterAmount());
                playerTwoBrainsLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoBrainsLabel.setForeground(Color.MAGENTA);
                playerTwoBrainsLabel.setBounds(50, 620, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoBrainsLabel);
            }

            if ("PlayerVsMachine".equals(modality)) {
                Player playerTwo = poobvszombies.getPlayerTwo();
    
                // Etiqueta para el nombre del Jugador 2
                playerTwoNameLabel = new JLabel("" + playerTwo.getName());
                playerTwoNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoNameLabel.setForeground(Color.RED);
                playerTwoNameLabel.setBounds(480, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoNameLabel);
            }
        }
    }

    /**
     * Método para agregar las etiquetas que muestran los puntajes de los jugadores.
     */
    private void addScoreLabels(JPanel panel) {
        // Etiqueta para el puntaje del Jugador 1
        if ("PlayerVsPlayer".equals(modality) || "PlayerVsMachine".equals(modality)
                || "MachineVsMachine".equals(modality)) {
            playerOneScoreLabel = new JLabel("Score: 0");
            playerOneScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerOneScoreLabel.setForeground(Color.YELLOW);
            playerOneScoreLabel.setBounds(560, 595, 300, 30); // Ajustar posición y tamaño
            panel.add(playerOneScoreLabel);

            if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)
                    || "PlayerVsMachine".equals(modality)) {
                // Etiqueta para el puntaje del Jugador 2
                playerTwoScoreLabel = new JLabel("Score: 0");
                playerTwoScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
                playerTwoScoreLabel.setForeground(Color.RED);
                playerTwoScoreLabel.setBounds(560, 625, 300, 30); // Ajustar posición y tamaño
                panel.add(playerTwoScoreLabel);
            }
        }
    }

    public void updateScoreLabels() {
        if (isPaused) return;
        playerOneScoreLabel.setText("Score: " + poobvszombies.getPlayerOne().getScore());
        playerTwoScoreLabel.setText("Score: " + poobvszombies.getPlayerTwo().getScore());

        // Refrescar las etiquetas para asegurarse de que se actualicen visualmente
        playerOneScoreLabel.revalidate();
        playerOneScoreLabel.repaint();
        playerTwoScoreLabel.revalidate();
        playerTwoScoreLabel.repaint();
    }
    
    public void showWinnerMessage(String winnerMessage) {
        JOptionPane.showMessageDialog(this, winnerMessage, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);
    }
    

    private void addZombieCards(JPanel panel) {
        if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality) && selectedZombies != null) {

            int x = 85; // Initial X position
            int y = 625; // Initial Y position
            for (String zombieName : selectedZombies) {
                if (zombieName != null) {

                    List<String> zombie = null;
                    for (List<String> zombies : ZOMBIES_VIEW) {
                        // Buscar el zombie en la lista de zombies y asignarlo a la variable zombie para
                        // tener todas sus posibles representaciones graficas
                        if (zombies.get(0).equals(zombieName)) {
                            // zombie ahora será un arreglo con todas las representaciones graficas del
                            // zombie
                            zombie = zombies;
                            break;
                        }
                    }

                    if (zombie == null)
                        continue;

                    // Add drag functionality
                    String dragImagePath = zombie.get(3);
                    ImageIcon icon = new ImageIcon(getClass().getResource(zombie.get(2)));
                    JLabel cardLabel = new JLabel(
                            new ImageIcon(icon.getImage().getScaledInstance(60, 85, Image.SCALE_SMOOTH)));
                    cardLabel.setBounds(x, y - 85, 100, 150);
                    panel.add(cardLabel);
                    cardLabel.setTransferHandler(new TransferHandler("icon") {
                        @Override
                        protected Transferable createTransferable(JComponent c) {
                            ImageIcon dragIcon = new ImageIcon(getClass().getResource(dragImagePath));
                            EntityData entityData = new EntityData("zombie", zombieName, dragIcon.getImage());
                            return new EntityTransferable(entityData);
                        }

                        @Override
                        public int getSourceActions(JComponent c) {
                            return COPY;
                        }
                    });

                    cardLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            JComponent comp = (JComponent) e.getSource();
                            TransferHandler handler = comp.getTransferHandler();
                            if (handler != null) {
                                handler.exportAsDrag(comp, e, TransferHandler.COPY);
                            }
                        }
                    });

                    x += 70; // Move X position for the next card
                }
            }
        }
    }

    private void addZombieTable(JPanel panel) {
        if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
            // Path of the zombie table image
            String zombieTableImagePath = "/images/zombie-table.png";

            // Load and scale the image
            ImageIcon zombieTableIcon = new ImageIcon(getClass().getResource(zombieTableImagePath));
            Image scaledZombieTableImage = zombieTableIcon.getImage().getScaledInstance(460, 80, Image.SCALE_SMOOTH);

            // Create a JLabel to display the zombie table
            JLabel zombieTableLabel = new JLabel(new ImageIcon(scaledZombieTableImage));
            zombieTableLabel.setBounds(10, 575, 460, 80); // Adjust position and size

            panel.add(zombieTableLabel);
        }
    }

    private void addBrainIcon(JPanel panel) {
        if ("PlayerVsPlayer".equals(modality) || "MachineVsMachine".equals(modality)) {
            // Path of the brain image
            String brainImagePath = "/images/brain.png";

            // Load and scale the image
            ImageIcon brainIcon = new ImageIcon(getClass().getResource(brainImagePath));
            Image scaledBrainImage = brainIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

            // Create a JLabel to display the brain
            JLabel brainLabel = new JLabel(new ImageIcon(scaledBrainImage));
            brainLabel.setBounds(50, 585, 40, 40); // Adjust position and size

            // Create a JLabel to display the text "100"
            JLabel textLabel = new JLabel("");
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(textLabel.getFont().deriveFont(20f));
            textLabel.setBounds(50, 625, 80, 30); // Adjust position and size

            panel.add(brainLabel);
            panel.add(textLabel);
        }
    }


    // Clase interna para representar cada temporizador
    private class TimerTask {
        private String message;
        private int durationInSeconds;

        public TimerTask(String message, int durationInSeconds) {
            this.message = message;
            this.durationInSeconds = durationInSeconds;
        }

        public String getMessage() {
            return message;
        }

        public int getDurationInSeconds() {
            return durationInSeconds;
        }
    }

    // Método para crear y mostrar el recurso en el tablero
    private void spawnResource(Resource resource) {
        // Seleccionar una celda aleatoria (5 filas, columnas de 1 a 9)
        int row = (int) (Math.random() * 5);
        int col = 1 + (int) (Math.random() * 9);

        final JPanel targetCell = gridCells[row][col];
        if (targetCell.getComponentCount() > 0) {
            // Si la celda está ocupada, intentar nuevamente (una única reintento por
            // simplicidad)
            row = (int) (Math.random() * 5);
            col = 1 + (int) (Math.random() * 9);
            @SuppressWarnings("unused")
            JPanel newTargetCell = gridCells[row][col];
        }

        // Crear JLabel con la imagen del recurso
        String imagePath = (resource.getType().equals(Resource.SOL)) ? "/images/sun.png"
                : "/images/brain.png";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel resourceLabel = new JLabel(new ImageIcon(scaledImage));
        resourceLabel.setHorizontalAlignment(JLabel.CENTER);
        targetCell.add(resourceLabel);
        targetCell.revalidate();
        targetCell.repaint();

        // Timer de 3 segundos para recoger el recurso automáticamente
        Timer collectTimer = new Timer(3000, (ActionEvent e) -> {
            if (isPaused) return; // No recolectar si el juego está pausado
            // Remover el recurso del tablero
            targetCell.remove(resourceLabel);
            targetCell.revalidate();
            targetCell.repaint();

            // Llamar al método collect del recurso
            resource.collect();

            // Sumar valor al equipo correspondiente
            // Determinar a quién sumar:
            // Asumiendo: PlayerOne = Plantas, PlayerTwo = Zombies
            // Si es SOL -> plantas (PlayerOne), si es BRAIN -> zombies (PlayerTwo)
            Player playerOne = poobvszombies.getPlayerOne();
            Player playerTwo = poobvszombies.getPlayerTwo();

            if (Resource.SOL.equals(resource.getType())) {
                // Sumar al equipo de plantas
                playerOne.getTeam().addResource(resource);
                playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                updateScoreLabels(); // Reflejar el puntaje actualizado

            } else if (Resource.BRAIN.equals(resource.getType())) {
                // Sumar al equipo de zombies
                playerTwo.getTeam().addResource(resource);
                playerTwoBrainsLabel.setText("" + playerTwo.getTeam().getResourceCounterAmount());
                updateScoreLabels(); // Reflejar el puntaje actualizado

            }

            ((Timer) e.getSource()).stop();
        });
        collectTimer.setRepeats(false);
        collectTimer.start();
    }

    // Método para inicializar los temporizadores
    private void initializeTimers(POOBvsZombies poobvsZombies) {
        if ("PlayerVsPlayer".equals(modality)) {
            timerTasks.add(new TimerTask("Planting time 1", Plants.PLANTING_TIME));
            timerTasks.add(new TimerTask("Round time", (int) poobvsZombies.getRoundTime()));
            timerTasks.add(new TimerTask("Planting time 2", Plants.PLANTING_TIME));
            timerTasks.add(new TimerTask("Last round", (int) poobvsZombies.getRoundTime()));

            // Timer para generar recursos cada 10s en PlayerVsPlayer
            resourceTimerForPlayerVsPlayer = new Timer(10000, e -> {
                if (isPaused) return; // No generar recursos si el juego está pausado
                String currentPhase = messageLabel.getText();
                if (currentPhase.contains("Planting time")) {
                    // Planting time -> soles
                    spawnResource(new Resource(Resource.SOL));
                } else if (currentPhase.contains("Round") || currentPhase.contains("Last round")) {
                    // Round time/Last round -> cerebros
                    spawnResource(new Resource(Resource.BRAIN));
                }
            });
            resourceTimerForPlayerVsPlayer.start();

        } else if ("PlayerVsMachine".equals(modality) || "MachineVsMachine".equals(modality)) {
            int matchTimeInSeconds = (int) poobvsZombies.getMatchTime();
            int hordersNumber = poobvsZombies.getHordersNumber();

            // Primero, 20 segundos para colocar plantas
            timerTasks.add(new TimerTask("Planting time", 20));

            // Validar que el tiempo total sea al menos 20 segundos y haya al menos una
            // horda
            if (matchTimeInSeconds < 1 || hordersNumber <= 0) {
                try {
                    throw new POOBvsZombiesException(POOBvsZombiesException.TIME_LIMIT);
                } catch (POOBvsZombiesException e) {
                    e.printStackTrace();
                }
                
            }

            // Calcular el tiempo restante después de la fase de plantación
            int remainingTime = matchTimeInSeconds;

            // Dividir el tiempo restante entre el número de hordas
            int hordeDuration = remainingTime / hordersNumber;
            int extraSeconds = remainingTime % hordersNumber; // Para segundos sobrantes

            for (int i = 1; i <= hordersNumber; i++) {
                int duration = hordeDuration;
                if (i <= extraSeconds) {
                    duration += 1; // Distribuir segundos sobrantes
                }
                timerTasks.add(new TimerTask("Horda " + i, duration));
            }

            // Timers para generar recursos cada 10s en PlayerVsMachine o MachineVsMachine
            if ("PlayerVsMachine".equals(modality)) {
                // En PlayerVsMachine: Soles cada 10 segundos
                resourceTimerForPvMOrMvM = new Timer(10000, e -> {
                    if (isPaused) return; // No generar recursos si el juego está pausado
                    spawnResource(new Resource(Resource.SOL));
                });
                resourceTimerForPvMOrMvM.start();

            } else if ("MachineVsMachine".equals(modality)) {
                // En MachineVsMachine: Soles y Cerebros cada 10 segundos
                resourceTimerMvM = new Timer(10000, e -> {
                    if (isPaused) return; // No generar recursos si el juego está pausado
                    String currentPhase = messageLabel.getText();
                    if (currentPhase.contains("Planting time")) {
                        spawnResource(new Resource(Resource.SOL));
                    }

                    else if (currentPhase.contains("Horda"))
                        spawnResource(new Resource(Resource.SOL));
                    spawnResource(new Resource(Resource.BRAIN));
                });
                resourceTimerMvM.start();
            }
        }
    }

    // Método para configurar las etiquetas en la interfaz
    private void setupTimerLabels(JPanel panel) {
        int startX = 670;
        int startY = 600;

        // Crear la etiqueta para el mensaje
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setBounds(startX, startY, 200, 30);

        // Crear la etiqueta para el tiempo
        timeLabel = new JLabel();
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setBounds(startX + 120, startY, 200, 30);

        // Agregar las etiquetas al panel
        panel.add(messageLabel);
        panel.add(timeLabel);

        // Ocultar las etiquetas inicialmente
        messageLabel.setVisible(false);
        timeLabel.setVisible(false);
    }

    // Método para iniciar los temporizadores secuencialmente
    private void startSequentialTimers(int index) {
        if (index >= timerTasks.size()) {
            // Todos los temporizadores han finalizado
            // Ocultar las etiquetas
            poobvszombies.calculateScores();
            String winnerMessage = poobvszombies.determineWinner();
            poobvszombies.endGame(winnerMessage);
            return;
        }

        
        TimerTask currentTask = timerTasks.get(index);
        int duration = currentTask.durationInSeconds;

        // Actualizar las etiquetas y hacerlas visibles
        messageLabel.setText(currentTask.message);
        timeLabel.setText(formatTime(duration));
        messageLabel.setVisible(true);
        timeLabel.setVisible(true);

        phaseTimer = new Timer(1000, null);
        if (isPaused) return; // No iniciar el temporizador si el juego está pausado
        phaseTimer.addActionListener(new ActionListener() {
            int remainingTime = duration;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingTime > 0) {
                    remainingTime--;
                    timeLabel.setText(formatTime(remainingTime));
                } else {
                    phaseTimer.stop();
                    // Ocultar las etiquetas
                    messageLabel.setVisible(false);
                    timeLabel.setVisible(false);
                    // Iniciar el siguiente temporizador
                    startSequentialTimers(index + 1);
                }
            }
        });

        phaseTimer.setInitialDelay(0);
        phaseTimer.start();
    }

    // Método para formatear el tiempo
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    // Method to show the matrix of entities each time an entity is added
    @SuppressWarnings("unused")
    private void showEntityMatrix() {
        StringBuilder matrixString = new StringBuilder();

        // Assuming the grid is 5 rows by 10 columns
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                String entity = poobvszombies.getEntity(row, col);
                matrixString.append(entity != null ? entity : "-");
                if (col < 9) {
                    matrixString.append(" ");
                }
            }
            matrixString.append("\n");
        }

        JOptionPane.showMessageDialog(this, matrixString.toString(), "Entity Matrix", JOptionPane.INFORMATION_MESSAGE);
    }

    private Plant createPlantInstance(String plantName) {
        switch (plantName) {
            case "Sunflower":
                return new Sunflower();
            case "Peashooter":
                return new Peashooter();
            case "WallNut":
                return new WallNut();
            case "PotatoMine":
                return new PotatoMine();
            case "ECIPlant":
                return new ECIPlant();
            default:
                return null;
        }
    }

    private Zombie createZombieInstance(String zombieName) {
        switch (zombieName) {
            case "Basic":
                return new Basic();
            case "Brainstein":
                return new Brainstein();
            case "BucketHead":
                return new Buckethead();
            case "Conehead":
                return new Conehead();
            case "ECIZombie":
                return new ECIZombie();
            default:
                return null;
        }
    }

    public void spawnSpecificResource(int row, int col, Resource resource) {
        JPanel targetCell = gridCells[row][col];
        // Determinar la ruta de la imagen basada en el tipo de recurso
        String imagePath;
        if (Resource.SOL.equals(resource.getType())) {
            imagePath = "/images/sun.png"; // Imagen regular del sol
        } else if (Resource.BIG_SOL.equals(resource.getType())) {
            imagePath = "/images/big-sun.png"; // Imagen para Big Sun
        } else if (Resource.BRAIN.equals(resource.getType())) {
            imagePath = "/images/brain.png"; // Imagen del cerebro
        } else if (Resource.THREE_BIG_SOL.equals(resource.getType())){
            imagePath = "/images/three-big-sun.png"; // Imagen para THREE_BIG_SOL
        }    
        else {
            imagePath = "/images/default.png"; // Imagen por defecto
        }
    
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        // Verificar si la imagen existe
        if (icon.getIconWidth() == -1) {
            // Imagen no encontrada, usar una imagen de placeholder
            System.err.println("Imagen no encontrada: " + imagePath);
            imagePath = "/images/default.png"; // Ruta alternativa
            icon = new ImageIcon(getClass().getResource(imagePath));
        }
    
        Image scaledImage;
    
        if (Resource.THREE_BIG_SOL.equals(resource.getType())) {
            scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Reducir tamaño para THREE_BIG_SOL
        } else {
            scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        }
    
        JLabel resourceLabel = new JLabel(new ImageIcon(scaledImage));
        resourceLabel.setHorizontalAlignment(JLabel.CENTER);
        targetCell.add(resourceLabel);
        targetCell.revalidate();
        targetCell.repaint();
        targetCell.setComponentZOrder(resourceLabel, 0);
    
        if (Resource.THREE_BIG_SOL.equals(resource.getType()) && !isPaused) {
            // Directamente sumar 150 soles al contador del jugador sin esperar a la recolección
            Player playerOne = poobvszombies.getPlayerOne();
            playerOne.getTeam().addResource(new Resource(Resource.SOL, 150));
            playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
            System.out.println("Se han sumado 150 soles al contador del jugador debido a THREE_BIG_SOL.");
    
            // Iniciar Timer para remover THREE_BIG_SOL después de 3 segundos
            collectTimer = new Timer(3000, (ActionEvent ev) -> {
                
                targetCell.remove(resourceLabel);
                targetCell.revalidate();
                targetCell.repaint();
                targetCell.setComponentZOrder(resourceLabel, 0);
                resource.collect();
    
                ((Timer) ev.getSource()).stop();
                System.out.println("THREE_BIG_SOL removido después de 3 segundos.");
            });
            collectTimer.setRepeats(false);
            collectTimer.start();
        } else {
            // Iniciar Timer de recolección automática para SOL, BIG_SOL y BRAIN
            Timer collectTimer = new Timer(3000, (ActionEvent ev) -> {
                
                // Sumar el valor al jugador correspondiente
                Player playerOne = poobvszombies.getPlayerOne();
                Player playerTwo = poobvszombies.getPlayerTwo();
                if (Resource.SOL.equals(resource.getType()) || Resource.BIG_SOL.equals(resource.getType())) {
                    playerOne.getTeam().addResource(resource);
                    playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                } else if (Resource.BRAIN.equals(resource.getType())) {
                    playerTwo.getTeam().addResource(resource);
                    playerTwoBrainsLabel.setText("" + playerTwo.getTeam().getResourceCounterAmount());
                }
    
                // Remover el recurso del tablero
                targetCell.remove(resourceLabel);
                targetCell.revalidate();
                targetCell.repaint();
                resource.collect();
    
                ((Timer) ev.getSource()).stop();
                System.out.println("Recurso " + resource.getType() + " removido después de 3 segundos.");
            });
            collectTimer.setRepeats(false);
            collectTimer.start();
            }
        }
    
    

    
    private Timer createEntityTimer(ResourceGenerator generator, int row, int col) {
        return new Timer(20000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resource res = generator.generateResource(row);
                if (res != null) {
                    spawnSpecificResource(row, col, res);
                    System.out.println("Generando recurso: " + res.getType() + " en (" + row + ", " + col + ")");
    
                    // Manejar condiciones especiales para ECIPlant
                    if (generator instanceof ECIPlant) {
                        ECIPlant eciPlant = (ECIPlant) generator;
                        Player playerOne = poobvszombies.getPlayerOne();
                        System.out.println("Contador de soles antes de verificar: " + playerOne.getTeam().getResourceCounterAmount());
    
                        // Verificar si el contador es <= 0 y si aún no se han generado los soles extras
                        if (playerOne.getTeam().getResourceCounterAmount() <= 0 && !eciPlant.hasGeneratedExtraSuns()) {
                            eciPlant.setExtraSunsGenerated(true);
                            System.out.println("ECIPlant detectó contador de soles <= 0. Generando THREE_BIG_SOL.");
    
                            // Generar THREE_BIG_SOL
                            Resource threeBigSol = new Resource(Resource.THREE_BIG_SOL, Resource.THREE_BIG_VALUE);
                            spawnSpecificResource(row, col, threeBigSol);
    
                            // Sumar 150 soles directamente al contador
                            playerOne.getTeam().addResource(new Resource(Resource.SOL, 150));
                            playerOneSunsLabel.setText("" + playerOne.getTeam().getResourceCounterAmount());
                            System.out.println("ECIPlant ha sumado 150 soles al contador.");
                        }
                    }
                }
            }
        });
    }
    
    
    public void removePlantAt(int row, int col) {
        JPanel cell = gridCells[row][col];
        cell.removeAll();
        cell.revalidate();
        cell.repaint();
    }

    @SuppressWarnings("unused")
    private int getZombieRow(JLabel zombieLabel) {
        Point location = zombieLabel.getLocation();
        int gridStartY = 80;
        int cellHeight = (500 + 20) / 5;
        int relativeY = location.y - gridStartY;
        int row = relativeY / cellHeight;
        return row;
    }

    // Añade este método dentro de la clase GardenMenu:
    public void spawnZombieAutomatically(int row, int col, Zombie zombie) {
        // Buscar la definición gráfica del zombie según su clase
        // Usamos el método entityName() de POOBvsZombies o podemos mapear directamente.
        String zombieName = null;
        if (zombie instanceof Basic) zombieName = "Basic";
        else if (zombie instanceof Brainstein) zombieName = "Brainstein";
        else if (zombie instanceof Buckethead) zombieName = "BucketHead";
        else if (zombie instanceof Conehead) zombieName = "Conehead";

        // Encontrar las imágenes del zombie en ZOMBIES_VIEW
        List<String> zombieData = null;
        for (List<String> zData : ZOMBIES_VIEW) {
            if (zData.get(0).equals(zombieName)) {
                zombieData = zData;
                break;
            }
        }
        if (zombieData == null) {
            System.err.println("No se encontró representación gráfica para el zombie: " + zombieName);
            return;
        }

        // zombieData: [0]: Nombre, [1]: imagen estatica, [2]: card, [3]: gif animado
        String dragImagePath = zombieData.get(3);
        ImageIcon icon = new ImageIcon(getClass().getResource(dragImagePath));

        // Obtener la celda destino para determinar posición
        JPanel targetCell = gridCells[row][col];
        @SuppressWarnings("unused")
        Point cellLocation = targetCell.getLocation();
        // La posición absoluta en el panel principal
        int x = 40 + col * 80; // Basado en el cálculo del grid (ya definido en zombieLogic)
        int y = 80 + row * 100; // Ajustar acorde a las dimensiones reales de cada celda

        JLabel zombieLabel = new JLabel(icon);
        zombieLabel.setHorizontalAlignment(JLabel.CENTER);
        zombieLabel.setBounds(x, y, 80, 100); 
        // Añadir el zombieLabel al panel principal (el primero que agregamos en el constructor)
        Container parentPanel = (Container) this.getContentPane().getComponent(0); // El panel principal
        if (parentPanel instanceof JPanel) {
            JPanel mainPanel = (JPanel) parentPanel;
            mainPanel.add(zombieLabel);
            mainPanel.setComponentZOrder(zombieLabel, 0);
            mainPanel.revalidate();
            mainPanel.repaint();
        }

        // Registrar el zombie en ZombieThreadManager para que se mueva y ataque
        zombieThreadManager.registerZombie(row, zombie, zombieLabel);
        
        // Añadir el zombieLabel a la lista movingZombies si es necesario
        movingZombies.add(zombieLabel);
    }

    /**
     * Método nuevo para manejar la generación automática de plantas.
     *
     * @param row   Fila donde se coloca la planta.
     * @param col   Columna donde se coloca la planta.
     * @param plant Instancia de la planta a visualizar.
     */
    public void spawnPlantAutomatically(int row, int col, Plant plant) {
        // Buscar la definición gráfica de la planta según su clase
        String plantName = null;
        if (plant instanceof Sunflower) plantName = "Sunflower";
        else if (plant instanceof Peashooter) plantName = "Peashooter";
        else if (plant instanceof WallNut) plantName = "WallNut";
        // Agrega otros tipos de plantas si es necesario

        // Encontrar las imágenes de la planta en PLANTS_VIEW
        List<String> plantData = null;
        for (List<String> pData : PLANTS_VIEW) {
            if (pData.get(0).equals(plantName)) {
                plantData = pData;
                break;
            }
        }
        if (plantData == null) {
            System.err.println("No se encontró representación gráfica para la planta: " + plantName);
            return;
        }

        // plantData: [0]: Nombre, [1]: imagen estática, [2]: card, [3]: gif animado
        String plantImagePath = plantData.get(3); // Usar la imagen animada

        ImageIcon icon = new ImageIcon(getClass().getResource(plantImagePath));

        // Obtener la celda destino para determinar posición
        JPanel targetCell = gridCells[row][col];
        @SuppressWarnings("unused")
        Point cellLocation = targetCell.getLocation();

        // // Calcular posición absoluta
        // int x = 40 + col * 80; // Basado en el cálculo del grid (ya definido en zombieLogic)
        // int y = 80 + row * 100; // Ajustar acorde a las dimensiones reales de cada celda

        JLabel plantLabel = new JLabel(icon);
        plantLabel.setHorizontalAlignment(JLabel.CENTER);
        //plantLabel.setBounds(x, y, 80, 100); // Ajustar tamaño según el grid

        targetCell.add(plantLabel);
        targetCell.revalidate();
        targetCell.repaint();
        // Si la planta es un generador de recursos, crear un temporizador para generar recursos
        if (plant instanceof ResourceGenerator) {
            Timer plantTimer = createEntityTimer((ResourceGenerator) plant, row, col);
            entityTimers.put(plant, plantTimer);
            plantTimer.start();
        }

        // Si la planta es un Peashooter, registrar su proyectil
        if (plant instanceof Peashooter) {
            ProjectTile projectile = new ProjectTile();
            // Usamos la posición relativa de la celda, aunque en este caso el projectTileThreadManager
            // podría necesitar cálculos adicionales. Ajustar según sea necesario.
            int x = targetCell.getX();
            int y = targetCell.getY();
            projectTileThreadManager.registerProjectTile(row, col, projectile, x, y);
        }
    }

    public JPanel getMainPanel() {
        return (JPanel) getContentPane().getComponent(0); 
        // Suponiendo que el panel principal es el primer componente del contentPane
        // Ajusta según tu estructura
    }
    
    public JLabel getPlantLabelAt(int row, int col) {
        if (row < 0 || row >= 5 || col < 0 || col >= 10) return null;
        JPanel cell = gridCells[row][col];
        if (cell != null && cell.getComponentCount() > 0) {
            java.awt.Component comp = cell.getComponent(0);
            if (comp instanceof JLabel) {
                return (JLabel) comp;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<String> selectedPlants = new ArrayList<>(Arrays.asList(
                    "Sunflower",
                    "Peashooter",
                    "WallNut",
                    "PotatoMine",
                    "ECIPlant"));
            ArrayList<String> selectedZombies = new ArrayList<>(Arrays.asList(
                    "Basic",
                    "Conehead",
                    "BucketHead",
                    "ECIZombie",
                    "Brainstein"));

            // GardenMenu frame = new GardenMenu(selectedPlants, selectedZombies,
            // "PlayerVsPlayer");s
            int matchTimer = 1;
            String namePlayerOne = "Player1";
            int sunAmount = 50;
            String namePlayerTwo = "Player2";
            int brainAmount = 100;
            POOBvsZombies poobvszombies;
            try {
                poobvszombies = new POOBvsZombies(matchTimer, namePlayerOne, selectedPlants, sunAmount,
                        namePlayerTwo, brainAmount, selectedZombies);

                        GardenMenu frame = new GardenMenu(poobvszombies);
                        frame.setVisible(true);
            } catch (POOBvsZombiesException e) {
                e.printStackTrace();
            }
            
        });
    }
}