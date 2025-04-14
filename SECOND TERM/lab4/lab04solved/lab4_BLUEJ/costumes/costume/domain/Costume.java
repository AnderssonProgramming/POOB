package domain;

public abstract class Costume{
    
    protected String name;
    protected int discount;
    
    
    public Costume(String name, int discount){
        this.name=name;
        this.discount=discount;
    }
    /**
     * Return the name
     * @return
     */
    public String name(){
        return name;
    }

 
    /**
     * Return the price to pay for the costume
     * @return
     * @throws CostumeShopException, if the price is not available or has an error
     */
    public abstract int price() throws HalloweenShopException;
    
    
    
    /**
     * Return the representation as string
     * @return
     * @throws CostumeShopException, if the data is not complete (price or discount)
     */    
    public abstract String data() throws HalloweenShopException;

}
