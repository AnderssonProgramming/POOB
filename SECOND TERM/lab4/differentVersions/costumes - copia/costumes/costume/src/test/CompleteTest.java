package test;
import domain.*;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CompleteTest{
   
 
    @Test
    public void shouldCalculateTheCostOfACompleteCostume(){
        Complete c = new Complete("Blanca Nieves", 5000, 0);
        c.addBasic(new Basic("Falda Amarilla", 20000, 0));
        c.addBasic(new Basic("Camiza Azul", 10000, 0));
        c.addBasic(new Basic("Capa roja", 30000, 0));
        try {
           assertEquals(65000,c.price());
        } catch (HalloweenShopException e){
            fail("Threw a exception");
        }    
    }    
    
    @Test
    public void shouldCalculateTheCostOfACompleteCostumeWithDiscount(){
        Complete c = new Complete("Blanca Nieves", 5000, 20);
        c.addBasic(new Basic("Falda Amarilla", 20000, 10));
        c.addBasic(new Basic("Camiza Azul", 10000, 10));
        c.addBasic(new Basic("Capa roja", 30000, 10));
        try {
           assertEquals(47200,c.price());
        } catch (HalloweenShopException e){
            fail("Threw a exception");
        }    
    }  
    
    @Test
    public void shouldThrowExceptionIfCostumeHasNoBasicCustom(){
        Complete c = new Complete("Blanca Nieves", 5000, 20);
        try { 
           int price=c.price();
           fail("Did not throw exception");
        } catch (HalloweenShopException e) {
            assertEquals(HalloweenShopException.COMPLETE_EMPTY,e.getMessage());
        }    
    }    
    
    
   @Test
    public void shouldThrowExceptionIfThereIsErrorInPrice(){
        Complete c = new Complete("Blanca Nieves", 5000, 20);
        c.addBasic(new Basic("Falda Amarilla", 20000, 10));
        c.addBasic(new Basic("Camiza Azul", -10000, 10));
        c.addBasic(new Basic("Capa roja", 30000, 10));
        try { 
           int price=c.price();
           fail("Did not throw exception");
        } catch (HalloweenShopException e) {
            assertEquals(HalloweenShopException.PRICE_ERROR,e.getMessage());
        }    
    }     
    
   @Test
    public void shouldThrowExceptionIfPriceIsNotKnown(){
        Complete c = new Complete("Blanca Nieves", 5000, 20);
        c.addBasic(new Basic("Falda Amarilla", 20000, 10));
        c.addBasic(new Basic("Camiza Azul",null, 10));
        c.addBasic(new Basic("Capa roja", 30000, 10));
        try { 
           int price=c.price();
           fail("Did not throw exception");
        } catch (HalloweenShopException e) {
            assertEquals(HalloweenShopException.PRICE_UNKNOWN,e.getMessage());
        }    
    }  
    
}