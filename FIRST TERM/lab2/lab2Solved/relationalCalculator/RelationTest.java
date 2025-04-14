import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de prueba para la clase Relation.
 * Proporciona pruebas unitarias para verificar la funcionalidad de la clase Relation.
 * Incluye casos de prueba para la creación de relaciones, inserción de tuplas, y
 * operaciones de comparación y manejo de errores.
 * 
 * @author Pedraza-Sánchez
 * @version 2024-2
 */
public class RelationTest {
    
    /**
     * Constructor por defecto para la clase de prueba RelationTest.
     */
    public RelationTest() {
    }

    /**
     * Configuración inicial para las pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @Before
    public void setUp() {
    }

    /**
     * Prueba para verificar que se puede crear una relación válida con atributos.
     * Verifica que se establezcan correctamente las columnas y que al inicio no tenga tuplas.
     */
    @Test
    public void shouldCreateAValidRelation() {
        String[] attributes = {"song", "artist", "album", "year"};
        Relation songs = new Relation(attributes);
        assertEquals(songs.columns(), 4);
        assertEquals(songs.tuples(), 0);
    }    

    /**
     * Prueba para verificar que se crea una relación vacía cuando los atributos son inválidos (duplicados).
     */
    @Test
    public void shouldCreateAEmptyRelationIfError() {
        String[] attributes = {"song", "artist", "song", "year"}; // Atributos duplicados
        Relation songs = new Relation(attributes);
        assertEquals(songs.columns(), 0); // La relación debe ser vacía
        assertEquals(songs.tuples(), 0);
    }

    /**
     * Prueba para verificar la inserción de tuplas válidas en la relación.
     * La relación debe aceptar tuplas con el número correcto de atributos.
     */
    @Test
    public void shouldInsertTuples() {
        String[] attributes = {"song", "artist", "album", "year"};
        String[] acrostico = {"Acróstico", "Shakira", "Las mujeres ya no lloran", "2023"};
        String[] negra = {"La Camisa Negra", "Juanes", "Mi Sangre", "2004"};

        Relation songs = new Relation(attributes);
        songs.insert(acrostico);   // Inserta la primera tupla
        songs.insert(negra);       // Inserta la segunda tupla

        assertEquals(songs.columns(), 4);  // Verifica las columnas
        assertEquals(songs.tuples(), 2);   // Verifica las tuplas
    }

    /**
     * Prueba para verificar que no se pueden insertar tuplas inválidas
     * (cuando no coinciden los atributos).
     */
    @Test
    public void shouldNotInsertInvalidTuples() {
        String[] attributes = {"song", "artist", "album", "year"};
        String[] acrostico = {"Acrostico", "Shakira", "Las Mujeres Ya No Lloran", "2023"};
        String[] negra = {"La Camisa Negra", "Juanes", "Mi Sangre"}; // Tupla con atributos incompletos
        Relation songs = new Relation(attributes);
        songs.insert(acrostico);
        assertEquals(songs.columns(), 4);
        assertEquals(songs.tuples(), 1); // Solo debe haber una tupla insertada
    }

    /**
     * Prueba para verificar que no se pueden insertar tuplas duplicadas (ignorando mayúsculas).
     */
    @Test
    public void shouldNotInsertRepeatedTuples() {
        String[] attributes = {"song", "artist", "album", "year"};
        String[] acrostico = {"Acrostico", "Shakira", "Las Mujeres Ya No Lloran", "2023"};
        String[] ocrostico = {"ACROSTICO", "SHAKIRA", "Las Mujeres Ya No Lloran", "2023"}; // Tupla duplicada con mayúsculas
        Relation songs = new Relation(attributes);
        songs.insert(acrostico);
        songs.insert(ocrostico);
        assertEquals(songs.columns(), 4);
        assertEquals(songs.tuples(), 2); // Solo debe haber una tupla (duplicada ignorada)
    }

    /**
     * Prueba para varios casos exitosos usando diferentes métodos de aserción.
     * Incluye ejemplos de uso de assertArrayEquals, assertEquals y assertFalse.
     */
    @Test
    public void shouldPass() {
        // Ejemplo de assertArrayEquals
        String[] characteristics = {"basketball", "soccer", "golf", "tennis"};
        String[] sports = {"basketball", "soccer", "golf", "tennis"};
        assertArrayEquals(characteristics, sports); // Las matrices deben ser iguales

        // Ejemplo de assertEquals
        int multiplication = 8 * 4;
        assertEquals(multiplication, 32); // Multiplicación correcta

        // Ejemplo de assertFalse
        assertFalse(7786776 < 47862); // Comparación correcta
    }

    /**
     * Prueba diseñada para fallar deliberadamente usando el método fail().
     * También incluye ejemplos de assertNull y assertTrue con resultados incorrectos.
     */
    @Test
    public void shouldFail() {
        boolean phi = true;
        boolean gamma = false;
        boolean predicate = phi && gamma;

        String setIdentity = "no null";
        assertNull("The identity of sets should be null", setIdentity); // Debería fallar porque no es nulo
        assertTrue("True and false is not equal to true.", predicate);  // Fallará porque es falso
        fail("Just to prove the method"); // Llamada deliberada a fail
    }

    /**
     * Prueba que genera un error (ArithmeticException) al intentar dividir por cero.
     * Verifica el manejo de excepciones.
     */
    @Test
    public void shouldErr() {
        String[] testing = {"Hello", ", this", "is", "my", "first", "program"};
        int a = 7;
        int b = 0;
        try {
            String[] testing1 = {"Hello", ", this", "is", "my", "first", "program"};
            assertArrayEquals(testing, testing1); // Arrays iguales

            int division = a / b; // Esta línea genera una excepción
            System.out.println("The division is: " + division);

            fail("Este código no debería alcanzarse debido a la división por cero.");
        } catch (ArithmeticException e) {
            throw e; // Manejo de la excepción
        }
    }

    /**
     * Método de limpieza que se ejecuta después de cada prueba.
     */
    @After
    public void tearDown() {
    }
}
