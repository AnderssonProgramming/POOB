package domain;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Log class provides a method to record exceptions to a log file.
 * It uses a FileWriter and PrintWriter to write error messages and stack traces to "errors.log".
 * If an IOException occurs while writing to the log file, it logs the failure using a Logger.
 */
public class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    // Método para registrar el error
    public static void record(Exception ex) {
        try (FileWriter fileWriter = new FileWriter("errors.log", true); // 'true' para agregar al final del archivo
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            
            // Escribir la información del error en el archivo
            printWriter.println("ERROR: " + ex.getMessage());
            printWriter.println("Stack Trace:");
            for (StackTraceElement element : ex.getStackTrace()) {
                printWriter.println("\t" + element);
            }
            printWriter.println("-----------------------------------");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write to log file.", e);
        }
    }
}

