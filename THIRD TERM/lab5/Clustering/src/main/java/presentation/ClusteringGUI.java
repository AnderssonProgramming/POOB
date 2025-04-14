package presentation;

import domain.Clustering;
import domain.ClusteringException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase de interfaz para ver la visualización gráfica del juego "Clustering".
 * @author Andersson David Sanchez Mendez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class ClusteringGUI extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuItemNew, menuItemOpen, menuItemSave, menuItemExit;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private JTextField inputHeight, inputWidth, inputPercentage;
    private JTextField scoreField, tiltingField;
    private JButton changeColorButton, resetButton, btnGenerateBoard, btnChangeConfig, changeSizeButton, finishButton, importButton ;
    private JPanel[] cells;
    private Clustering clustering;
    private char[][] initialBoardState; // Estado inicial del tablero para restaurar
    private boolean isInitialConfig; // Indica si estamos en la configuración inicial
    private Map<Integer, Color> customColors; // Mapa para almacenar colores personalizados de celdas

    /*Modelo*/
    private ClusteringGUI() {
        prepareElements();
        prepareElementsMenu();
        prepareActions();
    }

    /*Vista*/
    private void prepareElements() {
        setTitle("Clustering");
        setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        setResizable(false); // Desactivar la opción de maximizar

        // Panel de entrada de configuración en la parte superior
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Height:"));
        inputHeight = new JTextField(3);
        inputPanel.add(inputHeight);

        inputPanel.add(new JLabel("Width:"));
        inputWidth = new JTextField(3);
        inputPanel.add(inputWidth);

        inputPanel.add(new JLabel("Percentage:"));
        inputPercentage = new JTextField(3);
        inputPanel.add(inputPercentage);

        btnGenerateBoard = new JButton("Añadir Tablero");
        btnGenerateBoard.addActionListener(e -> prepareElementsBoard());
        inputPanel.add(btnGenerateBoard);

        btnChangeConfig = new JButton("Cambiar Configuración");
        btnChangeConfig.addActionListener(e -> changeConfiguration());
        btnChangeConfig.setVisible(false);
        inputPanel.add(btnChangeConfig);

        // Botón "Reiniciar" en la parte superior, debajo del título
        resetButton = new JButton("Reiniciar Juego");
        resetButton.addActionListener(e -> resetGame());
        resetButton.setVisible(false);
        inputPanel.add(resetButton);

        add(inputPanel, BorderLayout.NORTH);

        // Panel de control en la parte inferior con flechas y opciones
        controlPanel = new JPanel(new BorderLayout());

        JPanel arrowsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JButton btnNorth = new JButton("↑");
        btnNorth.addActionListener(e -> tiltBoard('D'));
        c.gridx = 1;
        c.gridy = 0;
        arrowsPanel.add(btnNorth, c);

        JButton btnWest = new JButton("←");
        btnWest.addActionListener(e -> tiltBoard('R'));
        c.gridx = 0;
        c.gridy = 1;
        arrowsPanel.add(btnWest, c);

        JButton btnCenter = new JButton();
        btnCenter.setEnabled(false);
        btnCenter.setPreferredSize(new Dimension(50, 50));
        c.gridx = 1;
        c.gridy = 1;
        arrowsPanel.add(btnCenter, c);

        JButton btnEast = new JButton("→");
        btnEast.addActionListener(e -> tiltBoard('L'));
        c.gridx = 2;
        c.gridy = 1;
        arrowsPanel.add(btnEast, c);

        JButton btnSouth = new JButton("↓");
        btnSouth.addActionListener(e -> tiltBoard('U'));
        c.gridx = 1;
        c.gridy = 2;
        arrowsPanel.add(btnSouth, c);

        controlPanel.add(arrowsPanel, BorderLayout.CENTER);

        JPanel leftControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        changeColorButton = new JButton("Cambiar Color");
        changeColorButton.addActionListener(e -> changeTileColors());
        leftControlsPanel.add(changeColorButton);
        controlPanel.add(leftControlsPanel, BorderLayout.WEST);

        JPanel rightControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        scoreField = new JTextField(5);
        scoreField.setEditable(false);
        tiltingField = new JTextField(5);
        tiltingField.setEditable(false);
        rightControlsPanel.add(new JLabel("Score:"));
        rightControlsPanel.add(scoreField);
        rightControlsPanel.add(new JLabel("Tilting:"));
        rightControlsPanel.add(tiltingField);
        controlPanel.add(rightControlsPanel, BorderLayout.EAST);

        changeSizeButton = new JButton("Cambiar tamaño pantalla");
        changeSizeButton.addActionListener(e -> prepareChangeScreenSize());
        rightControlsPanel.add(changeSizeButton);

        controlPanel.add(rightControlsPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.SOUTH);

        finishButton = new JButton("Finalizar");
        add(finishButton, BorderLayout.EAST);

        importButton = new JButton("Importar");
        importButton.setPreferredSize(new Dimension(120, 30));
        add(importButton, BorderLayout.WEST);

        // Inicializar mapa para almacenar colores personalizados
        customColors = new HashMap<>();
    }

    /*Vista*/
    private void prepareElementsMenu() {
        menuBar = new JMenuBar();
        menuFile = new JMenu("Archivo");
        menuItemNew = new JMenuItem("Nuevo");
        menuItemOpen = new JMenuItem("Abrir");
        menuItemSave = new JMenuItem("Salvar");
        menuItemExit = new JMenuItem("Salir");

        menuFile.add(menuItemNew);
        menuFile.add(menuItemOpen);
        menuFile.add(menuItemSave);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);

        menuBar.add(menuFile);
        setJMenuBar(menuBar);
    }


    /*Controlador*/
    private void prepareActions() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                int result = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Asignar el mismo comportamiento de salida al botón "Finalizar"
        finishButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ClusteringGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(
                        ClusteringGUI.this,
                        "Funcionalidad en construcción, por ahora importar = abrir:.\\n" + //
                                                        "Archivo seleccionado: " + selectedFile.getName(),
                        "Importar",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        // Asignar la funcionalidad al botón "Nuevo"
        menuItemNew.addActionListener(e -> {
            // Restablecer campos de entrada
            inputHeight.setText("");
            inputWidth.setText("");
            inputPercentage.setText("");

            // Restablecer visibilidad de botones
            btnGenerateBoard.setVisible(true);
            btnChangeConfig.setVisible(false);
            resetButton.setVisible(false);
            changeColorButton.setVisible(true); 

            // Reiniciar el Clustering y el estado del tablero
            clustering = null;
            initialBoardState = null;
            isInitialConfig = false;
            customColors.clear(); // Limpiar colores personalizados
            
            // Restablecer el puntaje y el número de inclinaciones
            scoreField.setText("0");
            tiltingField.setText("0");

            // Remover el panel del tablero completamente si existe
            if (boardPanel != null) {
                remove(boardPanel);
                boardPanel = null;
            }

            // Eliminar la referencia de los paneles de las celdas para evitar errores
            cells = null;

            // Actualizar la interfaz
            revalidate();
            repaint();

            JOptionPane.showMessageDialog(
                    ClusteringGUI.this,
                    "Se ha restablecido el juego. Ingrese nuevos valores para comenzar.",
                    "Nuevo Juego",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        menuItemExit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuItemOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ClusteringGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(
                        ClusteringGUI.this,
                        "Funcionalidad de abrir en construcción.\nArchivo seleccionado: " + selectedFile.getName(),
                        "Abrir",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        menuItemSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(ClusteringGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(
                        ClusteringGUI.this,
                        "Funcionalidad de salvar en construcción.\nArchivo seleccionado: " + selectedFile.getName(),
                        "Salvar",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    /*Vista*/
    private void prepareElementsBoard() {
        try {
            int height = Integer.parseInt(inputHeight.getText());
            int width = Integer.parseInt(inputWidth.getText());
            int percentage = Integer.parseInt(inputPercentage.getText());
    
            clustering = new Clustering(height, width, percentage);
            initialBoardState = copyBoard(clustering.getBoard());
            isInitialConfig = true;
    
            btnGenerateBoard.setVisible(false);
            btnChangeConfig.setVisible(true);
            resetButton.setVisible(true);
    
            customColors.clear(); // Limpiar los colores personalizados
            tiltingField.setText("0");
            updateBoard();
    
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese números válidos.");
        } catch (ClusteringException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    
    /*Vista*/
    private void prepareChangeScreenSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        changeSizeButton.setVisible(false);
    }

    /*Método usado para el botón de cambiar configuración */
    private void changeConfiguration() {
        prepareElementsBoard();
        tiltingField.setText("0"); // Reiniciar contador de inclinaciones
    }

    /**
     * Método usado para hacer el tilt correctamente, incluyendo si no hay colores básicos 
     * @param side direction to tilt.
     * 
    **/
    private void tiltBoard(char side) {
        if (clustering != null) {
            try {
                clustering.tilt(side);
                tiltingField.setText(String.valueOf(clustering.tiltings()));
    
                // Actualizar customColors
                updateCustomColors();
    
                updateBoard();
            } catch (ClusteringException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    /*Método para actualizar el tablero con los colores personalizados*/
    private void updateCustomColors() {
        Map<Integer, Color> newCustomColors = new HashMap<>();
        char[][] board = clustering.getBoard();
    
        for (int oldIndex : customColors.keySet()) {
            Color color = customColors.get(oldIndex);
    
            // Buscar la posición de la baldosa personalizada en el nuevo tablero
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 'c') {
                        int newIndex = i * board[i].length + j;
                        if (!newCustomColors.containsKey(newIndex)) {
                            newCustomColors.put(newIndex, color);
                            board[i][j] = 'v'; // Marcar como visitado para evitar duplicados
                            break;
                        }
                    }
                }
            }
        }
    
        // Restaurar el carácter 'c' en el tablero
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'v') {
                    board[i][j] = 'c';
                }
            }
        }
    
        customColors = newCustomColors;
    }
    
    
    /*Método que se dispara en el evento cuando se oprime reiniciar juego*/
    private void resetGame() {
        if (clustering != null) {
            try {
                if (isInitialConfig) {
                    // Reiniciar al estado inicial cuando se agregó el tablero por primera vez
                    clustering = new Clustering(clustering.getHeight(), clustering.getWidth(), clustering.getInitialPercentage());
                    clustering.setBoard(copyBoard(initialBoardState));
                    tiltingField.setText("0");
                } else {
                    // Reiniciar a la configuración modificada más reciente
                    clustering = new Clustering(clustering.getHeight(), clustering.getWidth(), clustering.getInitialPercentage());
                    tiltingField.setText("0");
                }
                updateBoard();
            } catch (ClusteringException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    /*Método para copiar el tablero y tenerlo de referencia para hacer el tilt o el reinicio*/
    private char[][] copyBoard(char[][] originalBoard) {
        char[][] copy = new char[originalBoard.length][originalBoard[0].length];
        for (int i = 0; i < originalBoard.length; i++) {
            System.arraycopy(originalBoard[i], 0, copy[i], 0, originalBoard[i].length);
        }
        return copy;
    }

    /*Método para actualizar las baldosas del color escogido a otro color personalizado*/
    private void changeTileColors() {
        Object[] options = {"Rojo", "Azul", "Verde", "Amarillo"};
        String selectedColor = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el color que desea cambiar:",
                "Seleccionar Color",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                "Rojo");
    
        if (selectedColor != null) {
            Color targetColor = switch (selectedColor) {
                case "Rojo" -> Color.RED;
                case "Azul" -> Color.BLUE;
                case "Verde" -> Color.GREEN;
                case "Amarillo" -> Color.YELLOW;
                default -> null;
            };
    
            Color newColor = JColorChooser.showDialog(this, "Seleccione un nuevo color", targetColor);
    
            if (newColor != null && targetColor != null) {
                //int width = clustering.getWidth();
                char targetChar = switch (selectedColor) {
                    case "Rojo" -> 'r';
                    case "Azul" -> 'b';
                    case "Verde" -> 'g';
                    case "Amarillo" -> 'y';
                    default -> ' ';
                };
    
                // Actualizar el arreglo board en Clustering
                char[][] board = clustering.getBoard();
                for (int i = 0; i < clustering.getHeight(); i++) {
                    for (int j = 0; j < clustering.getWidth(); j++) {
                        if (board[i][j] == targetChar) {
                            board[i][j] = 'c'; // Usar 'c' para representar el color personalizado
                        }
                    }
                }
    
                // Actualizar los colores en las celdas y en customColors
                for (int i = 0; i < cells.length; i++) {
                    if (cells[i].getBackground().equals(targetColor)) {
                        cells[i].setBackground(newColor);
                        customColors.put(i, newColor);
                    }
                }
                refresh();
            }
        }
    }
    
    /*Método para actualizar el tablero cuando se hace tilt o reinicio o nuevo tablero*/
    private void updateBoard() {
        if (boardPanel != null) {
            remove(boardPanel);
        }
    
        char[][] board = clustering.getBoard();
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(board.length, board[0].length));
    
        cells = new JPanel[board.length * board[0].length];
    
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
                int cellIndex = i * board[i].length + j;
    
                if (board[i][j] == 'c' && customColors.containsKey(cellIndex)) {
                    cell.setBackground(customColors.get(cellIndex));
                } else {
                    cell.setBackground(switch (board[i][j]) {
                        case 'r' -> Color.RED;
                        case 'g' -> Color.GREEN;
                        case 'b' -> Color.BLUE;
                        case 'y' -> Color.YELLOW;
                        default -> Color.LIGHT_GRAY;
                    });
                }
    
                cells[cellIndex] = cell;
                boardPanel.add(cell);
            }
        }
    
        add(boardPanel, BorderLayout.CENTER);
        scoreField.setText(String.valueOf(clustering.score()));
        revalidate();
        repaint();
    }
    
    /*Revalidar y repintar toda la interfaz en las posiciones escogidas del boceto inicial.*/
    private void refresh() {
        if (boardPanel != null) {
            boardPanel.revalidate();
            boardPanel.repaint();
        }
    }
    
    /**
     * Se conmpila para probar el juego.
     * @param args lo que recibe para probar el juego.
     */
    public static void main(String[] args) {
        ClusteringGUI gui = new ClusteringGUI();
        gui.setVisible(true);
    }
}
