package test;

import domain.Costume;
import domain.HalloweenShop;
import domain.HalloweenShopException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HalloweenShopAcceptanceTest {

    @Test
    public void acceptanceTestAddAndListCostumes() {
        // Paso 1: Crear una instancia de HalloweenShop (simulando abrir la tienda)
        HalloweenShop shop = new HalloweenShop();
        try {

            // Paso 2: Agregar nuevos disfraces básicos y completos
            shop.addBasic("Sombrero de mago", "10000", "10");
            shop.addBasic("Capa de vampiro", "15000", "5");
            shop.addComplete("Mago", "5000", "20", "Sombrero de mago\nCapa de vampiro");

            // Paso 3: Listar todos los disfraces para verificar que están incluidos
            String expectedOutput = "9 disfraces\n" +
                                    ">Camisa blanca. Precio:5000.Descuento:10\n" +
                                    ">Pantalon negro. Precio:10000.Descuento:20\n" +
                                    ">Capa negra. Precio:15000.Descuento:0\n" +
                                    ">Pantalon rojo. Precio:30000.Descuento:25\n" +
                                    ">Zorro. Maquillaje 2000. Descuento: 0\n" +
                                    "\tCamisa blanca. Precio:5000.Descuento:10\n" +
                                    "\tPantalon negro. Precio:10000.Descuento:20\n" +
                                    "\tCapa negra. Precio:15000.Descuento:0\n" +
                                    ">Diablo. Maquillaje 25000. Descuento: 50\n" +
                                    "\tPantalon rojo. Precio:30000.Descuento:25\n" +
                                    "\tCapa negra. Precio:15000.Descuento:0\n" +
                                    ">Sombrero de mago. Precio:10000.Descuento:10\n" +
                                    ">Capa de vampiro. Precio:15000.Descuento:5\n" +
                                    ">Mago. Maquillaje 5000. Descuento: 20\n" +
                                    "\tSombrero de mago. Precio:10000.Descuento:10\n" +
                                    "\tCapa de vampiro. Precio:15000.Descuento:5\n";

            assertEquals(expectedOutput, shop.toString(), "El método toString() debe devolver la lista completa de disfraces, incluyendo los añadidos.");

            // Paso 4: Consultar un disfraz específico para verificar su existencia
            Costume magoCostume = shop.consult("Mago");
            assertNotNull(magoCostume, "El disfraz 'Mago' debería existir en la tienda después de ser añadido.");
            assertEquals("Mago", magoCostume.name(), "El nombre del disfraz consultado debería ser 'Mago'.");

            // Paso 5: Buscar disfraces que comienzan con "Ca" para verificar la funcionalidad de búsqueda
            String searchOutput = shop.search("Ca");
            String expectedSearchOutput = "9 disfraces\n" +
                                        ">Camisa blanca. Precio:5000.Descuento:10\n" +
                                        ">Capa negra. Precio:15000.Descuento:0\n" +
                                        ">Capa de vampiro. Precio:15000.Descuento:5\n";

            assertEquals(expectedSearchOutput, searchOutput, "El método search() debería devolver la lista de disfraces que comienzan con 'Ca'.");
        } catch (HalloweenShopException e){
            System.out.println(e.getMessage());
        }    
    }
 
     @Test
    public void testAddDuplicateCostume() {
        HalloweenShop shop = new HalloweenShop();                

        // Attempt to add another costume with the same name and check for the correct exception
        HalloweenShopException exception = assertThrows(HalloweenShopException.class, () -> {
            // Add an initial basic costume
            shop.addBasic("Camisa negra", "5000", "10");
            shop.addBasic("Camisa negra", "7000", "15");
        });

        // Verify that the exception message matches the expected duplicate message
        assertEquals(HalloweenShopException.DUPLICATE_BASIC, exception.getMessage());
    }

    @Test
    public void testNumericFields() {
        HalloweenShop shop = new HalloweenShop();

        // Attempt to add a costume with non-numeric price and discount fields
        HalloweenShopException exception = assertThrows(HalloweenShopException.class, () -> {
            shop.addBasic("Harry Wand", "It doesn't have price", "Discounts are for mudblood");
        });

        // Verify that the exception message matches the expected non-numeric value message
        assertEquals(HalloweenShopException.NO_NUMERIC_VALUES, exception.getMessage());
    }

    @Test
    public void testInvalidDiscount() {
        HalloweenShop shop = new HalloweenShop();

        // Attempt to add a costume with an invalid discount value
        HalloweenShopException exception = assertThrows(HalloweenShopException.class, () -> {
            shop.addBasic("Harry Wand", "1", "1000");
        });

        // Verify that the exception message matches the expected invalid discount message
        assertEquals(HalloweenShopException.INVALID_DISCOUNT, exception.getMessage());
    }

    @Test 
    public void testNoBasicsComplete() {
        HalloweenShop shop = new HalloweenShop();

        HalloweenShopException exception = assertThrows(HalloweenShopException.class, () -> {

            shop.addComplete("Luffy", "10000", "0", "Sombrero de paja\nDevil fruit");
        });

        // Verify that the exception message matches the expected invalid discount message
        assertEquals(HalloweenShopException.NO_EXIST_BASICS, exception.getMessage());
    }
}
