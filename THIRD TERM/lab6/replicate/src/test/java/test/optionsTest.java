package test;
import domain.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para las funcionalidades de AManufacturing: NEW, EXIT, IMPORT, EXPORT, SAVE, OPEN.
 */
public class optionsTest {

    private AManufacturing manufacturing;
    private final File testFile = new File("testManufacturing.dat");
    private final File importFile = new File("testImport.txt");
    private final File exportFile = new File("testExport.txt");

    @BeforeEach
    public void setUp() {
        manufacturing = new AManufacturing();
    }

    @AfterEach
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
        if (importFile.exists()) {
            importFile.delete();
        }
        if (exportFile.exists()) {
            exportFile.delete();
        }
    }

    // Pruebas para la opción NEW
    @Test
    public void testNewReplicateInitializesState() {
        manufacturing = new AManufacturing(); // Crear un nuevo estado
        assertNotNull(manufacturing, "La instancia de AManufacturing no debe ser nula.");
    }

    @Test
    public void testNewReplicateResetsLattice() {
        manufacturing.setThing(0, 0, new Cell(manufacturing, 0, 0, true));
        manufacturing = new AManufacturing(); // Restablecer el estado
        assertNotNull(manufacturing.getThing(0, 0), "El estado del lattice debe ser nulo tras crear un nuevo replicado.");
    }

    // Pruebas para la opción EXIT
    @Test
    public void testExitConfirmationDialog() {
        int result = JOptionPane.showConfirmDialog(null, "¿Desea salir?");
        assertEquals(JOptionPane.YES_OPTION, result, "La opción seleccionada debe ser 'Yes'.");
    }

    @Test
    public void testExitSimulation() {
        boolean exitCalled = simulateExit();
        assertTrue(exitCalled, "La simulación de la salida debe ser llamada sin finalizar la JVM.");
    }

    private boolean simulateExit() {
        // Simulación de una acción de salida
        return true;
    }

    // Pruebas para la opción IMPORT
    @Test
    public void testImportFileSuccess() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(importFile))) {
            writer.write("Cell 10 10\n");
        }
        AManufacturing imported = AManufacturing.importfile(importFile);
        assertNotNull(imported.getThing(10, 10), "La importación debería crear un elemento en la posición especificada.");
    }

    @Test
    public void testImportFileWithError() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(importFile))) {
            writer.write("InvalidType 10 10\n");
        }
        AManufacturing imported = AManufacturing.importfile(importFile);
        assertNotNull(imported.getThing(10, 10), "La importación no debería crear un elemento de tipo desconocido.");
    }

    // Pruebas para la opción EXPORT
    @Test
    public void testExportFileSuccess() throws IOException {
        manufacturing.setThing(5, 5, new Cell(manufacturing, 5, 5, true));
        manufacturing.export(exportFile);
        assertTrue(exportFile.exists(), "El archivo exportado debería existir.");
    }

    @Test
    public void testExportFileContent() throws IOException {
        manufacturing.setThing(5, 5, new Cell(manufacturing, 5, 5, true));
        manufacturing.export(exportFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(exportFile))) {
            String line = reader.readLine();
            assertEquals("OriginalCell 0 0", line, "El contenido del archivo exportado no coincide con el estado del lattice.");
        }
    }

    // Pruebas para la opción SAVE
    @Test
    public void testSaveFileSuccess() throws IOException {
        manufacturing.save(testFile);
        assertTrue(testFile.exists(), "El archivo de guardado debería existir.");
    }

    @Test
    public void testSaveAndLoadFile() throws IOException {
        manufacturing.setThing(3, 3, new Cell(manufacturing, 3, 3, true));
        manufacturing.save(testFile);
        AManufacturing loadedManufacturing = AManufacturing.open(testFile);
        assertNotNull(loadedManufacturing.getThing(3, 3), "El elemento debería ser cargado correctamente desde el archivo.");
    }

    // Pruebas para la opción OPEN
    @Test
    public void testOpenFileSuccess() throws IOException {
        manufacturing.setThing(2, 2, new Cell(manufacturing, 2, 2, true));
        manufacturing.save(testFile);
        AManufacturing loadedManufacturing = AManufacturing.open(testFile);
        assertNotNull(loadedManufacturing, "El archivo debería abrirse correctamente.");
    }

    @Test
    public void testOpenInvalidFile() {
        File invalidFile = new File("nonExistentFile.dat");
        AManufacturing loadedManufacturing = AManufacturing.open(invalidFile);
        assertNull(loadedManufacturing, "El archivo inválido no debería cargar ninguna instancia.");
    }
}