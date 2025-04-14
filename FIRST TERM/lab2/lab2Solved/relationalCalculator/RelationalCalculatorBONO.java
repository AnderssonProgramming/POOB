import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de prueba para las funcionalidades adicionales (BONO) de la clase RelationalCalculator.
 * Proporciona pruebas unitarias para verificar las operaciones de unión e intersección
 * de relaciones con atributos iguales y diferentes.
 * 
 * Las pruebas incluyen casos con atributos coincidentes, atributos diferentes, y la
 * inserción de tuplas en varias relaciones.
 * 
 * @author Pedraza-Sánchez
 * @version 2024
 */
public class RelationalCalculatorBONO {
    private RelationalCalculator calc;

    /**
     * Configuración inicial para las pruebas.
     * Se ejecuta antes de cada método de prueba.
     * Inicializa la calculadora y asigna dos relaciones con atributos coincidentes.
     */
    @Before
    public void setUp() throws Exception {
        calc = new RelationalCalculator();

        // Crear dos relaciones con los mismos atributos para pruebas
        calc.assign("rel1", new String[] {"nombre", "edad"});
        calc.assign("rel2", new String[] {"nombre", "edad"});

        // Insertar tuplas en rel1
        calc.update("rel1", 'i', new String[] {"Alice", "25"});
        calc.update("rel1", 'i', new String[] {"Bob", "30"});

        // Insertar tuplas en rel2
        calc.update("rel2", 'i', new String[] {"Bob", "30"});
        calc.update("rel2", 'i', new String[] {"Charlie", "35"});
    }

    /**
     * Prueba para verificar la operación de unión entre dos relaciones.
     * Verifica que las tuplas de ambas relaciones se combinen correctamente y sin duplicados.
     */
    @Test
    public void testUnion() {
        // Realizar la unión de rel1 y rel2
        calc.assignUnion("unionResult", "rel1", "rel2");

        // Verificar que la unión tenga las tuplas correctas
        Relation unionResult = calc.get("unionResult");
        assertNotNull(unionResult);
        assertEquals(3, unionResult.tuples()); // Debe tener 3 tuplas (sin duplicados)

        // Verificar que las tuplas sean correctas
        assertTrue(unionResult.in(new String[] {"Alice", "25"}));
        assertTrue(unionResult.in(new String[] {"Bob", "30"})); // Bob está en ambas relaciones
        assertTrue(unionResult.in(new String[] {"Charlie", "35"}));
    }

    /**
     * Prueba para verificar la operación de intersección entre dos relaciones.
     * Solo deben permanecer las tuplas que están en ambas relaciones.
     */
    @Test
    public void testIntersection() {
        // Realizar la intersección de rel1 y rel2
        calc.assignIntersection("intersectionResult", "rel1", "rel2");

        // Verificar que la intersección tenga las tuplas correctas
        Relation intersectionResult = calc.get("intersectionResult");
        assertNotNull(intersectionResult);
        assertEquals(1, intersectionResult.tuples()); // Solo Bob está en ambas relaciones

        // Verificar que la tupla de Bob esté presente
        assertTrue(intersectionResult.in(new String[] {"Bob", "30"}));

        // Verificar que no haya otras tuplas
        assertFalse(intersectionResult.in(new String[] {"Alice", "25"}));
        assertFalse(intersectionResult.in(new String[] {"Charlie", "35"}));
    }

    /**
     * Prueba para verificar que la operación de unión falle cuando los atributos son diferentes.
     * Intenta realizar la unión de dos relaciones con atributos no coincidentes.
     */
    @Test
    public void testUnionWithDifferentAttributes() {
        // Crear una relación con diferentes atributos
        calc.assign("rel3", new String[] {"nombre", "apellido"});

        // Intentar realizar la unión de rel1 y rel3 (debe fallar)
        calc.assignUnion("invalidUnion", "rel1", "rel3");

        // Verificar que la operación haya fallado (no se debe crear la relación)
        assertNull(calc.get("invalidUnion"));
    }

    /**
     * Prueba para verificar que la operación de intersección falle cuando los atributos son diferentes.
     * Intenta realizar la intersección de dos relaciones con atributos no coincidentes.
     */
    @Test
    public void testIntersectionWithDifferentAttributes() {
        // Crear una relación con diferentes atributos
        calc.assign("rel3", new String[] {"nombre", "apellido"});

        // Intentar realizar la intersección de rel1 y rel3 (debe fallar)
        calc.assignIntersection("invalidIntersection", "rel1", "rel3");

        // Verificar que la operación haya fallado (no se debe crear la relación)
        assertNull(calc.get("invalidIntersection"));
    }
}
