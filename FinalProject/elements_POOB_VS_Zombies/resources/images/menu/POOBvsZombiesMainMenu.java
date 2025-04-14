import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class POOBvsZombiesMainMenu {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("POOBvsZombies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Custom panel with background image
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\menu\\modalityMenu.png").getImage();
 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Absolute positioning

        // Create buttons with text on them
        JButton playerVsPlayerButton = createMenuButton("Player vs Player", "SURVIVAL", 242);
        JButton playerVsMachineButton = createMenuButton("Player vs Machine", "ORIGINAL", 362);
        JButton machineVsMachineButton = createMenuButton("Machine vs Machine", "SURVIVAL", 472);

        // Add buttons to panel
        panel.add(playerVsPlayerButton);
        panel.add(playerVsMachineButton);
        panel.add(machineVsMachineButton);

        
        // Add action listeners for each button
        playerVsPlayerButton.addActionListener(e -> {
            // Open Player vs Player Menu
            PlayerVSPlayer playerVsPlayerMenu = new PlayerVSPlayer();
            playerVsPlayerMenu.setVisible(true);
            frame.dispose(); 
            // Aquí podrías abrir el menú correspondiente
        });


        playerVsMachineButton.addActionListener(e -> {
            // Open Player vs Machine Menu
            PlayerVSMachine playerVsMachineMenu = new PlayerVSMachine();
            playerVsMachineMenu.setVisible(true);
            frame.dispose(); // Close the main menu
        });

        machineVsMachineButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Opening Machine vs Machine Menu");
            // Aquí podrías abrir el menú correspondiente
        });

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }

    // Helper method to create buttons with consistent styling
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

    
}
