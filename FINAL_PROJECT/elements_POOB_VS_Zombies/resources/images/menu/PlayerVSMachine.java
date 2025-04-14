import javax.swing.*;
import java.awt.*;

public class PlayerVSMachine extends JFrame {
    private JButton playerNameButton, timeButton, numberButton;
    private JButton startButton;
    private JButton[] plantButtons;

    public POOBvsZombiesPlayervsMachine() {
        // Configurar el JFrame
        setTitle("Player vs Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel con fondo personalizado
        JPanel panel = new JPanel() {
            Image backgroundImage = new ImageIcon("C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\menu\\PlayervsMachineMenu.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);



        // Crear un JLabel para el texto con saltos de línea
        JLabel infoLabel = new JLabel("<html>"
        + "Machine will adopt its original<br>"
        + "behavior from PvsZ original game.<br>"
        + "You can configure your name, hordes <br>"
        + "duration and number. You can also <br>"
        + "select your plants to play. Have fun!"
        + "</html>");

        // Configurar posición y estilo del JLabel
        infoLabel.setBounds(485, 215, 500, 100); // Ajustar posición y tamaño
        infoLabel.setForeground(Color.WHITE); // Texto blanco
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));


        //Crear Select Plant text
        JLabel selectPlant = new JLabel("<html>SELECT PLANTS</html>");

        selectPlant.setBounds(385,525,500,100);
        selectPlant.setForeground(Color.WHITE);
        selectPlant.setFont(new Font("Arial",Font.PLAIN,9));


        // PlayerName
        JLabel playerNameLabel = new JLabel("PlayerName:");
        playerNameLabel.setBounds(260, 205, 150, 30);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        playerNameButton = createPlayerNameButton(225, 235);

        // Time
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(245, 345, 150, 30);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        timeButton = createTimeNumberButton(230, 370);

        // Number
        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(320, 345, 150, 30);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 10));
        numberButton = createTimeNumberButton(305, 370);

        // Botón START
        startButton = new JButton("¡START!");
        startButton.setBounds(485, 355, 160, 27);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.BLACK);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setEnabled(false);
        startButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Game Starting!\nPlayer: " + playerNameButton.getText() +
                    "\nTime: " + timeButton.getText() + "\nNumber: " + numberButton.getText());
        });

        // FlowLayout para las plantas
        JPanel plantSelectionPanel = new JPanel();
        plantSelectionPanel.setBounds(115, 590, 600, 150);
        plantSelectionPanel.setLayout(new FlowLayout());
        plantSelectionPanel.setOpaque(false);

        // Ruta específica para cada botón de planta
        String[] plantImages = {
            "C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\plants\\Sunflower\\Sunflower.jpg",
            "C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\plants\\Peashooter\\Peashooter.jpg",
            "C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\plants\\WallNut\\Wall-nut.jpg",
            "C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\plants\\PotatoMine\\Potato_Mine.jpg",
            "C:\\Users\\Usuario\\OneDrive - ESCUELA COLOMBIANA DE INGENIERIA JULIO GARAVITO\\Desktop\\POOB\\FINAL_PROJECT\\POOBvsZombies\\resources\\images\\plants\\ECIPlant\\ECIPlant.png"
        };

        // Inicializar el array de botones
        plantButtons = new JButton[5];

        // Asignar imágenes específicas a cada botón
        for (int i = 0; i < 5; i++) {
            // Declarar una variable final para capturar el valor de i
            final int index = i;

            // Crear un JPanel personalizado para cada planta
            JPanel plantPanel = new JPanel() {
                private Image plantImage;
                private Color overlayColor = new Color(255, 0, 0, 128); // Rojo translúcido inicial

                {
                    // Cargar la imagen desde la ruta especificada
                    ImageIcon icon = new ImageIcon(plantImages[index]);
                    plantImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Escalar imagen

                    // Añadir un MouseListener para alternar el color al hacer clic
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            if (overlayColor.equals(new Color(255, 0, 0, 128))) {
                                overlayColor = new Color(0, 255, 0, 128); // Verde translúcido
                            } else {
                                overlayColor = new Color(255, 0, 0, 128); // Rojo translúcido
                            }
                            repaint(); // Repintar el panel para reflejar el cambio
                        }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Dibujar la imagen en el fondo del panel
                    if (plantImage != null) {
                        g.drawImage(plantImage, 0, 0, getWidth(), getHeight(), this);
                    }
                    // Dibujar el rectángulo translúcido superpuesto
                    g.setColor(overlayColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            plantPanel.setPreferredSize(new Dimension(50, 50)); // Tamaño de cada panel
            plantPanel.setOpaque(false); // Transparencia para mostrar la imagen
            plantSelectionPanel.add(plantPanel); // Agregar el panel al contenedor de selección
        }





        // Agregar componentes al panel
        panel.add(infoLabel);
        panel.add(selectPlant);
        panel.add(playerNameLabel);
        panel.add(playerNameButton);
        panel.add(timeLabel);
        panel.add(timeButton);
        panel.add(numberLabel);
        panel.add(numberButton);
        panel.add(startButton);
        panel.add(plantSelectionPanel);

        add(panel); // Agregar el panel al JFrame
    }

    // Método para crear un botón de entrada
    private JButton createPlayerNameButton(int x, int y) {
        JButton button = new JButton();
        button.setBounds(x, y, 150, 30);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(229, 206, 172));
        button.setForeground(Color.BLACK);
        button.setEnabled(true); // Inicialmente habilitado
        button.addActionListener(e -> handleInput(button));
        return button;
    }

    // Método para crear un botón de entrada
    private JButton createTimeNumberButton(int x, int y) {
        JButton button = new JButton();
        button.setBounds(x, y, 60, 14);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(239, 162, 198));
        button.setForeground(Color.BLACK);
        button.setEnabled(true); // Inicialmente habilitado
        button.addActionListener(e -> handleInput(button));
        return button;
    }

    // Manejar entrada en botones
    private void handleInput(JButton button) {
        String label = button == playerNameButton ? "Player Name" :
                button == timeButton ? "Time" : "Number";

        String input = JOptionPane.showInputDialog(this, "Enter " + label + ":");
        if (input != null && !input.trim().isEmpty()) {
            button.setText(input);
            button.setEnabled(false); // Deshabilitar el botón después de la entrada
            checkFields(); // Validar si se pueden habilitar las acciones
        }
    }

    // Método para habilitar el botón START si todos los campos tienen valores
    private void checkFields() {
        boolean allFieldsFilled = !playerNameButton.getText().isEmpty() &&
                !timeButton.getText().isEmpty() &&
                !numberButton.getText().isEmpty();
        startButton.setEnabled(allFieldsFilled);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerVSMachine frame = new PlayerVSMachine();
            frame.setVisible(true);
        });
    }
}
