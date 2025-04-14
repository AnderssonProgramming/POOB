package presentation;
import domain.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Represents the graphical user interface for the AManufacturing system.
 * The GUI allows users to visualize and control the simulation of the manufacturing lattice.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class AManufacturingGUI extends JFrame {  
    public static final int SIDE = 11;
    public final int SIZE;
    private JButton ticTacButton;
    //private JPanel controlPanel;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem optionNew, optionOpen, optionSave, optionExit, optionExport, optionImport;
    private PhotoAManufacturing photo;
    private AManufacturing aManufacturing;
   
    /**
     * Creates a new AManufacturingGUI window and initializes its components.
     */
    private AManufacturingGUI() {
        aManufacturing = new AManufacturing();
        SIZE = aManufacturing.getSize();
        prepareElements();
        prepareElementsMenu();
        prepareActions();
        prepareActionsMenu();
    }
    
    /**
     * Prepares and sets up the GUI elements, including buttons and the main panel.
     */
    private void prepareElements() {
        setTitle("AManufacturing replicate ");
        photo = new PhotoAManufacturing(this);
        ticTacButton = new JButton("Tic-tac");
        setLayout(new BorderLayout());
        add(photo, BorderLayout.NORTH);
        add(ticTacButton, BorderLayout.SOUTH);
        // Set the size of the window based on the lattice size
        setSize(new Dimension(SIDE * SIZE + 15, SIDE * SIZE + 72));
        setResizable(false);
        photo.repaint();
    }

    private void prepareElementsMenu() {
        menuBar = new JMenuBar();
        menuFile = new JMenu("Archivo");
        optionNew = new JMenuItem("Nuevo");
        optionOpen = new JMenuItem("Abrir");
        optionSave = new JMenuItem("Guardar como");
        optionImport = new JMenuItem("Importar");
        optionExport = new JMenuItem("Exportar como");
        optionExit = new JMenuItem("Salir");

        menuFile.add(optionNew);
        menuFile.addSeparator();
        menuFile.add(optionOpen);
        menuFile.add(optionSave);
        menuFile.addSeparator();
        menuFile.add(optionImport);
        menuFile.add(optionExport);
        menuFile.addSeparator();
        menuFile.add(optionExit);

        menuBar.add(menuFile);
        setJMenuBar(menuBar);
    }


    /**
     * Prepares the actions for the GUI elements, including handling the tic-tac button click.
     */
    private void prepareActions() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);       
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                int result = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        ticTacButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ticTacButtonAction();
                }
            });
    }

    private void prepareActionsMenu(){
        
        // Asignar la funcionalidad al botón "Nuevo"
        optionNew.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                                    AManufacturingGUI.this,
                            "¿Está seguro de que desea crear un nuevo replicate? Esto reiniciará todo el progreso actual.",
                            "Nuevo Replicado",
                                    JOptionPane.YES_NO_OPTION
                        );

            if (result == JOptionPane.YES_OPTION) {
                aManufacturing = new AManufacturing();  
                photo.repaint();

                JOptionPane.showMessageDialog(
                        AManufacturingGUI.this,
                        "Se ha creado un nuevo replicate.",
                        "Nuevo Juego",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });


        optionExit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        
        //FIRST VERSION
        /** 
        optionOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try{
                    AManufacturing.open00(selectedFile);
                }
                catch (ReplicateException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        optionSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try{
                    aManufacturing.save00(selectedFile);
                }
                catch (ReplicateException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        optionImport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try{
                    AManufacturing.import00(selectedFile);
                }
                catch (ReplicateException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        optionExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try{
                    aManufacturing.export00(selectedFile);
                }
                catch (ReplicateException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        **/

        /** 
        //SECOND VERSION

        optionSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("DAT files", "dat"));
            int result = fileChooser.showSaveDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".dat")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".dat");
                }
                aManufacturing.save(selectedFile);
            }
        });

        optionOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("DAT files", "dat"));
            int result = fileChooser.showOpenDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                AManufacturing loadedManufacturing = AManufacturing.open(selectedFile);
                if (loadedManufacturing != null) {
                    aManufacturing = loadedManufacturing;
                    photo.repaint();
                }
            }
        });

        // Funcionalidad para "Exportar"
        optionExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));
            int result = fileChooser.showSaveDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".txt")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                }
                aManufacturing.export(selectedFile);
            }
        });

        // Funcionalidad para "Importar"
        optionImport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));
            int result = fileChooser.showOpenDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                AManufacturing loadedManufacturing = AManufacturing.importfile(selectedFile);
                if (loadedManufacturing != null) {
                    aManufacturing = loadedManufacturing;
                    photo.repaint();
                }
            }
        });
        **/

        //THIRD VERSION


        optionSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("DAT files", "dat"));
            int result = fileChooser.showSaveDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".dat")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".dat");
                }
                aManufacturing.save(selectedFile);
            }
        });

        optionOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("DAT files", "dat"));
            int result = fileChooser.showOpenDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                AManufacturing loadedManufacturing = AManufacturing.open(selectedFile);
                if (loadedManufacturing != null) {
                    aManufacturing = loadedManufacturing;
                    photo.repaint();
                }
            }
        });

        // Funcionalidad para "Exportar"
        optionExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));
            int result = fileChooser.showSaveDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".txt")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                }
                aManufacturing.export(selectedFile);
            }
        });

        // Funcionalidad para "Importar"
        optionImport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TXT files", "txt"));
            int result = fileChooser.showOpenDialog(AManufacturingGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                AManufacturing loadedManufacturing = AManufacturing.importfile(selectedFile);
                if (loadedManufacturing != null) {
                    aManufacturing = loadedManufacturing;
                    photo.repaint();
                }
            }
        });
    }

    /**
     * Handles the tic-tac button action by performing a tic-tac cycle in the AManufacturing system and repainting the panel.
     */
    private void ticTacButtonAction() {
        aManufacturing.ticTac();
        photo.repaint();
    }

    /**
     * Returns the instance of AManufacturing associated with this GUI.
     * @return The AManufacturing instance.
     */
    public AManufacturing getaManufacturing() {
        return aManufacturing;
    }
    
    /**
     * The main entry point of the application.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        AManufacturingGUI ca = new AManufacturingGUI();
        ca.setVisible(true);
    }  
}

/**
 * Represents the panel that visualizes the manufacturing lattice.
 * This panel is responsible for drawing the lattice grid and the Things within it.
 */
