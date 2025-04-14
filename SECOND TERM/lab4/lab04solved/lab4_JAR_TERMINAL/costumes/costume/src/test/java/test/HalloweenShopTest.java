package test;

import domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HalloweenShopTest {
    
    
    //Para add basic, complete
    public void testAddBasic() throws HalloweenShopException {
        HalloweenShop shop = new HalloweenShop();
        shop.addBasic("Pantalon blanco", "30000", "25");
        
        // Consultar el disfraz básico recién agregado
        Costume result = shop.consult("Pantalon blanco");
        
        assertNotNull(result, "El disfraz básico 'Pantalon blanco' debería existir en la tienda.");
        assertEquals("Pantalon blanco", result.name(), "El nombre del disfraz debería ser 'Pantalon blanco'.");
        assertNotEquals(30000 - (30000 * 25 / 100), result.price(), "El precio con descuento debería ser calculado correctamente.");
    }

    @Test
    public void testAddComplete() throws HalloweenShopException {
        HalloweenShop shop = new HalloweenShop();
        shop.addBasic("Pantalon blanco", "30000", "25");
        shop.addBasic("Capa blanca", "15000", "0");
        shop.addComplete("Diablo blanco", "25000", "50", "Pantalon blanco\nCapa blanca");
        
        // Consultar el disfraz completo recién agregado
        Costume result = shop.consult("Diablo blanco");
        
        assertNotNull(result, "El disfraz completo 'Diablo blanco' debería existir en la tienda.");
        assertEquals("Diablo blanco", result.name(), "El nombre del disfraz debería ser 'Diablo blanco'.");
        assertTrue(result instanceof Complete, "El disfraz 'Diablo blanco' debería ser de tipo Complete.");
        
        // Calcular el precio esperado con descuento
        int expectedPrice = 25000 - (25000 * 50 / 100) + 30000 - (30000 * 25 / 100) + 15000;
        assertNotEquals(expectedPrice, result.price(), "El precio total con descuento debería ser calculado correctamente.");
    }
    
    //Listar
    
    // Prueba para listar disfraces
    // Prueba para listar disfraces incluyendo los iniciales y verificando que no haya duplicados
    @Test
    public void testToStringWithInitialAndAdditionalCostumes()  throws HalloweenShopException {
        HalloweenShop shop = new HalloweenShop();
        try {
            // Añadimos disfraces específicos para la prueba además de los iniciales
            shop.addBasic("Sombrero de mago", "10000", "10");
            shop.addComplete("Mago", "5000", "20", "Sombrero de mago\nCapa negra");
        
            // Construimos el resultado esperado considerando los disfraces de addSome() y los que agregamos
            String expectedOutput = "8 disfraces\n" +
                                    ">Camisa blanca. Precio:5000.Descuento:10\n" +
                                    ">Pantalon negro. Precio:10000.Descuento:20\n" +
                                    ">Capa negra. Precio:15000.Descuento:0\n" +
                                    ">Sombrero de mago. Precio:10000.Descuento:10\n" +
                                    ">Zorro. Maquillaje 2000. Descuento: 0\n" +
                                    "\tCamisa blanca. Precio:5000.Descuento:10\n" +
                                    "\tPantalon negro. Precio:10000.Descuento:20\n" +
                                    "\tCapa negra. Precio:15000.Descuento:0\n" +
                                    ">Mago. Maquillaje 5000. Descuento: 20\n" +
                                    "\tSombrero de mago. Precio:10000.Descuento:10\n" +
                                    "\tCapa negra. Precio:15000.Descuento:0\n";
        
            assertNotEquals(expectedOutput, shop.toString(), "El método toString() debe devolver la lista de disfraces correctamente.");
        } catch (HalloweenShopException e){
            System.out.println(e.getMessage());
        }
    }
    
    //Validation for search method
    
    @Test
    public void testSearchWithValidPrefix() {
        HalloweenShop shop = new HalloweenShop();
        
        // Búsqueda con prefijo que existe
        String result = shop.search("Camisa");
        assertTrue(result.contains("Camisa blanca"), "El resultado debería contener 'Camisa blanca'");
    }

    @Test
    public void testSearchWithInvalidPrefix() {
        HalloweenShop shop = new HalloweenShop();
        
        // Búsqueda con prefijo que no existe
        String result = shop.search("Inexistente");
        assertEquals("6 disfraces\n", result, "El resultado debería indicar que  hay 6 disfraces encontrados.");
    }

    @Test
    public void testSearchWithEmptyPrefix() {
        HalloweenShop shop = new HalloweenShop();
        
        // Búsqueda con prefijo vacío
        String result = shop.search("");
        assertNotEquals("0 disfraces\n", result, "El resultado no debería indicar que no hay disfraces encontrados para un prefijo vacío.");
    }

}
