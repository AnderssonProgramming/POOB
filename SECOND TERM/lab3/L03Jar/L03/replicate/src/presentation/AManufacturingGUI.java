package presentation;
import domain.*;

import javax.swing.*;
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
    private JPanel controlPanel;
    private PhotoAManufacturing photo;
    private AManufacturing aManufacturing;
   
    /**
     * Creates a new AManufacturingGUI window and initializes its components.
     */
    private AManufacturingGUI() {
        aManufacturing = new AManufacturing();
        SIZE = aManufacturing.getSize();
        prepareElements();
        prepareActions();
    }
    
    /**
     * Prepares and sets up the GUI elements, including buttons and the main panel.
     */
    private void prepareElements() {
        setTitle("aManufacturing celular");
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

    /**
     * Prepares the actions for the GUI elements, including handling the tic-tac button click.
     */
    private void prepareActions() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);       
        ticTacButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ticTacButtonAction();
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
        setPreferredSize(new Dimension(gui.SIDE * gui.SIZE + 10, gui.SIDE * gui.SIZE + 10));         
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
            g.drawLine(c * gui.SIDE, 0, c * gui.SIDE, aManufacturing.getSize() * gui.SIDE);
        }
        for (int f = 0; f <= aManufacturing.getSize(); f++) {
            g.drawLine(0, f * gui.SIDE, aManufacturing.getSize() * gui.SIDE, f * gui.SIDE);
        }       
        // Draw the Things in the lattice
        for (int f = 0; f < aManufacturing.getSize(); f++) {
            for (int c = 0; c < aManufacturing.getSize(); c++) {
                if (aManufacturing.getThing(f, c) != null) {
                    g.setColor(aManufacturing.getThing(f, c).getColor());
                    if (aManufacturing.getThing(f, c).shape() == Thing.SQUARE) {                  
                        if (aManufacturing.getThing(f, c).isActive()) {
                            g.fillRoundRect(gui.SIDE * c + 1, gui.SIDE * f + 1, gui.SIDE - 2, gui.SIDE - 2, 2, 2);
                        } else {
                            g.drawRoundRect(gui.SIDE * c + 1, gui.SIDE * f + 1, gui.SIDE - 2, gui.SIDE - 2, 2, 2);    
                        }
                    } else {
                        if (aManufacturing.getThing(f, c).isActive()) {
                            g.fillOval(gui.SIDE * c + 1, gui.SIDE * f + 1, gui.SIDE - 2, gui.SIDE - 2);
                        } else {
                            g.drawOval(gui.SIDE * c + 1, gui.SIDE * f + 1, gui.SIDE - 2, gui.SIDE - 2);
                        }
                    }
                }
            }
        }
    }
}