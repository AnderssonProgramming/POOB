import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de prueba para la clase RelationalCalculator.
 * Proporciona pruebas unitarias para verificar la funcionalidad de la calculadora relacional,
 * incluyendo la creación de variables, asignación de relaciones, consulta e impresión.
 * 
 * @author Pedraza-Sánchez
 * @version 2024
 */
public class RelationalCalculatorTest {

    private RelationalCalculator calculator;

    /**
     * Configuración inicial para las pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @Before
    public void setUp() {
        calculator = new RelationalCalculator();
    }

    /**
     * Prueba para verificar la creación de una calculadora relacional vacía.
     * La calculadora no debe tener variables asignadas al inicio.
     */
    @Test
    public void shouldCreateCalculator() {
        assertNotNull(calculator);
        assertEquals(0, calculator.variables().length); // Al crearla, no debe tener variables
    }

    /**
     * Prueba para verificar que se puede asignar una relación válida a una variable.
     * La operación debe ser exitosa y la calculadora debe tener una variable.
     */
    @Test
    public void shouldAssignValidRelation() {
        String[] attributes = {"name", "age", "city"};
        calculator.assign("people", attributes);
        assertTrue(calculator.ok()); // La operación fue exitosa
        assertEquals(1, calculator.variables().length); // Debe haber una variable
    }

    /**
     * Prueba para verificar que no se puede asignar una relación inválida.
     * Se esperan errores al asignar relaciones nulas o con atributos vacíos.
     */
    @Test
    public void shouldNotAssignInvalidRelation() {
        try {
            // Forzar la asignación de una relación inválida (nula)
            calculator.assign("invalid", null);
            fail("Debería fallar la asignación de una relación inválida");
        } catch (Exception e) {
            assertFalse(calculator.ok()); // Operación fallida
        }

        // Intentar asignar una relación con atributos vacíos
        try {
            calculator.assign("empty", new String[0]);
            fail("Debería fallar la asignación de una relación con atributos vacíos");
        } catch (Exception e) {
            assertFalse(calculator.ok()); // Operación fallida
        }

        assertEquals(0, calculator.variables().length); // No debe haber variables asignadas
    }

    /**
     * Prueba para verificar que se puede consultar una variable asignada.
     * La variable debe coincidir con la esperada.
     */
    @Test
    public void shouldReturnAssignedVariables() {
        String[] attributes = {"name", "age", "city"};
        calculator.assign("people", attributes);
        String[] variables = calculator.variables();
        assertEquals(1, variables.length);
        assertEquals("people", variables[0]); // La variable debe coincidir
    }

    /**
     * Prueba para verificar que no se retornan variables cuando no hay ninguna asignada.
     */
    @Test
    public void shouldReturnEmptyVariablesWhenNoneAssigned() {
        String[] variables = calculator.variables();
        assertEquals(0, variables.length); // No debe haber variables asignadas al inicio
    }

    /**
     * Prueba para verificar que se puede imprimir una variable asignada.
     * El resultado debe incluir los atributos de la relación.
     */
    @Test
    public void shouldPrintAssignedVariable() {
        String[] attributes = {"name", "age", "city"};
        calculator.assign("people", attributes);
        String result = calculator.toString("people");
        assertTrue(result.contains("name, age, city")); // La representación debe incluir los atributos
    }

    /**
     * Prueba para verificar que se genera un error al intentar imprimir una variable no asignada.
     */
    @Test
    public void shouldNotPrintUnassignedVariable() {
        try {
            String result = calculator.toString("unknown");
            fail("Debería fallar cuando se intenta imprimir una variable no asignada");
        } catch (Exception e) {
            assertEquals("Relation not found", e.getMessage()); // Mensaje de error esperado
        }
    }
}