class PhotoAManufacturing extends JPanel {
    private AManufacturingGUI gui;

    /**
     * Creates a new PhotoAManufacturing panel associated with the given GUI.
     * Sets the background color to white and initializes the panel size.
     * @param gui The AManufacturingGUI instance associated with this panel.
     */
    public PhotoAManufacturing(AManufacturingGUI gui) {
        this.gui = gui;
        setBackground(Color.white);
        setPreferredSize(new Dimension(AManufacturingGUI.SIDE * gui.SIZE + 10, AManufacturingGUI.SIDE * gui.SIZE + 10));         
    }

    /**
     * Paints the components of the panel, including the grid lines and the Things in the lattice.
     * @param g The Graphics object used to draw on the panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        AManufacturing aManufacturing = gui.getaManufacturing();
        super.paintComponent(g);
         
        // Draw grid lines
        for (int c = 0; c <= aManufacturing.getSize(); c++) {
            g.drawLine(c * AManufacturingGUI.SIDE, 0, c * AManufacturingGUI.SIDE, aManufacturing.getSize() * AManufacturingGUI.SIDE);
        }
        for (int f = 0; f <= aManufacturing.getSize(); f++) {
            g.drawLine(0, f * AManufacturingGUI.SIDE, aManufacturing.getSize() * AManufacturingGUI.SIDE, f * AManufacturingGUI.SIDE);
        }       
        // Draw the Things in the lattice
        for (int f = 0; f < aManufacturing.getSize(); f++) {
            for (int c = 0; c < aManufacturing.getSize(); c++) {
                if (aManufacturing.getThing(f, c) != null) {
                    g.setColor(aManufacturing.getThing(f, c).getColor());
                    if (aManufacturing.getThing(f, c).shape() == Thing.SQUARE) {                  
                        if (aManufacturing.getThing(f, c).isActive()) {
                            g.fillRoundRect(AManufacturingGUI.SIDE * c + 1, AManufacturingGUI.SIDE * f + 1, AManufacturingGUI.SIDE - 2, AManufacturingGUI.SIDE - 2, 2, 2);
                        } else {
                            g.drawRoundRect(AManufacturingGUI.SIDE * c + 1, AManufacturingGUI.SIDE * f + 1, AManufacturingGUI.SIDE - 2, AManufacturingGUI.SIDE - 2, 2, 2);    
                        }
                    } else {
                        if (aManufacturing.getThing(f, c).isActive()) {
                            g.fillOval(AManufacturingGUI.SIDE * c + 1, AManufacturingGUI.SIDE * f + 1, AManufacturingGUI.SIDE - 2, AManufacturingGUI.SIDE - 2);
                        } else {
                            g.drawOval(AManufacturingGUI.SIDE * c + 1, AManufacturingGUI.SIDE * f + 1, AManufacturingGUI.SIDE - 2, AManufacturingGUI.SIDE - 2);
                        }
                    }
                }
            }
        }
    }
}