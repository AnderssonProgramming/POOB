package domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;


/**
 * Class representing a manufacturing lattice where different types of Things
 * can be placed.
 * The lattice consists of a grid where various cells and other entities can be
 * initialized and manipulated.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */

public class AManufacturing implements Serializable {
    private static final long serialVersionUID = 1L; // Asegura la compatibilidad de la clase serializada
    private static int SIZE = 50;
    private Thing[][] lattice;

    /**
     * Constructor for AManufacturing class.
     * Initializes the lattice with null values and sets up the initial pattern of
     * Things.
     */
    public AManufacturing() {
        lattice = new Thing[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                lattice[r][c] = null;
            }
        }
        //someThings();
        //someThings1();
        initializePattern();
    }

    /**
     * Returns the size of the lattice.
     * 
     * @return the size of the lattice.
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Retrieves a Thing from a specific position in the lattice.
     * 
     * @param r the row index.
     * @param c the column index.
     * @return the Thing at the specified position, or null if none exists.
     */
    public Thing getThing(int r, int c) {
        return lattice[r][c];
    }

    /**
     * Places a Thing in a specific position in the lattice.
     * 
     * @param r the row index.
     * @param c the column index.
     * @param e the Thing to be placed at the specified position.
     */
    public void setThing(int r, int c, Thing e) {
        lattice[r][c] = e;
    }

    /**
     * Initializes some specific Things in the lattice.
     * This includes cells, tourist cells, poison, and reflective cells.
     */
    public void someThings() {
        // CELLS
        Cell yersinia = new Cell(this, 10, 10, true);
        Cell listeria = new Cell(this, 15, 15, true);
        setThing(10, 10, yersinia);
        setThing(15, 15, listeria);

        // TOURIST CELLS
        TouristCell move = new TouristCell(this, 5, 5, true);
        TouristCell walk = new TouristCell(this, 20, 20, true);
        setThing(5, 5, move);
        setThing(20, 20, walk);

        // POISON
        Poison mercury = new Poison();
        Poison arsenic = new Poison();
        setThing(0, 0, mercury);
        setThing(0, getSize() - 1, arsenic);

        // Reflective Cells
        ReflectiveCell alice = new ReflectiveCell(this, 25, 25, true);
        ReflectiveCell bob = new ReflectiveCell(this, 30, 30, true);
        setThing(25, 25, alice);
        setThing(30, 30, bob);

        // Reflective Cells - unit test compilation in program
        ReflectiveCell Pedraza = new ReflectiveCell(this, 5, 10, true);
        ReflectiveCell Sanchez = new ReflectiveCell(this, 5, 6, true);
        setThing(5, 10, Pedraza);
        setThing(5, 6, Sanchez);

        // STICKY WALL
        // Define rows for each Wall
        int pedrazaRow = 4; // Example row for the Pedraza Wall
        int sanchezRow = 19; // Example row for the Sanchez Wall

        // Create the Walls with your names
        StickyWall pedrazaWall = new StickyWall(this, pedrazaRow);
        StickyWall sanchezWall = new StickyWall(this, sanchezRow);
    }


    /**
     * Initializes some specific Things in the lattice.
     * This includes cells, tourist cells, poison, and reflective cells.
     */
    public void someThings1() {
        // CELLS
        Cell yersinia = new Cell(this, 10, 10, true);
        setThing(10, 10, yersinia);

        // TOURIST CELLS
        TouristCell move = new TouristCell(this, 33, 17, true);
        TouristCell walk = new TouristCell(this, 44, 20, true);
        setThing(33, 17, move);
        setThing(44, 20, walk);

        // POISON
        Poison mercury = new Poison();
        Poison arsenic = new Poison();
        setThing(20, 20, mercury);
        setThing(49, 0, arsenic);
        

        // Reflective Cells
        ReflectiveCell alice = new ReflectiveCell(this, 49, 49, true);
        ReflectiveCell bob = new ReflectiveCell(this, 34, 30, true);
        setThing(49, 49, alice);
        setThing(34, 30, bob);


        // STICKY WALL
        // Define rows for each Wall
        //int pedrazaRow = 4; // Example row for the Pedraza Wall
        //int sanchezRow = 19; // Example row for the Sanchez Wall

        // Create the Walls with your names
        //StickyWall pedrazaWall = new StickyWall(this, pedrazaRow);
        //StickyWall sanchezWall = new StickyWall(this, sanchezRow);
    }
    /**
     * Initializes the lattice pattern with inactive original cells.
     * Sets up a 10x10 sub-grid in the bottom-left corner with active cells.
     */
    public void initializePattern() {
        // Initialize the entire lattice with inactive original cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                OriginalCell cell = new OriginalCell(this, r, c, false);
                setThing(r, c, cell);
            }
        }

        // Place active cells in the 10x10 sub-grid in the bottom-left corner
        ((OriginalCell) getThing(44, 5)).changeState(true); // Activate cell at (44,5)
        ((OriginalCell) getThing(45, 4)).changeState(true); // Activate cell at (45,4)
        ((OriginalCell) getThing(45, 5)).changeState(true); // Activate cell at (45,5)
    }

    /**
     * Counts the number of active neighboring Things around a specified position.
     * 
     * @param r the row index of the target position.
     * @param c the column index of the target position.
     * @return the number of active neighboring Things.
     */
    public int neighborsActive(int r, int c) {
        int num = 0;
        for (int dr = -1; dr < 2; dr++) {
            for (int dc = -1; dc < 2; dc++) {
                if ((dr != 0 || dc != 0) && inLatice(r + dr, c + dc) &&
                        (lattice[r + dr][c + dc] != null) && (lattice[r + dr][c + dc].isActive()))
                    num++;
            }
        }
        return (inLatice(r, c) ? num : 0);
    }

    /**
     * Checks if a specific position in the lattice is empty.
     * 
     * @param r the row index.
     * @param c the column index.
     * @return {@code true} if the position is empty, {@code false} otherwise.
     */
    public boolean isEmpty(int r, int c) {
        return (inLatice(r, c) && lattice[r][c] == null);
    }

    /**
     * Checks if the specified position is within the bounds of the lattice.
     * 
     * @param r the row index.
     * @param c the column index.
     * @return {@code true} if the position is within bounds, {@code false}
     *         otherwise.
     */
    protected boolean inLatice(int r, int c) {
        return ((0 <= r) && (r < SIZE) && (0 <= c) && (c < SIZE));
    }

    /**
     * Performs a tic-tac cycle on all Things in the lattice.
     * In each cycle, the Things decide their next state and then change their state
     * accordingly.
     */
    public void ticTac() {
        // Determine the next state of cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Thing thing = lattice[r][c];
                if (thing != null) {
                    thing.decide();
                }
            }
        }
        // Update the state of cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Thing thing = lattice[r][c];
                if (thing != null) {
                    thing.change();
                }
            }
        }
    }


    //FIRST VERSION

    /**
     * Opens the specified file.
     *
     * @param file the name or path of the file to be opened.
     * @throws ReplicateException if the method is called, indicating that the
     *                            "open" option is under construction.
     */
    public static AManufacturing open00(File file) throws ReplicateException {
        throw new ReplicateException(ReplicateException.OPTION_OPEN + "" + file.getName());
    }

    /**
     * Saves the specified file.
     *
     * @param file the name or path of the file to be saved.
     * @throws ReplicateException if the method is called, indicating that the
     *                            "save" option is under construction.
     */
    public void save00(File file) throws ReplicateException {
        throw new ReplicateException(ReplicateException.OPTION_SAVE + "" + file.getName());
    }


    /**
     * Imports a file.
     *
     * @param file the name or path of the file to be imported.
     * @throws ReplicateException if the method is called, indicating that the
     *                            "import" option is under construction.
     */
    public static AManufacturing import00(File file) throws ReplicateException {
        throw new ReplicateException(ReplicateException.OPTION_IMPORT + "" + file.getName());
    }

    /**
     * Exports data to the specified file.
     *
     * @param file the name or path of the file to which data should be exported.
     * @throws ReplicateException if the method is called, indicating that the
     *                            "export" option is under construction.
     */
    public void export00(File file) throws ReplicateException {
        throw new ReplicateException(ReplicateException.OPTION_EXPORT + "" + file.getName());
    }





    //SECOND VERSION

    /**
     * Opens a file and returns an instance of AManufacturing loaded from the file.
     * 
     * @param file the file to read the AManufacturing state from.
     * @return AManufacturing instance loaded from the file.
     * @throws IOException if an I/O error occurs during opening.
     * @throws ClassNotFoundException if the class definition cannot be found.
     */
    public static AManufacturing open01(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (AManufacturing) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
    * Saves the current state of AManufacturing to a file.
    * 
    * @param file the file where the state should be saved.
    * @throws IOException if an I/O error occurs during saving.
    */
    public void save01(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this);
        }catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Imports data from a text file to recreate the lattice state.
     * Each line in the file should represent an element, specifying the type and position.
     * 
     * @param file The text file to import the state from.
     * @return A new instance of AManufacturing with the imported state.
     */
    public static AManufacturing import01(File file) {
        AManufacturing manufacturing = new AManufacturing(); // Crear una nueva instancia vacía

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Elimina espacios en blanco alrededor de la línea
                if (!line.isEmpty()) {
                    String[] parts = line.split(" "); // Divide la línea en partes
                    if (parts.length >= 3) {
                        String type = parts[0];
                        int r = Integer.parseInt(parts[1]);
                        int c = Integer.parseInt(parts[2]);

                        Thing thing = null;
                        switch (type) {
                            case "Cell":
                                thing = new Cell(manufacturing, r, c, true);
                                break;
                            case "TouristCell":
                                thing = new TouristCell(manufacturing, r, c, true);
                                break;
                            case "Poison":
                                thing = new Poison();
                                break;
                            case "ReflectiveCell":
                                thing = new ReflectiveCell(manufacturing, r, c, true);
                                break;
                            default:
                                throw new IOException("Unknown element type in import file: " + type);
                        }

                        if (thing != null) {
                            manufacturing.setThing(r, c, thing);
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al importar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return manufacturing;
    }

    /**
     * Exports the current state of AManufacturing to a text file.
     *
     * @param file The file to which the state should be exported.
     * @throws IOException Displays an error message dialog if an I/O error occurs during exporting.
     */
    public void export01(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Iterate through each row and column of the lattice
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    Thing thing = lattice[r][c];
                    // If there is an element (not null), export its type and position
                    if (thing != null) {
                        String type = thing.getClass().getSimpleName(); // Get the class type of the element
                        writer.write(type + " " + r + " " + c); // Write the type, row, and column to the file
                        writer.newLine(); // Add a newline character after each element
                    }
                }
            }
        } catch (IOException e) {
            // Display an error message if something goes wrong during the export
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    //THIRD VERSION

    /**
     * Opens a file and returns an instance of AManufacturing loaded from the file.
     * 
     * @param file the file to read the AManufacturing state from.
     * @return AManufacturing instance loaded from the file.
     * @throws IOException if an I/O error occurs during opening.
     * @throws ClassNotFoundException if the class definition cannot be found.
     */
    public static AManufacturing open(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (AManufacturing) in.readObject();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo especificado. Verifique la ruta.", "Error al abrir", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo. El archivo podría estar dañado o no es válido.", "Error al abrir", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar la clase correspondiente al objeto en el archivo.", "Error de clase", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Retorna null en caso de error para que la aplicación maneje la lógica adecuadamente
    }

    /**
    * Saves the current state of AManufacturing to a file.
    * 
    * @param file the file where the state should be saved.
    * @throws IOException if an I/O error occurs during saving.
    */
    public void save(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this);
        }catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo especificado. Verifique la ruta.", "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Imports data from a text file to recreate the lattice state.
     * Each line in the file should represent an element, specifying the type and position.
     * 
     * @param file The text file to import the state from.
     * @return A new instance of AManufacturing with the imported state.
     */
    public static AManufacturing import02(File file) {
        AManufacturing manufacturing = new AManufacturing(); // Crear una nueva instancia vacía

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Elimina espacios en blanco alrededor de la línea
                if (!line.isEmpty()) {
                    String[] parts = line.split(" "); // Divide la línea en partes
                    if (parts.length != 3) {
                        throw new IOException("Formato incorrecto en el archivo de importación: " + line);
                    }    
                        String type = parts[0];
                        int r = Integer.parseInt(parts[1]);
                        int c = Integer.parseInt(parts[2]);

                        Thing thing = null;
                        switch (type) {
                            case "Cell":
                                thing = new Cell(manufacturing, r, c, true);
                                break;
                            case "TouristCell":
                                thing = new TouristCell(manufacturing, r, c, true);
                                break;
                            case "Poison":
                                thing = new Poison();
                                break;
                            case "ReflectiveCell":
                                thing = new ReflectiveCell(manufacturing, r, c, true);
                                break;
                            default:
                                throw new IOException("Unknown element type in import file: " + type);
                        }

                        if (thing != null) {
                            manufacturing.setThing(r, c, thing);
                        }
                    }
                }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo especificado. Verifique la ruta.", "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en el formato de los números en el archivo de importación. Asegúrese de que las coordenadas sean válidas.", "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de importación: " + e.getMessage(), "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        } catch (ArrayIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(null, "Error al intentar agregar células en donde hay posiciones inválidas.", "Error de límite", JOptionPane.ERROR_MESSAGE);
        }

        return manufacturing;
    }

    /**
     * Exports the current state of AManufacturing to a text file.
     *
     * @param file The file to which the state should be exported.
     * @throws IOException Displays an error message dialog if an I/O error occurs during exporting.
     */
    public void export02(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Iterate through each row and column of the lattice
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    Thing thing = lattice[r][c];
                    // If there is an element (not null), export its type and position
                    if (thing != null) {
                        String type = thing.getClass().getSimpleName(); // Get the class type of the element
                        writer.write(type + " " + r + " " + c); // Write the type, row, and column to the file
                        writer.newLine(); // Add a newline character after each element
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo especificado. Verifique la ruta.", "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo. Asegúrese de tener permisos de escritura.", "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }
    }






    //FOURTH VERSION---ONLY import and export


    /**
     * Imports data from a text file to recreate the lattice state.
     * Each line in the file should represent an element, specifying the type and position.
     * 
     * @param file The text file to import the state from.
     * @return A new instance of AManufacturing with the imported state.
     */
    public static AManufacturing import03(File file) {
        AManufacturing manufacturing = new AManufacturing(); // Crear una nueva instancia vacía
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0; // Contador para la línea actual
    
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim(); // Eliminar espacios en blanco
    
                if (!line.isEmpty()) {
                    String[] parts = line.split(" "); // Dividir la línea
    
                    // Verificar que haya exactamente 3 partes (tipo, fila, columna)
                    if (parts.length != 3) {
                        JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": Formato incorrecto. Se esperaban tres partes, pero se encontraron " + parts.length + ".", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
    
                    String type = parts[0];
                    String rowString = parts[1];
                    String colString = parts[2];
    
                    try {
                        int r = Integer.parseInt(rowString);
                        int c = Integer.parseInt(colString);
    
                        Thing thing = null;
    
                        switch (type) {
                            case "Cell":
                                thing = new Cell(manufacturing, r, c, false);
                                break;
                            case "TouristCell":
                                thing = new TouristCell(manufacturing, r, c, false);
                                break;
                            case "Poison":
                                thing = new Poison();
                                break;
                            case "ReflectiveCell":
                                thing = new ReflectiveCell(manufacturing, r, c, false);
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": Tipo de elemento desconocido '" + type + "'.", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                                continue;
                        }
    
                        if (thing != null) {
                            manufacturing.setThing(r, c, thing);
                        }
    
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": Las coordenadas '" + rowString + "' o '" + colString + "' no son números válidos.", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo especificado. Verifique la ruta.", "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de importación: " + e.getMessage(), "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }
    
        return manufacturing;
    }

    /**
     * Exports the current state of AManufacturing to a text file.
     *
     * @param file The file to which the state should be exported.
     * @throws IOException Displays an error message dialog if an I/O error occurs during exporting.
     */
    public void export03(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Iterate through each row and column of the lattice
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    Thing thing = lattice[r][c];
                    // If there is an element (not null), export its type and position
                    if (thing != null) {
                        String type = thing.getClass().getSimpleName(); // Get the class type of the element
                        writer.write(type + " " + r + " " + c); // Write the type, row, and column to the file
                        writer.newLine(); // Add a newline character after each element
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo especificado. Verifique la ruta.", "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo. Asegúrese de tener permisos de escritura.", "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }
    }








    //FIFTH VERSION

    /**
     * Imports data from a text file to recreate the lattice state.
     * Each line in the file should represent an element, specifying the type and position.
     * 
     * @param file The text file to import the state from.
     * @return A new instance of AManufacturing with the imported state.
     */
    public static AManufacturing importfile(File file) {
        AManufacturing manufacturing = new AManufacturing(); // Crear una nueva instancia vacía

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0; // Contador para la línea actual

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim(); // Eliminar espacios en blanco

                if (!line.isEmpty()) {
                    String[] parts = line.split(" "); // Dividir la línea

                    // Verificar que haya exactamente 3 partes (tipo, fila, columna)
                    if (parts.length != 3) {
                        JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": Formato incorrecto. Se esperaban tres partes, pero se encontraron " + parts.length + ".", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    String type = parts[0];
                    String rowString = parts[1];
                    String colString = parts[2];

                    try {
                        int r = Integer.parseInt(rowString);
                        int c = Integer.parseInt(colString);

                        Thing thing = null;

                        try {
                            // Usar reflexión para crear una instancia del tipo especificado
                            Class<?> clazz = Class.forName("domain." + type);
                            Constructor<?> constructor = clazz.getConstructor(AManufacturing.class, int.class, int.class, boolean.class);
                            thing = (Thing) constructor.newInstance(manufacturing, r, c, false);

                        } catch (ClassNotFoundException e) {
                            JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": Tipo de elemento desconocido '" + type + "'.", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                            continue;
                        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": No se pudo crear el objeto del tipo '" + type + "' debido a un error en el constructor.", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        if (thing != null) {
                            manufacturing.setThing(r, c, thing);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error en la línea " + lineNumber + ": Las coordenadas '" + rowString + "' o '" + colString + "' no son números válidos.", "Error de Importación", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo especificado. Verifique la ruta.", "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de importación: " + e.getMessage(), "Error al importar", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }

        return manufacturing;
    }

    /**
     * Exports the current state of AManufacturing to a text file.
     *
     * @param file The file to which the state should be exported.
     * @throws IOException Displays an error message dialog if an I/O error occurs during exporting.
     */
    public void export(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Iterate through each row and column of the lattice
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    Thing thing = lattice[r][c];
                    // If there is an element (not null), export its type and position
                    if (thing != null) {
                        String type = thing.getClass().getSimpleName(); // Get the class type of the element
                        writer.write(type + " " + r + " " + c); // Write the type, row, and column to the file
                        writer.newLine(); // Add a newline character after each element
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo especificado. Verifique la ruta.", "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo. Asegúrese de tener permisos de escritura.", "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Permiso denegado para acceder al archivo especificado.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
        }
    }



}



